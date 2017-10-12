package simple.project.oabg.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.entities.Lwyp;
import simple.project.oabg.service.LwypService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 劳务用品登记
 * 2017年9月4日
 * @author yc
 */
@Controller
@RequestMapping("/oabg/lwypdj")
public class LwypdjController extends SimplewebBaseController{
	
	private final static String MODELPATH="/oabg/lwypgl/lwypdj/";
	
	@Autowired
	private LwypService lwypService;

	
	/**
	 * 主页
	 * 2017年9月4日
	 * yc
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView model=new ModelAndView(MODELPATH+"index");
		return model;
	}

	/**
	 * 数据列表
	 * 2017年9月4日
	 * yc
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return lwypService.grid(map);
	}
	
	/**
	 * form表单页
	 * 2017年9月4日
	 * yc
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView form(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"form");
		return mav;
	}
	
	/**
	 * 保存
	 * 2017年9月4日
	 * yc
	 * @param t
	 * @return
	 */
	@ResponseBody
	@RequestMapping("save")
	public Result save(Lwyp t){
		return lwypService.saveObj(t);
	}
	
	/**
	 * 根据id获取详情
	 * 2017年9月4日
	 * yc
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getInfo")
	public Map<String,Object> getInfo(String id){
		return lwypService.getInfo(id);
	}
	
	/**
	 * 删除
	 * 2017年9月4日
	 * yc
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public Result delete(String ids){
		return lwypService.deleteByIds(ids);
	}
	
	/**
	 * 入库记录
	 * 2017年9月4日
	 * yc
	 * @param lwypId
	 * @return
	 */
	@RequestMapping("rkjlForm")
	public ModelAndView rkjlForm(Long lwypId){
		ModelAndView model = new ModelAndView();
		model.setViewName(MODELPATH+"rkjlForm");
		model.addObject("lwypRkjlList", lwypService.queryRkjlByLwypId(lwypId));
		return model;
	}
	
	/**
	 * 库存警报
	 * 2017年9月6日
	 * yc
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("jjGrid")
	public DataGrid jjGrid(@RequestParam Map<String,Object> map){
		return lwypService.jjGrid(map);
	}
	
	/**
	 * 查询警戒记录
	 * 2017年9月9日
	 * yc
	 * @return
	 */
	@ResponseBody
	@RequestMapping("checkHasJjjl")
	public Result checkHasJjjl(){
		return lwypService.checkHasJjjl();
	}
}
