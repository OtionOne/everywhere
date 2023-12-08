package com.powernode.everywhere.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.everywhere.article.domain.Region;
import com.powernode.everywhere.article.mapper.RegionMapper;
import com.powernode.everywhere.article.service.RegionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {

    @Override
    public List<Region> getHotRegions() {
        QueryWrapper<Region> wrapper = new QueryWrapper<>();
        wrapper.eq("ishot", Region.STATE_HOT).orderByAsc("seq");
        List<Region> ans = super.list(wrapper);
        return ans;
    }
}
