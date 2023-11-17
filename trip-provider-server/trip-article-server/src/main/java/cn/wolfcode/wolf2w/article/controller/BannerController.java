package cn.wolfcode.wolf2w.article.controller;

import cn.wolfcode.wolf2w.article.domain.Banner;
import cn.wolfcode.wolf2w.article.query.BannerQuery;
import cn.wolfcode.wolf2w.article.service.IBannerService;
import cn.wolfcode.wolf2w.common.web.response.JsonResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("banners")
public class BannerController {

    @Autowired
    private IBannerService bannerService;

    @GetMapping("/query")
    public JsonResult query(BannerQuery qo){
        IPage<Banner> page =  bannerService.queryPage(qo);
        return  JsonResult.success(page);
    }

    @GetMapping("/detail")
    public JsonResult detail(Long id){
        Banner banner = bannerService.getById(id);
        return  JsonResult.success(banner);
    }

    @PostMapping("/save")
    public JsonResult save(Banner banner){
        bannerService.saveOrUpdate(banner);
        return  JsonResult.success();
    }


    @PostMapping("/update")
    public JsonResult update(Banner banner){
        bannerService.saveOrUpdate(banner);
        return  JsonResult.success();
    }
    @PostMapping("/delete/{id}")
    public JsonResult delete(@PathVariable Long id){
        bannerService.removeById(id);
        return  JsonResult.success();
    }

    @GetMapping("/travel")
    public JsonResult travel(){
        List<Banner> list = bannerService.queryByType(Banner.TYPE_TRAVEL);
        return JsonResult.success(list);

    }

    @GetMapping("/strategy")
    public JsonResult strategy(){
        List<Banner> list = bannerService.queryByType(Banner.TYPE_STRATEGY);
        return JsonResult.success(list);
    }













}
