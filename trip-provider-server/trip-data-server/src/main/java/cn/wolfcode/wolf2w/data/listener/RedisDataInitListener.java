package cn.wolfcode.wolf2w.data.listener;

import cn.wolfcode.wolf2w.data.feign.IStrategyFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;


/**
 * redis数据初始化监听器
 */
@Component
public class RedisDataInitListener implements ApplicationListener<ContextStartedEvent> {

    @Autowired
    private IStrategyFeignService strategyFeignService;

    //当spring容器启动/初始化完成之后马上执行
    @Override
    public void onApplicationEvent(ContextStartedEvent contextRefreshedEvent) {
        System.out.println("--------------攻略统计对象初始化-begin--------------------------"+strategyFeignService);
        if(strategyFeignService != null){
            strategyFeignService.statisDataInit();
        }

        System.out.println("--------------攻略统计对象初始化-end--------------------------" + strategyFeignService);
    }
}
