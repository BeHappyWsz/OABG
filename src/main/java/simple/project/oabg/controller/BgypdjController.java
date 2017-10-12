package simple.project.oabg.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.entities.Bgyp;
import simple.project.oabg.service.BgypService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 办公用品登记
 * @author sxm
 * @created 2017年9月5日
 */
@Controller
@RequestMapping("/oabg/bgypdj")
public class BgypdjController extends SimplewebBaseController{
	
	private final static String MODELPATH="/oabg/bgypgl/bgypdj/";
	
	@Autowired
	private BgypService bgypService;

	
	/**
	 * 主页
	 * 2017年9月4日
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
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return bgypService.grid(map);
	}
	
	/**
	 * form表单页
	 * 2017年9月4日
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
	 * @param t
	 * @return
	 */
	@ResponseBody
	@RequestMapping("save")
	public Result save(Bgyp t){
		return bgypService.saveObj(t);
	}
	
	/**
	 * 根据id获取详情
	 * 2017年9月4日
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getInfo")
	public Map<String,Object> getInfo(String id){
		return bgypService.getInfo(id);
	}
	
	/**
	 * 删除
	 * 2017年9月4日
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public Result delete(String ids){
		return bgypService.deleteByIds(ids);
	}
	
	/**
	 * 入库记录
	 * 2017年9月4日
	 * @param BgypId
	 * @return
	 */
	@RequestMapping("rkjlForm")
	public ModelAndView rkjlForm(Long bgypId){
		ModelAndView model = new ModelAndView();
		model.setViewName(MODELPATH+"rkjlForm");
		model.addObject("bgypRkjlList", bgypService.queryRkjlByBgypId(bgypId));
		return model;
	}
	
	/**
	 * 库存警报
	 * 2017年9月9日
	 * yc
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("jjGrid")
	public DataGrid jjGrid(@RequestParam Map<String,Object> map){
		return bgypService.jjGrid(map);
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
		return bgypService.checkHasJjjl();
	}
}
