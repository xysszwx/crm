package com.zwx.crm.dao;

import com.zwx.crm.base.BaseMapper;
import com.zwx.crm.model.TreeModule;
import com.zwx.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {
   List<TreeModule> selectAllModel();

   String selectByMid(Integer mid);

   List<Module> queryAllModule();

   Module queryByGrandAName(@Param("grade") Integer grade,@Param("moduleName") String moduleName);

   Module queryByGrandAUrl(@Param("grade") Integer grade,@Param("url") String url);

   Module queryByParentId(Integer parentId);

   Module queryModulByOptValue(String optValue);

   Integer updateValid(Integer id);

   Integer selectCountByParent(Integer id);
}