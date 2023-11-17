package cn.wolfcode.wolf2w.comment.service;


import cn.wolfcode.wolf2w.comment.domain.StrategyComment;
import cn.wolfcode.wolf2w.comment.query.StrategyCommentQuery;
import org.springframework.data.domain.Page;

/**
 * 攻略评论的服务层接口
 */
public interface IStrategyCommentService {

    /**
     * 添加
     * @param comment
     */
    void save(StrategyComment comment);

    /**
     * 分析
     * @param qo
     * @return
     *
     * 此处的分页返回对象时：spring-data-MongoDB跟之前的MP的 Page 不一样
     */
    Page<StrategyComment> queryPage(StrategyCommentQuery qo);

    /**
     * 攻略评论点赞
     * @param cid
     * @param sid
     */
    void commentThumb(String cid, Long sid);
}
