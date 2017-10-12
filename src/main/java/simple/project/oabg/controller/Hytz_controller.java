package simple.project.oabg.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import simple.project.oabg.dao.DeOrgDao;
import simple.project.oabg.dao.DeRoleDao;
import simple.project.oabg.entities.Hyfk;
import simple.project.oabg.entities.Hytz;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.service.Hyfk_service;
import simple.project.oabg.service.Hytz_service;
import simple.project.oabg.service.Txl_service;
import simple.project.oabg.service.WddyService;
import simple.project.oabg.service.Yhgl_service;
import simple.system.simpleweb.module.system.file.service.FileService;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.util.StringKit;

/**
 * 用户管理
 * @author yzw
 * @created 2017年8月21日
 */
@Controller
@RequestMapping("/oabg/hytz/")
public class Hytz_controller extends SimplewebBaseController{
	protected Logger log = LoggerFactory.getLogger(WebApplicationStarter.class);
	@Autowired
	private DeOrgDao deOrgDao;
	@Autowired
	private DeRoleDao roleDao;
	@Autowired
	private UserService userService;
	@Autowired
	private Hytz_service hytzService;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private Txl_service txl_service;
	@Autowired
	private FileService fileService;
	@Autowired
	private Hyfk_service hyfk_service;
	@Autowired
	private WddyService wddyService;
	private final static String MODELPATH="/oabg/hytz/";
	
	/**
	 * 模块主页面
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		String name = userService.getCurrentUser().getRealname();
		ModelAndView model=new ModelAndView(MODELPATH+"index");
		model.addObject("name", name);
		return model;
	}
	/**
	 * 用户列表
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return hytzService.grid(map);
	}
	
	/**
	 * 新增/修改用户页面
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@RequestMapping("form")
	public ModelAndView form(){
		ModelAndView mav = new ModelAndView();
		Long fjrid = userService.getCurrentUser().getId();
		Txl txl = txl_service.getDao().getTxlByUserId(fjrid);
		if(txl!=null){
			mav.addObject("fjrid", txl.getId());
		}else{
			mav.addObject("fjrid", fjrid);
		}
		mav.setViewName(MODELPATH+"form");
		return mav;
	}
	
	/**
	 * 保存会议记录
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("save")
	public Result save(Hytz hytz){
		if(hytz.getHuiyitime().getTime()<new Date().getTime()){
				return new Result(false);
		}
		return hytzService.saveObj(hytz);
	}
	
	/**
	 * 获取单个会议信息
	 * @param id
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("getInfo")
	public Map<String,Object> getInfo(String id){
		if(StringKit.isEmpty(id)){
			return  new HashMap<String,Object>();
		}
		return hytzService.getInfo(id);
	}
	
	/**
	 * 删除会议记录
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	/*@ResponseBody
	@RequestMapping("deleteByIds")
	public Result deleteByIds(String ids){
		return hytzService.deleteByIds(ids);
	}*/
	/**
	 * 联系人
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@RequestMapping("lxr")
	public ModelAndView lxr(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lxr");
		return mav;
	}
	
	/**
	 * 部门联系人
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@RequestMapping("bm")
	public ModelAndView bm(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"bm");
		return mav;
	}
	
	/**
	 * 联系人列表
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@ResponseBody
	@RequestMapping("bmgrid")
	public DataGrid bmgrid(@RequestParam Map<String,Object> map){
		map.put("rolecode", "DWBM");
		return yhgl_service.grid(map);
	}
	
	/**
	 * 联系人列表
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@ResponseBody
	@RequestMapping("lxrgrid")
	public DataGrid lxrgrid(@RequestParam Map<String,Object> map){
		map.put("bmdw", "101");
		return yhgl_service.grid(map);
	}
	/**
	 * 反馈页面
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@RequestMapping("fkForm")
	public ModelAndView fk(String id){
		ModelAndView mav = new ModelAndView();
		Hytz h = hytzService.getDao().findOne(Long.parseLong(id));
		mav.addObject("urlList", hytzService.fileList(Long.valueOf(id)));	
		mav.addObject("h", h);
		mav.addObject("hyid", id);
		mav.setViewName(MODELPATH+"fkWin");
		List<Role> roles = userService.getCurrentUser().getRoles();
		String roleCode = "";
		for(Role r :roles){
			roleCode+=r.getRoleCode()+",";
		}
		Txl txl = txl_service.getDao().getTxlByUserId(userService.getCurrentUser().getId());
		mav.addObject("bmcode", txl.getBmdw()==null?"":txl.getBmdw().getCode());
		mav.addObject("roleCode", roleCode);
		return mav;
	}
	/**
	 * 反馈会议内容
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@ResponseBody
	@RequestMapping("hytzxx")
	public Map<String,Object> fkInfo(String idd){
		return hytzService.getInfo(idd);
	}
	
	/**
	 * 保存反馈信息
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@ResponseBody
	@RequestMapping("hyfk")
	public Result hyfk(Hyfk hyfk){
		return hytzService.SaveFk(hyfk);
	}
	
	/**
	 * 反馈情况页面
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@RequestMapping("fkqkForm")
	public ModelAndView fkqk(Long id){
		ModelAndView mav = new ModelAndView();
			Hytz hytz = hytzService.getDao().findOne(id);
			mav.addObject("hy", hytz);
			mav.addObject("listMap", hytzService.fkList(id));
			mav.setViewName(MODELPATH+"fkqkWin");
		return mav;
	}
	/**
	 * 反馈会议内容
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@ResponseBody
	@RequestMapping("fkzxx")
	public Map<String,Object> fkqkInfo(String idd){
		return hytzService.getInfo(idd);
	}
	/**
	 *局处室
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@ResponseBody
	@RequestMapping("jcs")
	public List<Long> jcs(){
		return hytzService.JcsList();
	}
	
	/**
	 *直属单位
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@ResponseBody
	@RequestMapping("zsdw")
	public List<Long> zsdw(){
		return hytzService.ZsdwList();
	}
	
	/**
	 *直属单位
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@ResponseBody
	@RequestMapping("xsqj")
	public List<Long> xsqj(){
		return hytzService.XsjqList();
	}
	
	/**
	 *时间校验
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@ResponseBody
	@RequestMapping("checkouttime")
	public Result checkouttime(Date huiyitime){
		return hytzService.Time(huiyitime);
	}
	
	/**
	 * 会议通知查看详情页面
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@RequestMapping("Win")
	public ModelAndView Hytzwin(Long id){
		ModelAndView mav = new ModelAndView();
		mav.addObject("urlList", hytzService.fileList(id));	
		hytzService.saveWdy(id.toString());
		if(hytzService.getHyfk(id)==null){
			mav.addObject("cc", "1");
		}else{
			mav.addObject("cc", "2");
		}
		Hytz h = hytzService.getDao().findOne(id);
		mav.addObject("h", h);
		mav.setViewName(MODELPATH+"Win");
		return mav;
	}
	
	/**
	 * 参会人员
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@RequestMapping("lxrchry")
	public ModelAndView lxrchry(String bmc){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lxcgry");
		mav.addObject("bmc", bmc);
		return mav;
	}
	
	/**
	 * 部门具体参会人员
	 * @param ids
	 * @return
	 * @author yzw
	 * @created 2017年8月23日
	 */
	@ResponseBody
	@RequestMapping("lxcgryrid")
	public DataGrid lxcgryrid(@RequestParam Map<String,Object> map,String bmc){
		map.remove("bmc");
		String [] b = bmc.split(",");
		map.put("bmdw", b[0]);
		return yhgl_service.grid(map);
	}
}
