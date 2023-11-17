package cn.wolfcode.wolf2w.article.controller;

import cn.wolfcode.wolf2w.article.domain.StrategyCondition;
import cn.wolfcode.wolf2w.article.domain.StrategyRank;
import cn.wolfcode.wolf2w.article.service.IStrategyConditionService;
import cn.wolfcode.wolf2w.article.service.IStrategyRankService;
import cn.wolfcode.wolf2w.common.web.response.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("strategyConditions")
public class StrategyConditionController {


    @Autowired
    private IStrategyConditionService strategyConditionService;


    @GetMapping("/condition")
    public JsonResult condition(int type){
        List<StrategyCondition> list = strategyConditionService.queryByType(type);
        return JsonResult.success(list);
    }

    @PostMapping("/conditionDataHandler")
    public JsonResult conditionDataHandler(int type){
        strategyConditionService.conditionDataHandler(type);
        return JsonResult.success();
    }


}
