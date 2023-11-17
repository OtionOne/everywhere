package cn.wolfcode.wolf2w.data.job;

import cn.wolfcode.wolf2w.data.feign.IStrategyFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/**
 * redis数据持久化定时任务
 */
@Component
public class RedisDataPersistenceJob {

    @Autowired
    private IStrategyFeignService strategyFeignService;

    @Scheduled(cron = "0/5 * * * * ? ")
    public void  doWork(){
        System.out.println("--------------------攻略统计数据持久化--begin--------------------------->" + strategyFeignService);
        strategyFeignService.statisDataPersistence();
        System.out.println("--------------------攻略统计数据持久化--end--------------------------->");
    }
}
