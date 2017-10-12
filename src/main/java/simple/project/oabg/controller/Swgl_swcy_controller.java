package simple.project.oabg.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.dic.model.SF;
import simple.project.oabg.entities.Wddy;
import simple.project.oabg.service.Swgl_swng_service;
import simple.project.oabg.service.Swgl_swsh_service;
import simple.project.oabg.service.WddyService;
import simple.system.simpleweb.module.system.file.dao.FileInfoDao;
import simple.system.simpleweb.module.system.file.service.FileService;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.util.StringKit;

/**
 * 收文管理之收文传阅controller
 * @author wsz
 * @created 2017年9月26日
 */
@Controller
@RequestMapping("/oabg/swcy/")
public class Swgl_swcy_controller  extends SimplewebBaseController{
	@Autowired
	private Swgl_swsh_service swgl_swsh_service;
	@Autowired
	private Swgl_swng_service swgl_swng_service;
	@Autowired
	private WddyService wddyService;
	@Autowired
	private FileInfoDao fileInfoDao;
	@Autowired
	private FileService fileService;
	@Autowired
	private UserService userService;
	/**模块公共路径**/
	private final static String MODELPATH="/oabg/swcy/";
	
	
	/**
	 * 模块主页面
	 * @author wsz
	 * @created 2017年9月26日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"index");
		return mav;
	}
	
	/**
	 * 传阅数据列表
	 * 2017年9月26日
	 * wsz
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid cyGrid(@RequestParam Map<String,Object> map){
		return swgl_swsh_service.cyGrid(map);
	}
	
	
	/**
	 * 查看详情页面
	 * @author wsz
	 * @created 2017年9月5日
	 * @param id 
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView form(Long id){
		ModelAndView mav=new ModelAndView(MODELPATH+"form");
		mav.addObject("message", swgl_swng_service.getInfo(id));
		mav.addObject("fjList", swgl_swng_service.getDao().findOne(id).getFileInfo());
		String username = userService.getCurrentUser().getUsername();
		//保存阅读时间
		Wddy wddy = wddyService.getDao().queryByGlid(String.valueOf(id), username, "2");
		if(StringKit.isEmpty(wddy.getYdsj())){
			wddy.setYdsj(new Date());
			SF sf = new SF();
			sf.setCode("1");// 1：已阅读 2：未阅读
			wddy.setState(sf);
			wddyService.save(wddy);
		}
		return mav;
	}
}
