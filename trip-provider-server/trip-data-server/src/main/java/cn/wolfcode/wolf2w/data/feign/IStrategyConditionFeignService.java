package cn.wolfcode.wolf2w.data.feign;

import cn.wolfcode.wolf2w.common.web.response.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "trip-article-server")
public interface IStrategyConditionFeignService {
    @PostMapping("/strategyConditions/conditionDataHandler")
    JsonResult<Void> conditionDataHandler(@RequestParam("type") int type);


}
