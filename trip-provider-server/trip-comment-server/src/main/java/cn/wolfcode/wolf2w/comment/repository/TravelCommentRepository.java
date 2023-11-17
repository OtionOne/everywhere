package cn.wolfcode.wolf2w.comment.repository;



import cn.wolfcode.wolf2w.comment.domain.TravelComment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 游记评论持久层操作接口
 */
public interface TravelCommentRepository extends MongoRepository<TravelComment, String> {
    /**
     * 通过游记id查询游记评论集合
     * @param travelId
     * @return
     */
    List<TravelComment> findByTravelId(Long travelId);
}
