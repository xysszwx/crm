package com.zwx.crm.dao;

import com.zwx.crm.base.BaseMapper;
import com.zwx.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole , Integer> {
    Integer countUserRole(Integer id);

    Integer deleteUserRole(Integer id);
}