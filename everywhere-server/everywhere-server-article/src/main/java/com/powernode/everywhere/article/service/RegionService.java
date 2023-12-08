package com.powernode.everywhere.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.powernode.everywhere.article.domain.Region;

import java.util.List;

public interface RegionService extends IService<Region> {
    List<Region> getHotRegions();
}
