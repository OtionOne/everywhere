package cn.wolfcode.wolf2w.article.service;

import cn.wolfcode.wolf2w.article.domain.Region;
import cn.wolfcode.wolf2w.article.query.RegionQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IRegionService extends IService<Region> {
    /**
     * 分页查询
     * @param qo
     * @return
     */
    Page<Region> queryPage(RegionQuery qo);

    /**
     * 热门区域
     * @return
     */
    List<Region> queryHot();
}
