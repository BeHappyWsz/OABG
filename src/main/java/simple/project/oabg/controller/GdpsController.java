package simple.project.oabg.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.entities.Gdpsbl;
import simple.project.oabg.service.GdpsService;
import simple.project.oabg.service.Yhgl_service;
import simple.system.simpleweb.module.system.file.service.FileService;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 工单派送
 * 2017年9月27日
 * @author wsz
 */
@Controller
@RequestMapping("/oabg/gdpsbl/gdps/")
public class GdpsController extends SimplewebBaseController{
	private final static String MODELPATH="/oabg/gdpsbl/gdps/";
	@Autowired
	private GdpsService gdpsService;
	@Autowired
	private FileService fileService;
	@Autowired
	private UserService userService;
	@Autowired
	private Yhgl_service yhgl_service;
	/**
	 * 主页
	 * 2017年9月27日
	 * wsz
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView model=new ModelAndView();
		model.setViewName(MODELPATH+"index");
		return model;
	}
	
	/**
	 * 返回grid列表
	 * @author wsz
	 * @created 2017年9月27日
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return gdpsService.grid(map);
	}
	
	
	/**
	 * 新增页面
	 * @author wsz
	 * @created 2017年9月27日
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView form(){
		User user = userService.getCurrentUser();
		ModelAndView model=new ModelAndView(MODELPATH+"form");
		model.addObject("psr", user.getUsername());
		return model;
	}
	
	/**
	 * 保存工单派送
	 * @param s 工单派送对象
	 * @param fjs 上传的附件
	 * @param sldws 受理单位
	 * @return 
	 * @author wsz
	 * @created 2017年9月7日
	 */
	@ResponseBody
	@RequestMapping("save")
	public Result save(Gdpsbl g,String fjs,String sldws){
		return gdpsService.saveGdpsbl(g, fjs, sldws);
	}
	
	/**
	 * 查看详情页面
	 * @author wsz
	 * @created 2017年9月27日
	 * @return
	 */
	@RequestMapping("lookForm")
	public ModelAndView lookForm(Long id){
		ModelAndView model=new ModelAndView(MODELPATH+"lookForm");
		model.addObject("message", gdpsService.getInfo(id));
		Gdpsbl findOne = gdpsService.getDao().findOne(id);
		model.addObject("fjList", findOne.getFileInfo());
		return model;
	}
	
	
	/**
	 * 查看流程记录信息
	 * @author wsz
	 * @created 2017年9月27日
	 * @return
	 */
	@RequestMapping("lcjlForm")
	public ModelAndView lcjlForm(String id){
		ModelAndView model=new ModelAndView(MODELPATH+"lcjlForm");
		model.addObject("gdpsFlowList", gdpsService.queryFlowById(id));
		return model;
	}
	
	
	/**
	 * 办理期限起始时间
	 * @param 
	 * @return
	 * @author wsz
	 * @created 2017年9月27日
	 */
	@ResponseBody
	@RequestMapping("checkoutStrtime")
	public Result checkoutStrtime(Date timeStr,Date timeEnd ){
		return gdpsService.TimeStr(timeStr, timeEnd);
	}
	
	/**
	 * 办理期限结束时间
	 * @param 
	 * @return
	 * @author wsz
	 * @created 2017年9月27日
	 */
	@ResponseBody
	@RequestMapping("checkoutendtime")
	public Result checkoutendtime(Date timeStr,Date timeEnd ){
		return gdpsService.TimeEnd(timeStr, timeEnd);
	}
}
