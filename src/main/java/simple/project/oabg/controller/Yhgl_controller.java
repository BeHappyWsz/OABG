package simple.project.oabg.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.base.utils.StringSimple;
import simple.project.oabg.dao.DeOrgDao;
import simple.project.oabg.dao.DeRoleDao;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.service.Yhgl_service;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.util.StringKit;

/**
 * 用户管理
 * @author wsz
 * @created 2017年8月21日
 */
@Controller
@RequestMapping("/oabg/yhgl/")
public class Yhgl_controller extends SimplewebBaseController{

	@Autowired
	private DeOrgDao deOrgDao;
	@Autowired
	private DeRoleDao roleDao;
	@Autowired
	private YhglDao yhglDao;
	@Autowired
	private UserService userService;
	@Autowired
	private Yhgl_service yhglService;
	
	private final static String MODELPATH="/oabg/yhgl/";
	
	/**
	 * 模块主页面
	 * @author wsz
	 * @created 2017年8月21日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"index");
		return mav;
	}
	
	/**
	 * 用户列表
	 * @param map
	 * @return
	 * @author wsz
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return yhglService.grid(map);
	}
	
	/**
	 * 新增/修改用户页面
	 * @return
	 * @author wsz
	 * @created 2017年8月22日
	 */
	@RequestMapping("form")
	public ModelAndView form(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"form");
		return mav;
	}
	
	/**
	 * 新增/修改用户
	 * @return
	 * @author wsz
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("save")
	public Result save(@RequestParam Map<String,Object> map){
		String username = (String) map.get("username");
		User checkUser = null;
		if(! StringKit.isEmpty(username)){
			checkUser = userService.getUserByUsername(username);
		}
		if(StringKit.isEmpty(checkUser)){//不存在该username
			return yhglService.createUser(map);
		}else{//更新
			return yhglService.updataUser(map);
		}
	}
	
	/**
	 * 检查用户名是否已经存在
	 * @param username
	 * @return
	 * @author wsz
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("check")
	public Result check(String username){
		User checkUser = null;
		if(! StringKit.isEmpty(username)){
			checkUser = userService.getUserByUsername(username);
		}
		if("".equals(StringSimple.nullToEmpty(checkUser))){
			return new Result(true,"");
		}else{
			return new Result(false,"已存在该用户名");
		}
	}
	
	/**
	 * 获取单个用户信息
	 * @param id
	 * @return
	 * @author wsz
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("getInfo")
	public Map<String,Object> getInfo(Long id){
		if(StringKit.isEmpty(id)){
			return  new HashMap<String,Object>();
		}
		return yhglService.getInfo(id);
	}
	
	/**
	 * 逻辑删除用户
	 * @param ids
	 * @return
	 * @author wsz
	 * @created 2017年8月23日
	 */
	@ResponseBody
	@RequestMapping("deleteByIds")
	public Result deleteByIds(String ids){
		String[] isList =ids.split(",");
		for(String id : isList){
			yhglService.deleteTxl(Long.parseLong(id));
		}
		return new Result(true,"删除成功");
	}
	
	/**
	 * 局长/分管领导进行分管部门配置时进行部门唯一被分管配置
	 * @return
	 * @author wsz
	 * @created 2017年8月31日
	 */
	@ResponseBody
	@RequestMapping("getFgbms")
	public String getFgbms(){
		String result = "";
		List<Txl> fgbms = yhglDao.getFgbms();
		for(Txl t : fgbms){
			if(!StringKit.isEmpty(t.getFgbms()))
				result += t.getFgbms() +",";
		}
		if(!"".equals(result)){
			result = result.substring(0, result.length()-1);
		}
		return result;
	}
	
}
