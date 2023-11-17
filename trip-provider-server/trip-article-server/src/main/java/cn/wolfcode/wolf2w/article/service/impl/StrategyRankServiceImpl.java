package cn.wolfcode.wolf2w.article.service.impl;

import cn.wolfcode.wolf2w.article.domain.Strategy;
import cn.wolfcode.wolf2w.article.domain.StrategyRank;
import cn.wolfcode.wolf2w.article.mapper.StrategyRankMapper;
import cn.wolfcode.wolf2w.article.service.IStrategyRankService;
import cn.wolfcode.wolf2w.article.service.IStrategyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* 攻略排行服务接口实现
*/
@Service
@Transactional
public class StrategyRankServiceImpl extends ServiceImpl<StrategyRankMapper, StrategyRank> implements IStrategyRankService {


    @Autowired
    private IStrategyService strategyService;


    @Override
    public List<StrategyRank> queryByType(int type) {
        QueryWrapper<StrategyRank> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        wrapper.inSql("statis_time", "(select max(statis_time) from strategy_rank where type = "+type+")");
        wrapper.orderByDesc("statisnum");
        wrapper.last("limit 10");
        return super.list(wrapper);
    }

    @Override
    public void rankDataHandler(int type) {
        //暂时先折腾热门
        //1：查询大表(strategy)
        QueryWrapper<Strategy> wrapper = new QueryWrapper<>();
        if(StrategyRank.TYPE_HOT == type){
            //select  * from strategy order by viewnum + replynum  desc limit 10
            wrapper.orderByDesc("viewnum + replynum");
        }else if(StrategyRank.TYPE_CHINA == type){
            //国内 select * from strategy  where isabroad = 0 order by favornum + thumbsupnum  desc limit 10;
            wrapper.orderByDesc("favornum + thumbsupnum");
            wrapper.eq("isabroad", 0);
        }else if(StrategyRank.TYPE_ABROAD == type){
            //国内 select * from strategy  where isabroad = 1 order by favornum + thumbsupnum  desc limit 10;
            wrapper.orderByDesc("favornum + thumbsupnum");
            wrapper.eq("isabroad", 1);
        }
        wrapper.last("limit 10");
        List<Strategy> list = strategyService.list(wrapper);
        Date now = new Date();
        List<StrategyRank> ranks = new ArrayList<>();
        //2: 添加到小表(strategy_rank)
        for (Strategy strategy : list) {
            StrategyRank rank = new StrategyRank();
            rank.setStrategyTitle(strategy.getSubTitle());
            rank.setStrategyId(strategy.getId());
            rank.setDestId(strategy.getDestId());
            rank.setDestName(strategy.getDestName());
            if(StrategyRank.TYPE_HOT == type){
                rank.setStatisnum(strategy.getReplynum() + strategy.getViewnum() + 0L);
            }else{
                rank.setStatisnum(strategy.getFavornum() + strategy.getThumbsupnum() + 0L);
            }
            rank.setStatisTime(now);
            rank.setType(type);
            ranks.add(rank);
        }
        super.saveBatch(ranks);

    }
}
