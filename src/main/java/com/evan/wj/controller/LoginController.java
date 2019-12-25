package com.evan.wj.controller;


import com.evan.wj.pojo.User;
import com.evan.wj.result.Result;
import com.evan.wj.result.ResultFactory;
import com.evan.wj.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
@CrossOrigin
public class LoginController {


    @Autowired
    UserService userService;


    @CrossOrigin
    @PostMapping(value = "api/login")
    @ResponseBody
    public Result  login(@RequestBody User userRequest, HttpSession session){

        String username = userRequest.getUsername();
        String password = userRequest.getPassword();
        username = HtmlUtils.htmlEscape(username);

        //User user = userService.get(username, password);

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, userRequest.getPassword());
        usernamePasswordToken.setRememberMe(true);


        try {
            subject.login(usernamePasswordToken);

            // 生成随机 token 并存储在 session 中
            User user = userService.getUser(username);
            return ResultFactory.buildSuccessResult(usernamePasswordToken);
        }catch (AuthenticationException e){

            String message = "账号密码错误";
            return ResultFactory.buildFailResult(message);
        }


    }

//    @GetMapping("/login")
//    public Result login() {
//        String message = "非法登录";
//        return ResultFactory.buildSuccessResult(message);
//    }



    @CrossOrigin
    @PostMapping(value = "api/register")
    @ResponseBody
    public Result register(@RequestBody User user){

        String username = user.getUsername();
        String password = user.getPassword();
        username = HtmlUtils.htmlEscape(username);

        user.setUsername(username);

        boolean exist = userService.isExist(username);

        if (exist) {
            String message = "用户名已被使用";
            return ResultFactory.buildFailResult(message);
        }


        // 默认生成 16 位盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();


        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userService.addOrUpdate(user);

        return ResultFactory.buildSuccessResult(user);

    }


    @ResponseBody
    @GetMapping("api/logout")
    public Result logout(){

        Subject subject = SecurityUtils.getSubject();

        subject.logout();

        String messge = "成功登出";


        return ResultFactory.buildSuccessResult(messge);

    }

    @ResponseBody
    @GetMapping(value = "api/authentication")
    public String authentication(){
        return "身份认证成功";
    }


}
