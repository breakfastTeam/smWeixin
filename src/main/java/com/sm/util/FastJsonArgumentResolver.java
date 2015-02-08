package com.sm.util;

import com.alibaba.fastjson.JSON;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import javax.servlet.http.HttpServletRequest;

public class FastJsonArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(FastJson.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        // content-type不是json的不处理
//        if (!request.getContentType().contains("application/json")) {
//            return null;
//        }

        String value = request.getParameter(parameter.getParameterName());
        // 利用fastjson转换为对应的类型
        if(JSONObjectWrapper.class.isAssignableFrom(parameter.getParameterType())){
            return new JSONObjectWrapper(JSON.parseObject(value));
        } else {
            return JSON.parseObject(value, parameter.getParameterType());
        }
    }

}