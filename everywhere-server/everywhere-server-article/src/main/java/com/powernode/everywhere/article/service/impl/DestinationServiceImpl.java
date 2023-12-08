package com.powernode.everywhere.article.service.impl;


import com.alibaba.nacos.api.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.everywhere.article.domain.Destination;
import com.powernode.everywhere.article.domain.Region;
import com.powernode.everywhere.article.mapper.DestinationMapper;
import com.powernode.everywhere.article.qo.DestinationQuery;
import com.powernode.everywhere.article.service.DestinationService;
import com.powernode.everywhere.article.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class DestinationServiceImpl extends ServiceImpl<DestinationMapper, Destination> implements DestinationService {

    @Autowired
    private DestinationMapper destinationMapper;

    @Autowired
    private RegionService regionService;

    @Override
    public List<Destination> fetchMySon(Long regionId) {
        Region region = regionService.getById(regionId);
        if (region == null) {
            return Collections.emptyList();
        }
        List<Long> ids = region.parseRefIds();
        if (ids == null || ids.size() == 0) {
            return Collections.emptyList();
        }
        return super.listByIds(ids);
    }

    @Override
    public Page<Destination> pageOut(DestinationQuery query) {
        Page<Destination> pageInfo = new Page<>(query.getCurrent(), query.getSize());
        QueryWrapper<Destination> wrapper = new QueryWrapper<>();
        wrapper.isNull((query.getParentId() == null), "parent_id");
        wrapper.eq((query.getParentId() != null), "parent_id", query.getParentId());
        wrapper.like(query.getKeyword()!=null,"name",query.getKeyword());
        return this.page(pageInfo,wrapper);
    }

    @Override
    public List<Destination> getToasts(Long destId) {
        List<Destination> list = new ArrayList<>();
        while(destId != null){
            Destination destination = destinationMapper.selectById(destId);
            if(destination==null){
                break;
            }
            list.add(destination);
            destId = destination.getParentId();
        }
        Collections.reverse(list);
        return list;
    }

    @Override
    public List<Destination> getDestinationByRegionID(Long rid) {
        Region region = regionService.getById(rid);
        if(region==null&&rid!=-1){
            return Collections.emptyList();
        }
        List<Destination> ans=new ArrayList<>();
        if(rid==-1){
            QueryWrapper<Destination> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id",1);
            ans = this.list(wrapper);
        }else{
            ans=this.listByIds(region.parseRefIds());
        }
        for(Destination now : ans){
            now.setChildren(this.list(new QueryWrapper<Destination>().eq("parent_id",now.getId()).last("limit 10")));
        }
        return ans;
    }


}
