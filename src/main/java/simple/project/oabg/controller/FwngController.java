package simple.project.oabg.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.communal.util.Utils;
import simple.project.oabg.entities.Fwng;
import simple.project.oabg.entities.FwngFlow;
import simple.project.oabg.service.FwngService;
import simple.project.oabg.service.Yhgl_service;
import simple.system.simpleweb.module.system.file.service.FileService;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 发文拟稿controller
 * @author sxm
 * @created 2017年8月28日
 */
@Controller
@RequestMapping("/oabg/fwng/")
public class FwngController extends SimplewebBaseController{
	
	@Autowired
	private FwngService fwngService;
	@Autowired
	private FileService fileService;
	@Autowired
	private UserService userService;
	@Autowired
	private Yhgl_service yhgl_service;
	/**模块公共路径**/
	private final static String MODELPATH="/oabg/fwng/";
	
	/**
	 * 返回主页面
	 * @author sxm
	 * @created 2017年8月28日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView model=new ModelAndView(MODELPATH+"index");
		return model;
	}
	
	/**
	 * 返回新增页面
	 * @author sxm
	 * @created 2017年8月29日
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView form(){
		//打开新增页面，往页面添加拟稿人和部门，待添加
		User user=userService.getCurrentUser();
		ModelAndView model=new ModelAndView(MODELPATH+"form");
		model.addObject("uname", user.getUsername());
		model.addObject("uName", user.getRealname());
		model.addObject("uid", user.getId());
		return model;
	}
	
	/**
	 * 返回grid列表
	 * @author sxm
	 * @created 2017年8月29日
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		DataGrid dg = fwngService.grid(map);
		return dg;
	}
	
	/**
	 * 保存form表单信息
	 * @author sxm
	 * @created 2017年8月30日
	 * @param fwng
	 * @param zsdws
	 * @param csdws
	 * @param fjs
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public Result save(Fwng fwng,String zsdws,String csdws,String fjs){
		fwngService.save(fwng, zsdws, csdws,fjs);
		return new Result(true);
	}
	
	/**
	 * 获取详情
	 * @author sxm
	 * @created 2017年8月30日
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getInfo")
	public Map<String,Object> getInfo(String id){
		return fwngService.getInfo(id);
	}
	
	/**
	 * 删除
	 * @author sxm
	 * @created 2017年8月30日
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteByIds")
	public Result delete(String ids){
		List<Long> idList=Utils.stringToLongList(ids, ",");
		fwngService.logicDelete(idList);
		return new Result(true);
	}
	
	/**
	 * 联系人页面
	 * @return
	 * @author wsz
	 * @created 2017年9月5日
	 */
	@RequestMapping("lxr")
	public ModelAndView lxr(String flag){
		ModelAndView model=new ModelAndView(MODELPATH+"lxr");
		model.addObject("flag", flag);
		return model;
	}
	/**
	 * 联系人列表
	 * @param map
	 * @return
	 * @author wsz
	 * @created 2017年9月5日
	 */
	@ResponseBody
	@RequestMapping("lxrgrid")
	public DataGrid lxrgrid(@RequestParam Map<String,Object> map){
		map.put("bmdw", "101");
		return yhgl_service.grid(map);
	}
	
	/**
	 * 部门联系人页面
	 * @return
	 * @author wsz
	 * @created 2017年9月19日
	 */
	@RequestMapping("bm")
	public ModelAndView bm(String flag){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"bm");
		mav.addObject("flag", flag);
		return mav;
	}
	
	/**
	 * 部门单位用户列表
	 * @param map
	 * @return
	 * @created 2017年9月19日
	 */
	@ResponseBody
	@RequestMapping("bmgrid")
	public DataGrid bmgrid(@RequestParam Map<String,Object> map){
		map.put("rolecode", "DWBM");
		return yhgl_service.grid(map);
	}
	
	/**
	 * 查看流程记录
	 * @author sxm
	 * @created 2017年9月5日
	 * @param id
	 * @return
	 */
	@RequestMapping("lcjlForm")
	public ModelAndView lcjlForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/oabg/fwsh/lcjlForm");
		mav.addObject("fwshFlowList", fwngService.queryFlowById(id));
		return mav;
	}
	
	/**
	 * 保存审核信息
	 * @author sxm
	 * @created 2017年9月9日
	 * @param fwngFlow
	 * @param fwngid
	 * @param flag
	 * @param sflz
	 * @param qtcz
	 * @param sfqf
	 * @param bhqr
	 * @param djbh
	 * @param dzqz
	 * @param dzqzs
	 * @return
	 */
	@ResponseBody
	@RequestMapping("fwsh/save")
	public Result saveObj(FwngFlow fwngFlow,String fwngid,String fwzt,String sflz,String qtcz,String sfqf,String bhqr,String djbh,String dzqz,String dzqzs){
		fwngService.saveObj(fwngFlow, fwngid, fwzt, sflz, qtcz, sfqf, bhqr, djbh, dzqz, dzqzs);
		return new Result(true);
	}
}
