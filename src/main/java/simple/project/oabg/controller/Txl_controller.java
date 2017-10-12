package simple.project.oabg.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.base.utils.StringSimple;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.service.Txl_service;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
/**
 * 通讯录管理
 * @author wsz
 * @created 2017年8月29日
 */
@Controller
@RequestMapping("/oabg/txl/")
public class Txl_controller extends SimplewebBaseController{

	private final static String MODELPATH="/oabg/txl/";
	
	@Autowired
	private YhglDao yhglDao;
	@Autowired
	private UserService userService;
	@Autowired
	private Txl_service txl_service;
	/**
	 * 模块主页面
	 * @author wsz
	 * @created 2017年8月29日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView mav =  new ModelAndView();
		mav.addObject("msg", getUserMsg());
		mav.setViewName(MODELPATH+"index");
		return mav;
	}
	
	/**
	 * 获取当前用户的信息
	 * @return
	 */
	public Map<String,Object> getUserMsg(){
		Map<String,Object> map  =new HashMap<String,Object>();
		User user = userService.getCurrentUser();
		boolean isAd = false;
		for(Role r : user.getRoles()){
			if("admin".equals(r.getRoleCode())){
				map.put("isAdmin", "yes");
				isAd = true;
			}else{
				map.put("isAdmin", "no");
			}
		}
		if(isAd){
			map.put("bmdwCode", "0");
		}
		Long id = user.getId();
		map.put("uid", id);
		//暂时排除管理员登录
		Txl txl = yhglDao.getTxlByUser(id);
		if(!"".equals(StringSimple.nullToEmpty(txl))){
			String code = txl.getBmdw().getCode();
			map.put("bmdwCode", code);
		}
		return map;
	}
	
	
	/**
	 * 通讯录列表
	 * @param map
	 * @return
	 * @author wsz
	 * @created 2017年8月30日
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return txl_service.grid(map);
	}
	
	/**
	 * 新增/修改用户页面
	 * @return
	 * @author wsz
	 * @created 2017年8月30日
	 */
	@RequestMapping("form")
	public ModelAndView form(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"form");
		return mav;
	}
	
	/**
	 * 通讯录新增信息
	 * @param map
	 * @return
	 * @author wsz
	 * @created 2017年8月30日
	 */
	@ResponseBody
	@RequestMapping("save")
	public Result saveTxl(@RequestParam Map<String,Object> map){
		return txl_service.saveTxl(map);
	}
	
	/**
	 * 双击通讯录，查询数据
	 * @param id
	 * @return
	 * @author wsz
	 * @created 2017年8月30日
	 */
	@ResponseBody
	@RequestMapping("getInfo")
	public Map<String,Object> getInfo(Long id){
		return txl_service.getInfo(id);
	}
	
	/**
	 * 通讯录删除
	 * @param ids
	 * @return
	 * @author wsz
	 * @created 2017年8月31日
	 */
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
			txl_service.deleteTxl(Long.parseLong(id));
		}
		return new Result(true,"删除成功");
	}
}
