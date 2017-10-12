package simple.project.oabg.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.entities.Gdpsbl;
import simple.project.oabg.service.GdblService;
import simple.project.oabg.service.GdpsService;
import simple.project.oabg.service.Yhgl_service;
import simple.system.simpleweb.module.system.file.service.FileService;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.util.StringKit;

/**
 * 工单办理
 * 2017年9月27日
 * @author wsz
 */
@Controller
@RequestMapping("/oabg/gdpsbl/gdbl/")
public class GdblController extends SimplewebBaseController{

	private final static String MODELPATH="/oabg/gdpsbl/gdbl/";
	
	@Autowired
	private GdblService gdblService;
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
	 * 返回待回复人员能看到的工单派送grid列表
	 * @author wsz
	 * @created 2017年9月27日
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return gdblService.grid(map);
	}
	
	
	/**
	 * 查看工单派送详情页面
	 * @author wsz
	 * @created 2017年9月27日
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView form(Long id){
		ModelAndView model=new ModelAndView();
		model.setViewName(MODELPATH+"form");
		model.addObject("message", gdpsService.getInfo(id));
		Gdpsbl gdpsbl = gdblService.getDao().findOne(id);
		if(!StringKit.isEmpty(gdpsbl)){
			model.addObject("fjList", gdpsbl.getFileInfo());
		}
		return model;
	}
	
	/**
	 * 进行办理页面
	 * @author wsz
	 * @created 2017年9月27日
	 * @return
	 */
	@RequestMapping("blForm")
	public ModelAndView blForm(){
		ModelAndView model=new ModelAndView();
		model.setViewName(MODELPATH+"blForm");
		return model;
	}
	
	/**
	 * 办理
	 * @author wsz
	 * @created 2017年9月27日
	 * @param id 工单派送id
	 * @param suggestion 办理意见
	 * @return
	 */
	@ResponseBody
	@RequestMapping("doBl")
	public Result doBl(Long id,String suggestion){
		return gdblService.doBl(id,suggestion);
	}
	
	/**
	 * 工单派送的流程记录
	 * 2017年9月27日
	 * wsz
	 * @return
	 */
	@RequestMapping("lcjlForm")
	public ModelAndView lcjlForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lcjlForm");
		mav.addObject("gdpsFlowList", gdpsService.queryFlowById(id));
		return mav;
	}
	
	/**
	 * 判断当前办理日期是否超过办理截止日期
	 * 2017年9月27日
	 * wsz
	 * @param id 工单派送id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("checkBlsj")
	public Map<String,Object> checkBlsj(Long id){
		return gdblService.checkHfrq(id);
	}
}
