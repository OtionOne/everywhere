package cn.wolfcode.wolf2w.article.service.impl;

import cn.wolfcode.wolf2w.article.domain.Destination;
import cn.wolfcode.wolf2w.article.domain.Region;
import cn.wolfcode.wolf2w.article.mapper.DestinationMapper;
import cn.wolfcode.wolf2w.article.query.DestinationQuery;
import cn.wolfcode.wolf2w.article.service.IDestinationService;
import cn.wolfcode.wolf2w.article.service.IRegionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class DestinationServiceImpl extends ServiceImpl<DestinationMapper, Destination> implements IDestinationService {

    @Autowired
    private IRegionService regionService;

    @Override
    public List<Destination> queryByRid(Long rid) {
        //select ref_ids from region where id = 5;

        //select * from destination  where id in(52,53,54)

        //步骤1：通过rid 找到需要处理区域对象
        Region region = regionService.getById(rid);
        //String ids = region.getRefIds();  //id,id..
        List<Long> ids = region.parseRefIds();

        //步骤2：将区域对象下所关联的目的地id集合获取，然后查询列表
        return super.listByIds(ids);
    }

    @Override
    public List<Destination> queryByRidForWebsite(Long rid) {
        QueryWrapper<Destination> wrapper = new QueryWrapper<>();
        //查询指定id下区域挂载目的地集合
        List<Destination> list = new ArrayList<>();
        if(rid == -1){
            //国内--查询中国下所有省份

            wrapper.eq("parent_id", 1L);  //中国id
            list = super.list(wrapper);
        }else{
            //其他
            list = this.queryByRid(rid);
        }
        //遍历查询目的地中子目的地集合
        for (Destination dest : list) {
            wrapper.clear(); //清除条件
            //QueryWrapper<Destination> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", dest.getId());  //中国id
            wrapper.last(" limit 5");

            List<Destination> children = super.list(wrapper);
            dest.setChildren(children);
        }
        return list;
    }

    @Override
    public Page<Destination> queryPage(DestinationQuery qo) {
        Page<Destination> page = new Page<>(qo.getCurrentPage(), qo.getPageSize());
        QueryWrapper<Destination> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.hasText(qo.getKeyword()), "name", qo.getKeyword());


        wrapper.eq(qo.getParentId() != null , "parent_id", qo.getParentId());
        wrapper.isNull(qo.getParentId() == null , "parent_id");

        return super.page(page, wrapper);
    }

    // 根>>中国>>广东>>广州      destId=355
    @Override
    public List<Destination> queryToasts(Long destId) {
        List<Destination> list = new ArrayList<>();

        //方案1： 循环while parent_id is null
        //方案2： 递归
        this.createToast(list, destId);
        Collections.reverse(list);   //集合反转： ABC  --- CBA
        return list;
    }


    private void createToast(List<Destination> list, Long destId){
        if(destId == null){
            return;
        }
        Destination dest = super.getById(destId);
        list.add(dest);

        if(dest.getParentId() != null){
            this.createToast(list, dest.getParentId());
        }
    }

}
