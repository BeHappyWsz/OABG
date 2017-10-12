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

import simple.project.communal.util.Utils;
import simple.project.oabg.entities.Swng;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.service.Swgl_swng_service;
import simple.project.oabg.service.Yhgl_service;
import simple.system.simpleweb.module.system.file.dao.FileInfoDao;
import simple.system.simpleweb.module.system.file.service.FileService;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;


/**
 * 收文拟稿controller
 * @author wsz
 * @created 2017年9月4日
 */
@Controller
@RequestMapping("/oabg/swng/")
public class Swgl_swng_controller extends SimplewebBaseController{

	@Autowired
	private FileInfoDao fileInfoDao;
	@Autowired
	private FileService fileService;
	@Autowired
	private UserService userService;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private Swgl_swng_service swgl_swng_service;
	/**模块公共路径**/
	private final static String MODELPATH="/oabg/swng/";
	
	/**
	 * 模块主页面
	 * @author wsz
	 * @created 2017年9月04日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"index");
		return mav;
	}
	
	/**
	 * 新增页面
	 * @author wsz
	 * @created 2017年9月4日
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView form(){
		String username=userService.getCurrentUser().getRealname();
		ModelAndView mav=new ModelAndView(MODELPATH+"form");
		mav.addObject("username", username);
		return mav;
	}
	
	/**
	 * 查看详情页面
	 * @author wsz
	 * @created 2017年9月22日
	 * @return
	 */
	@RequestMapping("lookForm")
	public ModelAndView lookForm(Long id){
		ModelAndView mav=new ModelAndView(MODELPATH+"lookForm");
		mav.addObject("message", swgl_swng_service.getInfo(id));
		mav.addObject("fjList", swgl_swng_service.getDao().findOne(id).getFileInfo());
		return mav;
	}
	
	/**
	 * 保存form表单信息
	 * @author wsz
	 * @created 2017年9月4日
	 * @param fwng
	 * @param zsdws
	 * @param csdws
	 * @param fjs
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public Result save(Swng swng,String fjs){
		return swgl_swng_service.saveSwng(swng, fjs);
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
		return swgl_swng_service.getInfo(id);
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
		return swgl_swng_service.grid(map);
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
		boolean result=swgl_swng_service.logicDelete(idList);
		return new Result(result);
	}
	
	/**
	 * 流程记录form表单页
	 * 2017年9月5日
	 * wsz
	 * @return
	 */
	@RequestMapping("lcjlForm")
	public ModelAndView lcjlForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lcjlForm");
		mav.addObject("swngFlowList", swgl_swng_service.queryFlowById(id));
		return mav;
	}
	
	/**
	 * 分发批阅form表单页
	 * 2017年9月7日
	 * wsz
	 * @return
	 */
	@RequestMapping("ffpyForm")
	public ModelAndView ffpyForm(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"ffpyForm");
		return mav;
	}
	
	
	/**
	 * 批量分发批阅
	 * @param ids 收文id
	 * @param fglds 将要分发给的分管领导
	 * @return
	 * @author wsz
	 * @created 2017年9月7日
	 */
	@ResponseBody
	@RequestMapping("doFfpy")
	public Result doFfpy(String ids,String fglds){
		return swgl_swng_service.doFfpys(ids,fglds);
	}
	
	/**
	 * 收文处理form表单页
	 * 2017年9月7日
	 * wsz
	 * @return
	 */
	@RequestMapping("swclForm")
	public ModelAndView readForm(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"swclForm");
		return mav;
	}

	/**
	 * 联系人页面
	 * @return
	 * @author wsz
	 * @created 2017年9月7日
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
	 * @created 2017年9月7日
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
	 * @created 2017年9月20日
	 */
	@RequestMapping("bm")
	public ModelAndView bm(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"bm");
		return mav;
	}
	
	/**
	 * 部门单位用户列表
	 * @param map
	 * @return
	 * @author wsz
	 * @created 2017年9月20日
	 */
	@ResponseBody
	@RequestMapping("bmgrid")
	public DataGrid bmgrid(@RequestParam Map<String,Object> map){
		map.put("rolecode", "DWBM");
		return yhgl_service.grid(map);
	}
	
	/**
	 * 批量收文处理
	 * @param ids 批量收文ids
	 * @param sjr 收件人usernames
	 * @param swcl 处理类型 1传阅2分办3销毁
	 * @param name 收件人realnames
	 * @return
	 * @author wsz
	 * @created 2017年9月7日
	 */
	@ResponseBody
	@RequestMapping("doSwcl")
	public Result doSwcl(String ids,String sjr,String swcl,String name){
		if("1".equals(swcl)){//前台直接默认传阅
			return swgl_swng_service.cySws(ids,sjr,name);
		}else if("2".equals(swcl)){//分办 已取消分办功能
//			return swgl_swng_service.fbSw(id,sjr,name);
		}else if("3".equals(swcl)){//销毁 已取消销毁功能
//			return swgl_swng_service.xhs(ids);
		}
		return new Result(false);
	}
	
	/**
	 *局处室
	 * @param ids
	 * @return
	 * @author wsz
	 * @created 2017年9月9日
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
	 * @param ids
	 * @return
	 * @author wsz
	 * @created 2017年9月9日
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
	 * @param ids
	 * @return
	 * @author wsz
	 * @created 2017年9月9日
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
	 * 收文拟稿之发文日期控制
	 * @param 
	 * @return
	 * @author wsz
	 * @created 2017年9月21日
	 */
	@ResponseBody
	@RequestMapping("fwrqCheck")
	public Result fwrqCheck(Date fwrq){
		return swgl_swng_service.fwrqCheck(fwrq);
	}
}
