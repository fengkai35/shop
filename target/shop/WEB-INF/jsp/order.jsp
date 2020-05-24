<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>


<head>
    <title>订单详情页</title>
</head>
<body>


<div style="width:100%;text-align:center">

            <h1>${order.data.stateInfo}</h1>

<form name=alipayment action="/shop/pay/paying" method=post
			target="_blank">
<br>
订单编号：<input style="border:none;width: 260px" type="text" name="orderId" value="${order.data.successKill.orderNumber}" readonly="true">
<br>
支付金额：<input style="border:none;width: 260px" type="text" name="price" value="${order.data.successKill.price}" readonly="true">
<br>
商品编号：<input style="border:none;width: 260px" name="orderName" value="${order.data.successKill.seckillId}" readonly="true">
<br>
下单时间：<input style="border:none;width: 260px" type="text" name="createTime" value="${order.data.successKill.createTime}" readonly="true">
<br><br>
<input type="submit"  value="马上支付订单">
</form>
</div>
</body>
</html>