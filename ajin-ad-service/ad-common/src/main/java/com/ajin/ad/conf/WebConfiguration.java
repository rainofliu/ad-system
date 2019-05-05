package com.ajin.ad.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Auther: ajin
 * @Date: 2019/4/4 21:30
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 清空消息转换器
        converters.clear();
        // 将java对象转化成json对象
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
