package simple.project.oabg.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.service.Dagl_jysh_service;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 借阅审核
 * @author yinchao
 * @date 2017年9月9日
 */
@Controller
@RequestMapping("/oabg/jysh/")
public class Dagl_jysh_controller extends SimplewebBaseController{

	private final static String MODELPATH="/oabg/dagl/jysh/";
	
	@Autowired
	private Dagl_jysh_service dagl_jysh_service;
	
	/**
	 * 显示首页
	 * @return
	 * @author yinchao
	 * @date 2017年9月9日
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView mav=new ModelAndView(MODELPATH+"index");
		return mav;
	}
	
	/**
	 * 显示待审核列表
	 * @param map
	 * @return
	 * @author yinchao
	 * @date 2017年9月9日
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return dagl_jysh_service.grid(map);
	}
	
	/**
	 * 查看详细页面
	 * @return
	 * @author yinchao
	 * @date 2017年9月9日
	 */
	@RequestMapping("form")
	public ModelAndView form(){
		ModelAndView mav=new ModelAndView(MODELPATH+"form");
		return mav;
	}
	
	/**
	 * 查看详细
	 * @param id
	 * @return
	 * @author yinchao
	 * @date 2017年9月9日
	 */
	@ResponseBody
	@RequestMapping("getInfo")
	public Map<String,Object> getInfo(Long id){
		return dagl_jysh_service.getInfo(id);
	}

	/**
	 * 审批页面
	 * @return
	 * @author yinchao
	 * @date 2017年9月9日
	 */
	@RequestMapping("shForm")
	public ModelAndView shForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.addObject("id", id);
		mav.setViewName(MODELPATH+"shForm");
		return mav;
	}
	
	/**
	 * 保存审核
	 * @param t
	 * @return
	 * @author yinchao
	 * @date 2017年9月9日
	 */
	@ResponseBody
	@RequestMapping("save")
	public Result save(long id,String tgzt,String suggestion){
		return dagl_jysh_service.saveObj(id,tgzt,suggestion);
	}
	
	/**
	 * 归还
	 * @param id
	 * @return
	 * @author yinchao
	 * @date 2017年9月14日
	 */
	@ResponseBody
	@RequestMapping("gh")
	public Result gh(long id){
		return dagl_jysh_service.gh(id);
	}
}
