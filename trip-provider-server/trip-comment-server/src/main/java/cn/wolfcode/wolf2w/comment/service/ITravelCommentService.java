package cn.wolfcode.wolf2w.comment.service;


import cn.wolfcode.wolf2w.comment.domain.TravelComment;
import cn.wolfcode.wolf2w.comment.query.TravelCommentQuery;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 游记评论的服务层接口
 */
public interface ITravelCommentService {

    /**
     * 添加
     * @param comment
     */
    void save(TravelComment comment);

    /**
     * 分析
     * @param qo
     * @return
     *
     * 此处的分页返回对象时：spring-data-MongoDB跟之前的MP的 Page 不一样
     */
    Page<TravelComment> queryPage(TravelCommentQuery qo);

    /**
     * 游记评论列表
     * @param travelId
     * @return
     */
    List<TravelComment> queryByTravelId(Long travelId);
}
