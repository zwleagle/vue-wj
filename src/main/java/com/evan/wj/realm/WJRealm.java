package com.evan.wj.realm;

import com.evan.wj.pojo.User;
import com.evan.wj.service.AdminPermissionService;
import com.evan.wj.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.elasticsearch.common.util.ByteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;


public class WJRealm extends AuthorizingRealm {


    @Autowired
    UserService userService;


    @Autowired
    AdminPermissionService adminPermissionService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {


        String username = principalCollection.getPrimaryPrincipal().toString();

        Set<String> permissions = adminPermissionService.listPermissionURLsByUser(username);

        SimpleAuthorizationInfo s = new SimpleAuthorizationInfo();

        s.setStringPermissions(permissions);
        return s;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {


        String username = authenticationToken.getPrincipal().toString();

        User user = userService.getUser(username);
        String passwordIdDb = user.getPassword();
        String salt = user.getSalt();

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, passwordIdDb,
                ByteSource.Util.bytes(salt), getName());


        return authenticationInfo;
    }
}
