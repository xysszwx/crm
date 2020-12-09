layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    $(function () {
        loadModuleInfo();
    });

//权限树形结构的对象
    var zTreeObj = null;
    var roleId = $('[name="roleId"]').val();

//加载树形结构
    function loadModuleInfo() {


        //发送请求返回树形结构的数据
        $.ajax({
            type: 'get',
            url: ctx + "/module/queryAllModule",
            data:{
                roleId:roleId
            },
            dataType: 'json',
            success: function (data) {
                // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
                var setting = {
                    check: { //开启多选框
                        enable: true
                    },
                    callback: { //监听多选框/单选框的选中
                        onCheck: zTreeOnCheck
                    },
                    data: {
                        simpleData: {  //使用简单json数据
                            enable: true
                        }
                    }
                };

                $(document).ready(function () {
                    zTreeObj = $.fn.zTree.init($("#test1"), setting, data);
                });
            }
        })
    }

    function zTreeOnCheck(){
        var nodes = zTreeObj.getCheckedNodes(true);
        console.log(nodes);
        console.log(roleId);
        //拼接出多个资源模块的id
        var mId = "mId=";
        for (var i = 0; i < nodes.length; i++) {
            //判断是不是倒数第二条个数据
            if (i < nodes.length - 1) {
                mId += nodes[i].id + "&mId=";
            } else {
                mId += nodes[i].id;
            }
        }
                //发送请求 授权
                $.ajax({
                    type:'get',
                    url:ctx + '/role/addGrant',
                    data:mId + "&roleId=" + roleId,
                    dataType:'json',
                    success:function(data){

                        if (data.code == 200) {
                            layer.msg(data.msg,{icon:6})
                        } else {
                            layer.msg(data.msg, {icon: 5});
                        }
                    }
                })
    }


});