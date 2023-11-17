package cn.wolfcode.wolf2w.article.feign.fallback;

import cn.wolfcode.wolf2w.article.feign.IUserInfoFeignService;
import cn.wolfcode.wolf2w.common.web.response.JsonResult;
import cn.wolfcode.wolf2w.member.domain.UserInfo;
import org.springframework.stereotype.Component;


@Component
public class UserInfoFeignServiceFallBack implements IUserInfoFeignService {
    @Override
    public JsonResult<UserInfo> detail(Long id) {
        System.out.println("走降级了.........");
        return JsonResult.success(new UserInfo());
    }
}
