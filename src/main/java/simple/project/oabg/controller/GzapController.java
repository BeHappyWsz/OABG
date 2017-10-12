package simple.project.oabg.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import simple.project.oabg.dic.model.SF;
import simple.project.oabg.entities.Gzap;
import simple.project.oabg.entities.GzapYd;
import simple.project.oabg.entities.gzjhXQ;
import simple.project.oabg.service.Gzjhservice;
import simple.project.oabg.service.GzjhydService;
import simple.system.simpleweb.module.user.model.Role;
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
@RequestMapping("/oabg/gzjh/")
public class GzapController extends SimplewebBaseController{
	protected Logger log = LoggerFactory.getLogger(WebApplicationStarter.class);
	@Autowired
	private Gzjhservice gzjhservice;
	@Autowired
	private GzjhydService gzjhydService;
	@Autowired
	private UserService userService;
	@Autowired
	private final static String MODELPATH="/oabg/gzjh/";
	
	/**
	 * 模块主页面
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView model=new ModelAndView(MODELPATH+"index");
		List<Role> roles = userService.getCurrentUser().getRoles();
		String role = "";
		for(Role r :roles ){
			role+=r.getRoleCode()+",";
		}
		model.addObject("role", role);
		return model;
	}
	/**
	 * 新增
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView form(){
		ModelAndView model=new ModelAndView(MODELPATH+"form");
		return model;
	}
	
	/**
	 * 申请审核列表
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return gzjhservice.grid(map);
	}
	
	/**
	 * 保存
	 * @param map
	 * @returns
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("save")
	public Result save(Gzap gzap,gzjhXQ gzjhXQ,String idd){
		return gzjhservice.saveObj(gzap, gzjhXQ,idd);
	}
	
	/**
	 * 保存
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("getinfo")
	public Map<String, Object> getinfo(Long id){
		return gzjhservice.getInfo(id);
	}
	
	/**
	 * 保存
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	@ResponseBody
	@RequestMapping("tick")
	public Result tick(Gzap gzap,gzjhXQ gzjhXQ,String idd){
		return gzjhservice.tickObj(gzap, gzjhXQ, idd);
	}
	
	/**
	 * 打开查看页面
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@RequestMapping("win")
	public ModelAndView Win(Long id){
		ModelAndView model=new ModelAndView(MODELPATH+"Win");
		Map<String, Object> map = gzjhservice.Info(id);
		gzjhservice.saveWdy(id.toString());
		List<Role> roles = new ArrayList<Role>();
		roles = userService.getCurrentUser().getRoles();
		List<String> roleCode = new ArrayList<String>();
		for(Role r :roles){
			roleCode.add(r.getRoleCode());
		}
		if(!roleCode.contains("BGSZR")){
			GzapYd gzapYd = gzjhydService.getDao().queryByGzjhIDandName(id, userService.getCurrentUser().getName());
			SF sf = new SF();
			sf.setCode("1");
			gzapYd.setSfyd(sf);
			gzjhydService.save(gzapYd);
		}
		model.addObject("map", map);
		return model;
	}
	
	/**
	 * 新增
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getWeek")
	public Result getWeek(String start,String end){
		return gzjhservice.dateToWeek(start, end);
	}
	
	/**
	 * 导出
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@RequestMapping(value="excel")
	 public void export(Long id,HttpServletResponse response){
		Map<String, Object> m = new HashMap<String,Object>();
		String biaoti = "";
		try {
			m=gzjhservice.ExipotInfo(id);
			biaoti = m.get("bt").toString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	ModelExport.exportWithModel("model/gzap/gzap.xls",biaoti,m,response);
    }
	
	/**
	 * 时间校验
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	@ResponseBody
	@RequestMapping("checkouttime")
	public Result checkTime(Date timeStr,Date timeEnd){
		return gzjhservice.TimeEnd(timeStr, timeEnd);
	}
}
