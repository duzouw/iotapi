package cc.mrbird.febs.common.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author MrBird
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUser implements Serializable {

    private static long serialVersionUID = 761748087824726463L;
    private String clientId;
    private String clientName;
    private String clientAccount;
    private String clientStatus;
    @JsonIgnore
    private String password;
    private String username;
    @JsonIgnore
    private Set<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
}
