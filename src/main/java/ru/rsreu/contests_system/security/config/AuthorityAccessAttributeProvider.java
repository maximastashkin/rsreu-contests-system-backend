package ru.rsreu.contests_system.security.config;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.user.Authority;

@Component
public class AuthorityAccessAttributeProvider {
    public String formActiveUnblockedAttribute(Authority authority) {
        String roleAuthority = authority != Authority.NONE ? String.format("hasAuthority('%s') AND", authority) : "";
        return String.format(
                "%s hasAuthority('%s') AND hasAuthority('%s')", roleAuthority, Authority.ACTIVE, Authority.UNBLOCKED);
    }
}
