package com.ajin.ad.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @Auther: ajin
 * @Date: 2019/4/4 19:59
 */
@Slf4j
@Component
public class PreRequestFilter extends ZuulFilter {
    /**
     * 过滤器类型
     **/
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 获取当前请求的上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 当前的时间戳
        ctx.set("startTime",System.currentTimeMillis());
        return null;
    }
}
