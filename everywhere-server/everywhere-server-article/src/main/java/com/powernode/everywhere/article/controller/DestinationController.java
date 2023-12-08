package com.powernode.everywhere.article.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.everywhere.article.domain.Destination;
import com.powernode.everywhere.article.qo.DestinationQuery;
import com.powernode.everywhere.article.service.DestinationService;
import com.powernode.everywhere.article.service.RegionService;
import com.powernode.everywhere.common.core.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article/destination")
public class DestinationController {

    @Autowired
    private DestinationService DestinationService;


    @GetMapping("/detail")
    public JsonResult getById(String id) {
        return JsonResult.success(DestinationService.getById(id));
    }


    @GetMapping("/listAll")
    public JsonResult listAll() {
        return JsonResult.success(DestinationService.list());
    }

    @PostMapping("/saveOne")
    public JsonResult saveOne(Destination destination) {
        return JsonResult.success(DestinationService.save(destination));
    }

    @PostMapping("/updateOne")
    public JsonResult updateOne(Destination destination) {
        return JsonResult.success(DestinationService.updateById(destination));
    }

    @PostMapping("/deleteOne/{id}")
    public JsonResult deleteOne(@PathVariable Long id) {
        return JsonResult.success(DestinationService.removeById(id));
    }


    @GetMapping("/pageList")
    public JsonResult pageList(DestinationQuery query) {
        return JsonResult.success(DestinationService.pageOut(query));
    }

    @GetMapping("/toasts")
    public JsonResult getToasts(Long destId) {
        return JsonResult.success(DestinationService.getToasts(destId));
    }

    @GetMapping("/getDestinationByRegionID")
    public JsonResult getDestinationByRegionID(Long rid) {
        return JsonResult.success(DestinationService.getDestinationByRegionID(rid));
    }
}
