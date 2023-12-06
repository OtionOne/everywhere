package com.powernode.everywhere.auth.interceptor;

import com.powernode.everywhere.common.core.exception.CoolDownException;
import com.powernode.everywhere.common.redis.utils.RedisCache;
import com.powernode.everywhere.user.anno.LoginRequired;
import com.powernode.everywhere.user.domain.VO.LoginUser;
import com.powernode.everywhere.user.key.UserRedisKeyPrefix;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component
public class LoginInterceptor implements HandlerInterceptor {


    private final RedisCache redisCache;

    @Value("${jwt.secret}")
    private String secret;

    public LoginInterceptor(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        //判断是否有LoginRequired注解
        // 0.2 将 handler 对象转换为 HandlerMethod 对象
        HandlerMethod method = (HandlerMethod) handler;
        // 0.3 从 HandlerMethod 对象中获取对应的 Controller 对象
        Class<?> controllerClass = method.getBeanType();
        // 0.4 分别从 Controller 和 HandlerMethod 上获取 @RequireLogin 注解
        LoginRequired classAnno = controllerClass.getAnnotation(LoginRequired.class);
        LoginRequired methodAnno = method.getMethodAnnotation(LoginRequired.class);
        // 0.5 如果一个都拿不到, 直接放行
        if (classAnno == null && methodAnno == null) {
            return true;
        }
        //从请求头中拿到token
        String token = request.getHeader("token");
        if (token == null || token.equals("")) {
            //没有token，未登录
            throw new CoolDownException("用户未认证,是不是没有登录捏o.O?");
        }
        //判断jwt是否有效
        try {
            Jws<Claims> line = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            Claims body = line.getBody();
            String uuid = String.valueOf(body.get("uuid"));
            UserRedisKeyPrefix prefix = UserRedisKeyPrefix.USER_LOGIN_INFO_STRING;
            String key = prefix.getPrefix()+":" + uuid;
            LoginUser user = redisCache.getCacheObject(key);
            if (user == null) {
                //token过期
                throw new CoolDownException("用户长期未操作,登录过期喵qwq");
            }
            long now = System.currentTimeMillis();

            if (now - user.getExpireTime() < 20 * 60 * 1000) {
                //还有不到20分钟过期，刷新token
                user.setLoginTime(now);
                user.setExpireTime(now + 30 * 60 * 1000);
                redisCache.setCacheObject(key, user, 30L, TimeUnit.MINUTES);
            }
        }catch (SignatureException e){
            throw new CoolDownException("认证令牌被人篡改辣!!!重新登录试一下吧o.O?");
        }


        return true;
    }
}
