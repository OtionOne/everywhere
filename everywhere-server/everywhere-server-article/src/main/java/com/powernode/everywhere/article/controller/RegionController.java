package com.powernode.everywhere.article.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.everywhere.article.domain.Destination;
import com.powernode.everywhere.article.domain.Region;
import com.powernode.everywhere.article.service.DestinationService;
import com.powernode.everywhere.article.service.RegionService;
import com.powernode.everywhere.common.core.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article/region")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @Autowired
    private DestinationService destinationService;


    @GetMapping("/pageList")
    public JsonResult pageList(Page<Region> pageInfo) {
        return JsonResult.success(regionService.page(pageInfo));
    }


    @GetMapping("/detail")
    public JsonResult getById(String id) {
        return JsonResult.success(regionService.getById(id));
    }


    @GetMapping("/listAll")
    public JsonResult listAll() {
        return JsonResult.success(regionService.list());
    }

    @PostMapping("/saveOne")
    public JsonResult saveOne(Region region){
        return JsonResult.success(regionService.save(region));
    }

    @PostMapping("/updateOne")
    public JsonResult updateOne(Region region){
        return JsonResult.success(regionService.updateById(region));
    }

    @PostMapping("/deleteOne/{id}")
    public JsonResult deleteOne(@PathVariable String id){
        return JsonResult.success(regionService.removeById(id));
    }

    @GetMapping("/{id}/destinations")
    public JsonResult getDestination(@PathVariable Long id){
        return JsonResult.success(destinationService.fetchMySon(id));
    }


    @GetMapping("/hotRegion")
    public JsonResult hotRegion(){
        return JsonResult.success(regionService.getHotRegions());
    }

}
