layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    /**
     * 用户列表展示
     */
    var  tableIns = table.render({
        elem: '#userList',
        url : ctx + '/user/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "userListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'userName', title: '用户名', minWidth:50, align:"center"},
            {field: 'email', title: '用户邮箱', minWidth:100, align:'center'},
            {field: 'phone', title: '用户电话', minWidth:100, align:'center'},
            {field: 'trueName', title: '真实姓名', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
            {title: '操作', minWidth:150, templet:'#userListBar',fixed:"right",align:"center"}
        ]]
    });

    $("#btnSearch").click(function(){
        tableIns.reload({
            where: { //设定异步数据接口的额外参数，任意设
                userName:$('[name = "userName"]').val(),
                email:$('[name = "email"]').val(),
                phone:$('[name = "phone"]').val()
            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });

    table.on("toolbar(users)",function (obj) {
        if(obj.event=='add'){
            openAddAndUpdate();
        }else if(obj.event=='del'){
            var checkStatus = table.checkStatus(obj.config.id);
            deleteBatch(checkStatus.data);
        }
    });
    table.on("tool(users)",function (obj) {
        if(obj.event=='edit'){
            openAddAndUpdate(obj.data.id);
        }else if(obj.event == 'del'){
            var arr = [obj.data];
            deleteBatch(arr);
        }
    })

   function openAddAndUpdate(id){
       var title = "用户管理-用户添加";
       var url = ctx + "/user/toAddAndUpdate";
       if(id!=null){
           url += "?id="+id;
       }
       layer.open({
           type:2,
           title:title,
           content:url,
           maxmin:true,
           area:["650px","400px"]
       })
   }
   
   
   function deleteBatch(data) {
       if(data.length<1 || data == null){
           layer.msg("请选择要删除的记录");
           return;
       }

       layer.confirm("您确定要删除选中的记录吗？",{
           btn:["确认","取消"],
       },function (index) {
           layer.close(index);

           var ids = "";
           ids += "ids=" + data[0].id;
           console.log(ids);
           for(var i=1;i<data.length;i++){
               ids+= "&ids="+data[i].id;
           }
           console.log(ids);
           $.ajax({
               type:"post",
               url:ctx+"/user/deleteBatch",
               data:ids,
               dataType:"json",
               success:function (data) {
                   if(data.code == 200){
                       layer.msg("删除成功",{icon:6});
                       tableIns.reload();
                   }else{
                       layer.msg(data.msg , {icon:5});
                   }
               }
           });

       });

   }

});