package cn.wolfcode.wolf2w.gateway.filter;

import cn.wolfcode.wolf2w.common.web.response.JsonResult;
import cn.wolfcode.wolf2w.gateway.util.Md5Utils;
import com.alibaba.fastjson2.JSON;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 签名拦截(防篡改)
 */
//@Component
public class SignFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //签名验证
        Map<String, Object> param = new HashMap<>();

        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
            if ("sign".equals(entry.getKey())){
                continue;
            }
            param.put(entry.getKey(), entry.getValue().get(0));
        }
        String signatures = Md5Utils.signatures(param);
        String sign = queryParams.getFirst("sign");
        if(sign == null || !sign.equalsIgnoreCase(signatures)){
            exchange.getResponse().getHeaders().add("Content-Type","application/json;charset=UTF-8");
            String ret = JSON.toJSONString(new JsonResult(501, "签名校验失败", "不好意思咯"));
            byte[] bytes = ret.getBytes(StandardCharsets.UTF_8);
            DataBuffer wrap = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(wrap));
        }

        return chain.filter(exchange);
    }
}
