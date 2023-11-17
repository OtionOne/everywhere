package cn.wolfcode.wolf2w.article.service.impl;

import cn.wolfcode.wolf2w.article.domain.Strategy;
import cn.wolfcode.wolf2w.article.domain.StrategyCondition;
import cn.wolfcode.wolf2w.article.mapper.StrategyConditionMapper;
import cn.wolfcode.wolf2w.article.service.IStrategyConditionService;
import cn.wolfcode.wolf2w.article.service.IStrategyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* 攻略条件服务接口实现
*/
@Service
@Transactional
public class StrategyConditionServiceImpl extends ServiceImpl<StrategyConditionMapper,StrategyCondition> implements IStrategyConditionService {

    @Autowired
    private IStrategyService strategyService;



    @Override
    public List<StrategyCondition> queryByType(int type) {

        //select * from strategy_condition
        //where type = 3 and statis_time in(select max(statis_time) from strategy_condition where type = 3)
        //order by count desc
        QueryWrapper<StrategyCondition> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        wrapper.inSql("statis_time", "select max(statis_time) from strategy_condition where type = " + type);
        wrapper.orderByDesc("count");
        return super.list(wrapper);
    }

    @Override
    public void conditionDataHandler(int type) {
        //当前折腾主题
        if(StrategyCondition.TYPE_THEME == type){
            //1:查大表（strategy）
            QueryWrapper<Strategy> wrapper = new QueryWrapper<>();
            wrapper.select("theme_id refid,  theme_name name, count(id) count");
            wrapper.groupBy("theme_id", "theme_name");
            wrapper.orderByDesc("count");
            List<Map<String, Object>> maps = strategyService.listMaps(wrapper);
            //2：添加小表(strategy_condition)
            List<StrategyCondition>  scs = new ArrayList<>();
            Date now = new Date();
            for (Map<String, Object> map : maps) {
                StrategyCondition sc = new StrategyCondition();
                sc.setRefid(Long.parseLong(map.get("refid").toString()));
                sc.setName(map.get("name").toString());
                sc.setCount(Integer.parseInt(map.get("count").toString()));
                sc.setStatisTime(now);
                sc.setType(type);
                scs.add(sc);
            }
            super.saveBatch(scs);
        }else if(StrategyCondition.TYPE_ABROAD == type){
            //1:查大表（strategy）
            QueryWrapper<Strategy> wrapper = new QueryWrapper<>();
            wrapper.select("dest_id refid,  dest_name name, count(id) count");
            wrapper.eq("isabroad", 1);
            wrapper.groupBy("dest_id", "dest_name");
            wrapper.orderByDesc("count");
            List<Map<String, Object>> maps = strategyService.listMaps(wrapper);
            //2：添加小表(strategy_condition)
            List<StrategyCondition>  scs = new ArrayList<>();
            Date now = new Date();
            for (Map<String, Object> map : maps) {
                StrategyCondition sc = new StrategyCondition();
                sc.setRefid(Long.parseLong(map.get("refid").toString()));
                sc.setName(map.get("name").toString());
                sc.setCount(Integer.parseInt(map.get("count").toString()));
                sc.setStatisTime(now);
                sc.setType(type);
                scs.add(sc);
            }
            super.saveBatch(scs);
        }else if(StrategyCondition.TYPE_CHINA == type){
            //1:查大表（strategy）
            QueryWrapper<Strategy> wrapper = new QueryWrapper<>();
            wrapper.select("dest_id refid,  dest_name name, count(id) count");
            wrapper.eq("isabroad", 0);
            wrapper.groupBy("dest_id", "dest_name");
            wrapper.orderByDesc("count");
            List<Map<String, Object>> maps = strategyService.listMaps(wrapper);
            //2：添加小表(strategy_condition)
            List<StrategyCondition>  scs = new ArrayList<>();
            Date now = new Date();
            for (Map<String, Object> map : maps) {
                StrategyCondition sc = new StrategyCondition();
                sc.setRefid(Long.parseLong(map.get("refid").toString()));
                sc.setName(map.get("name").toString());
                sc.setCount(Integer.parseInt(map.get("count").toString()));
                sc.setStatisTime(now);
                sc.setType(type);
                scs.add(sc);
            }
            super.saveBatch(scs);
        }




    }
}
