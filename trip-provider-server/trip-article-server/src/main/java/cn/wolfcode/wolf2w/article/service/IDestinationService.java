package cn.wolfcode.wolf2w.article.service;

import cn.wolfcode.wolf2w.article.domain.Destination;
import cn.wolfcode.wolf2w.article.query.DestinationQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IDestinationService extends IService<Destination> {
    /**
     * 查询指定区域下挂载目的地集合
     * @param rid
     * @return
     */
    List<Destination> queryByRid(Long rid);
    /**
     * 查询指定区域下挂载目的地集合
     * @param rid
     * @return
     */
    List<Destination> queryByRidForWebsite(Long rid);
    /**
     * 分页查询
     * @param qo
     * @return
     */
    Page<Destination> queryPage(DestinationQuery qo);

    /**
     * 查询吐司
     * @param destId
     * @return
     */
    List<Destination> queryToasts(Long destId);


}
