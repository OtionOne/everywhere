package cn.wolfcode.wolf2w.common.sercurity.resolver;

import cn.wolfcode.wolf2w.common.redis.service.RedisService;
import cn.wolfcode.wolf2w.common.redis.util.RedisKeys;
import cn.wolfcode.wolf2w.common.sercurity.annotation.UserParam;
import cn.wolfcode.wolf2w.member.domain.UserInfo;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;


/**
 * 自定义用户参数解析器
 * 作用：将请求映射方法中的UserInfo类型的参数解析成当前登录用户对象， 并注入到
 */
public class UserInfoArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private RedisService redisService;

    //指定当前参数解析器能支持解析的参数类型
    //此处表示该解析器只解析UserInfo类型。
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == UserInfo.class
                && parameter.hasParameterAnnotation(UserParam.class);
    }
    //执行解析逻辑，前提：supportsParameter 方法返回值必须为true
    //此处为：将userInfo类型解析成当前登录用户对象。
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        //获取当前登录用户对象
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = request.getHeader("token");
        String userStr = redisService.getCacheObject(RedisKeys.USER_LOGIN_TOKEN.join(token));
        return JSON.parseObject(userStr, UserInfo.class);
    }
}
