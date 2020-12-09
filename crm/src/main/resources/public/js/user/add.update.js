layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        formSelects = layui.formSelects;// 引入 formSelects 模块
    /**
     * 加载下拉框数据
     */
    //监听表单
    form.on("submit(addOrUpdateUser)",function(data){
        //加载层
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        //添加
        var url = ctx + "/user/addUser";
        //获取当前页面中的id
        var id = $("[name = 'id']").val();
        if(id){
            url = ctx + "/user/updateUser";
        }

        //发送请求
        $.post(url,data.field,function(data){
            if(data.code == 200){
                //提示用户添加成功
                layer.msg("数据添加成功",{icon:6});
                //关闭加载层
                top.layer.close(index);
                //关闭添加窗口
                layer.closeAll("iframe");

                //刷新页面的营销记录
                parent.location.reload();
            }else{
                layer.msg(data.msg,{icon:5});
            }
        });



        return false;//阻止表单提交
    });

    var id = $("[name='id']").val();
    formSelects.config('selectId',{
        type:"post",
        searchUrl:ctx + "/role/queryAllRoles?id=" +id,
//自定义返回数据中name的key, 默认 name
        keyName: 'roleName',
//自定义返回数据中value的key, 默认 value
        keyVal: 'id'
    },true);


    $("#closeBtn").click(function () {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });
});