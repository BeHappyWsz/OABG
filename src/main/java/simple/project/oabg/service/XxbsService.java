package simple.project.oabg.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import simple.base.utils.StringSimple;
import simple.project.communal.convert.Cci;
import simple.project.communal.convert.CommonConvert;
import simple.project.communal.util.Utils;
import simple.project.oabg.dao.XxbsDao;
import simple.project.oabg.dao.XxbsSqFlowDao;
import simple.project.oabg.dic.model.Tgzt;
import simple.project.oabg.dic.model.Xxbsly;
import simple.project.oabg.dto.XxbsDto;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.entities.Xxbs;
import simple.project.oabg.entities.XxbsSqFlow;
import simple.system.simpleweb.module.system.dic.service.DicItemService;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;


	
/** 
 * @author  YZW: 
 * @date 创建时间：2017年9月19日 下午1:34:07 
 * @parameter  
 */
@Service
public class XxbsService extends AbstractService<XxbsDao, Xxbs, Long> {
	@Autowired
	private UserService userService;
	@Autowired
	private XxbsSqFlowDao xxbsSqFlowDao;
	@Autowired
	private DicItemService dicItemService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private WddbService wddbService;
	@Autowired
	private Txl_service txlservice;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	public XxbsService(XxbsDao dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
	public DataGrid grid(Map<String , Object> map){
		String name = userService.getCurrentUser().getUsername();
		map.put("bsxm", name);
		String date = StringSimple.nullToEmpty(map.get("bssj"));
		if(!"".equals(date)){
			String dateStr = date+"-01 00:00:00";
			String dateEnd = getDate(date);
			try {
				map.put("bssjstart", sdf.parseObject(dateStr));
				map.put("bssjend", sdf.parseObject(dateEnd));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		Page<Xxbs> page = queryForPage(XxbsDto.class, map);
		return new CommonConvert<Xxbs>(page, new Cci<Xxbs>() {
			@Override
			public Map<String, Object> convertObj(Xxbs x) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", x.getId());
				map.put("bsbm", x.getBsbm()==null?"":x.getBsbm().getName());
				map.put("bssj",sdf.format(x.getBssj()));
				map.put("bsbt", x.getBsbt());
				map.put("lczt", null == x.getLczt() ? "" : x.getLczt().getCode());
				map.put("lcztName", null == x.getLczt() ? "" : x.getLczt().getName());
				return map;
			}
		}).getDataGrid();
	}
	/**
	 * 查找单个信息
	 * */
	public Map< String, Object> getInfo(Long id){
		Map<String, Object> map = new HashMap<String, Object>();
		Xxbs xxbs = getDao().findOne(id);
		map.put("id", xxbs.getId());
		map.put("bsbm", xxbs.getBsbm()==null?"":xxbs.getBsbm().getCode());
		map.put("bsbmName", xxbs.getBsbm()==null?"":xxbs.getBsbm().getName());
		map.put("bssj",new Date());
		map.put("bsbt", xxbs.getBsbt());
		map.put("bsnr", xxbs.getBsnr());
		return map;
	}
	
	/**
	 * 保存
	 * */
	public Result saveObj(Xxbs xxbs){
		if(StringSimple.nullToEmpty(xxbs.getId()).equals("")){
			Xxbsly xxbsly = new Xxbsly();
			xxbsly.setCode("1");
			String name = userService.getCurrentUser().getUsername();
			xxbs.setBsxm(name);
			xxbs.setLczt(xxbsly);
			save(xxbs);
		}else{
			Xxbs old = getDao().findOne(xxbs.getId());
			String name = userService.getCurrentUser().getUsername();
			old.setBsxm(name);
			old.setBsbm(xxbs.getBsbm());
			old.setBsbt(xxbs.getBsbt());
			old.setBsnr(xxbs.getBsnr());
			old.setBssj(xxbs.getBssj());
			Xxbsly xxbsly = new Xxbsly();
			xxbsly.setCode("1");
			old.setLczt(xxbsly);
			save(old);
		}
			XxbsSqFlow xxbsSqFlow = new XxbsSqFlow();
			xxbsSqFlow.setClsj(new Date());
			xxbsSqFlow.setGlid(StringSimple.nullToEmpty(xxbs.getId()));
			xxbsSqFlow.setCnode("提交");
			Tgzt tgzt = new Tgzt();
			tgzt.setCode("1");
			xxbsSqFlow.setTgzt(tgzt);
			xxbsSqFlow.setClsj(new Date());
			xxbsSqFlow.setPuser(userService.getCurrentUser().getUsername());
			xxbsSqFlow.setSuggestion("");
			xxbsSqFlowDao.save(xxbsSqFlow);
			List<User> userList= yhgl_service.getUserByRoleCode("XXGLY");
			String name="";
			if(!userList.isEmpty()){
				for(User u :userList){
					name+="#"+u.getUsername();
				}
			}
			name+=("").equals(name)?"":"#";
			Txl txl = txlservice.getDao().getTxlByUserId(userService.getCurrentUser().getId());
			String xx="";
			if(txl!=null){
				xx = txl.getBmdw()==null?"":txl.getBmdw().getName(); 
			}
		return wddbService.saveNewWddb(Long.valueOf(StringSimple.nullToEmpty(xxbs.getId())), "140", xx+"信息报送申请", name, "1");
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
		List<XxbsSqFlow> list = xxbsSqFlowDao.queryByGlid(id);
		for (XxbsSqFlow t : list) {
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
	 * 删除
	 * @param id
	 * @author yzw
	 * @created 2017年8月23日
	 */
	public Result deleteByIds(String ids) {
		List<Long> idList = Utils.stringToLongList(ids, ",");
		logicDelete(idList);
		return new Result(true, "删除成功!");
	}
	
	/**
	 * 获得信息报送的报表
	 * @author yzw       
	 * @created 2017年8月18日
	 */
/*	public JSONObject getBaoBiao(String bssj,String bsbm){
		String where="";
		MapSqlParameterSource parameters = new MapSqlParameterSource(); 
		
		if(!StringKit.isEmpty(bssj)){
			String bssjstart = bssj +"-01 00:00:00";
			where+=" and bssj >= :bssjstart";
			parameters.addValue("bssjstart", bssjstart);
		}
		if(!StringKit.isEmpty(bssj)){
			String bssjend = getDate(bssj);
			where+=" and bssj <= :bssjend";
			parameters.addValue("bssjend", bssjend);
		}
		if(!StringKit.isEmpty(bsbm)){ 
			where+=" and bsbm_code = :bsbm";
			parameters.addValue("bsbm", bsbm);
		}
		String sql="SELECT bsbm_code bsbm,bsbt,bssj from t_xxbs where deleted = 0 and sfly_code = 1 "+where;
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		List<Map<String,Object>> listMap = namedParameterJdbcTemplate.queryForList(sql, parameters);
		System.out.println(sql);
		List<Map<String, Object>> lmap = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> m :listMap){
			DicItem bmdw = dicItemService.getDao().findItemByDicCodeAndCode("BMDW", m.get("bsbm").toString());
			m.put("bsbm", bmdw.getName());
			String Date = m.get("bssj").toString();
			try {
				Date date = sdf.parse(Date);
				m.put("bssj", sdf.format(date));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lmap.add(m);
		}
		JSONObject ja=new JSONObject();
		ja.put("baobiao", lmap);
		ja.put("biaoti", "信息报送统计表");
		return ja;
	}*/
	/**
	 * 获得信息报送的报表
	 * @author yzw       
	 * @created 2017年8月18日
	 */
	/*public JSONObject getBaoBiaoTj(String bssj,String bsbm){
		String where="";
		MapSqlParameterSource parameters = new MapSqlParameterSource(); 
		
		if(!StringKit.isEmpty(bssj)){
			String bssjstart = bssj +"-01 00:00:00";
			where+=" and bssj >= :bssjstart";
			parameters.addValue("bssjstart", bssjstart);
		}
		if(!StringKit.isEmpty(bssj)){
			String bssjend = getDate(bssj);
			where+=" and bssj <= :bssjend";
			parameters.addValue("bssjend", bssjend);
		}
		if(!StringKit.isEmpty(bsbm)){ 
			where+=" and bsbm_code = :bsbm";
			parameters.addValue("bsbm", bsbm);
		}
		String sql="SELECT COUNT(*) zs,bsbm_code from t_xxbs where deleted = 0 and sfly_code = 1 "+where+" GROUP BY bsbm_code";
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		List<Map<String,Object>> listMap = namedParameterJdbcTemplate.queryForList(sql, parameters);
		System.out.println(sql);
		List<Map<String, Object>> lmap = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> m :listMap){
			DicItem bmdw = dicItemService.getDao().findItemByDicCodeAndCode("BMDW", m.get("bsbm_code").toString());
			m.put("bsbm", bmdw.getName());
			lmap.add(m);
		}
		JSONObject ja=new JSONObject();
		ja.put("baobiao", lmap);
		ja.put("biaoti", "信息报送统计表");
		return ja;
	}*/
	/**
	 * 根据年月后的天数
	 * @author yzw       
	 * @created 2017年8月18日
	 */
	public String getDate(String date){
		String d = "";
		String da = date.substring(0, 4);
		String dat = date.substring(5);
		if(Integer.parseInt(da)%4==0&&Integer.parseInt(da)%100!=0||Integer.parseInt(da)%400==0){
			if(dat.equals("2")){
				d=date +"-29 23:59:59";
			}
		}else{
			if(dat.equals("2")){
				d=date +"-28 23:59:59";
			}
		}
		if(dat.equals("01")){
			d = date  +"-31 23:59:59";
		}else if(dat.equals("03")){
			d = date  +"-31 23:59:59";
		}else if(dat.equals("04")){
			d = date  +"-30 23:59:59";
		}else if(dat.equals("05")){
			d = date  +"-31 23:59:59";
		}else if(dat.equals("06")){
			d = date  +"-30 23:59:59";
		}else if(dat.equals("07")){
			d = date  +"-31 23:59:59";
		}else if(dat.equals("08")){
			d = date  +"-31 23:59:59";
		}else if(dat.equals("09")){
			d = date  +"-30 23:59:59";
		}else if(dat.equals("10")){
			d = date  +"-31 23:59:59";
		}else if(dat.equals("11")){
			d = date  +"-30 23:59:59";
		}else if(dat.equals("12")){
			d = date  +"-31 23:59:59";
		}
		
		return d;
	}
	/**
	 * 回填信息
	 * @author yzw       
	 * @created 2017年8月18日
	 */
	public Map<String , Object> getinfo(){
		Map<String , Object> map = new HashMap<String, Object>();
		Txl txl = txlservice.getDao().getTxlByUserId(userService.getCurrentUser().getId());
		if(txl!=null){
			map.put("bsbm", txl.getBmdw()==null?"":txl.getBmdw().getCode());
			map.put("bsbmName", txl.getBmdw()==null?"":txl.getBmdw().getName());
		}
		return map;
	}
	
	/**
	 * 查询报表
	 * 2017年9月22日
	 * yc
	 * @param tmap
	 * @return
	 */
	public Map<String,Object> queryReport(Map<String,Object> tmap){
		String date = StringSimple.nullToEmpty(tmap.get("bssj"));
		Date dateTime = new Date();
		String time = new SimpleDateFormat("yyyy").format(dateTime);
		String yearmonth = new SimpleDateFormat("yyyy-MM").format(dateTime);
		if("".equals(date)){
			date = yearmonth;
		}
		String dateStr = date+"-01 00:00:00";
		String dateEnd = getDate(date);
		try {
			tmap.put("bssjstart", sdf.parseObject(dateStr));
			tmap.put("bssjend", sdf.parseObject(dateEnd));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Date bssjs = null ;
		Date bssje = null ;
		try {
			bssjs = sdf.parse(time+"-01-01 00:00:00");
			bssje = sdf.parse(time+"-12-31 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(!"".equals(StringSimple.nullToEmpty(tmap.get("bsbm")))&&"".equals(StringSimple.nullToEmpty(tmap.get("bssj")))){
			tmap.put("bssjstart", bssjs);
			tmap.put("bssjend",bssje);
		}
		DecimalFormat df = new DecimalFormat("#0.0");
		tmap.put("sfly", "1");
		List<Xxbs> list = query(XxbsDto.class, tmap);
		Map<String,Map<String,Object>> reMap = new HashMap<String, Map<String,Object>>();
		for (int i = 0; i < list.size(); i++) {
			Xxbs t = list.get(i);
			String bmdw = null == t.getBsbm() ? "" : t.getBsbm().getCode();
			if(reMap.containsKey(bmdw)){
				continue;
			}
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("index", i);
			m.put("bmdw", bmdw);
			m.put("bmdwname", null == t.getBsbm() ? "" : t.getBsbm().getName());
			m.put("ordernum", null == t.getBsbm() ? 0 : t.getBsbm().getOrderNum());
			m.put("total", 0);
			m.put("totalscore", 0.0);
			m.put("bss", new ArrayList<Map<String,Object>>());
			//年度分数累计
			m.put("scorey",  df.format(getDao().queryYearScoreByBmdw(StringSimple.nullToEmpty(m.get("bmdw")), bssjs, bssje)));
			reMap.put(bmdw, m);
		}
		
		for (Xxbs t : list) {
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("bsbt", t.getBsbt());
			m.put("bssj", null == t.getBssj() ? "" : s.format(t.getBssj()));
			String bmdw = null == t.getBsbm() ? "" : t.getBsbm().getCode();
			Map<String,Object> bms = reMap.get(bmdw);
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> bss = (List<Map<String, Object>>) bms.get("bss");
			bss.add(m);
			int total = (Integer) bms.get("total");
			bms.put("total", total+1);
			Double totalscore = Double.parseDouble(StringSimple.nullToEmpty(bms.get("totalscore")));
			Double s = t.getScore();
			if("".equals(StringSimple.nullToEmpty(s))){
				s = 0.0;
			}
			bms.put("totalscore", df.format(totalscore+ s));
		}
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		Set<String> reKeys = reMap.keySet();
		for (String key : reKeys) {
			res.add(reMap.get(key));
		}
		Collections.sort(res,new Comparator<Map<String,Object>>(){
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				int orderNum1 = (Integer) o1.get("ordernum");
				int orderNum2 = (Integer) o2.get("ordernum");
				return orderNum1 == orderNum2 ? 0 : (orderNum1 > orderNum2 ? 1 : -1);
			}
		});
		/*Map<String , Object> map = new HashMap<String, Object>();
		Date dateTime = new Date();
		String time = sdf.format(dateTime);
		time = time.substring(0, 11);*/
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data", res);
		date = date.substring(0, 4)+"年";
		result.put("bssj", date);
		result.put("zbr", userService.getCurrentUser().getRealname());
		result.put("zbsj", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		result.put("zbdw", "常州市民政局");
		return result;
	}
	
	/**
	 * 导出查询--略有差异
	 * 2017年9月23日
	 * yc
	 * @param tmap
	 * @return
	 */
	public Map<String,Object> exportReport(Map<String,Object> tmap){
		String date = StringSimple.nullToEmpty(tmap.get("bssj"));
		Date dateTime = new Date();
		String time = new SimpleDateFormat("yyyy").format(dateTime);
		String yearmonth = new SimpleDateFormat("yyyy-MM").format(dateTime);
		if("".equals(date)){
			date = yearmonth;
			
		}
		String dateStr = date+"-01 00:00:00";
		String dateEnd = getDate(date);
		try {
			tmap.put("bssjstart", sdf.parseObject(dateStr));
			tmap.put("bssjend", sdf.parseObject(dateEnd));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Date bssjs = null ;
		Date bssje = null ;
		try {
			bssjs = sdf.parse(time+"-01-01 00:00:00");
			bssje = sdf.parse(time+"-12-31 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(!"".equals(StringSimple.nullToEmpty(tmap.get("bsbm")))&&"".equals(StringSimple.nullToEmpty(tmap.get("bssj")))){
			tmap.put("bssjstart", bssjs);
			tmap.put("bssjend",bssje);
		}
		DecimalFormat df = new DecimalFormat("#0.0");
		tmap.put("sfly", "1");
		List<Xxbs> list = query(XxbsDto.class, tmap);
		Map<String,Map<String,Object>> reMap = new HashMap<String, Map<String,Object>>();
		for (int i = 0; i < list.size(); i++) {
			Xxbs t = list.get(i);
			String bmdw = null == t.getBsbm() ? "" : t.getBsbm().getCode();
			if(reMap.containsKey(bmdw)){
				continue;
			}
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("index", i);
			m.put("bmdw", bmdw);
			m.put("bmdwname", null == t.getBsbm() ? "" : t.getBsbm().getName());
			m.put("ordernum", null == t.getBsbm() ? 0 : t.getBsbm().getOrderNum());
			m.put("totalscore", 0.0);
			m.put("total", 0);
			//年度分数累计
			m.put("scorey",  df.format(getDao().queryYearScoreByBmdw(StringSimple.nullToEmpty(m.get("bmdw")), bssjs, bssje)));
			reMap.put(bmdw, m);
		}
		
		for (Xxbs t : list) {
			String bmdw = null == t.getBsbm() ? "" : t.getBsbm().getCode();
			Map<String,Object> bms = reMap.get(bmdw);
			int total = (Integer) bms.get("total");
			bms.put("total", total+1);
			Double totalscore = Double.parseDouble(StringSimple.nullToEmpty(bms.get("totalscore")));
			bms.put("totalscore", df.format(totalscore+t.getScore()));
		}
		
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < list.size(); i++) {
			Xxbs t = list.get(i);
			String bmdw = null == t.getBsbm() ? "" : t.getBsbm().getCode();
			Map<String,Object> bms = reMap.get(bmdw);
			//塞入信息
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("index", i + 1);
			m.put("bmdwname", bms.get("bmdwname"));
			m.put("bsbt", t.getBsbt());
			m.put("bssj", null == t.getBssj() ? "" : sdf.format(t.getBssj()));
			m.put("total", bms.get("total"));
			m.put("ordernum", bms.get("ordernum"));
			m.put("totalscore", bms.get("totalscore"));
			//年度分数累计
			m.put("scorey",  bms.get("scorey"));
			res.add(m);
			
		}
		Collections.sort(res,new Comparator<Map<String,Object>>(){

			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				int orderNum1 = (Integer) o1.get("ordernum");
				int orderNum2 = (Integer) o2.get("ordernum");
				return orderNum1 == orderNum2 ? 0 : (orderNum1 > orderNum2 ? 1 : -1);
			}
		});
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data", res);
		date = date.substring(0, 4)+"年";
		result.put("bssj", date);
		result.put("zbr", userService.getCurrentUser().getRealname());
		result.put("zbsj", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		result.put("zbdw", "常州市民政局");
		return result;
	}
	
	/**
	 * 导出查询--略有差异
	 * 2017年9月23日
	 * yc
	 * @param tmap
	 * @return
	 */
	public Map<String,Object> queryPrint(Map<String,Object> tmap){
		String date = StringSimple.nullToEmpty(tmap.get("bssj"));
		Date dateTime = new Date();
		String time = new SimpleDateFormat("yyyy").format(dateTime);
		String yearmonth = new SimpleDateFormat("yyyy-MM").format(dateTime);
		if("".equals(date)){
			date = yearmonth;
		}
		String dateStr = date+"-01 00:00:00";
		String dateEnd = getDate(date);
		try {
			tmap.put("bssjstart", sdf.parseObject(dateStr));
			tmap.put("bssjend", sdf.parseObject(dateEnd));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Date bssjs = null ;
		Date bssje = null ;
		try {
			bssjs = sdf.parse(time+"-01-01 00:00:00");
			bssje = sdf.parse(time+"-12-31 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(!"".equals(StringSimple.nullToEmpty(tmap.get("bsbm")))&&"".equals(StringSimple.nullToEmpty(tmap.get("bssj")))){
			tmap.put("bssjstart", bssjs);
			tmap.put("bssjend",bssje);
		}
		DecimalFormat df = new DecimalFormat("#0.0");
		tmap.put("sfly", "1");
		List<Xxbs> list = query(XxbsDto.class, tmap);
		Map<String,Map<String,Object>> reMap = new HashMap<String, Map<String,Object>>();
		for (int i = 0; i < list.size(); i++) {
			Xxbs t = list.get(i);
			String bmdw = null == t.getBsbm() ? "" : t.getBsbm().getCode();
			if(reMap.containsKey(bmdw)){
				continue;
			}
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("index", i);
			m.put("bmdw", bmdw);
			m.put("bmdwname", null == t.getBsbm() ? "" : t.getBsbm().getName());
			m.put("ordernum", null == t.getBsbm() ? 0 : t.getBsbm().getOrderNum());
			m.put("total", 0);
			m.put("totalscore", 0.0);
			m.put("bss", new ArrayList<Map<String,Object>>());
			//年度分数累计
			m.put("scorey",  df.format(getDao().queryYearScoreByBmdw(StringSimple.nullToEmpty(m.get("bmdw")), bssjs, bssje)));
			reMap.put(bmdw, m);
		}
		
		for (Xxbs t : list) {
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("bsbt", t.getBsbt());
			m.put("bssj", null == t.getBssj() ? "" : s.format(t.getBssj()));
			String bmdw = null == t.getBsbm() ? "" : t.getBsbm().getCode();
			Map<String,Object> bms = reMap.get(bmdw);
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> bss = (List<Map<String, Object>>) bms.get("bss");
			bss.add(m);
			int total = (Integer) bms.get("total");
			bms.put("total", total+1);
			Double totalscore = Double.parseDouble(StringSimple.nullToEmpty(bms.get("totalscore")));
			Double s = t.getScore();
			if("".equals(StringSimple.nullToEmpty(s))){
				s = 0.0;
			}
			bms.put("totalscore", df.format(totalscore+ s));
		}
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		Set<String> reKeys = reMap.keySet();
		for (String key : reKeys) {
			res.add(reMap.get(key));
		}
		Collections.sort(res,new Comparator<Map<String,Object>>(){
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				int orderNum1 = (Integer) o1.get("ordernum");
				int orderNum2 = (Integer) o2.get("ordernum");
				return orderNum1 == orderNum2 ? 0 : (orderNum1 > orderNum2 ? 1 : -1);
			}
		});
		/*Map<String , Object> map = new HashMap<String, Object>();
		Date dateTime = new Date();
		String time = sdf.format(dateTime);
		time = time.substring(0, 11);*/
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("data", res);
		date = date.substring(0, 4)+"年";
		result.put("bssj", date);
		result.put("zbr", userService.getCurrentUser().getRealname());
		result.put("zbsj", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		result.put("zbdw", "常州市民政局");
		return result;
	}
}
