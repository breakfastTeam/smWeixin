package com.sm.interceptor;

import com.sm.util.SmUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Ez丶kkk on 15/2/8.
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String contextPath=request.getContextPath();
        String url=request.getServletPath().toString();
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute(SmUtils.SEESION_USER_ID);
        if (userId == null) {
            if (!url.contains("manage/login") && !url.contains("manage/toLogin")) {
                if (!isAjaxRequest(request)) {
                    response.sendRedirect(contextPath + "/manage/login");
                } else {
                    response.reset();
                    try {
                        PrintWriter writer = response.getWriter();
                        writer.write("RELOGIN");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private boolean isAjaxRequest(HttpServletRequest request){
        String header = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(header) ? true:false;
        return isAjax;
    }
}
