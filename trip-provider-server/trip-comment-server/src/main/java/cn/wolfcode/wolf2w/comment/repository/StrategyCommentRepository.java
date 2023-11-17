package cn.wolfcode.wolf2w.comment.repository;


import cn.wolfcode.wolf2w.comment.domain.StrategyComment;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 攻略评论持久层操作接口
 */
public interface StrategyCommentRepository extends MongoRepository<StrategyComment, String> {
}
