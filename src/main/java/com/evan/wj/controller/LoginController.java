package com.evan.wj.controller;


import com.evan.wj.pojo.User;
import com.evan.wj.result.Result;
import com.evan.wj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
public class LoginController {


    @Autowired
    UserService userService;


    @CrossOrigin
    @PostMapping(value = "api/login")
    @ResponseBody
    public Result  login(@RequestBody User userRequest, HttpSession session){

        String username = userRequest.getUsername();
        String password = userRequest.getPassword();


        User user = userService.get(username, password);


        if (user == null) {
            String message = "账号密码错误";
            System.out.println(message);

            return new Result(400);
        } else {

            session.setAttribute("user", user);

            return new Result(200);
        }

    }
}
