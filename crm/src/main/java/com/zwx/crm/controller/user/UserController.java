package com.zwx.crm.controller.user;

import com.zwx.crm.base.BaseController;
import com.zwx.crm.base.ResultInfo;
import com.zwx.crm.model.UserModel;
import com.zwx.crm.query.UserQuery;
import com.zwx.crm.service.UserService;
import com.zwx.crm.utils.LoginUserUtil;
import com.zwx.crm.utils.UserIDBase64;
import com.zwx.crm.vo.User;
import org.apache.ibatis.annotations.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController{

    @Resource
    private UserService userService;


    @PostMapping("login")
    @ResponseBody
    public ResultInfo login(String userName , String userPwd){
        ResultInfo resultInfo = new ResultInfo();

        UserModel userModel = userService.userlogin(userName, userPwd);
        resultInfo.setResult(userModel);

        return resultInfo;

    }



    @PostMapping("/update")
    @ResponseBody
    public ResultInfo updatePass(HttpServletRequest request,String oldPassword, String newPassword, String confirmPassword){

        ResultInfo resultInfo = new ResultInfo();
        int id = LoginUserUtil.releaseUserIdFromCookie(request);


            userService.updateUserPwd(id,oldPassword,newPassword,confirmPassword);


        return resultInfo;
    }

    @RequestMapping("/saleMan")
    @ResponseBody
    public List<Map<String, Object>> selectSaleMan(){
       return userService.selectSaleMan();
    }

    @RequestMapping("toPasswordPage")
    public String toupdate(){

        return "user/password";
    }

    @RequestMapping("index")
    public String toIndex(){

        return "user/user";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String ,Object> queryByParams(UserQuery userQuery){
        return userService.queryByParamsForTable(userQuery);
    }

    @RequestMapping("addUser")
    @ResponseBody
    public ResultInfo addUser(User user){
        System.out.println(user.getRoleIds());
        userService.addUser(user);
        return success();
    }
    @RequestMapping("updateUser")
    @ResponseBody
    public ResultInfo updateUser(User user){

        userService.updateUser(user);
        return success();
    }

    @RequestMapping("/toAddAndUpdate")
    public String toAddAndUpdate(Integer id , Model model){
        if(id!=null){
            User user = userService.selectByPrimaryKey(id);
            model.addAttribute("user",user);
        }

        return "user/add_update";
    }


    @RequestMapping("deleteBatch")
    @ResponseBody
    public ResultInfo deleteBatch(Integer[] ids){
        userService.deleteUserBatch(ids);
        return success();
    }
}
