package simple.project.oabg.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.service.LwypSqjlService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 劳务用品审批
 * 2017年9月4日
 * @author yc
 */
@Controller
@RequestMapping("/oabg/lwypsp")
public class LwypSpController extends SimplewebBaseController{
	
	private final static String MODELPATH="/oabg/lwypgl/lwypsp/";
	
	@Autowired
	private LwypSqjlService lwypSqjlService;

	
	/**
	 * 主页
	 * 2017年9月4日
	 * yc
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView model=new ModelAndView(MODELPATH+"index");
		return model;
	}

	/**
	 * form表单页
	 * 2017年9月4日
	 * yc
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView form(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"form");
		mav.addObject("lwypsp_form_info", lwypSqjlService.getInfo(id));
		return mav;
	}
	
	/**
	 * form表单页
	 * 2017年9月4日
	 * yc
	 * @return
	 */
	@RequestMapping("spForm")
	public ModelAndView spForm(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"spForm");
		return mav;
	}

	/**
	 * 数据列表
	 * 2017年9月4日
	 * yc
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return lwypSqjlService.grid(map);
	}
	
	/**
	 * 执行审批
	 * 2017年9月4日
	 * yc
	 * @param id
	 * @param sftg
	 * @param suggestion
	 * @return
	 */
	@ResponseBody
	@RequestMapping("doSp")
	public Result doSp(String id,String sftg,String suggestion){
		return lwypSqjlService.doSp(id, sftg, suggestion);
	}
}
