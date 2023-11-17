package cn.wolfcode.wolf2w.comment.controller;

import cn.wolfcode.wolf2w.comment.domain.TravelComment;
import cn.wolfcode.wolf2w.comment.service.ITravelCommentService;
import cn.wolfcode.wolf2w.common.redis.service.RedisService;
import cn.wolfcode.wolf2w.common.redis.util.RedisKeys;
import cn.wolfcode.wolf2w.common.sercurity.annotation.RequireLogin;
import cn.wolfcode.wolf2w.common.web.response.JsonResult;
import cn.wolfcode.wolf2w.member.domain.UserInfo;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("travelComments")
public class TravelCommentController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ITravelCommentService travelCommentService;

    @RequireLogin
    @PostMapping("/save")
    public JsonResult commentAdd(TravelComment comment, HttpServletRequest request){
        String token = request.getHeader("token");
        String userStr = redisService.getCacheObject(RedisKeys.USER_LOGIN_TOKEN.join(token));
        UserInfo userInfo = JSON.parseObject(userStr, UserInfo.class);

        BeanUtils.copyProperties(userInfo, comment);
        comment.setUserId(userInfo.getId());
        travelCommentService.save(comment);
        return JsonResult.success();
    }

    @GetMapping("/query")
    public JsonResult comments(Long travelId){
        return JsonResult.success(travelCommentService.queryByTravelId(travelId));
    }





}
