package com.zwx.crm.controller;

import com.zwx.crm.annotation.RequirePermission;
import com.zwx.crm.base.BaseController;
import com.zwx.crm.base.ResultInfo;
import com.zwx.crm.query.SaleChanceQuery;
import com.zwx.crm.service.SaleChanceService;
import com.zwx.crm.utils.AssertUtil;
import com.zwx.crm.utils.CookieUtil;
import com.zwx.crm.utils.LoginUserUtil;
import com.zwx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;


    @RequestMapping("list")
    @ResponseBody
    @RequirePermission(code = "101001")
    public Map<String ,Object> queryByParams(SaleChanceQuery saleChanceQuery,Integer flag ,HttpServletRequest request){
        if(flag!=null && flag==1){
            int id = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(id);
        }

        Map<String, Object> saleChanceMap = saleChanceService.queryByParams(saleChanceQuery);

        return saleChanceMap;
    }

    @RequestMapping("index")
    public String  toindex(){
        return "saleChance/sale_chance";
    }


    @RequestMapping("save")
    @ResponseBody
    @RequirePermission(code = "101002")
    public ResultInfo saveSaleChance(HttpServletRequest request,SaleChance saleChance){
        String userName = CookieUtil.getCookieValue(request, "userName");


        saleChance.setCreateMan(userName);

        saleChanceService.insertSaleChane(saleChance);

        return success();
    }


    @RequestMapping("update")
    @ResponseBody
    @RequirePermission(code = "101004")
    public ResultInfo updateSaleChance(SaleChance saleChance){

        saleChanceService.updateSaleChance(saleChance);
        return success();
    }



    @RequestMapping("toAddUpdate")
    public String toUpdate(Model model,Integer saleChanceId){
        if(saleChanceId!=null){
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
            model.addAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }

    @RequestMapping("/deleteBatch")
    @ResponseBody
    @RequirePermission(code = "101003")
    public ResultInfo deleteBatch(Integer[] ids){
        AssertUtil.isTrue(ids == null || ids.length < 1,"未选中删除的数据");
         saleChanceService.updateDelete(ids);
         return success();
    }


    /**
     * 更新营销机会状态
     */
    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDev(Integer id,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("营销状态修改成功");
    }

}
