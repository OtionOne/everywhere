package cn.wolfcode.wolf2w.article.controller;

import cn.wolfcode.wolf2w.article.domain.Destination;
import cn.wolfcode.wolf2w.article.domain.Region;
import cn.wolfcode.wolf2w.article.query.RegionQuery;
import cn.wolfcode.wolf2w.article.service.IDestinationService;
import cn.wolfcode.wolf2w.article.service.IRegionService;
import cn.wolfcode.wolf2w.common.web.response.JsonResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("regions")
public class RegionController {


    @Autowired
    private IRegionService regionService;

    @Autowired
    private IDestinationService destinationService;


    @GetMapping("/detail")
    public JsonResult detail(Long id){
        return JsonResult.success(regionService.getById(id));
    }


    @PostMapping("/save")
    public JsonResult save(Region region){
        regionService.save(region);
        return JsonResult.success();
    }

    @PostMapping("/update")
    public JsonResult update(Region region){
        regionService.updateById(region);
        return JsonResult.success();
    }


    @GetMapping("/query")
    public JsonResult query(RegionQuery qo){
        Page<Region> page = regionService.queryPage(qo);
        return JsonResult.success(page);
    }


    //参数路径
    @GetMapping("/{id}/destination")
    public JsonResult queryDestByRid(@PathVariable Long id){
        List<Destination> list = destinationService.queryByRid(id);
        return JsonResult.success(list);
    }


    //普通参数
    @GetMapping("/destination")
    public JsonResult queryDestByRid2(Long rid){
        List<Destination> list = destinationService.queryByRidForWebsite(rid);
        return JsonResult.success(list);
    }


    @PostMapping("/delete/{id}")
    public JsonResult delete(@PathVariable Long id){
        regionService.removeById(id);
        return JsonResult.success();
    }



    @GetMapping("/hot")
    public JsonResult hot(){
        List<Region> list = regionService.queryHot();
        return JsonResult.success(list);
    }
}
