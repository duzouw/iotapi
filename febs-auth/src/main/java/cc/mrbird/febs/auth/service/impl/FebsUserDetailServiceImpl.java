package cc.mrbird.febs.auth.service.impl;

import cc.mrbird.febs.auth.entity.Client;
import cc.mrbird.febs.auth.manager.UserManager;
import cc.mrbird.febs.auth.mapper.ClientMapper;
import cc.mrbird.febs.common.core.entity.FebsAuthUser;
import cc.mrbird.febs.common.core.entity.constant.ParamsConstant;
import cc.mrbird.febs.common.core.entity.constant.SocialConstant;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author MrBird
 */
@Service
@RequiredArgsConstructor
public class FebsUserDetailServiceImpl implements UserDetailsService {

    private final UserManager userManager;

    private final ClientMapper clientMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Client client = clientMapper.selectOne(new QueryWrapper<Client>().lambda().eq(Client::getClientAccount, username));
        if (null != client) {
            boolean notLocked = false;

            if (StringUtils.equals(Client.YES, client.getClientStatus())) {
                notLocked = true;
            }
            String permissions = userManager.findUserPermissions(client.getClientId());

            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.NO_AUTHORITIES;
            if (StringUtils.isNotBlank(permissions)) {
                grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(permissions);
            }

            FebsAuthUser authUser = new FebsAuthUser(client.getClientAccount(), client.getSecurityPassword(), true, true, true, notLocked,
                    grantedAuthorities);

            BeanUtils.copyProperties(client, authUser);

            client = null;
            permissions = null;
            grantedAuthorities = null;

            return authUser;
        } else {
            throw new UsernameNotFoundException("");
        }
    }

}
