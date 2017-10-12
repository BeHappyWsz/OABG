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
import simple.project.oabg.dao.DeOrgDao;
import simple.project.oabg.dao.DeRoleDao;
import simple.project.oabg.service.Gzdj_service;
import simple.project.oabg.service.Gzsq_service;
import simple.project.oabg.service.Hyfk_service;
import simple.project.oabg.service.Hytz_service;
import simple.project.oabg.service.Yhgl_service;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 用户管理
 * @author yzw
 * @created 2017年8月21日
 */
@Controller
@RequestMapping("/oabg/gzgl/gzsh")
public class Gzsh_controller extends SimplewebBaseController{
	protected Logger log = LoggerFactory.getLogger(WebApplicationStarter.class);
	@Autowired
	private DeOrgDao deOrgDao;
	@Autowired
	private DeRoleDao roleDao;
	@Autowired
	private UserService userService;
	@Autowired
	private Hytz_service hytzService;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private Hyfk_service hyfk_service;
	@Autowired
	private Gzsq_service gzsq_service;
	@Autowired
	private Gzdj_service gzdj_service;
	private final static String MODELPATH="/oabg/gzgl/gzsh/";
	
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
	 * 申请审核列表
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("shgrid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return gzsq_service.SPgrid(map);
	}
	/**
	 * form表单页
	 * 2017年9月4日
	 * yc
	 * @return
	 */
	@RequestMapping("shform")
	public ModelAndView form(Long id){
		ModelAndView mav = new ModelAndView();
		mav.addObject("id", id);
		mav.addObject("gzsp_form_info", gzsq_service.getInfo(id));
		mav.setViewName(MODELPATH+"form");
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
	 * 流程记录form表单页
	 * 2017年9月4日
	 * yc
	 * @return
	 */
	@RequestMapping("gzlcForm")
	public ModelAndView lcjlForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lcjlForm");
		mav.addObject("lwypSqFlowList", gzsq_service.queryFlowById(id));
		return mav;
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
		return gzsq_service.doSp(id, sftg, suggestion);
	}
	
	/**
	 * 获取公章使用查询页面
	 * @param id
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@RequestMapping("query")
	public ModelAndView query(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"query");
		return mav;
	}
	
	/**
	 * 公章列表
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("querygrid")
	public DataGrid querygrid(@RequestParam Map<String,Object> map){
		return gzdj_service.querygrid(map);
	}
}
