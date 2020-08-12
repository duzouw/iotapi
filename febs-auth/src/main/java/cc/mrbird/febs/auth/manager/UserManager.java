package cc.mrbird.febs.auth.manager;

import cc.mrbird.febs.auth.entity.Authority;
import cc.mrbird.febs.auth.entity.ClientAuthority;
import cc.mrbird.febs.auth.mapper.*;
import cc.mrbird.febs.common.core.entity.constant.FebsConstant;
import cc.mrbird.febs.common.core.entity.constant.StringConstant;
import cc.mrbird.febs.common.core.entity.system.Menu;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.entity.system.UserDataPermission;
import cc.mrbird.febs.common.core.entity.system.UserRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户业务逻辑
 *
 * @author MrBird
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserManager {

    private final AuthorityMapper authorityMapper;
    private final ClientAuthorityMapper clientAuthorityMapper;


    /**
     * 通过用户id查询用户权限串
     *
     * @param id 用户名
     * @return 权限
     */
    public String findUserPermissions(String id) {
        List<String> ids = new ArrayList<>();

        clientAuthorityMapper.selectList(new QueryWrapper<ClientAuthority>().lambda().eq(ClientAuthority::getClientId, id)).forEach(x -> ids.add(x.getAuthorityId()));
        if (ids.isEmpty()){
            return null;
        }
        return authorityMapper.selectList(new QueryWrapper<Authority>().lambda().in(Authority::getAuthorityId, ids)).stream().map(Authority::getAuthority).collect(Collectors.joining(","));

    }

}
