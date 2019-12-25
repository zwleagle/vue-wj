package com.evan.wj.service;


import com.evan.wj.dao.AdminPermissionDAO;
import com.evan.wj.dao.AdminRoleDAO;
import com.evan.wj.dao.AdminRolePermissionDAO;
import com.evan.wj.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AdminPermissionService {


    @Autowired
    AdminPermissionDAO adminPermissionDAO;


    @Autowired
    UserService userService;

    @Autowired
    AdminUserRoleService adminUserRoleService;


    @Autowired
    AdminRoleService adminRoleService;



    @Autowired
    AdminRolePermissionService adminRolePermissionService;

    public Set<String> listPermissionURLsByUser(String username) {

        User user = userService.getUser(username);


        List<AdminRole> roles = new ArrayList<>();
        List<AdminPermission> permissions = new ArrayList<>();
        Set<String> URLs = new HashSet<>();


        List<AdminUserRole> urs = adminUserRoleService.listAllByUid(user.getId());

        for (AdminUserRole  userRole: urs) {

            roles.add(adminRoleService.findById(userRole.getRid()));

        }


        for (AdminRole adminRole: roles ) {

            List<AdminRolePermission> rps = adminRolePermissionService.findAllByRid(adminRole.getId());

            for (AdminRolePermission rp : rps) {
                URLs.add(adminPermissionDAO.findById(rp.getPid()).getUrl());
            }
        }

        return URLs;

    }


    public boolean needFilter(String requestAPI) {
        List<AdminPermission> ps = adminPermissionDAO.findAll();
        for (AdminPermission p: ps) {
            if (p.getUrl().equals(requestAPI)) {
                return true;
            }
        }
        return false;
    }

}
