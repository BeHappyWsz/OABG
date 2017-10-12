package simple.project.oabg.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.service.SbwxSqService;
import simple.project.oabg.service.Yhgl_service;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 设备维修审批
 * @author wsz
 * @created 2017年9月20日
 */
@Controller
@RequestMapping("/oabg/sbwx/sbwxsp")
public class SbwxSpController extends SimplewebBaseController{

private final static String MODELPATH="/oabg/sbwx/sbwxsp/";
	
	@Autowired
	private UserService userService;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private SbwxSqService sbwxSqService;
	/**
	 * 模块主页面
	 * @author wsz
	 * @created 2017年9月20日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView model=new ModelAndView();
		model.setViewName(MODELPATH+"index");
		return model;
	}
	
	/**
	 * 获取所有申请记录列表
	 * @author wsz
	 * @created 2017年9月20日
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return sbwxSqService.grid(map);
	}
	
	
	/**
	 * 新增查询页面
	 * @author wsz
	 * @created 2017年9月20日
	 * 
	 */
	@RequestMapping("form")
	public ModelAndView form(Long id){
		ModelAndView model=new ModelAndView();
		model.setViewName(MODELPATH+"form");
		model.addObject("message", sbwxSqService.getInfo(id));
		return model;
	}
	
	/**
	 * 获取某条申请记录的信息内容
	 * @author wsz
	 * @created 2017年9月20日
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getInfo")
	public Map<String,Object> getInfo(Long id){
		return sbwxSqService.getInfo(id);
	}
	
	
	/**
	 * 流程记录form表单页
	 * 2017年9月20日
	 * wsz
	 * @param 该条申请记录的id
	 * @return
	 */
	@RequestMapping("lcjlForm")
	public ModelAndView lcjlForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lcjlForm");
		mav.addObject("swngFlowList", sbwxSqService.queryFlowById(id));
		return mav;
	}
	
	
	/**
	 * 审批页面
	 * @author wsz
	 * @created 2017年9月20日
	 */
	@RequestMapping("spForm")
	public ModelAndView spForm(String id){
		ModelAndView model=new ModelAndView();
		model.setViewName(MODELPATH+"spForm");
		model.addObject("id", id);
		return model;
	}
	
	
	/**
	 * 进行审批
	 * @author wsz
	 * @created 2017年9月20日
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("doSp")
	public Result doSp(Long id,String sftg,String suggestion){
		return sbwxSqService.doSp(id,sftg,suggestion);
	}
	
	/**
	 * 进行已维修状态变换
	 * @author wsz
	 * @created 2017年9月20日
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("doYwx")
	public Result doYwx(Long id){
		return sbwxSqService.doYwx(id);
	}
	
	/**
	 * 大量数据进行已维修状态变换
	 * @author wsz
	 * @created 2017年9月20日
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("doYwxs")
	public Result doYwxs(String ids){
		return sbwxSqService.doYwxs(ids);
	}
}
