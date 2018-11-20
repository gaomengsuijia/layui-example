layui.define(['layer'], function(exports) {
    "use strict";

    var $ = layui.jquery,
        layer = layui.layer;

    var myobj = {
        //外呼场景渲染
        getDataList:function (form) {
            $.ajax({
                type:'post',
                dataType : "json",
                url: layui.setter.serverURL + "/calloutScene/list",
                contentType: 'application/json;charset=utf-8',
                success:function(response){
                    if ("0" == response.code){
                        var data=response.data;
                        var t;
                        if (data != null){
                            $.each(data,function (index,element) {
                                t = '<option value="' +element.id +  '">' +element.calloutType +  '</option>';
                                $('#sceneId').append(t);
                            })
                        }
                    }

                    form.render();
                }
            })
        },
        //去掉字符所有的空格
        trim:function (str,is_global="g") {
            var result;
            result = str.replace(/(^\s+)|(\s+$)/g,"");
            if(is_global.toLowerCase()=="g") {
                result = result.replace(/\s/g,"");
            }
            return result;
        }
    };

    exports('mypub', myobj);
});
