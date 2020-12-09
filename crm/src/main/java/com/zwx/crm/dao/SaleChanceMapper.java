package com.zwx.crm.dao;

import com.zwx.crm.base.BaseMapper;
import com.zwx.crm.vo.SaleChance;

import java.util.List;

public interface SaleChanceMapper extends BaseMapper<SaleChance , Integer> {

    int updateByPrimaryKey(SaleChance saleChance);


}