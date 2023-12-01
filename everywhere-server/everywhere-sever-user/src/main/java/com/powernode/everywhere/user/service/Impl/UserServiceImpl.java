package com.powernode.everywhere.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.everywhere.common.core.util.JsonResult;
import com.powernode.everywhere.common.redis.utils.RedisCache;
import com.powernode.everywhere.user.domain.UserInfo;
import com.powernode.everywhere.user.mapper.UserMapper;
import com.powernode.everywhere.user.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.language.bm.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserInfo> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private RedisCache redisCache;


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
}
