package cn.wolfcode.wolf2w.article.service;

import cn.wolfcode.wolf2w.article.domain.StrategyCondition;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * 攻略条件服务接口
 */
public interface IStrategyConditionService extends IService<StrategyCondition>{


    /**
     * 根据类型查询攻略条件列表
     * @param type
     * @return
     */
    List<StrategyCondition> queryByType(int type);

    /**
     * 攻略条件数据维护
     * @param type
     */
    void conditionDataHandler(int type);
}
