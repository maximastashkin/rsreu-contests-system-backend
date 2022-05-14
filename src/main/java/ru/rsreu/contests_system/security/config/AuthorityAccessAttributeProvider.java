package ru.rsreu.contests_system.security.config;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.user.Authority;

@Component
public class AuthorityAccessAttributeProvider {
    public String formActiveUnblockedAttribute(Authority... authorities) {
        StringBuilder baseAuthoritiesAttribute = formUserAuthorityAttributePart(authorities);
        baseAuthoritiesAttribute.append(formActiveUnblockedAttributePart());
        return baseAuthoritiesAttribute.toString();
    }

    private String formActiveUnblockedAttributePart() {
        return String.format(" AND hasAuthority('%s') AND hasAuthority('%s')",
                Authority.ACTIVE, Authority.UNBLOCKED);
    }

    private StringBuilder formUserAuthorityAttributePart(Authority... authorities) {
        StringBuilder result = new StringBuilder();
        result.append("(");
        formMainPartOfAttribute(result, authorities);
        result.append(")");
        return result;
    }

    private void formMainPartOfAttribute(StringBuilder result, Authority[] authorities) {
        for (int i = 0; i < authorities.length; i++) {
            if (isSignificantAuthority(authorities[i])) {
                result.append(formOneAuthorityAttribute(authorities[i]));
            }
            if (isNotLastAuthority(authorities.length, i)) {
                result.append(" OR ");
            }
        }
    }

    private boolean isNotLastAuthority(int length, int i) {
        return i != length - 1;
    }

    private boolean isSignificantAuthority(Authority authority) {
        return authority != Authority.NONE;
    }

    private String formOneAuthorityAttribute(Authority authority) {
        return String.format("hasAuthority('%s')", authority);
    }
}
