package cn.wolfcode.wolf2w.search.controller;

import cn.wolfcode.wolf2w.article.domain.Destination;
import cn.wolfcode.wolf2w.article.domain.Strategy;
import cn.wolfcode.wolf2w.article.domain.Travel;
import cn.wolfcode.wolf2w.common.util.ParamMap;
import cn.wolfcode.wolf2w.common.web.response.JsonResult;
import cn.wolfcode.wolf2w.member.domain.UserInfo;
import cn.wolfcode.wolf2w.search.domain.DestinationEs;
import cn.wolfcode.wolf2w.search.domain.StrategyEs;
import cn.wolfcode.wolf2w.search.domain.TravelEs;
import cn.wolfcode.wolf2w.search.domain.UserInfoEs;
import cn.wolfcode.wolf2w.search.feign.IDestinationFeignService;
import cn.wolfcode.wolf2w.search.feign.IStrategyFeignService;
import cn.wolfcode.wolf2w.search.feign.ITravelFeignService;
import cn.wolfcode.wolf2w.search.feign.IUserInfoFeignService;
import cn.wolfcode.wolf2w.search.query.SearchQueryObject;
import cn.wolfcode.wolf2w.search.service.*;
import cn.wolfcode.wolf2w.search.vo.SearchResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
public class SearchController {
    @Autowired
    private IDestinationFeignService destinationFeignService;
    @Autowired
    private IUserInfoFeignService userInfoFeignService;
    @Autowired
    private ITravelFeignService travelFeignService;
    @Autowired
    private IStrategyFeignService strategyFeignService;

    @Autowired
    private ISearchService searchService;
    //es服务
    @Autowired
    private IDestinationEsService destinationEsService;
    @Autowired
    private IStrategyEsService strategyEsService;
    @Autowired
    private ITravelEsService travelEsService;
    @Autowired
    private IUserInfoEsService userInfoEsService;



    @GetMapping("/q")
    public JsonResult search(SearchQueryObject qo) throws UnsupportedEncodingException {

        qo.setKeyword(URLDecoder.decode(qo.getKeyword(), "UTF-8"));


        switch (qo.getType()){
            case SearchQueryObject.TYPE_DEST: return this.searchDest(qo);
            case SearchQueryObject.TYPE_STRATEGY: return this.searchStrategy(qo);
            case SearchQueryObject.TYPE_TRAVEL: return this.searchTravel(qo);
            case SearchQueryObject.TYPE_USER: return this.searchUser(qo);
            default: return this.searchAll(qo);
        }
    }
    //精确查询目的地
    private JsonResult searchDest(SearchQueryObject qo) {

        //页面输入目的地，精确查询该目的地对象：dest
        Destination dest = destinationFeignService.queryByName(qo.getKeyword()).getData();

        SearchResultVO vo = new SearchResultVO();

        //dest不为空， 查询该目的地下所有攻略，游记， 用户
        if(dest != null){
            List<Travel> ts = travelFeignService.queryByDestName(dest.getName()).getData();
            List<Strategy> sts = strategyFeignService.queryByDestName(dest.getName()).getData();
            List<UserInfo> us = userInfoFeignService.queryByCity(dest.getName()).getData();

            vo.setTravels(ts);
            vo.setStrategys(sts);
            vo.setUsers(us);
            vo.setTotal(ts.size() + sts.size() + us.size() + 0L);

        }
        //如果为空，提示
        return  JsonResult.success(ParamMap.newInstance().put("result", vo).put("qo", qo).put("dest", dest));
    }
    //攻略搜索
    private JsonResult searchStrategy(SearchQueryObject qo) {
        //全文搜索  + 高亮显示  + 分页
        return  JsonResult.success(ParamMap.newInstance().put("page", this.createStrategyPage(qo)).put("qo", qo));
    }
    //游记搜索
    private JsonResult searchTravel(SearchQueryObject qo) {
        //全文搜索  + 高亮显示  + 分页
        return  JsonResult.success(ParamMap.newInstance().put("page", this.createTravelPage(qo)).put("qo", qo));
    }
    //用户搜索
    private JsonResult searchUser(SearchQueryObject qo) {
        //全文搜索  + 高亮显示  + 分页
        return  JsonResult.success(ParamMap.newInstance().put("page", this.createUserPage(qo)).put("qo", qo));
    }

    //搜索所有
    private JsonResult searchAll(SearchQueryObject qo) {

        SearchResultVO vo = new SearchResultVO();

        Page<Strategy> sts = this.createStrategyPage(qo);
        Page<Travel> ts = this.createTravelPage(qo);
        Page<UserInfo> us = this.createUserPage(qo);
        Page<Destination> ds = this.createDestPage(qo);

        vo.setStrategys(sts.getContent());
        vo.setUsers(us.getContent());
        vo.setTravels(ts.getContent());
        vo.setDests(ds.getContent());

        vo.setTotal(sts.getTotalElements() + us.getTotalElements()
                + ts.getTotalElements() + ds.getTotalElements());

        return  JsonResult.success(ParamMap.newInstance().put("result", vo).put("qo", qo));
    }

    private Page<Strategy> createStrategyPage(SearchQueryObject qo){
        return searchService.searchWithHighlight(StrategyEs.INDEX_NAME, Strategy.class, qo,
                "title", "subTitle", "summary");
    }
    private Page<Travel> createTravelPage(SearchQueryObject qo){
        Page<Travel> ts = searchService.searchWithHighlight(TravelEs.INDEX_NAME, Travel.class, qo,
                "title", "summary");
            for (Travel t : ts) {
            t.setAuthor(userInfoFeignService.detail(t.getAuthorId()).getData());
        }
        return ts;
    }
    private Page<UserInfo> createUserPage(SearchQueryObject qo){
        return searchService.searchWithHighlight(UserInfoEs.INDEX_NAME, UserInfo.class, qo,
                "info", "city");
    }
    private Page<Destination> createDestPage(SearchQueryObject qo){
        return searchService.searchWithHighlight(DestinationEs.INDEX_NAME, Destination.class, qo,
                "info", "name");
    }





    @GetMapping("/dataInit")
    public JsonResult dataInit(SearchQueryObject qo) {

//攻略
        List<Strategy> sts = strategyFeignService.list().getData();
        for (Strategy st : sts) {
            StrategyEs es = new StrategyEs();
            BeanUtils.copyProperties(st, es);
            strategyEsService.save(es);
        }
        //游记
        List<Travel> ts = travelFeignService.list().getData();
        for (Travel t : ts) {
            TravelEs es = new TravelEs();
            BeanUtils.copyProperties(t, es);
            travelEsService.save(es);
        }
        List<UserInfo> uf = userInfoFeignService.list().getData();
        for (UserInfo u : uf) {
            UserInfoEs es = new UserInfoEs();
            BeanUtils.copyProperties(u, es);
            userInfoEsService.save(es);
        }
        //目的地
        List<Destination> dests = destinationFeignService.list().getData();
        for (Destination d : dests) {
            DestinationEs es = new DestinationEs();
            BeanUtils.copyProperties(d, es);
            destinationEsService.save(es);
        }


        return JsonResult.success();
    }



}
