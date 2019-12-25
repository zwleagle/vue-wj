package com.evan.wj.service;


import com.evan.wj.dao.AdminMenuDAO;
import com.evan.wj.pojo.AdminMenu;
import com.evan.wj.pojo.AdminRoleMenu;
import com.evan.wj.pojo.AdminUserRole;
import com.evan.wj.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminMenuService {

    @Autowired
    AdminMenuDAO adminMenuDAO;

    @Autowired
    UserService userService;

    @Autowired
    AdminUserRoleService adminUserRoleService;


    @Autowired
    AdminRoleMenuService adminRoleMenuService;

    public List<AdminMenu> getMenusByCurrentUser() {

        String username = SecurityUtils.getSubject().getPrincipal().toString();

        User user = userService.getUser(username);

        List<AdminUserRole>  adminUserRoles = adminUserRoleService.listAllByUid(user.getId());


        List<AdminMenu> menus = new ArrayList<>();

        for (AdminUserRole userRole : adminUserRoles) {

            List<AdminRoleMenu> roleMenus = adminRoleMenuService.findAllByRid(userRole.getRid());
            for (AdminRoleMenu roleMenu : roleMenus) {
                menus.add(adminMenuDAO.findById(roleMenu.getMid()));
            }

        }

        return menus;
    }

    public List<AdminMenu> getAllByParentId(Integer pid){

      return adminMenuDAO.findAllByParentId(pid);
    }


}
