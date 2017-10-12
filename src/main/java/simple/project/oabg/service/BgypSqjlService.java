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
import simple.project.oabg.dao.BgypSqFlowDao;
import simple.project.oabg.dao.BgypSqjlDao;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.dic.model.Crk;
import simple.project.oabg.dic.model.Tgzt;
import simple.project.oabg.dic.model.Tylc;
import simple.project.oabg.dto.BgypSqjlDto;
import simple.project.oabg.entities.Bgyp;
import simple.project.oabg.entities.BgypRkjl;
import simple.project.oabg.entities.BgypSqFlow;
import simple.project.oabg.entities.BgypSqjl;
import simple.project.oabg.entities.Txl;
import simple.system.simpleweb.module.system.dic.model.DicItem;
import simple.system.simpleweb.module.system.dic.service.DicItemService;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;

/**
 * 劳务用品
 * 2017年9月4日
 * @author yc
 */
@Service
public class BgypSqjlService extends AbstractService<BgypSqjlDao, BgypSqjl, Long>{
	
	@Autowired
	private UserService userService;
	@Autowired
	private BgypService bgypService;
	@Autowired
	private BgypSqFlowDao bgypSqFlowDao;
	@Autowired
	private YhglDao yhglDao;
	@Autowired
	private BgypRkjlService bgypRkjlService;
	@Autowired
	private WddbService wddbService;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private DicItemService dicItemService;
	private List<String> sqUserRoles = new ArrayList<String>();
	@Autowired
	public BgypSqjlService(BgypSqjlDao dao) {
		super(dao);
		sqUserRoles.add("");
	}
	
	/**
	 * 数据列表
	 * 2017年9月4日
	 * yc
	 * @param map
	 * @return
	 */
	public DataGrid grid(Map<String,Object> map){
		if("".equals(StringSimple.nullToEmpty(map.get("spgrid")))){
			User u = userService.getCurrentUser();
			List<Role> roles = u.getRoles();
			boolean isFgld = false;
			for(Role r : roles){
				if("FGLD".equals(r.getRoleCode())){
					isFgld = true;
				}
			}
			if(isFgld){//分管领导查询分管部门下的所有申请信息
				Txl txl = yhgl_service.getDao().getTxlByUserId(u.getId());
				List<String> bm = new ArrayList<String>();
				for(String str : txl.getFgbms().split(",")){
					DicItem dicItem = dicItemService.getDao().findItemByDicCodeAndCode("bmdw", str);
					if(null != dicItem){
						bm.add(dicItem.getName());
					}
				}
				map.put("sqbms", bm);
			}else{
				map.put("sqrUserName", userService.getCurrentUser().getUsername());
			}
		}
		
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
		if(!"".equals(StringSimple.nullToEmpty(map.get("lczts")))){
			List<String> lczts = Utils.stringToStringList(StringSimple.nullToEmpty(map.get("lczts")), ",");
			map.put("lczts",lczts);
		}
		Page<BgypSqjl> page = queryForPage(BgypSqjlDto.class, map);
		return new CommonConvert<BgypSqjl>(page, new Cci<BgypSqjl>() {
			@Override
			public Map<String, Object> convertObj(BgypSqjl t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("sqr", t.getSqr());
				map.put("sqbm", t.getSqbm());
				map.put("lxdh", t.getLxdh());
				map.put("sqrq",null == t.getSqrq() ? "" :new SimpleDateFormat("yyyy-MM-dd").format(t.getSqrq()));
				map.put("name", t.getName());
				map.put("sl", t.getSl());
				map.put("lczt", null == t.getLczt() ? "" : t.getLczt().getCode());
				map.put("lcztName", null == t.getLczt() ? "" : t.getLczt().getName());
				Bgyp bgyp = bgypService.getDao().findOne(t.getBgypId());
				map.put("leftnum", null == bgyp ? "" : bgyp.getSl());
				map.put("bgypfl", null == t.getBgypfl() ? "" : t.getBgypfl().getCode());
				map.put("bgypflName", null == t.getBgypfl() ? "" : t.getBgypfl().getName());
				return map;
			}
		}).getDataGrid();
	}
	
	/**
	 * 根据id获取详情
	 * 2017年9月4日
	 * @param id
	 * @return
	 */
	public Map<String,Object> getInfo(String id){
		Map<String,Object> map=new HashMap<String, Object>();
		if("".equals(StringSimple.nullToEmpty(id))){
			map.putAll(getCurUserInfo());
		}else{
			BgypSqjl t=getDao().findOne(Long.valueOf(id));
			map.put("id", t.getId());
			map.put("name", t.getName());
			map.put("sl", t.getSl());
			map.put("sqyy", t.getSqyy());
			map.put("sqr", t.getSqr());
			map.put("sqrq", null == t.getSqrq() ? "" : new SimpleDateFormat("yyyy-MM-dd").format(t.getSqrq()));
			map.put("sqbm", t.getSqbm());
			map.put("lxdh", t.getLxdh());
			map.put("lwypId", t.getBgypId());
			Bgyp bgyp = bgypService.getDao().findOne(t.getBgypId());
			map.put("leftnum", null == bgyp ? "" : bgyp.getSl());
			map.put("bgypfl", null == t.getBgypfl() ? "" : t.getBgypfl().getCode());
			map.put("bgypflName", null == t.getBgypfl() ? "" : t.getBgypfl().getName());
		}
		return map;
	}
	
