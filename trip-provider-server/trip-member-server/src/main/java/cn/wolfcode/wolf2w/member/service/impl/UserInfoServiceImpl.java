package cn.wolfcode.wolf2w.member.service.impl;

import cn.wolfcode.wolf2w.common.exception.LogicException;
import cn.wolfcode.wolf2w.common.redis.service.RedisService;
import cn.wolfcode.wolf2w.common.redis.util.RedisKeys;
import cn.wolfcode.wolf2w.common.util.AssertUtil;
import cn.wolfcode.wolf2w.common.util.Consts;
import cn.wolfcode.wolf2w.member.domain.UserInfo;
import cn.wolfcode.wolf2w.member.mapper.UserInfoMapper;
import cn.wolfcode.wolf2w.member.service.IUserInfoService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
@Transactional
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {
    @Autowired
    private RedisService redisService;


    @Override
    public boolean checkPhone(String phone) {

        //select count(id) from userInfo where phone = #{phone}  count > 0
        //select * from userInfo where phone= #{phone}   user ！= null
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        UserInfo one = super.getOne(wrapper);

        return one != null;
    }

   /* @Value("${sms.url}")
    private String url;
    @Value("${sms.appkey}")
    private String appkey;*/


    @Override
    public void sendVerifyCode(String phone) {

        //创建验证码
        String code = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 4);
        //拼接短信
        StringBuilder sb = new StringBuilder(80);
        sb.append("您注册的验证码是：").append(code).append("，请在")
                .append(Consts.VERIFY_CODE_VAI_TIME).append("分钟之内使用！");

        //发送短信
        System.out.println(sb); //假装发送
        //使用java 发起http请求
        String appkey = "dd1f7d99cd632060789a56cfaa3b77ce";
        String url = "https://way.jd.com/chuangxin/dxjk?mobile={1}&content=【xxxx】你的验证码是：{2}，3分钟内有效！&appkey={3}";

        RestTemplate template = new RestTemplate();
        String ret = template.getForObject(url, String.class, phone, code, appkey);
        System.out.println(ret);

        if(!ret.contains("Success")){
            throw  new LogicException("短信发送失败");
        }

        //缓存验证码--key：phone  value： code

        //唯一性，可读性，时效性，灵活性
        //String key = "verify_code:" + phone;
        //方案1：使用常量类方式
        //String key = Consts.VERIFY_CODE + Consts.splitStr() + phone;
        //方案2： 使用枚举类
        String key = RedisKeys.REGIST_VERIFY_CODE.join(phone);

        redisService.setCacheObject(key, code, RedisKeys.REGIST_VERIFY_CODE.getTime(), TimeUnit.SECONDS);
    }

    @Override
    public void regist(String phone, String nickname, String password, String rpassword, String verifyCode) {
        //判断手机号/昵称/密码/确认密码/验证码是否为空
        AssertUtil.hasText(phone, "手机号码不能为空");
        AssertUtil.hasText(nickname, "昵称不能为空");
        AssertUtil.hasText(password, "密码不能为空");
        AssertUtil.hasText(rpassword, "确认密码不能为空");
        AssertUtil.hasText(verifyCode, "验证码不能为空");
        //判断2次输入密码是否一致
        AssertUtil.isEqauls(password, rpassword, "两次输入密码不一致");
        //判断手机号码格式是否正确-- @TODO java 正则表达式--Pattern
        //判断手机是否唯一
        if(this.checkPhone(phone)){
            throw new LogicException("手机号已经被注册了");
        }

        //判断验证码是否一样
        String key = RedisKeys.REGIST_VERIFY_CODE.join(phone);
        String code = redisService.getCacheObject(key);


        if(!verifyCode.equalsIgnoreCase(code)){
            throw new LogicException("验证码过期或者错误");
        }
        //实现注册
        UserInfo userInfo = new UserInfo();
        userInfo.setNickname(nickname);
        userInfo.setPhone(phone);
        userInfo.setPassword(password);  //假装已经加密
        userInfo.setHeadImgUrl("/images/default.jpg");
        //重要属性自己控制
        userInfo.setState(UserInfo.STATE_NORMAL);
        super.save(userInfo);
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        //username/password 校验--后续自己去校验
        //查询数据库-实现登录
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", username).eq("password", password);
        UserInfo user = super.getOne(wrapper);
        if(user == null){
            throw new LogicException("账号或密码错误");
        }else if(UserInfo.STATE_DISABLE == user.getState()){
            throw new LogicException("账号被冻结了，请联系管理员");
        }
        //缓存用户信息--
        //token为key， user为value
        String token = UUID.randomUUID().toString();
        String key = RedisKeys.USER_LOGIN_TOKEN.join(token);
        //方案1：user对象以string类型缓存到redis中--偏重查询，修改非常麻烦
        // user_login_token:xxx  -----> "{username:’dafei‘, password:‘666’}”

        //方案2：user对象以hash类型缓存到redis中---偏重修改，查询操作麻烦
        // user_login_token:xxx  -----> {
        //                                   username:"dafei",
        //                                   password:"666"
        //                              }
        //当前使用方案1：后续不改动数据
        // String value = user.toString();   //错：不允许  class。。。。
        String value = JSON.toJSONString(user);
        redisService.setCacheObject(key, value,RedisKeys.USER_LOGIN_TOKEN.getTime(), TimeUnit.SECONDS);
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", user);  //对象
        return map;
    }

    @Override
    public UserInfo getUserByToken(String token) {

        if(!StringUtils.hasText(token)){
            return null;    //1
            //throw new LogicException("token不能为null");  // 2
        }
        String key = RedisKeys.USER_LOGIN_TOKEN.join(token);
        if (redisService.hasKey(key)) {
            String userStr = redisService.getCacheObject(key);
            UserInfo userInfo = JSON.parseObject(userStr, UserInfo.class);

            //重置30分钟
            redisService.expire(key, RedisKeys.USER_LOGIN_TOKEN.getTime(), TimeUnit.SECONDS);
            return  userInfo;
        }
        return null;
    }
}
