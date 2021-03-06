package simple.project.oabg.service;

import java.text.DecimalFormat;
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
import simple.project.oabg.dao.GwjdsqDao;
import simple.project.oabg.dao.GwjdsqFlowDao;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.dic.model.GwjdLczt;
import simple.project.oabg.dic.model.Tgzt;
import simple.project.oabg.dto.GwjdsqDto;
import simple.project.oabg.entities.Gwjdsq;
import simple.project.oabg.entities.GwjdsqFlow;
import simple.project.oabg.entities.Txl;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;

/**
 * 公务接待申请
 * 2017年9月19日
 * @author yc
 */
@Service
public class GwjdsqService extends AbstractService<GwjdsqDao, Gwjdsq, Long>{
	
	@Autowired
	private UserService userService;
	@Autowired
	private GwjdsqFlowDao gwjdsqFlowDao;
	@Autowired
	private WddbService wddbService;
	@Autowired
	private YhglDao yhglDao;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	public GwjdsqService(GwjdsqDao dao) {
		super(dao);
	}
	
	/**
	 * 数据列表
	 * 2017年9月4日
	 * yc
	 * @param map
	 * @return
	 */
	public DataGrid grid(Map<String,Object> map){
		String rolecode = "";
		if("1".equals(StringSimple.nullToEmpty(map.get("gridfrom")))){
			//申请
			map.put("sqrUserName", userService.getCurrentUser().getUsername());
		}else if("2".equals(StringSimple.nullToEmpty(map.get("gridfrom")))){
			//审核
			//处长
			boolean isCz = false;
			boolean isBgsfzr = false;
			boolean isCw = false;
			List<Role> roles = userService.getCurrentUser().getRoles();
			for (Role role : roles) {
				if("CZ".equals(role.getRoleCode())){
					isCz = true;
				}else if("BGSFZR".equals(role.getRoleCode())){
					isBgsfzr = true;
				}else if("GHCWC".equals(role.getRoleCode())){
					isCw = true;
				}
			}
			if(isCz && !isCw && !isBgsfzr){
				Txl t = yhgl_service.getDao().getTxlByUserId(userService.getCurrentUser().getId());
				map.put("sqrbmcode", null == t ? "-1" : (null == t.getBmdw() ? "-1" : t.getBmdw().getCode()));
				rolecode = "CZ";
			}else if(isBgsfzr){
				List<String> lczts = new ArrayList<String>();
				lczts.add("2");
				lczts.add("3");
				lczts.add("4");
				lczts.add("5");
				map.put("lczts", lczts);
				rolecode = "BGSFZR";
			}else if(isCw){
				List<String> lczts = new ArrayList<String>();
				lczts.add("3");
				lczts.add("4");
				lczts.add("5");
				map.put("lczts", lczts);
				rolecode = "GHCWC";
			}
		}else if("3".equals(StringSimple.nullToEmpty(map.get("gridfrom")))){
			//审批
			Txl t = yhgl_service.getDao().getTxlByUserId(userService.getCurrentUser().getId());
			String fgbms = StringSimple.nullToEmpty(t.getFgbms());
			if(!"".equals(fgbms)){
				map.put("sqrbmcodein", Utils.stringToStringList(fgbms, ","));
			}
			List<String> lczts = new ArrayList<String>();
			lczts.add("4");
			lczts.add("5");
			map.put("lczts", lczts);
			rolecode = "FGLD";
		}
		final String rrolecode = rolecode;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!"".equals(StringSimple.nullToEmpty(map.get("sqrq_s")))){
			try {
				map.put("sqrq_s", sdf.parseObject(StringSimple.nullToEmpty(map.get("sqrq_s"))+" 00:00:00"));
			} catch (ParseException e) {
				//
			}
		}
		if(!"".equals(StringSimple.nullToEmpty(map.get("sqrq_e")))){
			try {
				map.put("sqrq_e", sdf.parseObject(StringSimple.nullToEmpty(map.get("sqrq_e"))+" 23:59:59"));
			} catch (ParseException e) {
				//
			}
		}
		if(!"".equals(StringSimple.nullToEmpty(map.get("jdsj_s")))){
			try {
				map.put("jdsj_s", sdf.parseObject(StringSimple.nullToEmpty(map.get("jdsj_s"))+" 00:00:00"));
			} catch (ParseException e) {
				//
			}
		}
		if(!"".equals(StringSimple.nullToEmpty(map.get("jdsj_e")))){
			try {
				map.put("jdsj_e", sdf.parseObject(StringSimple.nullToEmpty(map.get("jdsj_e"))+" 23:59:59"));
			} catch (ParseException e) {
				//
			}
		}

