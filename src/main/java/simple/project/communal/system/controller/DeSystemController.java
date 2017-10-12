package simple.project.communal.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.AbstractController;

/**
 * 系统主页
 * @author yc
 * @date 2017年1月20日
 */
@Controller
public class DeSystemController extends AbstractController{
	
	@Autowired
	private UserService userService;
	
	/**
	 * 主页
	 * @return
	 * @author yc
	 * @date 2017年1月20日
	 */
	@RequestMapping(value="/system/init.shtml")
	public ModelAndView index(){
		ModelAndView modelView = new ModelAndView();
		modelView.setViewName("/system/welcome");
		User user=userService.getCurrentUser();
		List<Role> roles=user.getRoles();
		for(Role role:roles){
			if("GWY".equals(role.getRoleCode())){
				modelView.setViewName("/system/welcome1");
			}else if("JYWYSZR".equals(role.getRoleCode())){
				modelView.setViewName("/system/welcome1");
			}
		}
		return modelView;
	}
	
}
