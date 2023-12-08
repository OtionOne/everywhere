package com.powernode.everywhere.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.powernode.everywhere.article.domain.Destination;
import com.powernode.everywhere.article.domain.Region;
import com.powernode.everywhere.article.qo.DestinationQuery;
import com.powernode.everywhere.common.core.util.JsonResult;

import java.util.List;

public interface DestinationService extends IService<Destination> {
    List<Destination> fetchMySon(Long regionId);

    Page<Destination> pageOut(DestinationQuery query);

    List<Destination> getToasts(Long destId);

    List<Destination> getDestinationByRegionID(Long rid);
}
