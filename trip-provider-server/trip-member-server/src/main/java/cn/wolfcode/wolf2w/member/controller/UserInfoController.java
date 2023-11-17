package cn.wolfcode.wolf2w.member.controller;

import cn.wolfcode.wolf2w.common.exception.LogicException;
import cn.wolfcode.wolf2w.common.redis.service.RedisService;
import cn.wolfcode.wolf2w.common.redis.util.RedisKeys;
import cn.wolfcode.wolf2w.common.sercurity.annotation.RequireLogin;
import cn.wolfcode.wolf2w.common.web.response.JsonResult;
import cn.wolfcode.wolf2w.member.domain.UserInfo;
import cn.wolfcode.wolf2w.member.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("userInfos")
public class UserInfoController {
    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private RedisService redisService;

    @GetMapping("/detail")
    public UserInfo detail(Long id){
        return userInfoService.getById(id);
    }

    @GetMapping("/checkPhone")
    public JsonResult checkPhone(String phone){

        //操作表--mapper---userInfoService
        boolean ret = userInfoService.checkPhone(phone);

        return JsonResult.success(ret);
    }

    @GetMapping("/sendVerifyCode")
    public JsonResult sendVerifyCode(String phone){

        userInfoService.sendVerifyCode(phone);

        return JsonResult.success();
    }

    @PostMapping("/regist")
    public JsonResult regist(String phone, String nickname, String password, String rpassword, String verifyCode){

        ////.....这个位置进行参数校验

        userInfoService.regist(phone, nickname, password, rpassword, verifyCode);
        return JsonResult.success();
    }


    @PostMapping("/login")
    public JsonResult login(String username, String password){
        Map<String, Object> map = userInfoService.login(username, password);
        return JsonResult.success(map);
    }



    @RequireLogin   //必须登录之后才可以进入
    @GetMapping("/currentUser")
    public JsonResult currentUser(HttpServletRequest request){
        //获取
        String token = request.getHeader("token");
        //以token为key查询用户信息
        UserInfo userInfo = userInfoService.getUserByToken(token);
        return JsonResult.success(userInfo);
    }
}
