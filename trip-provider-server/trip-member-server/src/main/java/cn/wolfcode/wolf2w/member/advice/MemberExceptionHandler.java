package cn.wolfcode.wolf2w.member.advice;

import cn.wolfcode.wolf2w.common.exception.CommonExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//在trip -common 定义异常处理规范
//后续在每个服务中，定制异常处理具体实现


//专门当前微服务自己独有异常
//后续项目复杂之后， 会有很多自定义的异常，每个微服务，都有自己独有的异常，每个异常处理方式不一定一样
//所以不同微服务，定制自己独有的统一异常处理器。
@RestControllerAdvice
public class MemberExceptionHandler  extends CommonExceptionHandler {
}
