package simple.project.oabg.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.dao.ZcglSqFlowDao;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.entities.ZcglSqjl;
import simple.project.oabg.service.ZcglSpService;
import simple.project.oabg.service.ZcglSqjlService;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 资产管理 申请记录
 * @author yinchao
 * @date 2017年9月6日
 */
@Controller
@RequestMapping("/oabg/zcsq/")
public class ZcglSqjlController extends SimplewebBaseController {

	private final static String MODELPATH = "/oabg/zcgl/";
	
	@Autowired
	private ZcglSqjlService zcglSqjlService;
	@Autowired
	private ZcglSqFlowDao zcglSqFlowDao;
	@Autowired
	private ZcglSpService zcglSpService;
	@Autowired
	private UserService userService;
	@Autowired
	private YhglDao yhglDao;
	
	/**
	 * 领用主页
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@RequestMapping("ly/index")
	public ModelAndView indexLy(){
		ModelAndView model=new ModelAndView(MODELPATH+"zcsqyl/index");
		return model;
	}
	
	/**
	 * 销毁主页
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@RequestMapping("xh/index")
	public ModelAndView indexXh(){
		ModelAndView model=new ModelAndView(MODELPATH+"zcsqxh/index");
		return model;
	}
	
	/**
	 * 查询
	 * @param map
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@ResponseBody
	@RequestMapping("ly/grid")
	public DataGrid gridLy(@RequestParam Map<String,Object> map){
		return zcglSqjlService.gridLy(map);
	}
	
	/**
	 * 查询
	 * @param map
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@ResponseBody
	@RequestMapping("xh/grid")
	public DataGrid gridXh(@RequestParam Map<String,Object> map){
		return zcglSqjlService.gridXh(map);
	}
	
	/**
	 * 打开新增/查看页面
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@RequestMapping("ly/form")
	public ModelAndView formLy(){
		User user = userService.getCurrentUser();
		Txl txl = yhglDao.getTxlByUser(user.getId());
		ModelAndView mav = new ModelAndView();
		mav.addObject("sqr", user.getRealname());
		mav.addObject("sqbm", txl.getBmdw() == null ? "" : txl.getBmdw().getCode());
		mav.addObject("lxdh", txl.getMobile() == null ? "" : txl.getMobile());
		mav.setViewName(MODELPATH+"zcsqyl/form");
		return mav;
	}
	
	/**
	 * 查看流程
	 * @return
	 * @author yinchao
	 * @date 2017年9月7日
	 */
	@RequestMapping("ly/lcForm")
	public ModelAndView lcForm(String zcglSqjlId){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"zcsqyl/lcForm");
		mav.addObject("zcglSqFlowList", zcglSpService.queryFlowById(zcglSqjlId));
		return mav;
	}
	
	/**
	 * 打开新增/查看页面
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@RequestMapping("xh/form")
	public ModelAndView formXh(){
		User user = userService.getCurrentUser();
		Txl txl = yhglDao.getTxlByUser(user.getId());
		ModelAndView mav = new ModelAndView();
		mav.addObject("sqr", user.getRealname());
		mav.addObject("sqbm", txl.getBmdw() == null ? "" : txl.getBmdw().getCode());
		mav.addObject("lxdh", txl.getMobile() == null ? "" : txl.getMobile());
		mav.setViewName(MODELPATH+"zcsqxh/form");
		return mav;
	}
	
	/**
	 * 查看
	 * @param id
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@ResponseBody
	@RequestMapping("ly/getInfo")
	public Map<String,Object> getInfoLy(String id){
		return zcglSqjlService.getInfoLy(id);
	}
	
	/**
	 * 查看
	 * @param id
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@ResponseBody
	@RequestMapping("xh/getInfo")
	public Map<String,Object> getInfoXh(String id){
		return zcglSqjlService.getInfoXh(id);
	}
	
	/**
	 * 保存新增
	 * @param t
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@ResponseBody
	@RequestMapping("ly/save")
	public Result saveLy(ZcglSqjl t){
		return zcglSqjlService.saveObjLy(t);
	}
	
	/**
	 * 保存新增
	 * @param t
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@ResponseBody
	@RequestMapping("xh/save")
	public Result save(ZcglSqjl t){
		return zcglSqjlService.saveObjXh(t);
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@ResponseBody
	@RequestMapping("ly/delete")
	public Result deleteLy(String ids){
		return zcglSqjlService.deleteByIds(ids);
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	@ResponseBody
	@RequestMapping("xh/delete")
	public Result deleteXh(String ids){
		return zcglSqjlService.deleteByIds(ids);
	}

}
