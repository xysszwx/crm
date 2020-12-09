package com.zwx.crm.controller;

import com.zwx.crm.base.BaseController;
import com.zwx.crm.base.ResultInfo;
import com.zwx.crm.dao.PermissionMapper;
import com.zwx.crm.query.RoleQuery;
import com.zwx.crm.service.RoleService;
import com.zwx.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;



    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAll(Integer id){
        return roleService.queryAll(id);
    }

    @RequestMapping("index")
    public String toIndex(){
        return "role/role";
    }

    @RequestMapping("addOrUpdateRolePage")
    public String toAddAndUpdate(Integer id , Model model){
        if(id!=null){
            Role role = roleService.selectByPrimaryKey(id);
            model.addAttribute("role",role);
        }

        return "role/add_update";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String ,Object> queryRole(RoleQuery roleQuery){
     return   roleService.queryByParamsForTable(roleQuery);
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveRole(Role role){

        roleService.addRole(role);

        return success();
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){

        roleService.updateRole(role);
        return success();
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        roleService.deleteRole(id);
        return success();
    }

    @RequestMapping("toGrantPage")
    public String toGrantPage(Integer roleId,Model model){
        if(roleId!=null){
            model.addAttribute("roleId",roleId);
        }
        return "role/grant";
    }

    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer[] mId,Integer roleId){
        roleService.addGrant(mId,roleId);
        return success("授权成功");
    }
}
