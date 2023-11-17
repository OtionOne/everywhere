package cn.wolfcode.wolf2w.data.job;

import cn.wolfcode.wolf2w.article.domain.StrategyRank;
import cn.wolfcode.wolf2w.data.feign.IStrategyRankFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 攻略排行分时统计报数据维护定时任务
 */
@Component
public class StartegyRankDataJob {
    @Autowired
    private IStrategyRankFeignService strategyRankFeignService;

    @Scheduled(cron = "0/5 * * * * ? ")
    public void doWork(){
        System.out.println("-------------------------攻略排行数据维护--begin---------------------------------->");
        strategyRankFeignService.rankDataHandler(StrategyRank.TYPE_ABROAD);  //国外
        strategyRankFeignService.rankDataHandler(StrategyRank.TYPE_CHINA);  //国内
        strategyRankFeignService.rankDataHandler(StrategyRank.TYPE_HOT);    //热门
        System.out.println("-------------------------攻略排行数据维护--end---------------------------------->");
    }
}
