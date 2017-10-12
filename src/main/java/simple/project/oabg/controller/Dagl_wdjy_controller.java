package simple.project.oabg.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.communal.util.Utils;
import simple.project.oabg.dao.WdjySqFlowDao;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.entities.WdjySqFlow;
import simple.project.oabg.entities.WdjySqjl;
import simple.project.oabg.service.Dagl_gddj_service;
import simple.project.oabg.service.Dagl_wdjySqjl_service;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 文档管理之文档借阅
 * 2017年9月5日
 * @author wsz
 */
@Controller
@RequestMapping("/oabg/wdjy")
public class Dagl_wdjy_controller extends SimplewebBaseController{

	private final static String MODELPATH="/oabg/dagl/wdjy/";
	
	@Autowired
	private YhglDao yhglDao;
	@Autowired
	private UserService userService;
	@Autowired
	private Dagl_gddj_service dagl_gddj_service;
	@Autowired
	private Dagl_wdjySqjl_service dagl_wdjySqjl_service;
	@Autowired
	private WdjySqFlowDao wdjySqFlowDao;
	/**
	 * 主页
	 * 2017年9月4日
	 * wsz
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView model=new ModelAndView();
		model.setViewName(MODELPATH+"index");
		return model;
	}
	
	/**
	 * 借阅申请form表单页
	 * 2017年9月8日
	 * wsz
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView form(){
		User user = userService.getCurrentUser();
		Txl txl = yhglDao.getTxlByUser(user.getId());
		ModelAndView model=new ModelAndView(MODELPATH+"form");
		model.addObject("sqr", user.getRealname());
		model.addObject("sqrUserName", user.getUsername());
		model.addObject("sqbmName", txl.getBmdw() == null ? "" : txl.getBmdw().getName());
		model.addObject("sqbmCode", txl.getBmdw() == null ? "" : txl.getBmdw().getCode());
		model.addObject("lxdh", txl.getMobile() == null ? "" : txl.getMobile());
		return model;
	}
	
	/**
	 * 申请借阅的数据列表
	 * 2017年9月8日
	 * wsz
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return dagl_wdjySqjl_service.grid(map);
	}
	
	/**
	 * 保存文档借阅申请
	 * 2017年9月8日
	 * wsz
	 * @param t
	 * @return
	 */
	@ResponseBody
	@RequestMapping("save")
	public Result save(WdjySqjl w){
		return dagl_wdjySqjl_service.saveJySqjl(w);
	}
	
	/**
	 * 根据id获取借阅信息详情
	 * 2017年9月8日
	 * wsz
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getInfo")
	public Map<String,Object> getInfo(Long id){
		return dagl_wdjySqjl_service.getInfo(id);
	}
	
	/**
	 * 删除借阅信息
	 * 2017年9月8日
	 * wsz
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public Result delete(String ids){
		List<Long> list = Utils.stringToLongList(ids, ",");
		boolean del = dagl_wdjySqjl_service.logicDelete(list);
		if(del){
			return new Result(true,"删除成功");
		}else{
			return new Result(true,"删除失败");
		}
	}
	
	/**
	 * 流程记录form表单页
	 * 2017年9月8日
	 * wsz
	 * @return
	 */
	@RequestMapping("lcjlForm")
	public ModelAndView lcjlForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lcjlForm");
		List<WdjySqFlow> wdjySqFlowList = wdjySqFlowDao.queryByGlid(id);
		mav.addObject("wdjySqFlowList", wdjySqFlowList);
		return mav;
	}
	
	/**
	 * 所有文档form
	 * 2017年9月8日
	 * wsz
	 * @return
	 */
	@RequestMapping("wdForm")
	public ModelAndView wdForm(){
		return new ModelAndView(MODELPATH+"wdForm");
	}
	
	/**
	 * 所有文档信息列表
	 * 2017年9月8日
	 * wsz
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("wdGrid")
	public DataGrid wdGrid(@RequestParam Map<String,Object> map){
		return dagl_gddj_service.grid(map);
	}
	
	/**
	 * 借阅起始时间
	 * @param 
	 * @return
	 * @author wsz
	 * @created 2017年9月13日
	 */
	@ResponseBody
	@RequestMapping("checkoutStrtime")
	public Result checkoutStrtime(Date timeStr,Date timeEnd ){
		return dagl_gddj_service.TimeStr(timeStr, timeEnd);
	}
	
	/**
	 * 借阅结束时间
	 * @param 
	 * @return
	 * @author wsz
	 * @created 2017年9月13日
	 */
	@ResponseBody
	@RequestMapping("checkoutendtime")
	public Result checkoutendtime(Date timeStr,Date timeEnd ){
		return dagl_gddj_service.TimeEnd(timeStr, timeEnd);
	}
}
