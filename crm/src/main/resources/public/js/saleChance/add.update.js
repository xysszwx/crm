layui.use(['table','layer','form'],function() {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        form = layui.form,
        $ = layui.jquery,
        table = layui.table;


    form.on("submit(addOrUpdateSaleChance)",function (data) {
        
        var index = layer.msg("数据提交中",{
            icon:16, // 图标
            time:false, // 不关闭
            shade:0.8 // 设置遮罩的透明度
        });


        var id = $('[name="id"]').val();

        if(id){
            var  url = ctx + "/sale_chance/update"

        }else{
            var  url = ctx + "/sale_chance/save";
        }

        $.post(url,data.field,function (data) {
            if(data.code==200){
                layer.msg("数据添加成功",{icon:6});

                layer.close(index);

                layer.closeAll("iframe");

                parent.location.reload();
            }else{
                layer.msg(data.msg,{icon:5});
            }
        })
        return false;
    })


    $.get(ctx+"/user/saleMan",function (data) {
         var am =   $('[name="am"]').val();
         console.log(data);
         for(var i=0 ; i<data.length ; i++){
             if(am == data[i].id){
                 $('#assignMan').append("<option selected value="+data[i].id+">"+data[i].uname+"</option>");
             }else{
                 $('#assignMan').append("<option  value="+data[i].id+">"+data[i].uname+"</option>");
             }
        }
        layui.form.render("select");
    });




    $("#closeBtn").click(function () {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });

});