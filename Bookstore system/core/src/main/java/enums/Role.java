package enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public enum Role {
    ADMIN,
    USER;

    @Deprecated
    public SimpleGrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + this.name());
    }
}
