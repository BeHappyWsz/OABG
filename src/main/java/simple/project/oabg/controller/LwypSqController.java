package simple.project.oabg.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.entities.LwypSqjl;
import simple.project.oabg.service.LwypSqjlService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 劳务用品申请
 * 2017年9月4日
 * @author yc
 */
@Controller
@RequestMapping("/oabg/lwypsq")
public class LwypSqController extends SimplewebBaseController{
	
	private final static String MODELPATH="/oabg/lwypgl/lwypsq/";
	
	@Autowired
	private LwypSqjlService lwypSqjlService;

	
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
	 * 数据列表
	 * 2017年9月4日
	 * yc
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return lwypSqjlService.grid(map);
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
	public Result save(LwypSqjl t){
		return lwypSqjlService.saveObj(t);
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
		return lwypSqjlService.getInfo(id);
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
		return lwypSqjlService.deleteByIds(ids);
	}
	
	/**
	 * 流程记录form表单页
	 * 2017年9月4日
	 * yc
	 * @return
	 */
	@RequestMapping("lcjlForm")
	public ModelAndView lcjlForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lcjlForm");
		mav.addObject("lwypSqFlowList", lwypSqjlService.queryFlowById(id));
		return mav;
	}
}
