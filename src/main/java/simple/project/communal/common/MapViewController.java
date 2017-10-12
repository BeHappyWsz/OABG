package simple.project.communal.common;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * 地图标记公用controller
 * @author wm
 * @date 2016年12月14日
 */
@Controller
@RequestMapping("/mapView")
public class MapViewController{
	
	/**
	 * 打开地图标记窗口
	 * @param jd 经度
	 * @param wd 纬度
	 * @param area 地区
	 * @return
	 * @author wm
	 * @date 2016年12月14日
	 */
	@RequestMapping(value = "/chooseMapWin")
	public ModelAndView chooseMapWin(String jd,String wd,String area) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("jd", jd);
		modelAndView.addObject("wd", wd);
		modelAndView.addObject("area", area);
		modelAndView.setViewName("/mapView/chooseMapWin");
		return modelAndView;
	}
	
	/**
	 * 显示地图页面
	 * @param jd
	 * @param wd
	 * @param area
	 * @return
	 * @author wm
	 * @date 2016年12月14日
	 */
	@RequestMapping(value = "/showMapWin")
	public ModelAndView showMapWin(String jd,String wd,String area) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("jd", jd);
		modelAndView.addObject("wd", wd);
		modelAndView.addObject("area", area);
		modelAndView.setViewName("/mapView/showMapWin");
		return modelAndView;
	}
}
