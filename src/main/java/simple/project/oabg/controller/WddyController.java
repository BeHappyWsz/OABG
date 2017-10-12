package simple.project.oabg.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.base.utils.StringSimple;
import simple.project.oabg.service.Dagl_gddj_service;
import simple.project.oabg.service.FwngService;
import simple.project.oabg.service.WddyService;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 我的待阅
 * @author sxm
 * @created 2017年9月4日
 */
@Controller
@RequestMapping("/oabg/wddb/dy/")
public class WddyController extends SimplewebBaseController{
	@Autowired
	private WddyService wddyService;
	@Autowired
	private Dagl_gddj_service dagl_gddj_service;
	@Autowired
	private UserService userService;
	@Autowired
	private FwngService fwngService;
	
	private final static String MODELPATH="/oabg/wddb/dy/";
	
	/**
	 * 返回主页
	 * @author sxm
	 * @created 2017年9月4日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView model=new ModelAndView(MODELPATH+"index");
		return model;
	}
	
	
	/**
	 * 返回grid列表
	 * @author sxm
	 * @created 2017年9月4日
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		DataGrid dg = wddyService.grid(map);
		return dg;
	}
	
	/**
	 * 返回form表单
	 * @author sxm
	 * @created 2017年9月4日
	 * @return
	 */
	@RequestMapping("form1")
	public ModelAndView form1(String glid,String sjly){
		ModelAndView model=new ModelAndView();
		sjly=StringSimple.nullToEmpty(sjly);
		if(!"".equals(sjly)){
			if("1".equals(sjly)){
				model=new ModelAndView(MODELPATH+"form");
				model.addObject("fjs", wddyService.getFjs(glid));
				model.addObject("dzqzs", wddyService.getDzqzs(glid));
				model.addObject("fwsh_info", fwngService.getInfo(glid));
			}
		}
		return model;
	}
	
	/**
	 * 重定向form表单
	 * @author sxm
	 * @created 2017年9月4日
	 * @return
	 */
	@RequestMapping("form")
	public String form(String id,String sjly,String glid){
		String path = "";
		if("1".equals(sjly)){
			path = "/oabg/wddb/dy/form1?glid="+glid+"&sjly="+sjly;
		}else if("2".equals(sjly)){
			path = "/oabg/swng/lookForm?id="+glid;
		}else if("3".equals(sjly)){
			path = "/oabg/hytz/Win?id="+glid;
		}
		return "redirect:"+path;
	}
	
	
	/**
	 * 获取详情
	 * @author sxm
	 * @created 2017年9月4日
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getInfo1")
	public Map<String,Object> getInfo(String id,String sjly){
		Map<String,Object> map=new HashMap<String, Object>();
		map= wddyService.getInfo(id);
		return map;
	}
	
	/**
	 * 重定向getinfo
	 * @author sxm
	 * @created 2017年9月12日
	 * @param id
	 * @param sjly
	 * @param glid
	 * @return
	 */
	@RequestMapping("getInfo")
	public String getInfo(String id,String sjly,String glid){
		String path = "";
		if("1".equals(sjly)){
			path = "/oabg/wddb/dy/getInfo1?id="+id+"&sjly="+sjly;
		}else if("2".equals(sjly)){
			path = "/oabg/swng/getInfo?id="+glid;
		}else if("3".equals(sjly)){
			path = "/oabg/hytz/getInfo?id="+glid;
		}
		return "redirect:"+path;
	}
	
	/**
	 * 阅读完成
	 * @author sxm
	 * @created 2017年9月4日
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "close")
	public Result close(String id){
		wddyService.close(id);
		return new Result(true);
	}
	
	/**
	 * 保存我的待阅
	 * @author sxm
	 * @created 2017年9月4日
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public Result save(String sjly,String glid,String sjr){
		wddyService.save(sjly,glid,sjr);
		//自动归档
		dagl_gddj_service.gddj(Long.valueOf(glid), "2", userService.getCurrentUser().getUsername(), fwngService.getDao().findOne(Long.valueOf(glid)).getBt());
		
		return new Result(true);
	}
}
