package com.zwx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zwx.crm.base.BaseService;
import com.zwx.crm.dao.SaleChanceMapper;
import com.zwx.crm.query.SaleChanceQuery;
import com.zwx.crm.utils.AssertUtil;
import com.zwx.crm.utils.PhoneUtil;
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
public class SaleChanceService extends BaseService<SaleChance, Integer> {

    @Resource
    private SaleChanceMapper saleChanceMapper;

    //返回前台需要的数据格式
    public Map<String ,Object> queryByParams(SaleChanceQuery saleChanceQuery){
        Map<String ,Object> map = new HashMap<>();
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        List<SaleChance> saleChances = saleChanceMapper.selectByParams(saleChanceQuery);
        PageInfo<SaleChance> saleChancePageInfo = new PageInfo<>(saleChances);
        map.put("code",0);
        map.put("msg","success");
        map.put("count",saleChancePageInfo.getTotal());
        map.put("data",saleChancePageInfo.getList());
        return map;
    }


    @Transactional
    public void insertSaleChane(SaleChance saleChance){
        checkSaleChane(saleChance);
        //设置默认值
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());

        //判断
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(0);
            saleChance.setDevResult(0);
            saleChance.setAssignTime(null);
        }else{
            saleChance.setState(1);
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(1);
        }

        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)<1,"营销机会添加操作失败");

    }


    @Transactional
    public void updateSaleChance(SaleChance saleChance){
        SaleChance dbSaleChance = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(dbSaleChance==null,"用户不存在");

        AssertUtil.isTrue(saleChance.getId()==null,"id不能为空");

        saleChance.setUpdateDate(new Date());

        String assignMan = saleChance.getAssignMan();

        checkSaleChane(saleChance);

        if(StringUtils.isBlank(dbSaleChance.getAssignMan())){

            //如果本来没有分配
            if(!StringUtils.isBlank(saleChance.getAssignMan())){
                //传进来的分配了
                saleChance.setAssignTime(new Date());
                saleChance.setState(1);
                saleChance.setDevResult(1);
            }else{
                saleChance.setAssignTime(null);
                saleChance.setState(0);
                saleChance.setDevResult(0);
            }

        }else{
            //如果本来有分配人
            if(!StringUtils.isBlank(saleChance.getAssignMan())){
                //传进来分配了
                if(dbSaleChance.getAssignMan().equals(saleChance.getAssignMan())){
                    //与原来人名字相等
                    saleChance.setAssignTime(dbSaleChance.getAssignTime());
                    saleChance.setState(1);
                    saleChance.setDevResult(1);
                }else{
                    //与原来人不等
                    saleChance.setAssignTime(new Date());
                    saleChance.setState(1);
                    saleChance.setDevResult(1);
                }

            }else{
                //传进来没分配人
                saleChance.setAssignTime(null);
                saleChance.setState(0);
                saleChance.setDevResult(0);
            }

        }
        saleChance.setIsValid(1);
        saleChance.setCreateDate(dbSaleChance.getCreateDate());
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKey(saleChance)<1,"营销机会编辑失败");

    }


    public void  updateDelete(Integer[] ids){
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids)<1,"删除失败");
    }


    private void checkSaleChane(SaleChance saleChance) {
        AssertUtil.isTrue(saleChance.getCustomerName()==null,"客户名称为空");
        AssertUtil.isTrue(saleChance.getLinkMan()==null,"联系人不能为空");
        AssertUtil.isTrue(saleChance.getLinkPhone()==null,"联系电话不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(saleChance.getLinkPhone()),"联系电话格式不正确");
    }


    /**
     * 更新数据状态
     */
    @Transactional
    public void updateSaleChanceDevResult(Integer id,Integer devResult){
        AssertUtil.isTrue(id == null ,"营销机会数据不存在");
        AssertUtil.isTrue(devResult == null ,"开发状态不存在");
        //查询数据是否存在
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(saleChance == null ,"营销机会数据不存在");

        //设置更新的状态
        saleChance.setDevResult(devResult);
        saleChance.setUpdateDate(new Date());

        //执行更新
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) < 1,"营销状态更新失败");
    }


}
