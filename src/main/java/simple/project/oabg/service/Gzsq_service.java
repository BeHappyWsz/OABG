package simple.project.oabg.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import simple.project.oabg.dao.DeDicItemDao;
import simple.project.oabg.dao.GzsqDao;
import simple.project.oabg.dao.gzSqFlowDao;
import simple.project.oabg.dic.model.Tgzt;
import simple.project.oabg.dic.model.Tylc;
import simple.project.oabg.dto.GzsqDto;
import simple.project.oabg.entities.GzSqFlow;
import simple.project.oabg.entities.Gzsq;
import simple.project.oabg.entities.Gzsyjl;
import simple.system.simpleweb.module.system.dic.model.DicItem;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;

@Service
public class Gzsq_service extends AbstractService<GzsqDao, Gzsq, Long>{
	@Autowired
	private UserService userService;
	@Autowired
	private Gzdj_service gzdj_service;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private gzSqFlowDao gzSqFlowDao;
	@Autowired
	private DeDicItemDao dicItemDao;
	@Autowired
	private WddbService wddbService;
	@Autowired
	private Gzsyjl_service gzsyjl_service;
	@Autowired
	public Gzsq_service(GzsqDao dao) {
		super(dao);
	}
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 公章登记列表
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月21日
	 */
	public DataGrid grid(Map<String,Object> map){
		map.put("squsername", userService.getCurrentUser().getUsername());
		String str = StringSimple.nullToEmpty(map.get("gztimeStr"));
		String end = StringSimple.nullToEmpty(map.get("gztimeEnd"));
		try {
			if(!"".equals(str)){
				str+=" 00:00:00";
				Date gztimeStr = sdf.parse(str);
				map.put("gztimeStr",gztimeStr);
			}
			if(!"".equals(end)){
				end+=" 23:59:59";
				Date gztimeEnd = sdf.parse(end);
				map.put("gztimeEnd",gztimeEnd);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			Page<Gzsq> page = queryForPage(GzsqDto.class, map);
			return new CommonConvert<Gzsq>(page, new Cci<Gzsq>() {
				@Override
				public Map<String, Object> convertObj(Gzsq t) {
					Map<String, Object> map=new HashMap<String,Object>();
					map.put("id", t.getId());
					map.put("gzname", t.getGzname());
					map.put("ddtime", t.getUpdateTime());
					map.put("fqtime", t.getCreateTime());
					map.put("sqbm", t.getBmdw() == null?"":t.getBmdw().getName());
					map.put("sqname",t.getSqname());
					map.put("gztime", t.getGztime());
					map.put("gzsl", t.getGzsl());
					map.put("sqreason", t.getSqreason());
					map.put("lczt", null == t.getLczt() ? "" : t.getLczt().getCode());
					map.put("lcztName", null == t.getLczt() ? "" : t.getLczt().getName());
					return map;
				}
			}).getDataGrid();
		}
	
	/**
	 * 获取申请信息
	 * @param id
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	public Map<String, Object> getInfo(Long id) {
		Map<String,Object> map = new HashMap<String,Object>();
		Gzsq g = getDao().findOne(id);
		map.put("id",g.getId());
		map.put("sqname", g.getSqname());
		map.put("bmdwCode", g.getBmdw()==null?"":g.getBmdw().getCode());
		map.put("bmdwName", g.getBmdw()==null?"":g.getBmdw().getName());
		map.put("gztime", g.getGztime());
		map.put("gzsl", g.getGzsl());
		map.put("sqreason", g.getSqreason());
		return map;
	}
	/**
	 * 删除申请
	 * @param id
	 * @author yzw
	 * @created 2017年8月23日
	 */
	public Result deleteByIds(String ids) {
		List<Long> idList = Utils.stringToLongList(ids, ",");
		for(Long id :idList){
			Gzsq gzsq = getDao().findOne(id);
			if(gzsq.getLczt().getCode().equals("1")){
				return new Result(false, "未审核的数据无法删除!");
			}
		}
		logicDelete(idList);
		return new Result(true, "删除成功!");
	}
	/**
	 * 保存
	 * @param id
	 * @author yzw
	 * @created 2017年8月23日
	 */
	public Result saveOBJ(Gzsq gzsq) {
		if("".equals(StringSimple.nullToEmpty(gzsq.getId()))){
			String userName = userService.getCurrentUser().getUsername();
			gzsq.setSqusername(userName);
			Tylc lczt = new Tylc();
			lczt.setCode("1");
			gzsq.setLczt(lczt);
			save(gzsq);
		}else{
			Gzsq old = getDao().findOne(gzsq.getId());
			String userName = userService.getCurrentUser().getUsername();
			old.setSqusername(userName);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.MINUTE,-1);
			Date time = calendar.getTime();
			if(gzsq.getGztime().getTime()< time.getTime()){
				return new Result(false, "盖章时间填写有误");
			}
			old.setBmdw(gzsq.getBmdw());
			old.setGzname(gzsq.getGzname());
			old.setGztime(gzsq.getGztime());
			old.setGzsl(gzsq.getGzsl());
			old.setSqname(gzsq.getSqname());
			old.setSqreason(gzsq.getSqreason());
			Tylc lczt = new Tylc();
			lczt.setCode("1");
			old.setLczt(lczt);
			save(old);
		}
		GzSqFlow lcjl = new GzSqFlow();
		lcjl.setGlid(StringSimple.nullToEmpty(gzsq.getId()));
		lcjl.setCnode("提交");
		Tgzt tgzt = new Tgzt();
		tgzt.setCode("1");
		lcjl.setTgzt(tgzt);
		lcjl.setClsj(new Date());
		lcjl.setPuser(userService.getCurrentUser().getUsername());
		lcjl.setSuggestion("");
		gzSqFlowDao.save(lcjl);
		List<User> userList= yhgl_service.getUserByRoleCode("JYWYSZR");
		String name="";
		if(!userList.isEmpty()){
			for(User u :userList){
				name+="#"+u.getUsername();
			}
		}
		name+=("").equals(name)?"":"#";
		return wddbService.saveNewWddb(gzsq.getId(), "70",gzsq.getSqname()+"的公章申请", name, "1");
		}
		
	
	/**
	 * 查询流程记录
	 * 2017年9月4日
	 * yzw
	 * @param id
	 * @return
	 */
	public List<Map<String,Object>> queryFlowById(String id){
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		List<GzSqFlow> list = gzSqFlowDao.queryByGlid(id);
		for (GzSqFlow t : list) {
			Map<String,Object> m = new HashMap<String, Object>();
			User user = userService.getUserByUsername(t.getPuser());
			m.put("pUserName",  null == user ? "" : user.getRealname());
			m.put("cnode", t.getCnode());
			m.put("tgztName", null == t.getTgzt() ? "" : t.getTgzt().getName());
			m.put("suggestion", t.getSuggestion());
			m.put("clsj", sdf.format(t.getClsj()));
			res.add(m);
		}
		return res;
	}
	/**
	 * 执行审批
	 * 2017年9月4日
	 * YZW
	 * @param id
	 * @param sftg
	 * @param suggestion
	 * @return
	 */
	public Result doSp(String id,String sftg,String suggestion){
		Gzsq t = getDao().findOne(Long.parseLong(id));
		Tylc lczt = new Tylc();
		lczt.setCode("1".equals(StringSimple.nullToEmpty(sftg)) ? "2" : "9");
		t.setLczt(lczt);
		save(t);
		//保存流程记录
		GzSqFlow lcjl = new GzSqFlow();
		lcjl.setGlid(id);
		lcjl.setCnode("审批");
		Tgzt tgzt = new Tgzt();
		tgzt.setCode("1".equals(StringSimple.nullToEmpty(sftg)) ? "1" : "2");
		lcjl.setTgzt(tgzt);
		lcjl.setRealname(userService.getCurrentUser().getRealname());
		lcjl.setPuser(userService.getCurrentUser().getUsername());
		lcjl.setSuggestion(suggestion);
		lcjl.setClsj(new Date());
		gzSqFlowDao.save(lcjl);
		if("1".equals(StringSimple.nullToEmpty(sftg))){
			Gzsyjl gzsyjl = new Gzsyjl();
			gzsyjl.setBmdw(t.getBmdw());
			gzsyjl.setGzname(t.getGzname());
			gzsyjl.setName(t.getSqname());
			gzsyjl.setGzsl(t.getGzsl());
			gzsyjl_service.save(gzsyjl);
		}
		return new Result(true);
	}
	
	/**
	 * 时间校验
	 * @author YZW
	 * @created 2017年9月5日
	 */
	public Result Time(Date gztime){
		if(!"".equals(StringSimple.nullToEmpty(gztime))){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.MINUTE,-1);
			Date time = calendar.getTime();
			if(gztime.getTime() < time.getTime()){
				return new Result(false, "盖章时间不能小于当前时间");
			}
		}
		return new Result(true);
	}
	/**
	 * 时间校验
	 * @author YZW
	 * @created 2017年9月5日
	 */
	public DataGrid SPgrid(Map<String,Object> map){
		if(!"".equals(StringSimple.nullToEmpty(map.get("bmdw")))){
			List<String> codeList = new ArrayList<String>();
			String name = "%"+StringSimple.nullToEmpty(map.get("bmdw"))+"%";
			List<DicItem> dicItems = dicItemDao.findItemByDicCodeAndName(name, "BMDW");
			if(!dicItems.isEmpty()){
				for(DicItem d :dicItems){
					codeList.add(d.getCode());
				}
			}else{
				codeList.add("-1");
			}
			map.put("bmdws", codeList);
		}
		map.remove("bmdw");
		List<String> lczts = new ArrayList<String>();
		lczts.add("1");
		lczts.add("2");
		map.put("lczts", lczts);
			Page<Gzsq> page = queryForPage(GzsqDto.class, map);
			return new CommonConvert<Gzsq>(page, new Cci<Gzsq>() {
				@Override
				public Map<String, Object> convertObj(Gzsq t) {
					Map<String, Object> map=new HashMap<String,Object>();
					map.put("id", t.getId());
					map.put("gzname", t.getGzname());
					map.put("ddtime", t.getUpdateTime());
					map.put("fqtime", t.getCreateTime());
					map.put("sqbm", t.getBmdw() == null?"":t.getBmdw().getName());
					map.put("sqname",t.getSqname());
					map.put("gztime", t.getGztime());
					map.put("gzsl", t.getGzsl());
					map.put("sqreason", t.getSqreason());
					map.put("lczt", null == t.getLczt() ? "" : t.getLczt().getCode());
					map.put("lcztName", null == t.getLczt() ? "" : t.getLczt().getName());
					return map;
				}
			}).getDataGrid();
		}
}