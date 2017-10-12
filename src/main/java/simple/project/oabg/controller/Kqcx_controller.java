package simple.project.oabg.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.service.kqcx_service;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 考勤查询
 * @author yzw
 * @created 2017年8月21日
 */
@Controller
@RequestMapping("/oabg/kqgl/kqcx")
public class Kqcx_controller extends SimplewebBaseController{
	@Autowired
	private kqcx_service kqcx_service;
	
	private final static String MODELPATH="/oabg/kqgl/kqcx/";
	
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
	 *考勤列表
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return kqcx_service.grid(map);
	}
	
}
