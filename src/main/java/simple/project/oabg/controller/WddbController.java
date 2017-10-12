package simple.project.oabg.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.service.WddbService;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 劳务用品登记
 * 2017年9月4日
 * @author yc
 */
@Controller
@RequestMapping("/oabg/wddb/db")
public class WddbController extends SimplewebBaseController{
	
	private final static String MODELPATH="/oabg/wddb/db/";
	
	@Autowired
	private WddbService wddbService;
	@Autowired
	private UserService userService;

	
	/**
	 * 主页
	 * 2017年9月4日
	 * yc
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		User user=userService.getCurrentUser();
		ModelAndView model=new ModelAndView(MODELPATH+"index");
		List<Role> roles=user.getRoles();
		String roleCode="";
		for(Role role:roles){
			roleCode=role.getRoleCode();
			break;
		}
		model.addObject("roleCode", roleCode);
		return model;
	}

	/**
	 * form表单页
	 * 2017年9月4日
	 * yc
	 * @return
	 */
	@RequestMapping("form")
	public String form(String sjly,String id,String glid,String lcztcode){
		String path = "";
		if("10".equals(sjly)){
			if("1".equals(lcztcode)||"2".equals(lcztcode))
				path = "/oabg/swsh/form?id="+glid;
			else
				path = "/oabg/swng/form?id="+glid;
		}else if("20".equals(sjly)){
			path = "/oabg/kqgl/qjspOne/spFirst?id="+glid;
		}else if("30".equals(sjly)){
			path = "/oabg/kqgl/ccspOne/spFirst?id="+glid;
		}else if("40".equals(sjly)){
			path = "/oabg/zcsp/form?id="+glid;
		}else if("50".equals(sjly)){
			path = "/oabg/bgypsp/form?id="+glid;
		}else if("60".equals(sjly)){
			path = "/oabg/lwypsp/form?id="+glid;
		}else if("70".equals(sjly)){
			path = "/oabg/gzgl/gzsh/shform?id="+glid;
		}else if("80".equals(sjly)){
			path = "/oabg/jysh/form?id="+glid;
		}else if("90".equals(sjly)){
			if("6".equals(lcztcode) || "7".equals(lcztcode) || "8".equals(lcztcode) || "11".equals(lcztcode)|| "10".equals(lcztcode)){
				path="/oabg/fwsh/form1?id="+glid;
			}else{
				path="/oabg/fwsh/form?id="+glid;	
			}
			
		}else if("100".equals(sjly)){
			path="/oabg/tsswhf/form?id="+glid;
		}else if("110".equals(sjly)){
			if("4".equals(lcztcode)||"5".equals(lcztcode)){
				path="/oabg/gwjd/gwjdsp/form?id="+glid;
			}else{
				path="/oabg/gwjd/gwjdsh/form?id="+glid;
			}
		}else if("120".equals(sjly)){
			if("4".equals(lcztcode)||"5".equals(lcztcode)){
				path="/oabg/hypx/hypxsp/form?id="+glid;
			}else{
				path="/oabg/hypx/hypxsh/form?id="+glid;
			}
		}else if("130".equals(sjly)){
			path="/oabg/sbwx/sbwxsp/form?id="+glid;
		}else if("140".equals(sjly)){
			path="/oabg/xxbsly/form?id="+glid;
		}
		return "redirect:"+path;
	}
	
	/**
	 * 数据列表
	 * 2017年9月8日
	 * yc
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return wddbService.grid(map);
	}
	
	/**
	 * 根据id获取详情
	 * 2017年9月4日
	 * yc
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getInfo")
	public Map<String,Object> getInfo(String id){
		return wddbService.getInfo(id);
	}
	
	/**
	 * 如果已办理，改变办理状态
	 * 2017年9月9日
	 * yc
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("changeIfHasBl")
	public Result changeIfHasBl(Long id){
		return wddbService.changeIfHasBl(wddbService.getDao().findOne(id));
	}
}
