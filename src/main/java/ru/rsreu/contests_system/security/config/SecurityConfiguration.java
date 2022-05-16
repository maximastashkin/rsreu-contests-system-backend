package ru.rsreu.contests_system.security.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.rsreu.contests_system.api.user.Authority;
import ru.rsreu.contests_system.security.api_key.ApiKeyConfigurer;
import ru.rsreu.contests_system.security.jwt.JwtConfigurer;

@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String[] AUTH_WHITELIST = {
            "/**/signup",
            "/**/auth",
            "/**/check-mail",
            "/**/refresh",
            "/**/check-organization-phone",
            "/**/check-organization-email",
            "/**/check-leader-email",
            "/**/applications/",
            "/**/confirm/**",
            "/**/orgs",
            "/**/orgs/all/*/*",
            "/**/events/all-actual/*/*",
            "/**/test/**",
            "/**/supported-languages",
            "/**/types"
    };

    private static final String[] AUTH_LIST = {
            "/**/users/info"
    };

    private static final String[] API_KEY_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html/**",
    };

    private static final String[] PARTICIPANT_LIST = {
            "/**/events/all-actual/user/**",
            "/**/events/all-completed/user/**",
            "/**/events/follow",
            "/**/events/unfollow",
            "/**/events/start",
            "/**/events/complete",
            "/**/tasks/performed",
            "/**/tasks/check",
            "/**/tasks/completed"
    };

    private static final String[] ORGANIZATION_LEADER_LIST = {
            "/**/organizers/**",
            "/**/leader/change"
    };

    private static final String[] ORGANIZATION_LEADER_AND_ORGANIZER_LIST = {
            "/**/events/"
    };

    private static final String[] ADMIN_LIST = {
            "/**/users/all/**",
            "/**/users/block",
            "/**/users/unblock",
            "/**/applications/**"
    };

    private final JwtConfigurer jwtConfigurer;

    private final ApiKeyConfigurer apiKeyConfigurer;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final AuthorityAccessAttributeProvider authorityAccessAttributeProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests(authorize ->
                        authorize
                                .antMatchers(AUTH_WHITELIST).permitAll()
                                .antMatchers(AUTH_LIST).authenticated()
                                .antMatchers(PARTICIPANT_LIST).access(
                                        authorityAccessAttributeProvider.formActiveUnblockedAttribute(
                                                Authority.PARTICIPANT))
                                .antMatchers(ORGANIZATION_LEADER_LIST).access(
                                        authorityAccessAttributeProvider.formActiveUnblockedAttribute(
                                                Authority.ORGANIZATION_LEADER))
                                .antMatchers(ORGANIZATION_LEADER_AND_ORGANIZER_LIST).access(
                                        authorityAccessAttributeProvider.formActiveUnblockedAttribute(
                                                Authority.ORGANIZATION_LEADER, Authority.ORGANIZER
                                        )
                                )
                                .antMatchers(ADMIN_LIST).access(
                                        authorityAccessAttributeProvider.formActiveUnblockedAttribute(
                                                Authority.ADMIN))
                                .anyRequest().denyAll()
                )
                .apply(jwtConfigurer)
                .and()
                .apply(apiKeyConfigurer)
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(API_KEY_WHITELIST);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}