/*package simple.project.oabg.controller;

import java.util.HashMap;
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
import simple.project.oabg.entities.Gzdj;
import simple.project.oabg.service.Gzdj_service;
import simple.project.oabg.service.Hyfk_service;
import simple.project.oabg.service.Hytz_service;
import simple.project.oabg.service.Yhgl_service;
import simple.system.simpleweb.module.system.file.service.FileService;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.util.StringKit;

*//**
 * 用户管理
 * @author yzw
 * @created 2017年8月21日
 *//*
@Controller
@RequestMapping("/oabg/gzgl/gzdj")
public class Gzdj_controller extends SimplewebBaseController{
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
	private FileService fileService;
	@Autowired
	private Hyfk_service hyfk_service;
	@Autowired
	private Gzdj_service gzdj_service;
	private final static String MODELPATH="/oabg/gzgl/gzdj/";
	
	*//**
	 * 模块主页面
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 *//*
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView model=new ModelAndView(MODELPATH+"index");
		return model;
	}
*//**
	 * 公章列表
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 *//*
	@ResponseBody
	@RequestMapping("djgrid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return gzdj_service.grid(map);
	}
	
	*//**
	 * 新增/修改公章
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 *//*
	@RequestMapping("djform")
	public ModelAndView form(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"form");
		return mav;
	}
	
	*//**
	 * 保存公章
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 *//*
	@ResponseBody
	@RequestMapping("djsave")
	public Result save(Gzdj gzdj){
		return gzdj_service.saveObj(gzdj);
	}
	
	*//**
	 * 获取单个工公章信息
	 * @param id
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 *//*
	@ResponseBody
	@RequestMapping("djInfo")
	public Map<String,Object> getInfo(Long id){
		if(StringKit.isEmpty(id)){
			return  new HashMap<String,Object>();
		}
		return gzdj_service.getInfo(id);
	}
	*//**
	 * 获取公章使用查询页面
	 * @param id
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 *//*
	@RequestMapping("query")
	public ModelAndView query(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"query");
		return mav;
	}
	
	*//**
	 * 公章列表
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 *//*
	@ResponseBody
	@RequestMapping("querygrid")
	public DataGrid querygrid(@RequestParam Map<String,Object> map){
		return gzdj_service.querygrid(map);
	}
	*//**
	 * 公章删除
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 *//*
	@ResponseBody
	@RequestMapping("deleteByDjIds")
	public Result deleteByIds(String ids){
		return gzdj_service.deleteByIds(ids);
	}
}
*/