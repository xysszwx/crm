package com.zwx.crm.service;

import com.zwx.crm.base.BaseService;
import com.zwx.crm.dao.ModuleMapper;
import com.zwx.crm.dao.PermissionMapper;
import com.zwx.crm.dao.RoleMapper;
import com.zwx.crm.utils.AssertUtil;
import com.zwx.crm.vo.Permission;
import com.zwx.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {


    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private ModuleMapper moduleMapper;

    public List<Map<String ,Object>> queryAll(Integer id){
        return roleMapper.queryAllRoles(id);
    }

    @Transactional
    public void addRole(Role role){
        Role role1 = roleMapper.selectByName(role.getRoleName());
        AssertUtil.isTrue(role1!=null,"角色名已存在");
        checkNameRemark(role);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        role.setIsValid(1);
        AssertUtil.isTrue(roleMapper.insertSelective(role)<1,"添加角色失败");

    }

    public void updateRole(Role role){
        AssertUtil.isTrue(role.getId()==null,"没有指定角色");
        Role role1 = roleMapper.selectByName(role.getRoleName());
        AssertUtil.isTrue(role1!=null && !role1.getId().equals(role.getId()),"角色名已存在");
        checkNameRemark(role);
        role.setUpdateDate(new Date());
        roleMapper.updateByPrimaryKeySelective(role);
    }


    @Transactional
    public void deleteRole(Integer id){
            AssertUtil.isTrue(id==null,"没有要删除的角色");
            AssertUtil.isTrue(roleMapper.updateValid(id)<1,"删除角色失败");

    }

    private void checkNameRemark(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleRemark()),"角色备注不能为空");

    }

    @Transactional
    public void addGrant(Integer[] mId, Integer roleId) {
        System.out.println(roleId);
        Role role = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(role==null,"角色不存在");
        Integer count = permissionMapper.selectCountByRoleId(roleId);
        if(mId!=null && mId.length>1){
            AssertUtil.isTrue(permissionMapper.deleteByRoleId(roleId)!=count,"角色删除失败");
            List<Permission> list = new ArrayList<>();
        for(Integer mid : mId){
            Permission permission = new Permission();
            permission.setRoleId(roleId);
            permission.setCreateDate(new Date());
            permission.setUpdateDate(new Date());
            permission.setModuleId(mid);
            permission.setAclValue(moduleMapper.selectByMid(mid));
            list.add(permission);
        }
        permissionMapper.insertBatch(list);
        }
    }
}
