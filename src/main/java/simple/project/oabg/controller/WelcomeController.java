package simple.project.oabg.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import simple.project.oabg.service.WelcomeService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;

/**
 * @author sxm
 * @created 2017年9月20日
 */
@Controller
@RequestMapping("/oabg/welcome/")
public class WelcomeController extends SimplewebBaseController{
	
	@Autowired
	private WelcomeService welcomeService;
	/**
	 * 饼状图+柱状图同时联动   上面方法为单一传递
	 * @author wsz
	 * @created 2017年6月27日
	 */
	@ResponseBody
	@RequestMapping("bar")
	public Map<String,Object> getBarData(@RequestParam Map<String,Object> map){
		return welcomeService.getBarData(map);
	}
}
