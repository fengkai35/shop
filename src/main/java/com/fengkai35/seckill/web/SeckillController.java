package com.fengkai35.seckill.web;

import com.fengkai35.seckill.dto.Exposer;
import com.fengkai35.seckill.dto.SeckillExecution;
import com.fengkai35.seckill.dto.SeckillResult;
import com.fengkai35.seckill.entity.OrderInfo;
import com.fengkai35.seckill.entity.Seckill;
import com.fengkai35.seckill.enums.SeckillStatEnum;
import com.fengkai35.seckill.exception.RepeatKillException;
import com.fengkai35.seckill.exception.SeckillCloseException;
import com.fengkai35.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
	
	private final Logger logger=LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	private SeckillService seckillService;


	@RequestMapping(value="",method=RequestMethod.GET)
	public String index(Model model){
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list",list);
		return "list";
	}



	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(Model model){
		List<Seckill> list = seckillService.getSeckillList();
		System.out.print("==============---" + list.size()+"========");
		model.addAttribute("list",list);
		return "list";
	}
	
	 @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
	    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
	        if(seckillId == null){
	            return "redirect:/seckill/list";
	        }
	        Seckill seckill = seckillService.getById(seckillId);
	        if(seckill == null){
	            return "redirect:/seckill/list";
	        }
	        model.addAttribute("seckill", seckill);
	        return "detail";
	    }

	    @RequestMapping(value = "/{seckillId}/exposer",
	                    method = RequestMethod.POST,
	                    produces = {"application/json;charset=UTF-8"})
	    @ResponseBody
	    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId){
	        SeckillResult<Exposer> result;
	        try {
	            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
	            result = new SeckillResult<Exposer>(true,exposer);
	        } catch (Exception e) {
	            result = new SeckillResult<Exposer>(false, e.getMessage());
	        }
	        return result;
	    }



	@RequestMapping(value = "/{seckillId}/{md5}/order",
			method = RequestMethod.GET)

	public String order(Model model,@PathVariable("seckillId")Long seckillId,
												   @PathVariable("md5")String md5,
												   @CookieValue(value = "killPhone", required = false)Long phone){
		SeckillResult<SeckillExecution> order;

		if(phone == null){
			order = new SeckillResult<SeckillExecution>(false, "未注册");
		}
		else{
			try {

				//SeckillExecution execution = seckillService.executeSeckillByProcedure(seckillId, phone, md5);
				SeckillExecution execution = seckillService.executeOrder(seckillId, phone, md5);
				order = new SeckillResult<SeckillExecution>(true, execution);
			} catch (SeckillCloseException e ) {
				SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
				order = new SeckillResult<SeckillExecution>(true, execution);
			}  catch (Exception e) {
				logger.error(e.getMessage(), e);
				SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
				order = new SeckillResult<SeckillExecution>(true, execution);
			}
		}


		model.addAttribute("order",order);

		return "order";
	}




//
//	    @RequestMapping(value = "/{seckillId}/{md5}/execution",
//	                    method = RequestMethod.POST,
//	                    produces = {"application/json;charset=UTF-8"})
//	    @ResponseBody
//	    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId")Long seckillId,
//                                                       @PathVariable("md5")String md5,
//                                                       @CookieValue(value = "killPhone", required = false)Long phone){
//	        if(phone == null){
//	            return new SeckillResult<SeckillExecution>(false, "未注册");
//	        }
//
//	        try {
//
//	        	//SeckillExecution execution = seckillService.executeSeckillByProcedure(seckillId, phone, md5);
//				SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
//
//
//	        	 return new SeckillResult<SeckillExecution>(true, execution);
//	        } catch (SeckillCloseException e) {
//	            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
//	            return new SeckillResult<SeckillExecution>(false, execution);
//	        } catch (RepeatKillException e) {
//	            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
//	            return new SeckillResult<SeckillExecution>(false, execution);
//	        } catch (Exception e) {
//	            logger.error(e.getMessage(), e);
//	            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
//	            return new SeckillResult<SeckillExecution>(false, execution);
//	        }
//	    }

		@RequestMapping(value = "/{userPhone}/addUser",
				method = RequestMethod.POST,
				produces = {"application/json;charset=UTF-8"})
		public void addUser(Model model,@PathVariable("userPhone") Long userPhone){
			System.out.println("ddd");
			seckillService.addUser(userPhone);

		}
	    
	    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
	    @ResponseBody
	    public SeckillResult<Long> time(){
	        Date now = new Date();
	        return new SeckillResult<Long>(true, now.getTime());
	    }
		//"0 11 20 ? * *"
		//"0/10 * * * * ?"
		@Scheduled(cron = "0 0/3 * * * ?")//每隔24小时执行一次
		public void clearOrder() {
			System.out.println("=-=============-------------=======开始执行定时任务"+new Date());
			seckillService.clearOrder();
		}

		//"0 11 20 ? * *"
		//"0/10 * * * * ?"
		@Scheduled(cron = "0 0/1 * * * ?")//每隔8小时执行一次
		public void unPaidOrder() {
			System.out.println("=-=============-------------=======开始执行定时任务"+new Date());
			seckillService.unPaidOrder();
		}

}
