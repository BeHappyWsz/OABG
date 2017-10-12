package simple.project.oabg.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.entities.Swng;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.service.Swgl_tssw_service;
import simple.project.oabg.service.Yhgl_service;
import simple.system.simpleweb.module.system.file.service.FileService;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 收文管理之特殊收文controller
 * @author wsz
 * @created 2017年9月4日
 */
@Controller
@RequestMapping("/oabg/tssw/")
public class Swgl_tssw_controller extends SimplewebBaseController{
	
	@Autowired
	private FileService fileService;
	@Autowired
	private UserService userService;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private Swgl_tssw_service swgl_tssw_service;
	/**模块公共路径**/
	private final static String MODELPATH="/oabg/tssw/";
	
	/**
	 * 返回主页面
	 * @author wsz
	 * @created 2017年9月4日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"index");
		return mav;
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
		return swgl_tssw_service.grid(map);
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
		ModelAndView model=new ModelAndView(MODELPATH+"form");
		model.addObject("swr", user.getUsername());
		return model;
	}
	
	
	/**
	 * 联系人页面
	 * @return
	 * @author wsz
	 * @created 2017年9月5日
	 */
	@RequestMapping("lxr")
	public ModelAndView lxr(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lxr");
		return mav;
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
	public ModelAndView bm(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"bm");
		return mav;
	}
	
	/**
	 * 保存特殊收文
	 * @param s 收文对象
	 * @param fjs 上传的附件
	 * @param sjr 待回复的人员
	 * @return 
	 * @author wsz
	 * @created 2017年9月7日
	 */
	@ResponseBody
	@RequestMapping("save")
	public Result save(Swng s,String fjs,String sjrs){
		return swgl_tssw_service.saveTsswng(s,fjs,sjrs);
	}
	
	/**
	 * 获取详情
	 * @author wsz
	 * @created 2017年9月7日
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getInfo")
	public Map<String,Object> getInfo(Long id){
		return swgl_tssw_service.getInfo(id);
	}
	
	/**
	 * 特殊收文的流程记录form表单页
	 * 2017年9月7日
	 * wsz
	 * @return
	 */
	@RequestMapping("lcjlForm")
	public ModelAndView lcjlForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lcjlForm");
		mav.addObject("tsswngFlowList", swgl_tssw_service.queryFlowById(id));
		return mav;
	}
	
	/**
	 * 回复起始时间
	 * @param 
	 * @return
	 * @author wsz
	 * @created 2017年9月13日
	 */
	@ResponseBody
	@RequestMapping("checkoutStrtime")
	public Result checkoutStrtime(Date timeStr,Date timeEnd ){
		return swgl_tssw_service.TimeStr(timeStr, timeEnd);
	}
	
	/**
	 * 回复结束时间
	 * @param 
	 * @return
	 * @author wsz
	 * @created 2017年9月13日
	 */
	@ResponseBody
	@RequestMapping("checkoutendtime")
	public Result checkoutendtime(Date timeStr,Date timeEnd ){
		return swgl_tssw_service.TimeEnd(timeStr, timeEnd);
	}
	
	
	/**
	 * 部门单位用户列表
	 * @param map
	 * @return
	 * @author wsz
	 * @created 2017年9月19日
	 */
	@ResponseBody
	@RequestMapping("bmgrid")
	public DataGrid bmgrid(@RequestParam Map<String,Object> map){
		map.put("rolecode", "DWBM");
//		map.put("pitemid", 35);
		return yhgl_service.grid(map);
	}
	
	/**
	 *局处室
	 * @return
	 * @author wsz
	 * @created 2017年9月19日
	 */
	@ResponseBody
	@RequestMapping("jcs")
	public List<String> jcs(){
		List<Txl> txlList = yhgl_service.getDao().getJcs();
		List<String> idlist = new ArrayList<String>();
		List <Txl> DWBMlist = new ArrayList<Txl>();
		if(!txlList.isEmpty()){
			for(Txl l :txlList){
				List<Role> roleList = l.getUser().getRoles();
				if(!roleList.isEmpty()){
					for(Role r :roleList){
						if(r.getRoleCode().equals("DWBM")){
							DWBMlist.add(l);
						}
					}
				}
			}
		}
		for(Txl txl : DWBMlist){
			idlist.add(txl.getName());
		}
		return idlist;
	}
	
	/**
	 *直属单位
	 * @return
	 * @author wsz
	 * @created 2017年9月19日
	 */
	@ResponseBody
	@RequestMapping("zsdw")
	public List<String> zsdw(){
		List<Txl> txlList = yhgl_service.getDao().getZsdw();
		List<String> idlist = new ArrayList<String>();
		List <Txl> DWBMlist = new ArrayList<Txl>();
		if(!txlList.isEmpty()){
			for(Txl l :txlList){
				List<Role> roleList = l.getUser().getRoles();
				if(!roleList.isEmpty()){
					for(Role r :roleList){
						if(r.getRoleCode().equals("DWBM")){
							DWBMlist.add(l);
						}
					}
				}
			}
		}
		for(Txl txl : DWBMlist){
			idlist.add(txl.getName());
		}
		return idlist;
	}
	
	/**
	 *直属单位
	 * @return
	 * @author wsz
	 * @created 2017年9月19日
	 */
	@ResponseBody
	@RequestMapping("xsqj")
	public List<String> xsqj(){
		List<Txl> txlList = yhgl_service.getDao().getXsq();
		List<String> idlist = new ArrayList<String>();
		List <Txl> DWBMlist = new ArrayList<Txl>();
		if(!txlList.isEmpty()){
			for(Txl l :txlList){
				List<Role> roleList = l.getUser().getRoles();
				if(!roleList.isEmpty()){
					for(Role r :roleList){
						if(r.getRoleCode().equals("DWBM")){
							DWBMlist.add(l);
						}
					}
				}
			}
		}
		for(Txl txl : DWBMlist){
			idlist.add(txl.getName());
		}
		return idlist;
	}
	
	/**
	 * 特殊收文查看详情页面
	 * @param id
	 * @return
	 * @author wsz
	 * @created 2017年9月22日
	 */
	@RequestMapping("lookForm")
	public ModelAndView lookForm(Long id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lookForm");
		mav.addObject("message", swgl_tssw_service.getInfo(id));
		Swng swng = swgl_tssw_service.getDao().findOne(id);
		mav.addObject("fjList", swng.getFileInfo());
		return mav;
	}
}

