package com.zwx.crm.service;

import com.zwx.crm.base.BaseService;
import com.zwx.crm.dao.UserMapper;
import com.zwx.crm.dao.UserRoleMapper;
import com.zwx.crm.model.UserModel;
import com.zwx.crm.utils.AssertUtil;
import com.zwx.crm.utils.Md5Util;
import com.zwx.crm.utils.PhoneUtil;
import com.zwx.crm.utils.UserIDBase64;
import com.zwx.crm.vo.User;
import com.zwx.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User, Integer> {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    public UserModel userlogin(String username, String password){
        checkUser(username,password);
        User user = userMapper.selectByName(username);
        AssertUtil.isTrue(user==null,"不存在该用户");
        checkPassword(user.getUserPwd(),password);
        UserModel userModel = buildUserModel(user);
        return userModel;
    }

    private UserModel buildUserModel(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserStrId(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());

        return userModel;
    }

    private void checkPassword(String password, String password1) {
        String encode = Md5Util.encode(password1);
        AssertUtil.isTrue(!encode.equals(password),"密码错误");
    }

    private void checkUser(String username , String password) {
        AssertUtil.isTrue(StringUtils.isBlank(username),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(password),"密码不能为空");
    }




    /**修改用户密码
     */
    public void updateUserPwd(Integer userId, String oldPassword, String newPassword,String confirmPassword){
        //通过id查询账户
        User user = userMapper.selectByPrimaryKey(userId);
        //id查询判断用户存在
        AssertUtil.isTrue(user == null,"用户未登录");
        //校验数据参数
        checkUserUpdateParams(oldPassword,newPassword,confirmPassword,user.getUserPwd());
        //设置新密码
        user.setUserPwd(Md5Util.encode(newPassword));
        user.setUpdateDate(new Date());

        //执行修改操作，返回结果信息ResultInfo
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1,"修改密码失败");
    }




    public List<Map<String , Object>> selectSaleMan(){
        return userMapper.selectSaleMan();
    }


    /**
     * 校验修改密码的数据
     1.获取cookie中id  非空   id查询判断用户存在
     2.原始密码 非空   与数据库中密码保持一致
     3.新密码   非空   新密码与原始密码不能一致
     4.确认密码 非空   确认密码与新密码一致
     5.执行修改操作，返回结果信息ResultInfo
     * @param oldPassword  用户输入原始密码
     * @param newPassword  新密码
     * @param confirmPassword  确认密码
     * @param dbPassword    数据库中密码
     */
    private void checkUserUpdateParams(String oldPassword, String newPassword, String confirmPassword,String dbPassword) {
        // 2.原始密码 非空   与数据库中密码保持一致
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"原始密码不能为空");
        AssertUtil.isTrue(!dbPassword.equals(Md5Util.encode(oldPassword)),"原始密码错误");

        // 3.新密码   非空   新密码与原始密码不能一致
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"新密码不能为空");
        AssertUtil.isTrue(newPassword.equals(oldPassword),"新密码与原始密码不能一致");

        //确认密码 非空   确认密码与新密码一致
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword),"确认密码不能为空");
        AssertUtil.isTrue(!confirmPassword.equals(newPassword),"新密码与确认密码不一致");
    }


    @Transactional
    public void addUser(User user){
        checkAddAndUpdateUser(user);
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        AssertUtil.isTrue(userMapper.insertHasKey(user)<1,"插入失败");
        userRoleConntion(user.getId(),user.getRoleIds());
    }

        private void userRoleConntion(Integer userId, String roleIds) {
        Integer count = userRoleMapper.countUserRole(userId);
        if(count!=null){
            userRoleMapper.deleteUserRole(userId);
        }
        if(StringUtils.isNotBlank(roleIds)) {
            List<UserRole> list = new ArrayList<>();
            String[] split = roleIds.split(",");
            for (String rId : split) {
                UserRole userRole = new UserRole();
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRole.setUserId(userId);
                Integer id = Integer.parseInt(rId);
                userRole.setRoleId(id);
                System.out.println(id);
                list.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(list) != list.size(),"角色绑定失败");
        }
    }

    @Transactional
    public void updateUser(User user){
        AssertUtil.isTrue(user.getId()==null,"系统异常");
        checkAddAndUpdateUser(user);
        user.setUpdateDate(new Date());
        userMapper.updateByPrimaryKeySelective(user);
        userRoleConntion(user.getId(),user.getRoleIds());
    }

    private void checkAddAndUpdateUser(User user) {
        AssertUtil.isTrue(StringUtils.isBlank(user.getUserName()),"用户名不能为空");
        User user1 = userMapper.selectByName(user.getUserName());
        if(user.getId()==null){
            AssertUtil.isTrue(user1!=null,"用户已存在");
        }else{
            AssertUtil.isTrue(user1!=null && !user.getUserName().equals(user1.getUserName()),"用户名已存在");
        }


        AssertUtil.isTrue(StringUtils.isBlank(user.getTrueName()),"真实姓名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(user.getEmail()),"邮箱不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(user.getPhone()),"电话号码不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(user.getPhone()),"电话号码格式不正确");

    }

    public void deleteUserBatch(Integer[] ids) {
        AssertUtil.isTrue(ids==null || ids.length <1,"没有选择要删除的数据");
        AssertUtil.isTrue(userMapper.deleteBatch(ids)<ids.length,"删除异常");

        for(Integer id : ids){
            userRoleMapper.deleteUserRole(id);
        }
    }
}
