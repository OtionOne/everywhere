package com.powernode.everywhere.user.controller;

import com.powernode.everywhere.common.core.util.JsonResult;
import com.powernode.everywhere.user.domain.UserInfo;
import com.powernode.everywhere.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public List<UserInfo> ListAll(){
        return userService.list();
    }

    @GetMapping("/checkPhone")
    public JsonResult<Boolean> PhoneExist(String phone){
        return userService.PhoneExist(phone);
    }
}
