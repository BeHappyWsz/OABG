package simple.project.oabg.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.entities.Hypxsq;
import simple.project.oabg.service.HypxsqService;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 会议培训申请
 * 2017年9月20日
 * @author yc
 */
@Controller
@RequestMapping("/oabg/hypx/")
public class HypxController extends SimplewebBaseController{
	
	private final static String MODELPATH="/oabg/hypx/";
	
	@Autowired
	private HypxsqService hypxsqService;
	@Autowired
	private UserService userService;
	
	/**
	 * 申请主页
	 * 2017年9月20日
	 * yc
	 * @return
	 */
	@RequestMapping("hypxsq/index")
	public ModelAndView hypxsqindex(){
		ModelAndView model=new ModelAndView(MODELPATH+"hypxsq/index");
		return model;
	}

	/**
	 * 申请form表单页
	 * 2017年9月20日
	 * yc
	 * @return
	 */
	@RequestMapping("hypxsq/form")
	public ModelAndView hypxsqform(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"hypxsq/form");
		return mav;
	}
	
	/**
	 * 审核主页
	 * 2017年9月20日
	 * yc
	 * @return
	 */
	@RequestMapping("hypxsh/index")
	public ModelAndView hypxshindex(){
		ModelAndView model=new ModelAndView(MODELPATH+"hypxsh/index");
		boolean isnotCw = true;
		boolean isnotBgsfzr = true;
		List<Role> roles = userService.getCurrentUser().getRoles();
		for (Role role : roles) {
			if("GHCWC".equals(role.getRoleCode())){
				isnotCw = false;
			}
			if("BGSFZR".equals(role.getRoleCode())){
				isnotBgsfzr = false;
			}
		}
		model.addObject("hypxindexshowczbtn", isnotCw && isnotBgsfzr);
		return model;
	}

	/**
	 * 审核form表单页
	 * 2017年9月20日
	 * yc
	 * @return
	 */
	@RequestMapping("hypxsh/form")
	public ModelAndView hypxshform(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"hypxsh/form");
		boolean isnotCw = true;
		boolean isnotBgsfzr = true;
		List<Role> roles = userService.getCurrentUser().getRoles();
		for (Role role : roles) {
			if("GHCWC".equals(role.getRoleCode())){
				isnotCw = false;
			}
			if("BGSFZR".equals(role.getRoleCode())){
				isnotBgsfzr = false;
			}
		}
		mav.addObject("hypxformshowczbtn", isnotCw && isnotBgsfzr);
		mav.addObject("hypxshInfo", hypxsqService.getInfo(id));
		return mav;
	}
	
	/**
	 * 审核form表单页
	 * 2017年9月20日
	 * yc
	 * @return
	 */
	@RequestMapping("hypxsh/shForm")
	public ModelAndView hypxshshForm(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"hypxsh/shForm");
		boolean isnotCw = true;
		boolean isnotBgsfzr = true;
		List<Role> roles = userService.getCurrentUser().getRoles();
		for (Role role : roles) {
			if("GHCWC".equals(role.getRoleCode())){
				isnotCw = false;
			}
			if("BGSFZR".equals(role.getRoleCode())){
				isnotBgsfzr = false;
			}
		}
		mav.addObject("hypxshformshowczbtn", isnotCw && isnotBgsfzr);
		return mav;
	}
	
	/**
	 * 审批主页
	 * 2017年9月20日
	 * yc
	 * @return
	 */
	@RequestMapping("hypxsp/index")
	public ModelAndView hypxspindex(){
		ModelAndView model=new ModelAndView(MODELPATH+"hypxsp/index");
		return model;
	}

	/**
	 * 审批form表单页
	 * 2017年9月20日
	 * yc
	 * @return
	 */
	@RequestMapping("hypxsp/form")
	public ModelAndView hypxspform(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"hypxsp/form");
		mav.addObject("hypxspInfo", hypxsqService.getInfo(id));
		return mav;
	}
	
	/**
	 * 审核form表单页
	 * 2017年9月20日
	 * yc
	 * @return
	 */
	@RequestMapping("hypxsp/spForm")
	public ModelAndView hypxspspForm(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"hypxsp/spForm");
		return mav;
	}

	/**
	 * 数据列表
	 * 2017年9月20日
	 * yc
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return hypxsqService.grid(map);
	}
	
	/**
	 * 保存
	 * 2017年9月20日
	 * yc
	 * @param t
	 * @return
	 */
	@ResponseBody
	@RequestMapping("save")
	public Result save(Hypxsq t){
		return hypxsqService.saveObj(t);
	}
	
	/**
	 * 根据id获取详情
	 * 2017年9月20日
	 * yc
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getInfo")
	public Map<String,Object> getInfo(String id){
		return hypxsqService.getInfo(id);
	}
	
	/**
	 * 删除
	 * 2017年9月20日
	 * yc
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public Result delete(String ids){
		return hypxsqService.deleteByIds(ids);
	}
	
	/**
	 * 执行审核
	 * 2017年9月20日
	 * yc
	 * @param id
	 * @param sftg
	 * @param suggestion
	 * @param stype
	 * @return
	 */
	@ResponseBody
	@RequestMapping("doSh")
	public Result doSh(String id,String sftg,String suggestion,String stype){
		if("cz".equals(stype)){
			return hypxsqService.doCzSh(id, sftg, suggestion);
		}else if("bgs".equals(stype)){
			return hypxsqService.doBgsSh(id, sftg, suggestion);
		}else if("cw".equals(stype)){
			return hypxsqService.doCwSh(id, sftg, suggestion);
		}
		return new Result(false);
	}
	
	/**
	 * 执行审批
	 * 2017年9月20日
	 * yc
	 * @param id
	 * @param sftg
	 * @param suggestion
	 * @param stype
	 * @return
	 */
	@ResponseBody
	@RequestMapping("doSp")
	public Result doSp(String id,String sftg,String suggestion){
		return hypxsqService.doSp(id, sftg, suggestion);
	}
	
	/**
	 * 流程记录form表单页
	 * 2017年9月20日
	 * yc
	 * @return
	 */
	@RequestMapping("lcjlForm")
	public ModelAndView lcjlForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"hypxsq/lcjlForm");
		mav.addObject("hypxFlowList", hypxsqService.queryFlowById(id));
		return mav;
	}
}
