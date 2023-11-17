package cn.wolfcode.wolf2w.article.service.impl;

import cn.wolfcode.wolf2w.article.domain.Banner;
import cn.wolfcode.wolf2w.article.mapper.BannerMapper;
import cn.wolfcode.wolf2w.article.query.BannerQuery;
import cn.wolfcode.wolf2w.article.service.IBannerService;
import cn.wolfcode.wolf2w.article.util.UploadUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* Banner服务接口实现
*/
@Service
@Transactional
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements IBannerService {

    @Override
    public IPage<Banner> queryPage(BannerQuery qo) {
        IPage<Banner> page = new Page<>(qo.getCurrentPage(), qo.getPageSize());
        QueryWrapper<Banner> wrapper = Wrappers.<Banner>query();
        wrapper.like(StringUtils.hasText(qo.getKeyword()), "title", qo.getKeyword()).or()
        .like(StringUtils.hasText(qo.getKeyword()), "subtitle", qo.getKeyword());
        return super.page(page, wrapper);
    }

    @Override
    public List<Banner> queryByType(int type) {
        QueryWrapper<Banner> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        wrapper.eq("state", Banner.STATE_NORMAL);
        wrapper.orderByAsc("seq");
        wrapper.last(" limit 5");

        return super.list(wrapper);
    }

    @Override
    public boolean saveOrUpdate(Banner entity) {

        //图片--将Base64字符串转换成图片缓存到阿里云
        if(UploadUtil.isBase64Pic(entity.getCoverUrl())){
            String imagePath = UploadUtil.uploadAliBase64(entity.getCoverUrl());
            entity.setCoverUrl(imagePath);
        }

        return super.saveOrUpdate(entity);
    }
}
