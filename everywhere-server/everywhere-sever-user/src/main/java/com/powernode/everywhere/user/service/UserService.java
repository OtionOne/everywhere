package com.powernode.everywhere.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.powernode.everywhere.common.core.util.JsonResult;
import com.powernode.everywhere.user.domain.DTO.UserInfo;

public interface UserService extends IService<UserInfo> {
    JsonResult<Boolean> PhoneExist(String phone);

    JsonResult register(UserInfo userInfo, String code);

    JsonResult login(String username, String password);
}
