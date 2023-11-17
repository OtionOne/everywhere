package cn.wolfcode.wolf2w.data.job;

import cn.wolfcode.wolf2w.article.domain.StrategyCondition;
import cn.wolfcode.wolf2w.data.feign.IStrategyConditionFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 攻略条件数据维护定时任务
 */
@Component
public class StrategyConditionDataJob {


    @Autowired
    private IStrategyConditionFeignService strategyConditionFeignService;


    @Scheduled(cron = "0/5 * * * * ? ")
    public void  doWork(){
        System.out.println("--------------------攻略条件数据维护--begin--------------------------->");
        strategyConditionFeignService.conditionDataHandler(StrategyCondition.TYPE_THEME);  //主题
        strategyConditionFeignService.conditionDataHandler(StrategyCondition.TYPE_ABROAD);  //国外
        strategyConditionFeignService.conditionDataHandler(StrategyCondition.TYPE_CHINA);  //国内
        System.out.println("--------------------攻略条件数据维护--end--------------------------->");
    }
}
