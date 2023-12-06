package com.powernode.everywhere.user.controller;

import com.powernode.everywhere.common.core.util.JsonResult;
import com.powernode.everywhere.user.anno.LoginRequired;
import com.powernode.everywhere.user.domain.DTO.UserInfo;
import com.powernode.everywhere.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @LoginRequired
    @GetMapping("/list")
    public List<UserInfo> ListAll(){
        return userService.list();
    }

    @GetMapping("/checkPhone")
    public JsonResult<Boolean> PhoneExist(String phone){
        return userService.PhoneExist(phone);
    }

    @GetMapping("/regist")
    public JsonResult register(UserInfo user,String verifyCode){
        return userService.register(user,verifyCode);
    }

    @PostMapping("/login")
    public JsonResult login(String username,String password){
        return userService.login(username,password);
    }

}
