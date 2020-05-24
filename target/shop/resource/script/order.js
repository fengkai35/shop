//主要用于存放交互逻辑的js代码
//javascript模块化
var order= {
    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        init: function (result) {
            var node = $('#seckill-box');
            if (result && result['success']){
                var killResult = result['data'];
                var state = killResult['state'];
                var stateInfo = killResult['stateInfo'];
                var successKill  = killResult['successKill'];
                var order_number = killResult['order_number'];
                //显示秒杀结果
                node.html('<span class="label stateInfo">'+stateInfo+'</span>')
                node.html('<span class="label order_number">'+order_number+'</span>')
                node.html('<button class="btn btn-primary btn-pay" id="payBtn">支付</button>');
                $('#killBtn').one('click',function(){
                    var params ={};
                    params.orderId = order_number;
                    $.post("pay/paying", params, function () {

                    });

            }


        }
    }
}

