<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>

<form name=alipayment action="/shop/pay/paying" method=post
			target="_blank" items="${orderInfo}">
Last name:<br>
<input type="text" name="orderId" value="${orderInfo.orderID}">
<br><br>
<input type="submit" value="Submit">
</form>

</body>
</html>