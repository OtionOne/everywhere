package cn.wolfcode.wolf2w.gateway.filter;

import cn.wolfcode.wolf2w.common.redis.service.RedisService;
import cn.wolfcode.wolf2w.common.redis.util.RedisKeys;
import cn.wolfcode.wolf2w.common.web.response.JsonResult;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

//@Component
public class BrushProofFilter implements GlobalFilter {
    @Autowired
    private RedisService redisService;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String url = exchange.getRequest().getURI().getPath();
        String ip = exchange.getRequest().getRemoteAddress().getAddress().toString();

        String key = RedisKeys.BRUSH_PROOF.join(url, ip);
        redisService.setnxCacheObject(key, 10, RedisKeys.BRUSH_PROOF.getTime(), TimeUnit.SECONDS);
        Long decrement = redisService.decrementCacheObjectValue(key);

        if(decrement < 0){
            exchange.getResponse().getHeaders().add("Content-Type","application/json;charset=UTF-8");

            String ret = JSON.toJSONString(JsonResult.error(500, "请勿频繁访问", "谢谢咯"));
            byte[] bytes = ret.getBytes(StandardCharsets.UTF_8);

            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }
        return chain.filter(exchange);
    }
}
