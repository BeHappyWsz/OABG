package simple.project.oabg.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.service.Swgl_swng_service;
import simple.project.oabg.service.Swgl_swsh_service;
import simple.system.simpleweb.module.system.file.dao.FileInfoDao;
import simple.system.simpleweb.module.system.file.service.FileService;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 收文管理之收文审核controller
 * @author wsz
 * @created 2017年9月5日
 */
@Controller
@RequestMapping("/oabg/swsh/")
public class Swgl_swsh_controller  extends SimplewebBaseController{
	@Autowired
	private Swgl_swsh_service swgl_swsh_service;
	@Autowired
	private Swgl_swng_service swgl_swng_service;
	@Autowired
	private FileInfoDao fileInfoDao;
	@Autowired
	private FileService fileService;
	@Autowired
	private UserService userService;
	/**模块公共路径**/
	private final static String MODELPATH="/oabg/swsh/";
	
	/**
	 * 模块主页面
	 * @author wsz
	 * @created 2017年9月5日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"index");
		String role = userService.getCurrentUser().getRoles().get(0).getRoleCode();
		mav.addObject("role", role);
		return mav;
	}
	
	/**
	 * 数据列表
	 * 2017年9月5日
	 * wsz
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return swgl_swsh_service.grid(map);
	}

	/**
	 * *查看详情页面
	 * @author wsz
	 * @created 2017年9月5日
	 * @param id 
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView form(Long id){
		String username=userService.getCurrentUser().getUsername();
		ModelAndView mav=new ModelAndView(MODELPATH+"form");
		mav.addObject("username", username);
		mav.addObject("message", swgl_swng_service.getInfo(id));
		mav.addObject("fjList", swgl_swng_service.getDao().findOne(id).getFileInfo());
		return mav;
	}
	
	/**
	 * 获取详情
	 * @author wsz
	 * @created 2017年9月5日
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getInfo")
	public Map<String,Object> getInfo(Long id){
		return swgl_swng_service.getInfo(id);
	}
	
	
	/**
	 * 拟办页面
	 * 2017年9月6日
	 * wsz
	 * @return
	 */
	@RequestMapping("nbForm")
	public ModelAndView nbForm(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"nbForm");
		return mav;
	}
	
	/**
	 * 批阅页面
	 * 2017年9月6日
	 * wsz
	 * @return
	 */
	@RequestMapping("spForm")
	public ModelAndView spForm(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"spForm");
		return mav;
	}
	
	/**
	 * 执行批量拟办
	 * 2017年9月6日
	 * wsz
	 * @param ids 所有待拟办的ids
	 * @param suggestion
	 * @return
	 */
	@ResponseBody
	@RequestMapping("doNb")
	public Result doNb(String ids,String suggestion,String fglds){
		return swgl_swng_service.doNbs(ids,suggestion,fglds);
	}
	
	
	/**
	 * 可批量执行批阅操作
	 * 2017年9月6日
	 * wsz
	 * @param ids 收文拟稿ids
	 * @param suggestion
	 * @return
	 */
	@ResponseBody
	@RequestMapping("doPy")
	public Result doPy(String ids,String suggestion){
		return swgl_swng_service.doPys(ids,suggestion);
	}
	/**
	 * 流程记录form表单页
	 * 2017年9月5日
	 * wsz
	 * @return
	 */
	@RequestMapping("lcjlForm")
	public ModelAndView lcjlForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lcjlForm");
		mav.addObject("swngFlowList", swgl_swng_service.queryFlowById(id));
		return mav;
	}
	
}
