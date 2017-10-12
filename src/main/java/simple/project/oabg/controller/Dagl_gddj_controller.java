package simple.project.oabg.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.communal.util.ModelExport;
import simple.project.communal.util.Utils;
import simple.project.oabg.entities.Gddj;
import simple.project.oabg.service.Dagl_gddj_service;
import simple.system.simpleweb.module.system.file.service.FileService;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 档案管理之归档登记controller
 * @author wsz
 * @created 2017年9月4日
 */
@Controller
@RequestMapping("/oabg/gddj/")
public class Dagl_gddj_controller extends SimplewebBaseController{

	@Autowired
	private Dagl_gddj_service dagl_gddj_service;
	@Autowired
	private FileService fileService;
	@Autowired
	private UserService userService;
	/**模块公共路径**/
	private final static String MODELPATH="/oabg/dagl/gddj/";
	
	/**
	 * 返回主页面
	 * @author wsz
	 * @created 2017年9月4日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView model=new ModelAndView();
		model.setViewName(MODELPATH+"index");
		return model;
	}
	
	/**
	 * 返回grid列表
	 * @author wsz
	 * @created 2017年9月4日
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return dagl_gddj_service.grid(map);
	}
	
	/**
	 * 返回新增页面
	 * @author wsz
	 * @created 2017年9月4日
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView form(){
		User user = userService.getCurrentUser();
		ModelAndView model=new ModelAndView();
		model.addObject("username", user.getUsername());
		model.addObject("cdrid", user.getId());
		model.setViewName(MODELPATH+"form");
		return model;
	}
	
	/**
	 * 保存form表单信息
	 * @author wsz
	 * @created 2017年9月4日
	 * @param gddj
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public Result save(Gddj gddj){
		try{
			dagl_gddj_service.save(gddj);
			return new Result(true);
		}catch(Exception e){
			return new Result(false,"保存失败");
		}
	}
	
	/**
	 * 获取详情
	 * @author wsz
	 * @created 2017年9月4日
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getInfo")
	public Map<String,Object> getInfo(Long id){
		return dagl_gddj_service.getInfo(id);
	}
	
	/**
	 * 删除
	 * @author wsz
	 * @created 2017年9月4日
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteByIds")
	public Result delete(String ids){
		List<Long> idList=Utils.stringToLongList(ids, ",");
		boolean result=dagl_gddj_service.logicDelete(idList);
		return new Result(result);
	}
	
	/**
	 * 导出收文登记表
	 * @author wsz
	 * @param response
	 * @created 2017年9月4日
	 */
	@RequestMapping("export")
	public void export(HttpServletResponse response){
		Map<String,Object> data = new HashMap<String, Object>();
		String title ="收文登记表";
		data.put("list", dagl_gddj_service.getSwList());
		data.put("zbsj", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		data.put("zbr", userService.getCurrentUser().getRealname());
		ModelExport.exportWithModel("model/swcx/swdjb.xls", title, data, response);
	}
}
