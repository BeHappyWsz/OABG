package simple.project.oabg.controller;

import java.util.List;
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
import simple.project.oabg.service.Qjsp_service;
import simple.project.oabg.service.Qjsq_service;
import simple.project.oabg.service.Txl_service;
import simple.system.simpleweb.module.user.model.Role;
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
@RequestMapping("/oabg/kqgl/qjspOne")
public class Qjsp_controller extends SimplewebBaseController{
	protected Logger log = LoggerFactory.getLogger(WebApplicationStarter.class);
	@Autowired
	private Qjsp_service qjsp_service;
	@Autowired
	private UserService userService;
	@Autowired
	private Txl_service txl_service;
	@Autowired
	private Qjsq_service qjsq_service;
	private final static String MODELPATH="/oabg/kqgl/qjspOne/";
	
	/**
	 * 模块主页面
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView model=new ModelAndView(MODELPATH+"index");
		List<Role > role = userService.getCurrentUser().getRoles();
		/*String js = role.get(0).getRoleCode();*/
		String js = "";
		for(Role r :role){
			if(role.get(role.size()-1)!=r){
				js+=r.getRoleCode()+",";
			}else{
				js+=r.getRoleCode();
			}
		}
		model.addObject("js", js);
		return model;
	}
/**
	 * 请假申请列表
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return qjsp_service.grid(map);
	}
	
	/**
	 * 流程记录页面
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@RequestMapping("lcjlFormWin")
	public ModelAndView lcjlFormWin(String id){
		ModelAndView model=new ModelAndView(MODELPATH+"lcjlForm");
		model.addObject("lcjl", qjsp_service.queryFlowById(id));
		return model;
	}
	
	/**
	 * 查看详情
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@RequestMapping("spFirst")
	public ModelAndView spFirst(Long id){
		ModelAndView model=new ModelAndView(MODELPATH+"form");
		model.addObject("sp1", qjsq_service.Getinfo(id));
		model.addObject("spFirstId", id);
		List<Role > role = userService.getCurrentUser().getRoles();
		String js = "";
		for(Role r :role){
			if(role.get(role.size()-1)!=r){
				js+=r.getRoleCode()+",";
			}else{
				js+=r.getRoleCode();
			}
		}
		model.addObject("js", js);
		return model;
	}
	
	/**
	 * 审批意见
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@RequestMapping("qjspF")
	public ModelAndView qjspF(){
		ModelAndView model=new ModelAndView(MODELPATH+"spFormS");
		List<Role > role = userService.getCurrentUser().getRoles();
		String js = "";
		for(Role r :role){
			if(role.get(role.size()-1)!=r){
				js+=r.getRoleCode()+",";
			}else{
				js+=r.getRoleCode();
			}
		}
		model.addObject("js", js);
		return model;
	}
	
	/**
	 * 执行审批
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@ResponseBody
	@RequestMapping("doSp")
	public Result doSp(String id,String sftg,String suggestion){
		return qjsp_service.doSp(id, sftg, suggestion);
	}
}
