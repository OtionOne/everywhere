package cn.wolfcode.wolf2w.member.service;

import cn.wolfcode.wolf2w.member.domain.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 用户服务接口
 */
public interface IUserInfoService extends IService<UserInfo> {

    /**
     * 检查手机号码是否存在
     * @param phone
     * @return  true:表示存在   false:不存在
     */
    boolean checkPhone(String phone);

    /**
     * 发送短信
     * @param phone
     */
    void sendVerifyCode(String phone);

    /**
     * 用户注册
     * @param phone
     * @param nickname
     * @param password
     * @param rpassword
     * @param verifyCode
     */
    void regist(String phone, String nickname, String password, String rpassword, String verifyCode);

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    Map<String, Object> login(String username, String password);

    /**
     * 通过token获取用户信息
     * @param token
     * @return
     */
    UserInfo getUserByToken(String token);
}
