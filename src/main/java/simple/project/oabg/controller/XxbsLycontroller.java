package simple.project.oabg.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.WebApplicationStarter;
import simple.project.oabg.service.XxbsLyService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 用户管理
 * @author yzw
 * @created 2017年8月21日
 */
@Controller
@RequestMapping("/oabg/xxbsly")
public class XxbsLycontroller extends SimplewebBaseController{
	protected Logger log = LoggerFactory.getLogger(WebApplicationStarter.class);
	@Autowired
	private final static String MODELPATH="/oabg/xxbsly/";
	
	@Autowired
	private XxbsLyService xxbslyService;
	
	/**
	 * 模块主页面
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView model=new ModelAndView(MODELPATH+"index");
		return model;
	}
	/**
	 * 数据列表
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String , Object> map){
		return xxbslyService.grid(map);
	}
	
	/**
	 * 新增修改页面
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView form(Long id){
		ModelAndView model=new ModelAndView(MODELPATH+"form");
		model.addObject("xxbsInfo", xxbslyService.getInfo(id));
		return model;
	}
	
	/**
	 * 根据ID查找单个信息
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@ResponseBody
	@RequestMapping("Info")
	public Map<String, Object> Info(Long id){
		return xxbslyService.getInfo(id);
	}
	
	/**
	 * 流程记录form表单页
	 * 2017年9月4日
	 * yc
	 * @return
	 */
	@RequestMapping("lcjlFormWin")
	public ModelAndView lcjlForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lcjlForm");
		mav.addObject("xxbsSqFlowList", xxbslyService.queryFlowById(id));
		return mav;
	}
	
	/**
	 * 流程记录form表单页
	 * 2017年9月4日
	 * yc
	 * @return
	 */
	@RequestMapping("xxbsly")
	public ModelAndView SpXxbs(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"spForm");
		return mav;
	}
	
	/**
	 * 录用
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@ResponseBody
	@RequestMapping("doLy")
	public Result doLy(String sfly , String suggestion , Long id){
		return xxbslyService.doSp(id, sfly, suggestion);
	}
	
	/**
	 * 流程记录form表单页
	 * 2017年9月4日
	 * yc
	 * @return
	 */
	@RequestMapping("dafen")
	public ModelAndView dfXxbs(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"dfForm");
		return mav;
	}
	
	/**
	 * 录用
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@ResponseBody
	@RequestMapping("doDf")
	public Result doDf(String score ,Long id){
		return xxbslyService.doDf(id, score);
	}
}
