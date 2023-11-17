package cn.wolfcode.wolf2w.article.service;


import cn.wolfcode.wolf2w.article.domain.StrategyRank;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * 攻略排行服务接口
 */
public interface IStrategyRankService extends IService<StrategyRank> {

    /**
     * 通过type类型查询对应rank排行数据
     * @param type
     * @return
     */
    List<StrategyRank> queryByType(int type);

    /**
     * 攻略排行数据维护
     * @param type
     */
    void rankDataHandler(int type);


}
