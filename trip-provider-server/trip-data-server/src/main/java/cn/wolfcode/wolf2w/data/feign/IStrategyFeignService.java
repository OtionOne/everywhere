package cn.wolfcode.wolf2w.data.feign;

import cn.wolfcode.wolf2w.common.web.response.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "trip-article-server")
public interface IStrategyFeignService {

    @PostMapping("/strategies/statisDataInit")
    JsonResult<Void> statisDataInit();

    @PostMapping("/strategies/statisDataPersistence")
    JsonResult<Void> statisDataPersistence();

}
