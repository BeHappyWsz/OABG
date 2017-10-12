package simple.project.oabg.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.WebApplicationStarter;
import simple.project.communal.util.ModelExport;
import simple.project.oabg.entities.Xxbs;
import simple.project.oabg.service.XxbsService;
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
@RequestMapping("/oabg/xxbs")
public class Xxbscontroller extends SimplewebBaseController{
	protected Logger log = LoggerFactory.getLogger(WebApplicationStarter.class);
	@Autowired
	private final static String MODELPATH="/oabg/xxbs/";
	@Autowired
	private UserService userService;
	@Autowired
	private XxbsService xxbsService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 模块主页面
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView model=new ModelAndView(MODELPATH+"index");
		return model;
	}
	/**
	 * 数据列表
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String , Object> map){
		return xxbsService.grid(map);
	}
	
	/**
	 * 新增修改页面
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView form(){
		ModelAndView model=new ModelAndView(MODELPATH+"form");
		model.addObject("date", sdf.format(new Date()));
		return model;
	}
	
	/**
	 * 根据ID查找单个信息
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@ResponseBody
	@RequestMapping("Info")
	public Map<String, Object> Info(Long id){
		return xxbsService.getInfo(id);
	}
	
	/**
	 * 根据ID查找单个信息
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@ResponseBody
	@RequestMapping("save")
	public Result SaveObj(Xxbs xxbs){
		return xxbsService.saveObj(xxbs);
	}
	
	/**
	 * 流程记录form表单页
	 * 2017年9月4日
	 * yc
	 * @return
	 */
	@RequestMapping("xxbslcForm")
	public ModelAndView lcjlForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lcjlForm");
		mav.addObject("xxbsSqFlowList", xxbsService.queryFlowById(id));
		return mav;
	}
	
	/**
	 * 流程记录form表单页
	 * 2017年9月4日
	 * yc
	 * @return
	 */
	@ResponseBody
	@RequestMapping("deleteByIds")
	public Result deletebyID(String ids){
		return xxbsService.deleteByIds(ids);
	}
	/**
	 * 报表页面
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@RequestMapping("xxbsbb")
	public ModelAndView xxbsbb(){
		ModelAndView model=new ModelAndView(MODELPATH+"xxbsbb/index");
		return model;
	}
	/**
	 * 加载报表
	 * @return
	 * @author yzw
	 * @date 2017年8月18日
	 */
	@RequestMapping(value = "baobiao")
	public ModelAndView baoBiao(@RequestParam Map<String,Object> tmap) {
		ModelAndView model = new ModelAndView();
		Map<String,Object> jo=xxbsService.queryReport(tmap);
		model.addObject("table_xxbsbb",jo);
		model.setViewName(MODELPATH+"xxbsbb/ccbb/xxbsTable");
		return model;
	}
	
	/**
	 * Excel导出
	 * @author yzw
	 * @date 2017年8月18日
	 * @param map
	 * @param response
	 */
	@RequestMapping("export")
    public void export(@RequestParam Map<String,Object> tmap,HttpServletResponse response){
		Map<String,Object> jo=xxbsService.exportReport(tmap);
    	ModelExport.exportWithModelAndMergeCells("model/xxbs/xxbs.xls","信息报送统计表",jo,response,0,1,0,3,4,5);
    }
	
	/**
	 * 打印
	 * @author yzw
	 * @date 2017年8月18日
	 * @param map
	 * @param response
	 */
	@RequestMapping("print")
	public ModelAndView print(@RequestParam Map<String,Object> tmap) {
		ModelAndView model = new ModelAndView();
		Map<String,Object> jo=xxbsService.queryPrint(tmap);
		model.addObject("table_xxbsbb",jo);
		model.setViewName(MODELPATH+"print1");
		return model;
	}
	
	/**
	 * 回填信息
	 * @return
	 * @author yzw
	 * @date 2017年8月18日
	 */
	@ResponseBody
	@RequestMapping(value = "getinfo")
	public Map<String, Object> getinfo(){
		return xxbsService.getinfo();
	}
	
}
