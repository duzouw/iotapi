package cc.mrbird.febs.common.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * @author MrBird
 */
@Data
@SuppressWarnings("all")
@EqualsAndHashCode(callSuper = true)
public class FebsAuthUser extends User {

    private static final long serialVersionUID = -6411066541689297219L;

    private String clientId;

    private String clientName;

    private String clientAccount;

    private String clientStatus;


    public FebsAuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public FebsAuthUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
