package com.zwx.crm.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.zwx.crm.base.BaseController;
import com.zwx.crm.base.ResultInfo;
import com.zwx.crm.model.TreeModule;
import com.zwx.crm.service.ModuleService;
import com.zwx.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {

    @Resource
    private ModuleService moduleService;

    @RequestMapping("queryAllModule")
    @ResponseBody
    public List<TreeModule> queryModel(Integer roleId){
        return moduleService.queryAllModule(roleId);
    }

    @RequestMapping("index")
    public String toIndex(){
        return "module/module";
    }

    @RequestMapping("toAdd")
    public String toAdd(Integer grade, Integer parentId, Model model){
        model.addAttribute("grade",grade);
        model.addAttribute("parentId",parentId);
        return "module/add";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String ,Object> list(){

        return moduleService.queryAllModule();
    }

    @RequestMapping("add")
    @ResponseBody
    public ResultInfo add(Module module){
        moduleService.insertModule(module);
        return success();
    }


    @RequestMapping("toUpdate")
    public String toUpdate(Integer id,Model model){
        Module module = moduleService.selectByPrimaryKey(id);
        model.addAttribute("module",module);
        return "module/update";
    }


    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(Module module){
        moduleService.updateModule(module);
        return success();
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer id){
        moduleService.deleteModule(id);
        return success();
    }
}
