package com.ajin.ad.advice;

import com.ajin.ad.annotation.IgnoreResponseAdvice;
import com.ajin.ad.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @Auther: ajin
 * @Date: 2019/4/4 21:03
 * 统一响应处理
 */
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {


    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // 类上加了注解IgnoreResponseAdvice，不拦截
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        // 方法上加了IgnoreResponseAdvice注解，不拦截
        if (methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        return true;

    }

    /**
     * @param o 响应对象
     **/
    @Nullable
    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(@Nullable Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        CommonResponse<Object> response = new CommonResponse<Object>(0, "");
        // 没有响应数据
        if (null == o) {
            return response;
        } else if (o instanceof CommonResponse) { // 响应对象本身就是CommonResponse类型，则无须setData
            response = (CommonResponse<Object>) o;
        } else {
            // 响应对象只是一个Object，所以需要setData
            response.setData(o);
        }
        return response;
    }
}
