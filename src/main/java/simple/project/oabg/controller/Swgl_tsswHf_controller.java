package simple.project.oabg.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.entities.Swng;
import simple.project.oabg.service.Swgl_tssw_service;
import simple.project.oabg.service.Yhgl_service;
import simple.system.simpleweb.module.system.file.service.FileService;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.util.StringKit;

/**
 * 收文管理之特殊收文回复controller
 * @author wsz
 * @created 2017年9月9日
 */
@Controller
@RequestMapping("/oabg/tsswhf/")
public class Swgl_tsswHf_controller extends SimplewebBaseController{
	@Autowired
	private FileService fileService;
	@Autowired
	private UserService userService;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private Swgl_tssw_service swgl_tssw_service;
	/**模块公共路径**/
	private final static String MODELPATH="/oabg/tsswhf/";
	
	/**
	 * 返回主页面
	 * @author wsz
	 * @created 2017年9月9日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"index");
		return mav;
	}
	
	
	/**
	 * 返回待回复人员能看到的特殊收文grid列表
	 * @author wsz
	 * @created 2017年9月9日
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "hfgrid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return swgl_tssw_service.hfGrid(map);
	}
	
	/**
	 * 查看特殊收文页面
	 * @author wsz
	 * @created 2017年9月4日
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView form(Long id){
		ModelAndView model=new ModelAndView();
		model.setViewName(MODELPATH+"form");
		model.addObject("message", swgl_tssw_service.getInfo(id));
		Swng swng = swgl_tssw_service.getDao().findOne(id);
		if(!StringKit.isEmpty(swng)){
			model.addObject("fjList", swng.getFileInfo());
		}
		return model;
	}
	
	/**
	 * 获取详情
	 * @author wsz
	 * @created 2017年9月7日
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getInfo")
	public Map<String,Object> getInfo(Long id){
		
		return swgl_tssw_service.getInfo(id);
	}
	
	/**
	 * 进行回复页面
	 * @author wsz
	 * @created 2017年9月9日
	 * @return
	 */
	@RequestMapping("hfForm")
	public ModelAndView hfForm(){
		ModelAndView model=new ModelAndView();
		model.setViewName(MODELPATH+"hfForm");
		return model;
	}
	
	/**
	 * 保存回复
	 * @author wsz
	 * @created 2017年9月9日
	 * @param id 特殊收文id
	 * @param suggestion 回复意见
	 * @return
	 */
	@ResponseBody
	@RequestMapping("doSave")
	public Result doSave(Long id,String suggestion){
		return swgl_tssw_service.doSave(id,suggestion);
	}
	
	
	/**
	 * 特殊收文的流程记录form表单页
	 * 2017年9月7日
	 * wsz
	 * @return
	 */
	@RequestMapping("lcjlForm")
	public ModelAndView lcjlForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lcjlForm");
		mav.addObject("tsswngFlowList", swgl_tssw_service.queryFlowById(id));
		return mav;
	}
	
	/**
	 * 判断当前回复日期是否超过回复截止日期
	 * 2017年9月7日
	 * wsz
	 * @param id 特殊收文id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("checkHfrq")
	public Map<String,Object> checkHfrq(Long id){
		return swgl_tssw_service.checkHfrq(id);
	}
}
