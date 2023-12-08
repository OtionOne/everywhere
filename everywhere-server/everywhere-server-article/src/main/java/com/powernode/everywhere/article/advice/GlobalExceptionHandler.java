package com.powernode.everywhere.article.advice;

import com.powernode.everywhere.common.core.exception.CoolDownException;
import com.powernode.everywhere.common.core.util.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CoolDownException.class)
    public JsonResult CoolDownExceptionHandler(CoolDownException e){
        return JsonResult.error(JsonResult.CODE_ERROR,e.getMessage(),null);
    }


    @ExceptionHandler(Exception.class)
    public JsonResult commonExceptionHandler(Exception e){
        log.error("全局异常处理器",e);
        return JsonResult.error(JsonResult.CODE_ERROR,"未知异常qwq,请联系管理员了解详情",null);
    }

}
