package simple.project.oabg.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import simple.project.oabg.dto.WddbDto;
import simple.project.oabg.dto.WddyDto;
import simple.project.oabg.entities.Wddb;
import simple.project.oabg.entities.Wddy;
import simple.project.oabg.service.WddbService;
import simple.project.oabg.service.WddyService;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;

/**
 * @author sxm
 * @created 2017年9月4日
 */
@Controller
@RequestMapping("/oabg/wddb/")
public class MessageController extends SimplewebBaseController{
	
	@Autowired
	private UserService userService;
	@Autowired
	private WddyService wddyService;
	@Autowired
	private WddbService wddbService;
	
	/**
	 * 返回未办事项
	 * @author sxm
	 * @created 2017年8月29日
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getNumber")
	public Result getNum(@RequestParam Map<String,Object> map){
		Map<String,Object> tmap=new HashMap<String, Object>();
		String username=userService.getCurrentUser().getUsername();
		map.put("username",username);
		map.put("state", "2");
		map.put("sfyb", "2");
		map.put("sjr", username);
		List<Wddy> wddyList=wddyService.query(WddyDto.class, map);
		List<Wddb> wddbList=wddbService.query(WddbDto.class, map);
		int dy=wddyList.size();
		int db=wddbList.size();
		tmap.put("dy", dy);
		tmap.put("db", db);
		return new Result(true,"",tmap);
	}
}
