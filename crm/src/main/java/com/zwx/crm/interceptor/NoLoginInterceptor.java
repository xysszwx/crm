package com.zwx.crm.interceptor;

import com.zwx.crm.exceptions.NoLoginException;
import com.zwx.crm.service.UserService;
import com.zwx.crm.utils.CookieUtil;
import com.zwx.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer id = LoginUserUtil.releaseUserIdFromCookie(request);
        if(id==null || userService.selectByPrimaryKey(id) == null){
            throw new NoLoginException();
        }

        return true;
    }
}
