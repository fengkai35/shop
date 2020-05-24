package com.fengkai35.pay.web;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.fengkai35.pay.service.PayService;
import com.fengkai35.pay.util.AlipayConfig;
import com.fengkai35.seckill.entity.Seckill;
import com.fengkai35.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/pay")
public class PayController {


	@RequestMapping(value="",method=RequestMethod.GET)
	public String list(Model model){
		return "payindex";
	}

	@Autowired
	private SeckillService seckillService;
	@Autowired
	private PayService payService;

	/*
	 * @Param orderId 订单的id
	 * @Param orderName 订单名称
	 * @Param price   价格
	 * @Param content 详细信息
	 * */
	@PostMapping(value="/paying",produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String doPost(@RequestParam("orderId") String orderId,
						 @RequestParam("orderName") String orderName,
						 @RequestParam("price") String price,
					   HttpServletResponse httpResponse)
			throws ServletException, IOException, AlipayApiException {

		System.out.println("=======================ddd---=------------------");

		String content = "无";
		//初始化配置信息
		payService.settingInfo();

//        初始化
		AlipayClient alipayClient = new DefaultAlipayClient
				(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, AlipayConfig.format, AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type); //获得初始化的AlipayClient
//        这个request对应网页支付
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
//        回调函数地址
		alipayRequest.setReturnUrl(AlipayConfig.return_url);
//        通知地址
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);//在公共参数中设置回跳和通知地址
//        订单号 名称 价格 描述
//        拼接json传给支付宝
		alipayRequest.setBizContent("{" +
				"    \"out_trade_no\":\"" + orderId + "\"," +
				"    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
				"    \"total_amount\":" + price + "," +
				"    \"subject\":\"" + orderName + "\"," +
				"    \"body\":\"" + content + "\"," +
				"    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
				"    \"extend_params\":{" +
				"    \"sys_service_provider_id\":\"2088511833207846\"" +
				"    }" +
				"  }");//填充业务参数

		String form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单

		return form;


//        会跳转到一个支付的页面进行支付
	}

	//回调函数  return_url填写的就是访问这里的地址
	@RequestMapping(value = "/return")
	public String alipayReturn(Model model, HttpServletRequest request, HttpServletRequest response) throws Exception {
		System.out.println("支付成功 进入同步接口");
		//获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes(AlipayConfig.charset), AlipayConfig.charset);
			params.put(name, valueStr);
		}
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
		//——请在这里编写您的程序（以下代码仅作参考）——
		if (signVerified) {
			System.out.println("成功进入");
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

			//付款金额，这里获取到三个参数就可以了，后面逻辑代码自己创作
			String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

			int result = 1;
			//这里根据自身的业务写代码，我这里删掉了
			result = seckillService.updatePayState(out_trade_no);

			if (result == 0) {
				model.addAttribute("resultinfo", "更新订单失败！请业务员联系后台管理员！");
			} else {
				model.addAttribute("resultinfo", "更新订单成功！");
			}
			System.out.println("* 订单号: {}" + out_trade_no);
			System.out.println("* 支付宝交易号: {}" + trade_no);
			System.out.println("* 实付金额: {}" + total_amount);
		} else {
			System.out.println("支付延签失败");
			return "failed";
		}
		return "success";//成功返回首页
	}

	//异步回调函数  notify_url填写的就是访问这里的地址
	@RequestMapping(value = "/notify_return")
	public String alipayNotifyReturn(Model model, HttpServletRequest request, HttpServletRequest response) throws Exception {
		System.out.println("支付成功 进入异步接口");
		//获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes(AlipayConfig.charset), AlipayConfig.charset);
			params.put(name, valueStr);
		}
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
		//——请在这里编写您的程序（以下代码仅作参考）——
		if (signVerified) {
			System.out.println("成功进入");
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

			//付款金额，这里获取到三个参数就可以了，后面逻辑代码自己创作
			String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

			int result = 1;
			//这里根据自身的业务写代码，我这里删掉了

			result = seckillService.updatePayState(out_trade_no);

			if (result == 0) {
				model.addAttribute("resultinfo", "更新订单失败！请业务员联系后台管理员！");
			} else {
				model.addAttribute("resultinfo", "更新订单成功！");
			}
			System.out.println("* 订单号: {}" + out_trade_no);
			System.out.println("* 支付宝交易号: {}" + trade_no);
			System.out.println("* 实付金额: {}" + total_amount);

		} else {
			System.out.println("支付延签失败");
			return "failed";
		}
		return "success";//成功返回首页
	}

}
