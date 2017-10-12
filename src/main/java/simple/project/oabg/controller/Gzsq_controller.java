package simple.project.oabg.controller;

import java.util.Date;
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
import simple.project.oabg.entities.Gzsq;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.service.Gzsq_service;
import simple.project.oabg.service.Hyfk_service;
import simple.project.oabg.service.Hytz_service;
import simple.project.oabg.service.Yhgl_service;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.util.StringKit;

/**
 * 用户管理
 * @author yzw
 * @created 2017年8月21日
 */
@Controller
@RequestMapping("/oabg/gzgl/gzsq")
public class Gzsq_controller extends SimplewebBaseController{
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
	private final static String MODELPATH="/oabg/gzgl/gzsq/";
	
	/**
	 * 模块主页面
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		String name = userService.getCurrentUser().getUsername();
		ModelAndView model=new ModelAndView(MODELPATH+"index");
		model.addObject("name", name);
		return model;
	}
/**
	 * 用户列表
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("sqgrid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return gzsq_service.grid(map);
	}
	
	/**
	 * 新增/修改用户页面
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@RequestMapping("sqform")
	public ModelAndView form(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"form");
		return mav;
	}
	
	/**
	 * 保存会议记录
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("sqsave")
	public Result save(Gzsq gzsq){
		return gzsq_service.saveOBJ(gzsq);
	}
	
	/**
	 * 获取单个会议信息
	 * @param id
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("sqInfo")
	public Map<String,Object> getInfo(Long id){
		if(StringKit.isEmpty(id)){
			return  new HashMap<String,Object>();
		}
		return gzsq_service.getInfo(id);
	}
	
	/**
	 * 删除申请记录
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@ResponseBody
	@RequestMapping("deleteByIds")
	public Result deleteByIds(String ids){
		return gzsq_service.deleteByIds(ids);
	}
	
	@ResponseBody
	@RequestMapping("info")
	public Map<String, Object> getTxl(Long id){
		id = userService.getCurrentUser().getId();
		Txl t = yhgl_service.getDao().getTxlByUser(id);
		Map<String , Object> map = new HashMap<String, Object>();
		if(t!=null){
			map.put("bmdwCode", t.getBmdw()==null?"":t.getBmdw().getCode());
			map.put("bmdwName", t.getBmdw()==null?"":t.getBmdw().getName());
			map.put("sqname", t.getUser().getRealname());
		}
		return map;
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
		mav.addObject("gzsqSqFlowList", gzsq_service.queryFlowById(id));
		return mav;
	}
	/**
	 *时间校验
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@ResponseBody
	@RequestMapping("checkouttime")
	public Result checkouttime(Date gztime){
		return gzsq_service.Time(gztime);
	}
	
}
