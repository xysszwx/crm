package com.zwx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zwx.crm.base.BaseService;
import com.zwx.crm.dao.ModuleMapper;
import com.zwx.crm.dao.PermissionMapper;
import com.zwx.crm.model.TreeModule;
import com.zwx.crm.utils.AssertUtil;
import com.zwx.crm.vo.Module;
import com.zwx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module ,Integer> {

    @Resource
    private ModuleMapper moduleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    public List<TreeModule> queryAllModule(Integer roleId){
        List<TreeModule> treeModules = moduleMapper.selectAllModel();
        List<Integer> modules = permissionMapper.selectByRole(roleId);
        for(TreeModule module : treeModules){
            if(modules.contains(module.getId())){
                module.setChecked(true);
                module.setOpen(true);
            }
        }
        return treeModules;
    }

    public Map<String ,Object> queryAllModule(){
        Map<String ,Object> map = new HashMap<>();
        List<Module> modules = moduleMapper.queryAllModule();
        map.put("code",0);
        map.put("msg","success");
        map.put("count",modules.size());
        map.put("data",modules);
        return map;
    }

    //层级
    //名字 同级不能重复
    //url
    //1,2层有父id
    //权限码
    //默认值
    @Transactional
    public void insertModule(Module module) {
        AssertUtil.isTrue(module.getGrade()==null,"层级不能为空");
        AssertUtil.isTrue(!(module.getGrade()==0||module.getGrade()==1||module.getGrade()==2),"层级不正确");

        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名不能为空");
        Module dbmodule = moduleMapper.queryByGrandAName(module.getGrade(),module.getModuleName());
        AssertUtil.isTrue(dbmodule!=null,"模块名称不能重复");

        if(module.getGrade() == 1) {
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()), "模块地址不能为空");
            dbmodule = moduleMapper.queryByGrandAUrl(module.getGrade(), module.getUrl());
            AssertUtil.isTrue(dbmodule != null, "模块地址已存在");
        }


        if(module.getGrade()==1||module.getGrade()==2){
        AssertUtil.isTrue(module.getParentId()==null,"父id不能为空");
        dbmodule =   moduleMapper.queryByParentId(module.getParentId());
        AssertUtil.isTrue(dbmodule==null,"父级模块不存在");
        }
        //权限码  非空  唯一
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空");
        dbmodule = moduleMapper.queryModulByOptValue(module.getOptValue());
        AssertUtil.isTrue(dbmodule != null,"权限码已存在");
        //默认值
        module.setIsValid((byte) 1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());

        //执行添加操作  判断受影响行数
        AssertUtil.isTrue(moduleMapper.insertSelective(module) < 1,"模块添加失败");

    }

    @Transactional
    public void updateModule(Module module) {
        AssertUtil.isTrue(module.getId()==null,"没有要修改的数据");
        Module dbmodule = moduleMapper.selectByPrimaryKey(module.getId());
        AssertUtil.isTrue(dbmodule==null,"待修改的模块不存在");

        AssertUtil.isTrue(module.getGrade()==null,"层级不能为空");
        AssertUtil.isTrue(!(module.getGrade()==0||module.getGrade()==1||module.getGrade()==2),"层级不正确");

        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名不能为空");
         dbmodule = moduleMapper.queryByGrandAName(module.getGrade(),module.getModuleName());
        AssertUtil.isTrue(dbmodule!=null&& !(dbmodule.getId().equals(module.getId())),"模块名称不能重复");

        if(module.getGrade() == 1) {
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()), "模块地址不能为空");
            dbmodule = moduleMapper.queryByGrandAUrl(module.getGrade(), module.getUrl());
            AssertUtil.isTrue(dbmodule != null && !(dbmodule.getId().equals(module.getId())), "模块地址已存在");
        }

        if(module.getGrade()==1||module.getGrade()==2){
            AssertUtil.isTrue(module.getParentId()==null,"父id不能为空");
            dbmodule =   moduleMapper.queryByParentId(module.getParentId());
            AssertUtil.isTrue(dbmodule==null && !(dbmodule.getId().equals(module.getId())) ,"父级模块不存在");
        }

        //权限码  非空  唯一
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空");
        dbmodule = moduleMapper.queryModulByOptValue(module.getOptValue());
        AssertUtil.isTrue(dbmodule != null &&!(dbmodule.getId().equals(module.getId())),"权限码已存在");
        //默认值
        module.setUpdateDate(new Date());

        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(module)<1,"修改失败");

    }

    @Transactional
    public void deleteModule(Integer id) {
        AssertUtil.isTrue(id==null,"系统异常");
        AssertUtil.isTrue(selectByPrimaryKey(id)==null,"待删除数据不存在");
        AssertUtil.isTrue(moduleMapper.selectCountByParent(id)>0,"有子项不允许删除");
        Integer count = permissionMapper.selectCountByMid(id);
        if(count>0){
            AssertUtil.isTrue(permissionMapper.deleteByMid(id)!=count,"删除关联权限异常");
        }


        AssertUtil.isTrue(moduleMapper.updateValid(id)<1,"删除失败");

    }
}
