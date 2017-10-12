package simple.project.oabg.controller;

import java.util.Date;
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
import simple.project.oabg.entities.Kqgl;
import simple.project.oabg.service.Qjsq_service;
import simple.project.oabg.service.Txl_service;
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
@RequestMapping("/oabg/kqgl/qjsq")
public class Qjsq_controller extends SimplewebBaseController{
	protected Logger log = LoggerFactory.getLogger(WebApplicationStarter.class);
	@Autowired
	private Qjsq_service qjsq_service;
	@Autowired
	private UserService userService;
	@Autowired
	private Txl_service txl_service;
	private final static String MODELPATH="/oabg/kqgl/qjsq/";
	
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
	 * 请假申请列表
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return qjsq_service.grid(map);
	}
	
	/**
	 * 新增页面
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView add(){
		ModelAndView model=new ModelAndView(MODELPATH+"form");
		return model;
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
		model.addObject("lcjl", qjsq_service.queryFlowById(id));
		return model;
	}
	/**
	 * 自动回填姓名，部门，联系方式
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@ResponseBody
	@RequestMapping("info")
	public Map<String, Object> info(){
		return qjsq_service.info();
	}
	
	/**
	 * 新增页面
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@ResponseBody
	@RequestMapping("save")
	public Result save(Kqgl kqgl){
		return qjsq_service.saveObj(kqgl);
	}
	
	/**
	 * 回填信息
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getInfo")
	public Map<String, Object> GetInfo(Long id){
		return qjsq_service.Getinfo(id);
	}
	
	/**
	 * 删除申请记录
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@ResponseBody
	@RequestMapping("deleteByIds")
	public Result deleteByIds(String ids){
		return qjsq_service.deleteByIds(ids);
	}
	
	/**
	 * 校验请假起始时间
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@ResponseBody
	@RequestMapping("checkoutStrtime")
	public Result checkoutStrtime(Date timeStr,Date timeEnd ){
		return qjsq_service.TimeStr(timeStr, timeEnd);
	}
	
	/**
	 * 校验请假结束时间
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@ResponseBody
	@RequestMapping("checkoutendtime")
	public Result checkoutendtime(Date timeStr,Date timeEnd ){
		return qjsq_service.TimeEnd(timeStr, timeEnd);
	}
}
