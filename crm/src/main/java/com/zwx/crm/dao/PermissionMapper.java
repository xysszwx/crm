package com.zwx.crm.dao;

import com.zwx.crm.base.BaseMapper;
import com.zwx.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    Integer selectCountByRoleId(Integer roleId);
    Integer deleteByRoleId(Integer roleId);

    List<Integer> selectByRole(Integer roleId);

    List<String> selectByUserId(Integer id);

    Integer selectCountByMid(Integer id);

    Integer deleteByMid(Integer id);
}