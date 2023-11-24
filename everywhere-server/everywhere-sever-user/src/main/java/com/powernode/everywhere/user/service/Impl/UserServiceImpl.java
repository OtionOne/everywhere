package com.powernode.everywhere.user.service.Impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.everywhere.user.domain.UserInfo;
import com.powernode.everywhere.user.mapper.UserMapper;
import com.powernode.everywhere.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserInfo> implements UserService {
}
