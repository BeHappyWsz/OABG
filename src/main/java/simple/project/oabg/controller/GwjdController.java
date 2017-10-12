package simple.project.oabg.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.entities.Gwjdsq;
import simple.project.oabg.service.GwjdsqService;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 公务接待申请
 * 2017年9月20日
 * @author yc
 */
@Controller
@RequestMapping("/oabg/gwjd/")
public class GwjdController extends SimplewebBaseController{
	
	private final static String MODELPATH="/oabg/gwjd/";
	
	@Autowired
	private GwjdsqService gwjdsqService;
	@Autowired
	private UserService userService;
	
	/**
	 * 申请主页
	 * 2017年9月20日
	 * yc
	 * @return
	 */
	@RequestMapping("gwjdsq/index")
	public ModelAndView gwjdsqindex(){
		ModelAndView model=new ModelAndView(MODELPATH+"gwjdsq/index");
		return model;
	}

	/**
	 * 申请form表单页
	 * 2017年9月20日
	 * yc
	 * @return
	 */
	@RequestMapping("gwjdsq/form")
	public ModelAndView gwjdsqform(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"gwjdsq/form");
		return mav;
	}
	
	/**
	 * 审核主页
	 * 2017年9月20日
	 * yc
	 * @return
	 */
	@RequestMapping("gwjdsh/index")
	public ModelAndView gwjdshindex(){
		ModelAndView model=new ModelAndView(MODELPATH+"gwjdsh/index");
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
		model.addObject("gwjdindexshowczbtn", isnotCw && isnotBgsfzr);
		return model;
	}

	/**
	 * 审核form表单页
	 * 2017年9月20日
	 * yc
	 * @return
	 */
	@RequestMapping("gwjdsh/form")
	public ModelAndView gwjdshform(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"gwjdsh/form");
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
		mav.addObject("gwjdformshowczbtn", isnotCw && isnotBgsfzr);
		mav.addObject("gwjdshInfo", gwjdsqService.getInfo(id));
		return mav;
	}
	
	/**
	 * 审核form表单页
	 * 2017年9月20日
	 * yc
	 * @return
	 */
	@RequestMapping("gwjdsh/shForm")
	public ModelAndView gwjdshshForm(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"gwjdsh/shForm");
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
		mav.addObject("gwjdshformshowczbtn", isnotCw && isnotBgsfzr);
		return mav;
	}
	
	/**
	 * 审批主页
	 * 2017年9月20日
	 * yc
	 * @return
	 */
	@RequestMapping("gwjdsp/index")
	public ModelAndView gwjdspindex(){
		ModelAndView model=new ModelAndView(MODELPATH+"gwjdsp/index");
		return model;
	}

	/**
	 * 审批form表单页
	 * 2017年9月20日
	 * yc
	 * @return
	 */
	@RequestMapping("gwjdsp/form")
	public ModelAndView gwjdspform(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"gwjdsp/form");
		mav.addObject("gwjdspInfo", gwjdsqService.getInfo(id));
		return mav;
	}
	
	/**
	 * 审核form表单页
	 * 2017年9月20日
	 * yc
	 * @return
	 */
	@RequestMapping("gwjdsp/spForm")
	public ModelAndView gwjdspspForm(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"gwjdsp/spForm");
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
		return gwjdsqService.grid(map);
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
	public Result save(Gwjdsq t){
		return gwjdsqService.saveObj(t);
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
		return gwjdsqService.getInfo(id);
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
		return gwjdsqService.deleteByIds(ids);
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
			return gwjdsqService.doCzSh(id, sftg, suggestion);
		}else if("bgs".equals(stype)){
			return gwjdsqService.doBgsSh(id, sftg, suggestion);
		}else if("cw".equals(stype)){
			return gwjdsqService.doCwSh(id, sftg, suggestion);
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
		return gwjdsqService.doSp(id, sftg, suggestion);
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
		mav.setViewName(MODELPATH+"gwjdsq/lcjlForm");
		mav.addObject("gwjdFlowList", gwjdsqService.queryFlowById(id));
		return mav;
	}
}
