package simple.project.oabg.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.service.ZcglSpService;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 审批
 * @author yinchao
 * @date 2017年9月7日
 */
@Controller
@RequestMapping("/oabg/zcsp/")
public class ZcglSpController extends SimplewebBaseController{
	
	private final static String MODELPATH="/oabg/zcgl/zcsp/";
	
	@Autowired
	private ZcglSpService zcglSpService;
	@Autowired
	private YhglDao yhglDao;
	@Autowired
	private UserService userService;
	
	/**
	 * 首页
	 * @return
	 * @author yinchao
	 * @date 2017年9月7日
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView model=new ModelAndView(MODELPATH+"index");
		boolean isnotJz = true;
		boolean isnotCw = true;
		List<Role> roles = userService.getCurrentUser().getRoles();
		for (Role role : roles) {
			if("JZ".equals(role.getRoleCode())){
				isnotJz = false;
			}else if("GHCWC".equals(role.getRoleCode())){
				isnotCw = false;
			}
		}
		model.addObject("zcspisnotjz", isnotJz);
		model.addObject("zcspiscw", isnotCw?"0":"1");
		return model;
	}
	
	/**
	 * 列表显示
	 * @param map
	 * @return
	 * @author yinchao
	 * @date 2017年9月7日
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return zcglSpService.grid(map);
	}
	
	/**
	 * 查看详细 页面
	 * @return
	 * @author yinchao
	 * @date 2017年9月7日
	 */
	@RequestMapping("form")
	public ModelAndView form(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"form");
		boolean isnotJz = true;
		List<Role> roles = userService.getCurrentUser().getRoles();
		for (Role role : roles) {
			if("JZ".equals(role.getRoleCode())){
				isnotJz = false;
			}
		}
		mav.addObject("zcspformisnotjz", isnotJz);
		mav.addObject("zcglSpFormInfo", zcglSpService.getInfo(id));
		return mav;
	}
	
	/**
	 * 显示审批页面
	 * @return
	 * @author yinchao
	 * @date 2017年9月7日
	 */
	@RequestMapping("spForm")
	public ModelAndView spForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.addObject("id", id);
		mav.setViewName(MODELPATH+"spForm");
		return mav;
	}
	
	/**
	 * 查看详细
	 * @param id
	 * @return
	 * @author yinchao
	 * @date 2017年9月7日
	 */
	@ResponseBody
	@RequestMapping("getInfo")
	public Map<String,Object> getInfo(String id){
		return zcglSpService.getInfo(id);
	}
	
	/**
	 * 保存审批结果
	 * @param t
	 * @return
	 * @author yinchao
	 * @date 2017年9月7日
	 */
	@ResponseBody
	@RequestMapping("save")
	public Result save(long id,String tgzt,String suggestion){
		return zcglSpService.doSp(id,tgzt,suggestion);
	}
	

	/**
	 * 流程记录form表单页
	 * 2017年9月4日
	 * yc
	 * @return
	 */
	@RequestMapping("lcjlForm")
	public ModelAndView lcjlForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lcjlForm");
		mav.addObject("zcglSpFlowList", zcglSpService.queryFlowById(id));
		return mav;
	}
}
