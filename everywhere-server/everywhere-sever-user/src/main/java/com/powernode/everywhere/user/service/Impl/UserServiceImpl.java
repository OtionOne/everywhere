package com.powernode.everywhere.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.everywhere.common.core.util.JsonResult;
import com.powernode.everywhere.user.domain.UserInfo;
import com.powernode.everywhere.user.mapper.UserMapper;
import com.powernode.everywhere.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserInfo> implements UserService {

    @Resource
    private  UserMapper userMapper;

    @Override
    public JsonResult<Boolean> PhoneExist(String phone) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserInfo::getPhone,phone);
        userMapper.selectOne(wrapper);
        if(userMapper.selectOne(wrapper)!=null)
            return JsonResult.error(500,"手机号已被注册",null);
        else
            return JsonResult.success();

    }
}
