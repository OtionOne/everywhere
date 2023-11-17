package cn.wolfcode.wolf2w.common.sercurity.interceptor;

import cn.wolfcode.wolf2w.common.redis.service.RedisService;
import cn.wolfcode.wolf2w.common.redis.util.RedisKeys;
import cn.wolfcode.wolf2w.common.sercurity.annotation.RequireLogin;
import cn.wolfcode.wolf2w.common.web.response.JsonResult;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 用户登录拦截
 */
public class CheckLoginInterceptor  implements HandlerInterceptor {
    @Autowired
    private RedisService redisService;
    //拦截逻辑
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println(request.getRequestURI());
        System.out.println(request.getMethod());
        System.out.println(handler);
        System.out.println(handler.getClass());
        System.out.println("------------------------");


        /**
         * HandlerMethod : 请求映射方法信息封装对象(信息处理对象)， 就是请求映射方法(接口)在springmvc中抽象对象
         *  里面封装请求映射方法所有信息： 方法名/方法参数/方法返回值/方法注解/映射路径等等....
         *
         *  1>Springmvc启动时候，解析所有请求映射方法，并将这些方法解析并封装为一个一个HandlerMethod对象
         *
         *  2>为了方便管理，mvc使用类似map集合封装这些对象
         *    key: 接口的url地址， value：HandlerMethod
         *    Map<String, HandlerMethod> map = ...
         *     map.put("/userInfos/currentUser", currentUser接口的封装HandlerMethod对象)
         *     map.put("/userInfos/login", login接口的封装HandlerMethod对象)
         *
         *  3>请求进入前端控制时，会解析请求上url地址，马上执行拦截器路径匹配，如果路劲满足拦截器拦截规则，那么该拦截器会拦截该路径
         *       CheckLoginInterceptor  --拦截规则： /**     满足： /userInfos/currentUser   所以， 当
         *      请求： /userInfos/currentUser  触发：CheckLoginInterceptor拦截器的拦截
         *
         *     拦截逻辑怎么执行的：
         *       请求url： /userInfos/currentUser   ---> 查询该url对应  HandlerMethod
         *        HandlerMethod handler = map.get("/userInfos/currentUser");
         *
         *       找拦截url的拦截器：CheckLoginInterceptor ----满足-----> checkLoginInterceptor
         *       checkLoginInterceptor.preHandle(request, response, handler);
         *
         *  springmvc 执行流程其中 拦截器执行部分
         *
         */

        //放行跨域预请求--放行后有可以进行跨域匹配，进而值跨域响应头
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        //当前请求你对应接口方法是否贴：@RequieLogin
        HandlerMethod hm = (HandlerMethod) handler;


        String token = request.getHeader("token");
        String userStr = redisService.getCacheObject(RedisKeys.USER_LOGIN_TOKEN.join(token));

        //重置30分钟
        if(StringUtils.hasText(userStr)){
            redisService.expire(RedisKeys.USER_LOGIN_TOKEN.join(token), RedisKeys.USER_LOGIN_TOKEN.getTime(), TimeUnit.SECONDS);
        }

        //表示接口贴有@RequireLogin
        if (hm.hasMethodAnnotation(RequireLogin.class)) {

            //预请求没有带token， 登录检查时不通过，直接返回false， 没有机会进行跨域匹配，导致预请求直接结束。没有设置到跨域的响应头

            if(!StringUtils.hasText(userStr)){
                //没有登录
                //json 格式提示
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(JSON.toJSONString(JsonResult.noLogin()));
                return false;
            }
        }

        //表示接口没有贴@RequireLogin
        return true;
    }
}
