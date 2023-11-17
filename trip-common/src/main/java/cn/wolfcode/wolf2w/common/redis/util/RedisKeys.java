package cn.wolfcode.wolf2w.common.redis.util;

import cn.wolfcode.wolf2w.common.util.Consts;
import lombok.Getter;
import lombok.Setter;

/**
 * Redis key 管理枚举类
 * 约定： 一个枚举实例代表一个Redis 的key
 */
@Getter
public enum RedisKeys {
    //用户登录key 实例对象
    USER_LOGIN_TOKEN("user_login_token", Consts.USER_INFO_TOKEN_VAI_TIME * 60L),
    //注册验证码码key 实例对象
    REGIST_VERIFY_CODE("regist_verify_code", Consts.VERIFY_CODE_VAI_TIME * 60L);
    private String prefix;    //key 的前缀
    private Long time;        //key 的有效时间，单位为 秒
    private RedisKeys(String prefix, Long time){
        this.prefix = prefix;
        this.time = time;
    }
    //拼接真实的key
    public String join(String ...values){
        StringBuilder sb = new StringBuilder(80);
        sb.append(this.prefix);
        for (String value : values) {
            sb.append(":").append(value);
        }
        return sb.toString();
    }
    public static void main(String[] args) {
        //key :    regist_verify_code:13700000000:xxx:yyy:zzz
        String key = RedisKeys.REGIST_VERIFY_CODE.join("13700000000", "xxx","yyy", "zzz");
        System.out.println(key);
    }

}
