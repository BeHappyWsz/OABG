package simple.project.oabg.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.communal.util.Utils;
import simple.project.oabg.entities.SbwxSqjl;
import simple.project.oabg.service.SbwxSqService;
import simple.project.oabg.service.Yhgl_service;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 设备维修申请
 * @author wsz
 * @created 2017年9月20日
 */
@Controller
@RequestMapping("/oabg/sbwx/sbwxsq")
public class SbwxSqController extends SimplewebBaseController{

	private final static String MODELPATH="/oabg/sbwx/sbwxsq/";
	
	@Autowired
	private UserService userService;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private SbwxSqService sbwxSqService;
	/**
	 * 模块主页面
	 * @author wsz
	 * @created 2017年9月20日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView model=new ModelAndView();
		model.setViewName(MODELPATH+"index");
		return model;
	}
	
	/**
	 * 获取所有自己的申请记录列表
	 * @author wsz
	 * @created 2017年9月20日
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return sbwxSqService.grid(map);
	}
	
	/**
	 * 新增页面
	 * @author wsz
	 * @created 2017年9月20日
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView form(){
		ModelAndView model=new ModelAndView();
		model.setViewName(MODELPATH+"form");
		User currentUser = userService.getCurrentUser();
		model.addObject("jsr", currentUser.getRealname());
		model.addObject("bxbm", yhgl_service.getDao().getTxlByUserId(currentUser.getId()).getBmdw().getCode());
		String role = "";
		List<Role> roles = currentUser.getRoles();
		for(Role r : roles){
			if("FGLD".equals(r.getRoleCode())){
				role ="1";
			}else if("CZ".equals(r.getRoleCode())){
				role = "2";
			}else if("GWY".equals(r.getRoleCode())){
				role ="3";
			}
		}
		if("1".equals(role)){
			
		}else if("2".equals(role)){
			model.addObject("bmfzr", yhgl_service.getFgld().getRealname());
		}else if("3".equals(role)){
			model.addObject("bmfzr", yhgl_service.getCz().getRealname());
		}
		return model;
	}
	
	/**
	 * 保存设备维修申请记录
	 * @author wsz
	 * @created 2017年9月20日
	 * @param ss
	 * @return
	 */
	@ResponseBody
	@RequestMapping("saveSqjl")
	public Result saveSqjl(SbwxSqjl ss,String bxbm){
		return sbwxSqService.saveSqjl(ss,bxbm);
	}
	
	/**
	 * 获取某条申请记录的信息内容
	 * @author wsz
	 * @created 2017年9月20日
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getInfo")
	public Map<String,Object> getInfo(Long id){
		return sbwxSqService.getInfo(id);
	}
	
	/**
	 * 删除未通过审批的申请记录
	 * @author wsz
	 * @created 2017年9月20日
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("deleteByIds")
	public Result deleteByIds(String ids){
		List<Long> list = Utils.stringToLongList(ids, ",");
		boolean isDel = sbwxSqService.logicDelete(list);
		if(isDel){
			return new Result(true,"删除成功");
		}else{
			return new Result(false,"删除失败");
		}
	}
	
	/**
	 * 流程记录form表单页
	 * 2017年9月20日
	 * wsz
	 * @param 该条申请记录的id
	 * @return
	 */
	@RequestMapping("lcjlForm")
	public ModelAndView lcjlForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lcjlForm");
		mav.addObject("swngFlowList", sbwxSqService.queryFlowById(id));
		return mav;
	}
}
