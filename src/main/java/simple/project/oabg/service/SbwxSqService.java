package simple.project.oabg.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import simple.base.utils.StringSimple;
import simple.project.communal.convert.Cci;
import simple.project.communal.convert.CommonConvert;
import simple.project.communal.util.Utils;
import simple.project.oabg.dao.SbwxSqFlowDao;
import simple.project.oabg.dao.SbwxSqjlDao;
import simple.project.oabg.dic.model.Bmdw;
import simple.project.oabg.dic.model.SbwxLczt;
import simple.project.oabg.dic.model.Tgzt;
import simple.project.oabg.dto.SbwxSqjlDto;
import simple.project.oabg.entities.SbwxSqFlow;
import simple.project.oabg.entities.SbwxSqjl;
import simple.project.oabg.entities.Txl;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;
import simple.system.simpleweb.platform.util.StringKit;
/**
 * 设备维修申请
 * @author wsz
 * @date 2017年9月20
 */
@Service
public class SbwxSqService extends AbstractService<SbwxSqjlDao, SbwxSqjl, Long>{

	@Autowired
	public SbwxSqService(SbwxSqjlDao dao) {
		super(dao);
	}

	@Autowired
	private SbwxSqFlowDao sbwxSqFlowDao;
	@Autowired
	private UserService userService;
	@Autowired
	private WddbService wddbService;
	@Autowired
	private Yhgl_service yhgl_service;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 获取所有自己的申请记录列表
	 * @author wsz
	 * @created 2017年9月20日
	 * @param map
	 * @return
	 */
	public DataGrid grid(Map<String, Object> map) {
		try {
			String times=StringSimple.nullToEmpty(map.get("bxrqs"));
			String timee=StringSimple.nullToEmpty(map.get("bxrqe"));
			if(!"".equals(times)){
				times+=" 00:00:00.000";
				map.put("bxrqs", sdf.parse(times));
			}
			if(!"".equals(timee)){
				timee+=" 23:59:59.998";
				map.put("bxrqe", sdf.parse(timee));
			}	
		} catch (ParseException e) {
			e.printStackTrace();
		}
		User u = userService.getCurrentUser();
		String role ="";
		List<Role> roles = u.getRoles();
		for(Role r : roles){
			if("JYWYSZR".equals(r.getRoleCode())){
				role = "1";
			}else if("FGLD".equals(r.getRoleCode())){
				role = "2";
			}
		}
		if("1".equals(role)){//
			List<String> list = new ArrayList<String>();
			list.add("1");
			list.add("2");
			list.add("3");
			map.put("sbwxLczts", list);
		}else if("2".equals(role)){//分管领导查询分管部门下的所有申请信息
			Txl txl = yhgl_service.getDao().getTxlByUserId(u.getId());
			List<String> bm = new ArrayList<String>();
			for(String str : txl.getFgbms().split(",")){
				bm.add(str);
			}
			map.put("bxbms", bm);
		}else{
			map.put("sqrUserName", u.getUsername());
		}
		Page<SbwxSqjl> page = queryForPage(SbwxSqjlDto.class, map);
		return new CommonConvert<SbwxSqjl>(page, new Cci<SbwxSqjl>() {
			@Override
			public Map<String, Object> convertObj(SbwxSqjl t) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("sqr", t.getSqrRealName());
				map.put("bmfzr", t.getBmfzr());
				map.put("bxbm", t.getBxbm() == null ? "" : t.getBxbm().getName());
				map.put("bxrq",t.getBxrq() == null ? "" : sdf1.format(t.getBxrq()));
				map.put("gzyy", t.getGzyy());
				map.put("lczt", t.getSbwxLczt() == null ? "" : t.getSbwxLczt().getCode());
				map.put("lcztName", t.getSbwxLczt() == null ? "" : t.getSbwxLczt().getName());
				return map;
			}
		}).getDataGrid();
	}
	
	/**
	 * 保存设备维修申请记录
	 * @author wsz
	 * @created 2017年9月20日
	 * @param ss
	 * @return
	 */
	public Result saveSqjl(SbwxSqjl ss,String bxbm) {
		User currentUser = userService.getCurrentUser();
		ss.setSqrRealName(currentUser.getRealname());
		ss.setSqrUserName(currentUser.getUsername());
		if(!StringKit.isEmpty(bxbm)){
			Bmdw bm = new Bmdw();
			bm.setCode(bxbm);
			ss.setBxbm(bm);
		}
		SbwxLczt lczt = new SbwxLczt();//报修流程状态1待审批2已审批3已维修9未通过
		lczt.setCode("1");
		ss.setSbwxLczt(lczt);
		save(ss);
		//流程
		SbwxSqFlow sqf = new SbwxSqFlow();
		Tgzt tgzt = new Tgzt();
		tgzt.setCode("1");
		sqf.setGlid(ss.getId().toString());
		sqf.setCnode("提交");
		sqf.setTgzt(tgzt);
		sqf.setPuser(currentUser.getUsername());
		sqf.setPuserName(currentUser.getRealname());
		sqf.setSuggestion("");
		sqf.setClsj(new Date());
		sbwxSqFlowDao.save(sqf);
		//放入我的待办 机要文印室主任 130设备维修
		List<User> list = yhgl_service.getUserByRoleCode("JYWYSZR");
		String name = list.get(0).getUsername();
		wddbService.saveNewWddb(ss.getId(), "130", currentUser.getRealname()+"的设备维修申请", "#"+name+"#", "1");
		return new Result(true,"申请成功");
	}

	
	/**
	 * 获取某条申请记录的信息内容
	 * @author wsz
	 * @created 2017年9月20日
	 * @param id
	 * @return
	 */
	public Map<String, Object> getInfo(Long id) {
		Map<String,Object> map  = new HashMap<String,Object>();
		SbwxSqjl ss = getDao().findOne(id);
		if(StringKit.isEmpty(ss)){
			return new HashMap<String,Object>();
		}
		map.put("id", ss.getId());
		map.put("bxrq", ss.getBxrq() == null ? "" : sdf1.format(ss.getBxrq()));
		map.put("bxbm", ss.getBxbm() == null ? "" : ss.getBxbm().getCode());
		map.put("bxbmName", ss.getBxbm() == null ? "" : ss.getBxbm().getName());
		map.put("jsr",ss.getJsr());
		map.put("bmfzr",ss.getBmfzr());
		map.put("gzyy", ss.getGzyy());
		return map;
	}

	/**
	 * 流程记录form表单页
	 * 2017年9月20日
	 * wsz
	 * @return
	 */
	public List<Map<String,Object>> queryFlowById(String id) {
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		List<SbwxSqFlow> list = sbwxSqFlowDao.queryByGlid(id);
		for(SbwxSqFlow ss : list){
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("pUserName", ss.getPuserName());
			m.put("cnode", ss.getCnode());
			m.put("clsj", ss.getClsj());
			m.put("czsj", ss.getClsj());
			m.put("tgztName", null == ss.getTgzt() ? "" : ss.getTgzt().getName());
			m.put("suggestion", ss.getSuggestion());
			res.add(m);
		}
		return res;
	}

	
	/**
	 * 进行审批
	 * @author wsz
	 * @created 2017年9月20日
	 * @param id
	 * @param suggestion 
	 * @param sftg 
	 * @return
	 */
	public Result doSp(Long id, String sftg, String suggestion) {
		User currentUser = userService.getCurrentUser();
		SbwxSqjl ss = getDao().findOne(id);
		if(StringKit.isEmpty(ss)){
			return new Result(false,"数据信息未找到");
		}
		String isTg = "1".equals(StringSimple.nullToEmpty(sftg)) ? "1" : "2";
		SbwxLczt lczt = new SbwxLczt();
		if("1".equals(isTg)){//通过
			lczt.setCode("2"); //流程状态1待审批2已审批3已维修9未通过
		}else{
			lczt.setCode("9");
		}
		ss.setSbwxLczt(lczt);
		save(ss);
		
		//流程记录
		Tgzt tgzt = new Tgzt();//1通过2不通过
		tgzt.setCode(isTg);
		SbwxSqFlow ssf = new SbwxSqFlow();
		ssf.setGlid(ss.getId().toString());
		ssf.setCnode("审批");
		ssf.setPuser(currentUser.getUsername());
		ssf.setPuserName(currentUser.getRealname());
		ssf.setTgzt(tgzt);
		ssf.setSuggestion(suggestion);
		ssf.setClsj(new Date());
		sbwxSqFlowDao.save(ssf);
		
		//审批后放进自己的我的待办中 //报修流程状态1待审批2已审批3已维修9未通过
		wddbService.saveNewWddb(ss.getId(), "130", currentUser.getRealname()+"的设备维修审批", "#"+currentUser.getUsername()+"#", "2");
		return new Result(true,"已审批");
	}

	/**
	 * 进行已维修状态变换
	 * @author wsz
	 * @created 2017年9月20日
	 * @param id
	 * @param suggestion 
	 * @param sftg 
	 * @return
	 */
	public Result doYwx(Long id) {
		SbwxSqjl ss = getDao().findOne(id);
		if(StringKit.isEmpty(ss)){
			return new Result(false,"数据信息未找到");
		}
		SbwxLczt lczt = new SbwxLczt();
		lczt.setCode("3");//流程状态1待审批2已审批3已维修9未通过
		ss.setSbwxLczt(lczt);
		save(ss);
		
		//流程记录
		Tgzt tgzt = new Tgzt();//1通过2不通过
		tgzt.setCode("1");
		SbwxSqFlow ssf = new SbwxSqFlow();
		ssf.setGlid(ss.getId().toString());
		ssf.setCnode("已维修");
		ssf.setPuser(userService.getCurrentUser().getUsername());
		ssf.setPuserName(userService.getCurrentUser().getRealname());
		ssf.setTgzt(tgzt);
		ssf.setSuggestion("");
		ssf.setClsj(new Date());
		sbwxSqFlowDao.save(ssf);
		return new Result(true,"已维修");
	}

	/**
	 * 大量数据进行已维修状态变换()
	 * @author wsz
	 * @created 2017年9月20日
	 * @param ids
	 * @return
	 */
	public Result doYwxs(String ids) {
		List<Long> list = Utils.stringToLongList(ids, ",");
		List<SbwxSqjl> findAll = getDao().findAll(list);
		SbwxLczt lczt = new SbwxLczt();
		lczt.setCode("3");//流程状态1待审批2已审批3已维修9未通过
		User currentUser = userService.getCurrentUser();
		for(SbwxSqjl ss : findAll){
			ss.setSbwxLczt(lczt);
			save(ss);
			
			//流程记录
			Tgzt tgzt = new Tgzt();//1通过2不通过
			tgzt.setCode("1");
			SbwxSqFlow ssf = new SbwxSqFlow();
			ssf.setGlid(ss.getId().toString());
			ssf.setCnode("已维修");
			ssf.setPuser(currentUser.getUsername());
			ssf.setPuserName(currentUser.getRealname());
			ssf.setTgzt(tgzt);
			ssf.setSuggestion("");
			ssf.setClsj(new Date());
			sbwxSqFlowDao.save(ssf);
		}
		return new Result(true,"已变更状态");
	}
	
}
