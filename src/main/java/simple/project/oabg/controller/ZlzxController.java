package simple.project.oabg.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.entities.Zlzx;
import simple.project.oabg.service.ZlzxService;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 资料中心
 * 2017年9月5日
 * @author yc
 */
@Controller
@RequestMapping("/oabg/zlzx")
public class ZlzxController extends SimplewebBaseController{
	
	private final static String MODELPATH="/oabg/zlzx/";
	
	@Autowired
	private ZlzxService zlzxService;
	@Autowired
	private UserService userService;

	
	/**
	 * 主页
	 * 2017年9月5日
	 * yc
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView model=new ModelAndView(MODELPATH+"index");
		model.addObject("zlzxCurUserName", userService.getCurrentUser().getUsername());
		return model;
	}

	/**
	 * 数据列表
	 * 2017年9月5日
	 * yc
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return zlzxService.grid(map);
	}
	
	/**
	 * form表单页
	 * 2017年9月5日
	 * yc
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView form(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"form");
		return mav;
	}
	
	/**
	 * 保存
	 * 2017年9月5日
	 * yc
	 * @param t
	 * @return
	 */
	@ResponseBody
	@RequestMapping("save")
	public Result save(Zlzx t){
		return zlzxService.saveObj(t);
	}
	
	/**
	 * 根据id获取详情
	 * 2017年9月5日
	 * yc
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getInfo")
	public Map<String,Object> getInfo(String id){
		return zlzxService.getInfo(id);
	}
	
	/**
	 * 删除
	 * 2017年9月5日
	 * yc
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public Result delete(String ids){
		return zlzxService.deleteByIds(ids);
	}
	
}
