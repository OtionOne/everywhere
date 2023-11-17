package cn.wolfcode.wolf2w.comment.controller;

import cn.wolfcode.wolf2w.comment.domain.StrategyComment;
import cn.wolfcode.wolf2w.comment.query.StrategyCommentQuery;
import cn.wolfcode.wolf2w.comment.service.IStrategyCommentService;
import cn.wolfcode.wolf2w.common.redis.service.RedisService;
import cn.wolfcode.wolf2w.common.redis.util.RedisKeys;
import cn.wolfcode.wolf2w.common.sercurity.annotation.RequireLogin;
import cn.wolfcode.wolf2w.common.web.response.JsonResult;
import cn.wolfcode.wolf2w.member.domain.UserInfo;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("strategyComments")
public class StrategyCommentController {

    @Autowired
    private IStrategyCommentService strategyCommentService;

    @Autowired
    private RedisService redisService;

    @RequireLogin
    @PostMapping("/save")
    public JsonResult save(StrategyComment comment, HttpServletRequest request){
        String token = request.getHeader("token");
        String userStr = redisService.getCacheObject(RedisKeys.USER_LOGIN_TOKEN.join(token));
        UserInfo userInfo = JSON.parseObject(userStr, UserInfo.class);
        BeanUtils.copyProperties(userInfo, comment);
        comment.setUserId(userInfo.getId());
        strategyCommentService.save(comment);



        return JsonResult.success();
    }
    @GetMapping("/query")
    public JsonResult query(StrategyCommentQuery qo){
        Page<StrategyComment> page = strategyCommentService.queryPage(qo);
        return JsonResult.success(page);
    }

    @RequireLogin
    @PostMapping("/thumb")
    public JsonResult thumb(String cid, HttpServletRequest request){
        String token = request.getHeader("token");
        String userStr = redisService.getCacheObject(RedisKeys.USER_LOGIN_TOKEN.join(token));
        UserInfo userInfo = JSON.parseObject(userStr, UserInfo.class);
        strategyCommentService.commentThumb(cid, userInfo.getId());
        return JsonResult.success();
    }

}
