package com.ajin.ad.advice;

import com.ajin.ad.exception.AdException;
import com.ajin.ad.exception.AdPlanException;
import com.ajin.ad.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: ajin
 * @Date: 2019/4/4 21:23
 * 统一异常处理类
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {
    /**
     * 捕获抛出的AdException异常并处理
     */
    @ExceptionHandler(value = AdException.class)
    public CommonResponse<String> handlerAdException(HttpServletRequest request, AdException ex) {
        CommonResponse<String> response=new CommonResponse<String>(-1,"business error");
        response.setData(ex.getMessage());
        return response;

    }

    @ExceptionHandler(value = AdPlanException.class)
    public CommonResponse<String> handlerAdPlanException(HttpServletRequest request, AdPlanException ex) {
        CommonResponse<String> response=new CommonResponse<String>(-1,"ad plan error");
        response.setData(ex.getMessage());
        return response;

    }
}