	/**
	 * 保存
	 * 2017年9月4日
	 * @param t
	 * @return
	 */
	public Result saveObj(BgypSqjl t){
		if("".equals(StringSimple.nullToEmpty(t.getId()))){
			Bgyp bgyp = bgypService.getDao().findOne(t.getBgypId());
			if(null == bgyp || bgyp.getDeleted())
				return new Result(false,"该劳务用品不存在");
			t.setName(bgyp.getName());
			t.setBgypfl(bgyp.getBgypfl());
			t.setSqrUserName(userService.getCurrentUser().getUsername());
			Tylc lczt = new Tylc();
			lczt.setCode("1");
			t.setLczt(lczt);
			save(t);
			
		}else{
			BgypSqjl old = getDao().findOne(t.getId());
			old.setSqr(t.getSqr());
			old.setSqbm(t.getSqbm());
			old.setLxdh(t.getLxdh());
			old.setSqyy(t.getSqyy());
			old.setSl(t.getSl());
			Bgyp bgyp = bgypService.getDao().findOne(t.getBgypId());
			if(null == bgyp || bgyp.getDeleted())
				return new Result(false,"该劳务用品不存在");
			old.setName(bgyp.getName());
			old.setSqrUserName(userService.getCurrentUser().getUsername());
			Tylc lczt = new Tylc();
			lczt.setCode("1");
			old.setLczt(lczt);
			old.setBgypfl(bgyp.getBgypfl());
			save(old);
		}
		//保存流程记录
		BgypSqFlow lcjl = new BgypSqFlow();
		lcjl.setGlid(StringSimple.nullToEmpty(t.getId()));
		lcjl.setCnode("提交");
		Tgzt tgzt = new Tgzt();
		tgzt.setCode("1");
		lcjl.setTgzt(tgzt);
		lcjl.setRealName(userService.getCurrentUser().getRealname());
		lcjl.setPuser(userService.getCurrentUser().getUsername());
		lcjl.setSuggestion("");
		bgypSqFlowDao.save(lcjl);
		List<User> userList= yhgl_service.getUserByRoleCode("JYWYSZR");
		String name="";
		if(!userList.isEmpty()){
			for(User u :userList){
				name+="#"+u.getUsername();
			}
		}
		name+="".equals(name)?"":"#";
		wddbService.saveNewWddb(t.getId(), "50", "办公用品申请", name, "1");
		return new Result(true);
	}
	
	/**
	 * 获取当前登录人信息
	 * 2017年9月4日
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
	 * 执行审批
	 * 2017年9月4日
	 * yc
	 * @param id
	 * @param sftg
	 * @param suggestion
	 * @return
	 */
	public Result doSp(String id,String sftg,String suggestion){
		BgypSqjl t = getDao().findOne(Long.parseLong(id));
		Bgyp bgyp =bgypService.getDao().findOne(t.getBgypId());
		if("1".equals(StringSimple.nullToEmpty(sftg)) && (bgyp.getSl() < t.getSl())){
			return new Result(false,"库存不足！");
		}
		Tylc lczt = new Tylc();
		lczt.setCode("1".equals(StringSimple.nullToEmpty(sftg)) ? "2" : "9");
		t.setLczt(lczt);
		save(t);
		//保存流程记录
		BgypSqFlow lcjl = new BgypSqFlow();
		lcjl.setGlid(id);
		lcjl.setCnode("审批");
		Tgzt tgzt = new Tgzt();
		tgzt.setCode("1".equals(StringSimple.nullToEmpty(sftg)) ? "1" : "2");
		lcjl.setTgzt(tgzt);
		lcjl.setRealName(userService.getCurrentUser().getRealname());
		lcjl.setPuser(userService.getCurrentUser().getUsername());
		lcjl.setSuggestion(suggestion);
		bgypSqFlowDao.save(lcjl);
		if("1".equals(StringSimple.nullToEmpty(sftg))){
			//审批通过，扣除相应数量
			if(0 != t.getSl()){
				//扣除数量
				int leftsl = bgyp.getSl() - t.getSl();
				bgyp.setSl(leftsl);
				bgypService.save(bgyp);
				BgypRkjl rkjl = new BgypRkjl();
				rkjl.setBgyp(bgyp);
				Crk crk = new Crk();
				crk.setCode("2");
				rkjl.setCrk(crk);
				rkjl.setSl(t.getSl());
				rkjl.setBz("审批通过，自动扣除");
				rkjl.setDate(new Date());
				rkjl.setUsername(userService.getCurrentUser().getUsername());
				bgypRkjlService.save(rkjl);
			}
		}
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
		List<BgypSqFlow> list = bgypSqFlowDao.queryByGlid(id);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (BgypSqFlow t : list) {
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("pUserName", StringSimple.nullToEmpty(t.getRealName()));
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
