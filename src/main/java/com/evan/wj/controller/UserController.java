package com.evan.wj.controller;


import com.evan.wj.pojo.AdminRole;
import com.evan.wj.pojo.User;
import com.evan.wj.result.Result;
import com.evan.wj.result.ResultFactory;
import com.evan.wj.service.AdminRoleService;
import com.evan.wj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {


    @Autowired
    UserService userService;

    @Autowired
    AdminRoleService adminRoleService;


    @GetMapping("/api/admin/user")
    @ResponseBody
    public List<User> listUsers() throws Exception {
        return userService.list();
    }

    @PutMapping("/api/admin/user")
    @ResponseBody
    public Result updateUserStatus(@RequestBody User requestUser) {

        User user = userService.getUser(requestUser.getUsername());
        user.setEnabled(requestUser.isEnabled());

        userService.addOrUpdate(user);

        String message = "用户"+ requestUser.getUsername() + "状态更新成功";
        return ResultFactory.buildSuccessResult(message);
    }



    @GetMapping("/api/admin/role")
    @ResponseBody
    public List<AdminRole> listRoles() throws Exception {
        return adminRoleService.list();
    }


    @PutMapping("/api/admin/role")
    @ResponseBody
    public Result updateRoleStatus(@RequestBody AdminRole requestRole) {
        AdminRole adminRole = adminRoleService.findById(requestRole.getId());
        adminRole.setEnabled(requestRole.isEnabled());
        adminRoleService.addOrUpdate(adminRole);
        String message = "用户"+ adminRole.getNameZh() + "状态更新成功";
        return ResultFactory.buildSuccessResult(message);
    }
}
