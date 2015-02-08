

$.fn.serializeJson=function(){
    var serializeObj={};
    var array=this.serializeArray();
    var str=this.serialize();
    $(array).each(function(){
        if(serializeObj[this.name]){
            if($.isArray(serializeObj[this.name])){
                serializeObj[this.name].push(this.value);
            }else{
                serializeObj[this.name]=[serializeObj[this.name],this.value];
            }
        }else{
            serializeObj[this.name]=this.value;
        }
    });
    return serializeObj;
};

$.extend({ mhtMsg:function(msg){
    new PNotify({
        text: msg,
        animate_speed: 'fast',
        type: 'success'
    });
}});

$.fn.twbsPagination.defaults.first='首页';
$.fn.twbsPagination.defaults.prev='前一页';
$.fn.twbsPagination.defaults.next='后一页';
$.fn.twbsPagination.defaults.last='最后一页';

$(function(){
    $(".fancybox").fancybox();
    if ($("[data-toggle=tooltip]").length) {
        $("[data-toggle=tooltip]").tooltip();
    }

})


function ajaxCallbackHandle(loginOutUrl){
    var ajax = $.ajax;
    $.ajax = function (opt) {
        //备份opt中error和success方法
        var fn = {
            success: function (data, textStatus, jqXHR) {
            }
        }
        if (opt.success) {
            fn.success = opt.success;
        }
        //扩展增强处理
        var _opt = $.extend(opt, {
            success: function (data, textStatus, jqXHR) {
                if (data=='RELOGIN') {
                    location.href = loginOutUrl;
                    return;
                }
                fn.success(data, textStatus, jqXHR);
            }
        });
        var def = ajax.call($, _opt);                                                                                                                             // 兼容不支持异步回调的版本
        if('done' in def){
            var done = def.done;
            def.done = function (func) {
                function _done(data) {
                    if (data=='RELOGIN') {
                        location.href = loginOutUrl;
                        return;
                    }
                    func(data);
                }

                done.call(def, _done);
                return def;
            };
        }
        return def;
    };
}