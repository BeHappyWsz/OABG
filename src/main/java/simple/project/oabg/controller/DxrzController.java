package simple.project.oabg.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.service.DxrzService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 短信日志
 * 2017年9月6日
 * @author yc
 */
@Controller
@RequestMapping("/oabg/dxrz")
public class DxrzController extends SimplewebBaseController{
	
	private final static String MODELPATH="/oabg/dxrz/";
	
	@Autowired
	private DxrzService dxrzService;

	
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
		mav.addObject("dxrz_form_info", dxrzService.getInfo(id));
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
		return dxrzService.grid(map);
	}
	
}
