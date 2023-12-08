package com.powernode.everywhere.user.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.everywhere.common.core.util.JsonResult;
import com.powernode.everywhere.common.redis.utils.RedisCache;
import com.powernode.everywhere.user.domain.DTO.UserInfo;
import com.powernode.everywhere.user.domain.VO.LoginUser;
import com.powernode.everywhere.user.key.UserRedisKeyPrefix;
import com.powernode.everywhere.user.mapper.UserMapper;
import com.powernode.everywhere.user.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserInfo> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private RedisCache redisCache;

    @Value("${jwt.secret}")
    private String secret;


    @Override
    public JsonResult<Boolean> PhoneExist(String phone) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserInfo::getPhone, phone);
        userMapper.selectOne(wrapper);
        if (userMapper.selectOne(wrapper) != null)
            return JsonResult.error(500, "手机号已被注册", null);
        else
            return JsonResult.success();

    }

    @Override
    public JsonResult register(UserInfo userInfo, String code) {
        String key = "REGISTER:PHONE:" + userInfo.getPhone();
        if (!redisCache.hasKey(key)) {
            return JsonResult.error(500, "验证码已过期", null);
        }
        if (redisCache.getCacheObject(key).equals(code) || code.isEmpty()) {
            log.info("验证码正确");
            redisCache.deleteObject(key);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encode = encoder.encode(userInfo.getPassword());
            userInfo.setPassword(encode);
            userMapper.insert(userInfo);
            return JsonResult.success();
        } else {
            return JsonResult.error(JsonResult.CODE_ERROR, "验证码错误", null);
        }
    }

    @Override
    public JsonResult login(String phone, String password) {
        //根据用户名查询用户信息
        if (phone.isEmpty() || password.isEmpty()) {
            return JsonResult.error(500, "用户名或密码不能为空", null);
        }
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<UserInfo>();
        wrapper.lambda().eq(UserInfo::getPhone, phone);
        UserInfo user = userMapper.selectOne(wrapper);
        //如果没有就抛出异常
        if(user==null){
            return JsonResult.error(500,"账号或密码错误",null);
        }
        //如果有就比较密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(password, user.getPassword());
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(user,loginUser);
        //没问题,并将user存入redis 生成token
        if(matches){
            long now = System.currentTimeMillis();
            long expire = now + (30 * 60 * 1000);
            loginUser.setLoginTime(now);
            loginUser.setExpireTime(expire);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            UserRedisKeyPrefix prefix = UserRedisKeyPrefix.USER_LOGIN_INFO_STRING;
            prefix.setTimeout(expire);
            prefix.setUnit(TimeUnit.MILLISECONDS);
            redisCache.setCacheObject(prefix,loginUser,uuid);
            HashMap<String, Object> payload = new HashMap<>();
            payload.put("uuid",uuid);
            String token = Jwts.builder().addClaims(payload).signWith(SignatureAlgorithm.HS256, secret).compact();
            payload.clear();

            payload.put("token",token);
            payload.put("user",loginUser);
            return JsonResult.success(payload);
        }else{
            return JsonResult.error(500,"账号或密码错误",null);
        }
    }
}
