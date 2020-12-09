package com.zwx.crm.controller;

import com.zwx.crm.base.BaseController;
import com.zwx.crm.dao.PermissionMapper;
import com.zwx.crm.service.UserService;
import com.zwx.crm.utils.LoginUserUtil;
import com.zwx.crm.vo.Permission;
import com.zwx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController extends BaseController {


    @Resource
    private UserService userService;

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 系统登录页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }

    
    // 系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    /**
     * 后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        int id = LoginUserUtil.releaseUserIdFromCookie(request);
        User user = userService.selectByPrimaryKey(id);
        request.setAttribute("user",user);
        List<String> list = permissionMapper.selectByUserId(id);

        request.getSession().setAttribute("permission",list);
        System.out.println(list.toString());
        return "main";
    }
}
