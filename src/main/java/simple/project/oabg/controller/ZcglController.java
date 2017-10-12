package simple.project.oabg.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.entities.Zcgl;
import simple.project.oabg.service.ZcglRkjlService;
import simple.project.oabg.service.ZcglService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

@Controller
@RequestMapping("/oabg/zcdj/")
public class ZcglController extends SimplewebBaseController{
	
	private final static String MODELPATH="/oabg/zcgl/zcdj/";
	
	@Autowired
	private ZcglService zcglService;
	@Autowired
	private ZcglRkjlService zcglRkjlService;
	
	/**
	 * 主页
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView model=new ModelAndView(MODELPATH+"index");
		return model;
	}
	
	/**
	 * 列表查询
	 * @param map
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@ResponseBody
	@RequestMapping("grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		return zcglService.grid(map);
	}
	
	/**
	 * 领用
	 * @param map
	 * @return
	 * @author yinchao
	 * @date 2017年9月8日
	 */
	@ResponseBody
	@RequestMapping("gridLy")
	public DataGrid gridLy(@RequestParam Map<String,Object> map){
		return zcglService.gridLy(map);
	}
	
	/**
	 * 销毁
	 * @param map
	 * @return
	 * @author yinchao
	 * @date 2017年9月8日
	 */
	@ResponseBody
	@RequestMapping("gridXh")
	public DataGrid gridXh(@RequestParam Map<String,Object> map){
		return zcglService.gridXh(map);
	}
	
	/**
	 * 打开新增/查看页面
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@RequestMapping("form")
	public ModelAndView form(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"form");
		return mav;
	}

	/**
	 * 查看
	 * @param id
	 * @return
	 * @author yinchao
	 * @date 2017年9月7日
	 */
	@ResponseBody
	@RequestMapping("getInfo")
	public Map<String,Object> getInfo(String id){
		return zcglService.getInfo(id);
	}
	
	/**
	 * 保存新增
	 * @param t
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@ResponseBody
	@RequestMapping("save")
	public Result save(Zcgl t){
		return zcglService.saveObj(t);
	}
	
	/**
	 * 打开新增入库页面
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@RequestMapping("rkform")
	public ModelAndView rkform(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"form_rk");
		return mav;
	}
	
	/**
	 * 保存新增
	 * @param t
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@ResponseBody
	@RequestMapping("saveRk")
	public Result saveRk(Zcgl t){
		return zcglService.saveObj(t);
	}
	
	/**
	 * 入库记录
	 * @param zcglId
	 * @return
	 * @author yinchao
	 * @date 2017年9月7日
	 */
	@RequestMapping("rkjlForm")
	public ModelAndView rkjlForm(Long zcglId){
		ModelAndView model = new ModelAndView();
		model.setViewName(MODELPATH+"rkjlForm");
		model.addObject("zcglRkjlList", zcglRkjlService.queryRkjlByZcglId(zcglId));
		return model;
	}

}
