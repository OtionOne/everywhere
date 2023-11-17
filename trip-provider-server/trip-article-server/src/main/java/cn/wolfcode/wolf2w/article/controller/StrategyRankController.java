package cn.wolfcode.wolf2w.article.controller;

import cn.wolfcode.wolf2w.article.domain.StrategyRank;
import cn.wolfcode.wolf2w.article.service.IStrategyRankService;
import cn.wolfcode.wolf2w.common.web.response.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("strategyRanks")
public class StrategyRankController {

    @Autowired
    private IStrategyRankService strategyRankService;

    @GetMapping("/rank")
    public JsonResult rank(int type){
        List<StrategyRank> list = strategyRankService.queryByType(type);
        return JsonResult.success(list);
    }

    @PostMapping("/rankDataHandler")
    public JsonResult rankDataHandler(int type){
        strategyRankService.rankDataHandler(type);
        return JsonResult.success();
    }

}
