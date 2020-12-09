package com.zwx.crm.dao;

import com.zwx.crm.base.BaseMapper;
import com.zwx.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
    List<Map<String , Object>> queryAllRoles (Integer id);
    int updateValid(Integer id);

    Role selectByName(String name);
}