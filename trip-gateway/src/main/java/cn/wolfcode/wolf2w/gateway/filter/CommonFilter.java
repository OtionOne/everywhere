package cn.wolfcode.wolf2w.gateway.filter;

import cn.wolfcode.wolf2w.common.redis.service.RedisService;
import cn.wolfcode.wolf2w.common.redis.util.RedisKeys;
import cn.wolfcode.wolf2w.common.util.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Component
public class CommonFilter implements GlobalFilter {
    @Autowired
    private RedisService redisService;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        /**
         * pre拦截逻辑
         * 在请求去到微服务之前，做了两个处理
         * 1.把客户端真实IP通过请求同的方式传递给微服务
         * 2.在请求头中添加FEIGN_REQUEST的请求头，值为0，标记请求不是Feign调用，而是客户端调用
         */
        ServerHttpRequest request = exchange.getRequest().mutate().
                header(Consts.REAL_IP,exchange.getRequest().getRemoteAddress().getHostString()).
                header(Consts.FEIGN_REQUEST_KEY,Consts.FEIGN_REQUEST_FALSE).
                build();
        return chain.filter(exchange.mutate().request(request).build()).then(Mono.fromRunnable(()->{
            /**
             * post拦截逻辑
             * 在请求执行完微服务之后,需要刷新token在redis的时间
             * 判断token不为空 && Redis还存在这个token对于的key,这时候需要延长Redis中对应key的有效时间.
             */
            String token =exchange.getRequest().getHeaders().getFirst(Consts.TOKEN_NAME);
            if(StringUtils.hasText(token)){
                redisService.expire(RedisKeys.USER_LOGIN_TOKEN.join(token), RedisKeys.USER_LOGIN_TOKEN.getTime(), TimeUnit.SECONDS);
            }
        }));
    }
}