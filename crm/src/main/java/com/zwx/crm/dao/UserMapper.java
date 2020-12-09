package com.zwx.crm.dao;

import com.zwx.crm.base.BaseMapper;
import com.zwx.crm.vo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User , Integer> {
    User selectByName(String userName);
    List<Map<String , Object>> selectSaleMan();
}