		Page<Gwjdsq> page = queryForPage(GwjdsqDto.class, map);
		return new CommonConvert<Gwjdsq>(page, new Cci<Gwjdsq>() {
			@Override
			public Map<String, Object> convertObj(Gwjdsq t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("sqr", t.getSqr());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				map.put("sqrq",null == t.getSqrq() ? "" :sdf.format(t.getSqrq()));
				map.put("jdsy", t.getJdsy());
				map.put("jdsj",null == t.getJdsj() ? "" :sdf2.format(t.getJdsj()));
				map.put("jdrs",t.getJdrs());
				map.put("ysdx",t.getYsdx());
				map.put("ysxx",t.getYsxx());
				String lcztcode = null == t.getLczt() ? "" : t.getLczt().getCode();
				map.put("lczt", lcztcode);
				map.put("lcztName", null == t.getLczt() ? "" : t.getLczt().getName());
				if("CZ".equals(rrolecode)&!"BGSFZR".equals(rrolecode)){
					if("1".equals(lcztcode)){
						map.put("blqk", "1");
					}else{
						map.put("blqk", "2");
					}
				}else if("BGSFZR".equals(rrolecode)){
					if("2".equals(lcztcode)){
						map.put("blqk", "1");
					}else{
						map.put("blqk", "2");
					}
				}else if("GHCWC".equals(rrolecode)){
					if("3".equals(lcztcode)){
						map.put("blqk", "1");
					}else{
						map.put("blqk", "2");
					}
				}else if("FGLD".equals(rrolecode)){
					if("4".equals(lcztcode)){
						map.put("blqk", "1");
					}else{
						map.put("blqk", "2");
					}
				}
				return map;
			}
		}).getDataGrid();
	}
	
	/**
	 * 根据id获取详情
	 * 2017年9月4日
	 * yc
	 * @param id
	 * @return
	 */
	public Map<String,Object> getInfo(String id){
		Map<String,Object> map=new HashMap<String, Object>();
		if("".equals(StringSimple.nullToEmpty(id))){
			map.putAll(getCurUserInfo());
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Gwjdsq t=getDao().findOne(Long.valueOf(id));
			map.put("id", t.getId());
			map.put("sqr", t.getSqr());
			map.put("sqrq", null == t.getSqrq() ? "" : sdf.format(t.getSqrq()));
			//接待信息
			map.put("jdsy", t.getJdsy());
			map.put("jddx", t.getJddx());
			map.put("jdjb", t.getJdjb());
			map.put("jdrs", t.getJdrs());
			map.put("pkrs", t.getPkrs());
			map.put("jdsj", null == t.getJdsj() ? "" : sdf2.format(t.getJdsj()));
			//会场信息
			map.put("hcdd", t.getHcdd());
			map.put("hcrs", t.getHcrs());
			map.put("hcbz", t.getHcbz());
			//住宿信息
			map.put("zsdd", t.getZsdd());
			map.put("zsrs", t.getZsrs());
			map.put("zsbz", t.getZsbz());
			//餐饮信息
			map.put("cydd", t.getCydd());
			map.put("cyrs", t.getCyrs());
			map.put("cybz", t.getCybz());
			//其他信息
			DecimalFormat df = new DecimalFormat("#0.00");
			map.put("qtje", null == t.getQtje() ? "" : df.format(t.getQtje()));
			map.put("ysdx", t.getYsdx());
			map.put("ysxx", t.getYsxx());
			map.put("bz", t.getBz());
		}
		return map;
	}
	
	/**
	 * 保存
	 * 2017年9月4日
	 * yc
	 * @param t
	 * @return
	 */
	public Result saveObj(Gwjdsq t){
		//科员
		boolean isKy = false;
		//处长
		boolean isCz = false;
		//办公室成员
		boolean isBgscy = false;
		//规划财务处
		//boolean isGhcwc = false;
		List<Role> roles = userService.getCurrentUser().getRoles();
		for (Role role : roles) {
			if("GWY".equals(role.getRoleCode())){
				isKy = true;
			}else if("CZ".equals(role.getRoleCode())){
				isCz = true;
			}else if("BGSZR".equals(role.getRoleCode())||"JYWYSZR".equals(role.getRoleCode())){
				isBgscy = true;
			}else if("GHCWC".equals(role.getRoleCode())){
				//isGhcwc = true;
			}
		}
		GwjdLczt lczt = new GwjdLczt();
		if(isKy){
			Txl txl = yhgl_service.getDao().getTxlByUserId(userService.getCurrentUser().getId());
			String bmdwcode = null == txl ? "" : (null == txl.getBmdw() ? "" : txl.getBmdw().getCode());
			if("102".equals(bmdwcode)){
				//办公室科员
				lczt.setCode("2");
			}else if("114".equals(bmdwcode)){
				//规划财务处
				lczt.setCode("3");
			}else{
				lczt.setCode("1");
			}
			
		}else if(isCz){
//			if(isGhcwc){
//				lczt.setCode("4");
//			}else{
				lczt.setCode("2");
//			}
		}else if(isBgscy){
			lczt.setCode("2");
		}
		
		if("".equals(StringSimple.nullToEmpty(t.getId()))){
			t.setSqrUserName(userService.getCurrentUser().getUsername());
			t.setLczt(lczt);
			Txl txl = yhgl_service.getDao().getTxlByUserName(userService.getCurrentUser().getUsername());
			t.setSqrbmcode(null == txl ? "" : (null == txl.getBmdw() ? "" : txl.getBmdw().getCode()));
			save(t);
			
		}else{
			Gwjdsq old = getDao().findOne(t.getId());
			old.setLczt(lczt);
			//接待信息
			old.setJddx(t.getJddx());
			old.setJdsy(t.getJdsy());
			old.setJdjb(t.getJdjb());
			old.setJdrs(t.getJdrs());
			old.setPkrs(t.getPkrs());
			old.setJdsj(t.getJdsj());
			//会场信息
			old.setHcdd(t.getHcdd());
			old.setHcrs(t.getHcrs());
			old.setHcbz(t.getHcbz());
			//住宿信息
			old.setZsdd(t.getZsdd());
			old.setZsrs(t.getZsrs());
			old.setZsbz(t.getZsbz());
			//餐饮信息
			old.setCydd(t.getCydd());
			old.setCyrs(t.getCyrs());
			old.setCybz(t.getCybz());
			//其他信息
			old.setQtje(t.getQtje());
			old.setYsdx(t.getYsdx());
			old.setYsxx(t.getYsxx());
			old.setBz(t.getBz());
			save(old);
		}
		//保存流程记录
		GwjdsqFlow lcjl = new GwjdsqFlow();
		lcjl.setGlid(StringSimple.nullToEmpty(t.getId()));
		lcjl.setCnode("提交");
		Tgzt tgzt = new Tgzt();
		tgzt.setCode("1");
		lcjl.setTgzt(tgzt);
		lcjl.setRealName(userService.getCurrentUser().getRealname());
		lcjl.setPuser(userService.getCurrentUser().getUsername());
		lcjl.setSuggestion("");
		gwjdsqFlowDao.save(lcjl);
		String name="";
		if(isKy){
			User cz = yhgl_service.getCz();
			if(null != cz){
				name = "#"+cz.getUsername()+"#";
			}
		}else if(isCz || isBgscy){
			User bgsfzr = yhgl_service.getBgsfzr();
			if(null != bgsfzr){
				name = "#"+bgsfzr.getUsername()+"#";
			}
		}
		wddbService.saveNewWddb(t.getId(), "110", t.getSqr()+"公务接待申请", name, lczt.getCode());
		return new Result(true);
	}
	
	/**
	 * 获取当前登录人信息
	 * 2017年9月4日
	 * yc
	 * @return
	 */
	public Map<String,Object> getCurUserInfo(){
		Map<String,Object> info = new HashMap<String, Object>();
		Txl t = yhglDao.getTxlByUser(userService.getCurrentUser().getId());
		if(null != t){
			info.put("sqbm",null ==  t.getBmdw() ? "" : t.getBmdw().getName());
			info.put("sqr",t.getRealname());
			info.put("lxdh","".equals(StringSimple.nullToEmpty(t.getBgdh()))?t.getMobile():t.getBgdh());
		}
		info.put("sqrq", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		return info;
	}
	
	/**
	 * 处长执行审核
	 * 2017年9月4日
	 * yc
	 * @param id
	 * @param sftg
	 * @param suggestion
	 * @return
	 */
	public Result doCzSh(String id,String sftg,String suggestion){
		Gwjdsq t = getDao().findOne(Long.parseLong(id));
		GwjdLczt lczt = new GwjdLczt();
		lczt.setCode("1".equals(StringSimple.nullToEmpty(sftg)) ? "2" : "9");
		t.setLczt(lczt);
		save(t);
		//保存流程记录
		GwjdsqFlow lcjl = new GwjdsqFlow();
		lcjl.setGlid(id);
		lcjl.setCnode("审核");
		Tgzt tgzt = new Tgzt();
		tgzt.setCode("1".equals(StringSimple.nullToEmpty(sftg)) ? "1" : "2");
		lcjl.setTgzt(tgzt);
		lcjl.setRealName(userService.getCurrentUser().getRealname());
		lcjl.setPuser(userService.getCurrentUser().getUsername());
		lcjl.setSuggestion(suggestion);
		gwjdsqFlowDao.save(lcjl);
		if("1".equals(StringSimple.nullToEmpty(sftg))){
			String name = "";
			List<User> userList= yhgl_service.getUserByRoleCode("BGSFZR");
			if(!userList.isEmpty()){
				for(User u :userList){
					name+="#"+u.getUsername();
				}
			}
			name+="".equals(name)?"":"#";
			wddbService.saveNewWddb(t.getId(), "110", t.getSqr()+"公务接待申请", name, lczt.getCode());
		}
		
		return new Result(true);
	}
	
	/**
	 * 办公室执行审核
	 * 2017年9月4日
	 * yc
	 * @param id
	 * @param sftg
	 * @param suggestion
	 * @return
	 */
	public Result doBgsSh(String id,String sftg,String suggestion){
		Gwjdsq t = getDao().findOne(Long.parseLong(id));
		GwjdLczt lczt = new GwjdLczt();
		lczt.setCode("1".equals(StringSimple.nullToEmpty(sftg)) ? "3" : "9");
		t.setLczt(lczt);
		save(t);
		//保存流程记录
		GwjdsqFlow lcjl = new GwjdsqFlow();
		lcjl.setGlid(id);
		lcjl.setCnode("审核");
		Tgzt tgzt = new Tgzt();
		tgzt.setCode("1".equals(StringSimple.nullToEmpty(sftg)) ? "1" : "2");
		lcjl.setTgzt(tgzt);
		lcjl.setRealName(userService.getCurrentUser().getRealname());
		lcjl.setPuser(userService.getCurrentUser().getUsername());
		lcjl.setSuggestion(suggestion);
		gwjdsqFlowDao.save(lcjl);
		if("1".equals(StringSimple.nullToEmpty(sftg))){
			String name = "";
			List<User> userList= yhgl_service.getUserByRoleCode("GHCWC");
			if(!userList.isEmpty()){
				for(User u :userList){
					name+="#"+u.getUsername();
				}
			}
			name+="".equals(name)?"":"#";
			wddbService.saveNewWddb(t.getId(), "110", t.getSqr()+"公务接待申请", name, lczt.getCode());
		}
		return new Result(true);
	}
	
	/**
	 * 财务执行审核
	 * 2017年9月4日
	 * yc
	 * @param id
	 * @param sftg
	 * @param suggestion
	 * @return
	 */
	public Result doCwSh(String id,String sftg,String suggestion){
		Gwjdsq t = getDao().findOne(Long.parseLong(id));
		GwjdLczt lczt = new GwjdLczt();
		lczt.setCode("1".equals(StringSimple.nullToEmpty(sftg)) ? "4" : "9");
		t.setLczt(lczt);
		save(t);
		//保存流程记录
		GwjdsqFlow lcjl = new GwjdsqFlow();
		lcjl.setGlid(id);
		lcjl.setCnode("审核");
		Tgzt tgzt = new Tgzt();
		tgzt.setCode("1".equals(StringSimple.nullToEmpty(sftg)) ? "1" : "2");
		lcjl.setTgzt(tgzt);
		lcjl.setRealName(userService.getCurrentUser().getRealname());
		lcjl.setPuser(userService.getCurrentUser().getUsername());
		lcjl.setSuggestion(suggestion);
		gwjdsqFlowDao.save(lcjl);
		if("1".equals(StringSimple.nullToEmpty(sftg))){
			User fgld = yhgl_service.getFgldByCode(t.getSqrbmcode());
			String name="#"+(null == fgld ? "":fgld.getUsername())+"#";
			wddbService.saveNewWddb(t.getId(), "110", t.getSqr()+"公务接待申请", name, lczt.getCode());
		}
		return new Result(true);
	}
	
	/**
	 * 执行审批
	 * 2017年9月4日
	 * yc
	 * @param id
	 * @param sftg
	 * @param suggestion
	 * @return
	 */
	public Result doSp(String id,String sftg,String suggestion){
		Gwjdsq t = getDao().findOne(Long.parseLong(id));
		GwjdLczt lczt = new GwjdLczt();
		lczt.setCode("1".equals(StringSimple.nullToEmpty(sftg)) ? "5" : "9");
		t.setLczt(lczt);
		save(t);
		//保存流程记录
		GwjdsqFlow lcjl = new GwjdsqFlow();
		lcjl.setGlid(id);
		lcjl.setCnode("审批");
		Tgzt tgzt = new Tgzt();
		tgzt.setCode("1".equals(StringSimple.nullToEmpty(sftg)) ? "1" : "2");
		lcjl.setTgzt(tgzt);
		lcjl.setRealName(userService.getCurrentUser().getRealname());
		lcjl.setPuser(userService.getCurrentUser().getUsername());
		lcjl.setSuggestion(suggestion);
		gwjdsqFlowDao.save(lcjl);
		return new Result(true);
	}
	
	/**
	 * 查询流程记录
	 * 2017年9月4日
	 * yc
	 * @param id
	 * @return
	 */
	public List<Map<String,Object>> queryFlowById(String id){
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		List<GwjdsqFlow> list = gwjdsqFlowDao.queryByGlid(id);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (GwjdsqFlow t : list) {
			Map<String,Object> m = new HashMap<String, Object>();
			User user = userService.getUserByUsername(t.getPuser());
			m.put("pUserName", null == user ? "" : user.getRealname());
			m.put("cnode", t.getCnode());
			m.put("czsj", null == t.getCreateTime() ? "" : sdf.format(t.getCreateTime()));
			m.put("tgztName", null == t.getTgzt() ? "" : t.getTgzt().getName());
			m.put("suggestion", t.getSuggestion());
			res.add(m);
		}
		return res;
	}
	

	/**
	 * 根据id删除
	 * 2017年9月4日
	 * yc
	 * @param ids
	 * @return
	 */
	public Result deleteByIds(String ids){
		List<Long> idList = Utils.stringToLongList(ids, ",");
		logicDelete(idList);
		return new Result(true);
	}
}
