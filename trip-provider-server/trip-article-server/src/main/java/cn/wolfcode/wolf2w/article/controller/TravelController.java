package cn.wolfcode.wolf2w.article.controller;

import cn.wolfcode.wolf2w.article.domain.Travel;
import cn.wolfcode.wolf2w.article.domain.TravelContent;
import cn.wolfcode.wolf2w.article.feign.IUserInfoFeignService;
import cn.wolfcode.wolf2w.article.query.TravelQuery;
import cn.wolfcode.wolf2w.article.service.IDestinationService;
import cn.wolfcode.wolf2w.article.service.ITravelService;
import cn.wolfcode.wolf2w.common.web.response.JsonResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("travels")
public class TravelController {
    @Autowired
    private ITravelService travelService;
    @Autowired
    private IDestinationService destinationService;

    @Autowired
    private IUserInfoFeignService userInfoFeignService;

    @GetMapping("/query")
    public JsonResult query(TravelQuery qo){
        IPage<Travel> page =  travelService.queryPage(qo);
        return  JsonResult.success(page);
    }

    @GetMapping("/detail")
    public JsonResult detail(Long id){
        Travel travel = travelService.getById(id);
        TravelContent content = travelService.getContent(id);
        travel.setContent(content);
        travel.setAuthor(userInfoFeignService.detail(travel.getAuthorId()).getData());
        return  JsonResult.success(travel);
    }


    @GetMapping("/list")
    public JsonResult list(){
        return  JsonResult.success(travelService.list());
    }

    @GetMapping("/content")
    public JsonResult content(Long id){
        TravelContent content = travelService.getContent(id);
        return  JsonResult.success(content);
    }
    @PostMapping("/save")
    public JsonResult save(Travel travel){
        travelService.saveOrUpdate(travel);
        return  JsonResult.success();
    }

    @PostMapping("/audit")
    public JsonResult audit(Long id, int state){

        travelService.audit(id, state);
        return  JsonResult.success();
    }

    @PostMapping("/update")
    public JsonResult update(Travel travel){
        travelService.saveOrUpdate(travel);
        return  JsonResult.success();
    }
    @PostMapping("/delete/{id}")
    public JsonResult delete(@PathVariable Long id){
        travelService.removeById(id);
        travelService.removeContent(id);
        return  JsonResult.success();
    }

    @GetMapping("/queryByDestName")
    public JsonResult queryByDestName(String destName){
        List<Travel> list = travelService.queryByDestName(destName);
        return JsonResult.success(list);
    }
}
