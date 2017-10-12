package simple.project.oabg.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import simple.base.utils.StringSimple;
import simple.project.communal.convert.Cci;
import simple.project.communal.convert.CommonConvert;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.dao.ZcglDao;
import simple.project.oabg.dao.ZcglSqjlDao;
import simple.project.oabg.dic.dao.BmdwDao;
import simple.project.oabg.dic.dao.SqlxDao;
import simple.project.oabg.dic.dao.TgztDao;
import simple.project.oabg.dic.dao.ZcglsplcDao;
import simple.project.oabg.dic.model.Bmdw;
import simple.project.oabg.dic.model.Crk;
import simple.project.oabg.dic.model.Sqlx;
import simple.project.oabg.dic.model.Tgzt;
import simple.project.oabg.dic.model.Zcglsplc;
import simple.project.oabg.dto.ZcglSqjlDto;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.entities.Zcgl;
import simple.project.oabg.entities.ZcglRkjl;
import simple.project.oabg.entities.ZcglSqFlow;
import simple.project.oabg.entities.ZcglSqjl;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;
import simple.system.simpleweb.platform.util.StringKit;

/**
 * 审批
 * @author yinchao
 * @date 2017年9月7日
 */
@Service
public class ZcglSpService extends AbstractService<ZcglSqjlDao, ZcglSqjl, Long>{

	@Autowired
	public ZcglSpService(ZcglSqjlDao dao) {
		super(dao);
	}
	@Autowired
	private ZcglService zcglService;
	@Autowired
	private UserService userService;
	@Autowired
	private ZcglSqFlowService zcglSqFlowService;
	@Autowired
	private ZcglRkjlService zcglRkjlService;
	@Autowired
	private YhglDao yhglDao;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private ZcglDao zcglDao;
	@Autowired
	private WddbService wddbService;
	@Autowired
	private TgztDao tgztDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private BmdwDao bmdwDao;
	@Autowired
	private SqlxDao sqlxDao;
	@Autowired
	private ZcglsplcDao zcglsplcDao;

	/**
	 * 列表返回grid
	 * @param map
	 * @return
	 * @author yinchao
	 * @date 2017年9月7日
	 */
	public DataGrid grid(Map<String, Object> map) {
		// 时间格式化
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (!"".equals(StringSimple.nullToEmpty(map.get("sqrqs")))) {
			try {
				map.put("sqrqs", sdf1.parseObject(StringSimple.nullToEmpty(map.get("sqrqs"))));
			} catch (ParseException e) {
			}
		}
		if (!"".equals(StringSimple.nullToEmpty(map.get("sqrqe")))) {

			try {
				map.put("sqrqe", sdf1.parseObject(StringSimple.nullToEmpty(map.get("sqrqe")) + " 23:59:59"));
			} catch (ParseException e) {
			}
		}
//		map.put("tgzt", "2");
		/* 权限划分 */
		boolean isCz = false;
		boolean isBgsfzr = false;
		boolean isBgszr = false;
		boolean isFgld = false;
		boolean isJz = false;
		boolean isCw = false;
		List<Role> roles = userService.getCurrentUser().getRoles();
		for (Role role : roles) {
			if("CZ".equals(role.getRoleCode())){
				isCz = true;
			}else if("BGSFZR".equals(role.getRoleCode())){
				isBgsfzr = true;
			}else if("BGSZR".equals(role.getRoleCode())){
				isBgszr = true;
			}else if("FGLD".equals(role.getRoleCode())){
				isFgld = true;
			}else if("JZ".equals(role.getRoleCode())){
				isJz = true;
			}else if("GHCWC".equals(role.getRoleCode())){
				isCw = true;
			}
		}
		Txl txl = yhglDao.getTxlByUser(userService.getCurrentUser().getId());
		String sqbm = null == txl ? "-1" : txl.getBmdw().getCode();//处长所属部门
		String fqbms = null == txl ? "-1" : txl.getFgbms();//分管领导和局长包含的部门
		List<String> fqbmsList = new ArrayList<String>();
		if(fqbms!=null){
			String[] f = fqbms.split(",");
			fqbmsList = Arrays.asList(f);
		}
		List<String> l = new ArrayList<String>();
		String rolecode = "";
		if (isCz && !isBgsfzr && !isCw) {
			l.add("2");
			l.add("3");
			l.add("4");
			l.add("5");
			l.add("6");
			map.put("zcglsplcJz", l);
			map.put("sqbm", sqbm);
			rolecode = "CZ";
		} else if (isFgld && !isJz) {
			l.add("2");
			l.add("4");
			l.add("5");
			l.add("6");
			map.put("zcglsplcJz", l);
			map.put("fqbmsList", fqbmsList);
			rolecode = "FGLD";
		} else if (isBgszr) {
			l.add("2");
			l.add("5");
			l.add("6");
			map.put("zcglsplcJz", l);
			rolecode = "BGSZR";
		} else if (isJz) {
			l.add("2");
			l.add("5");
			l.add("6");
			map.put("zcglsplcJz", l);
			rolecode = "JZ";
		} else if (isBgsfzr){
			l.add("2");
			l.add("7");
			l.add("4");
			l.add("5");
			l.add("6");
			map.put("zcglsplcJz", l);
			map.put("sqbm", sqbm);
			rolecode = "BGSFZR";
		}else if(isCw){
			l.add("2");
			l.add("3");
			l.add("6");
			l.add("8");
			map.put("zcglsplcJz", l);
			rolecode = "CW";
		}
		final String rrolecode = rolecode;
		Page<ZcglSqjl> page = queryForPage(ZcglSqjlDto.class, map);
		return new CommonConvert<ZcglSqjl>(page, new Cci<ZcglSqjl>() {
			@Override
			public Map<String, Object> convertObj(ZcglSqjl t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("sqr", t.getSqr());
				map.put("sqbm", t.getSqbm().getName());
				map.put("sqbmcode", t.getSqbm().getCode());
				map.put("lxdh", t.getLxdh());
				map.put("sqrq", new SimpleDateFormat("yyyy-MM-dd").format(t.getSqrq()));
				map.put("sqlx", null == t.getSqlx() ? "" : t.getSqlx().getName());
				String sqlx = null == t.getSqlx() ? "" : t.getSqlx().getCode();
				map.put("sqlxcode", sqlx);
				map.put("zcmc", t.getZcmc());
				map.put("sl", t.getSl());
				String lcztcode = t.getZcglsplc()==null?"":t.getZcglsplc().getCode();
				map.put("zcglsplc", lcztcode);
				map.put("zcglsplcName", t.getZcglsplc()==null?"":t.getZcglsplc().getName());
				
				if("CZ".equals(rrolecode)){
					if("2".equals(sqlx)){
						map.put("blqk", "3");
					}else{
						if("3".equals(lcztcode)){
							map.put("blqk", "1");
						}else{
							map.put("blqk", "2");
						}
					}
					
				}else if("FGLD".equals(rrolecode)){
					if("2".equals(sqlx)){
						map.put("blqk", "3");
					}else{
						if("4".equals(lcztcode)){
							map.put("blqk", "1");
						}else{
							map.put("blqk", "2");
						}
					}
				}else if("BGSZR".equals(rrolecode)){
					if("5".equals(lcztcode)){
						map.put("blqk", "1");
					}else{
						map.put("blqk", "2");
					}
				}else if("JZ".equals(rrolecode)){
					if("2".equals(sqlx)){
						map.put("blqk", "3");
					}else{
						if("6".equals(lcztcode)){
							map.put("blqk", "1");
						}else{
							map.put("blqk", "2");
						}
					}
				}else if("BGSFZR".equals(rrolecode)){
					if("2".equals(sqlx)){
						map.put("blqk", "3");
					}else{
						if("7".equals(lcztcode)){
							map.put("blqk", "1");
						}else{
							map.put("blqk", "2");
						}
					}
				}else if("CW".equals(rrolecode)){
					if("2".equals(sqlx)){
						map.put("blqk", "3");
					}else{
						if("8".equals(lcztcode)){
							map.put("blqk", "1");
						}else if("3".equals(lcztcode)){
							if("114".equals(t.getSqbm().getCode())){
								map.put("blqk", "1");
							}else{
								map.put("blqk", "2");
							}
						}else{
							map.put("blqk", "2");
						}
					}
				}
				return map;
			}
		}).getDataGrid();
		
		
	}

	/**
	 * 查看
	 * @param id
	 * @return
	 * @author yinchao
	 * @date 2017年9月7日
	 */
	public Map<String, Object> getInfo(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		ZcglSqjl t = getDao().findOne(Long.valueOf(id));
		map.put("id", t.getId());
		map.put("sqr", t.getSqr());
		map.put("sqbm", t.getSqbm().getName());
		map.put("sqrq", null == t.getSqrq() ? "" : new SimpleDateFormat("yyyy-MM-dd").format(t.getSqrq()));
		map.put("lxdh", t.getLxdh());
		map.put("zcmc", t.getZcmc());
		map.put("sl", t.getSl());
		map.put("pp", t.getPp());
		map.put("xh", t.getXh());
		map.put("zcglsplc", null == t.getZcglsplc() ? "" : t.getZcglsplc().getCode());
		map.put("sqlx", null == t.getSqlx() ? "" : t.getSqlx().getName());
		if("1".equals(t.getSqlx().getCode())){
			map.put("yy", t.getSqyy());
		}else{
			map.put("yy", t.getXhyy());
		}
		return map;
	}

	/**
	 * 保存审批结果
	 * @param t
	 * @return
	 * @author yinchao
	 * @date 2017年9月7日
	 */
	public Result doSp(long id, String tgztCode, String suggestion) {
		ZcglSqjl t = getDao().findOne(id);
		t.setSuggestion(suggestion);
		Tgzt ttt = new Tgzt();
		ttt.setCode(tgztCode);
		t.setTgzt(ttt);
		/** 记录流程表 */
		ZcglSqFlow zsf = new ZcglSqFlow();
		zsf.setCzr(userService.getCurrentUser().getRealname());
		zsf.setCzsj(new SimpleDateFormat("yyyy-MM-dd HH:mm:dd").format(new Date()));
		zsf.setZcglSqjlId(t.getId());
		zsf.setSuggestion(suggestion);
		
		ZcglSqjl zcglSqjl = getDao().findOne(t.getId());
		
		Zcglsplc z = new Zcglsplc();
		if("7".equals(t.getZcglsplc().getCode())){
 			zsf.setCzmc("审批");
 			z.setCode("6");
 			zcglSqjl.setZcglsplc(z);
		}else if("3".equals(t.getZcglsplc().getCode())){
			String sqbm = null == t.getSqbm() ? "" :t.getSqbm().getCode();
			if("102".equals(sqbm)||"103".equals(sqbm)||"114".equals(sqbm)||"104".equals(sqbm)||"105".equals(sqbm)||"106".equals(sqbm)){
				zsf.setCzmc("审批");
	 			z.setCode("6");
	 			zcglSqjl.setZcglsplc(z);
			}else{
				zsf.setCzmc("审批");
	 			z.setCode("4");
	 			zcglSqjl.setZcglsplc(z);
			}
 			
		}else if("4".equals(t.getZcglsplc().getCode())){
			zsf.setCzmc("审批");
			z.setCode("5");
			zcglSqjl.setZcglsplc(z);
			//如果通过，保存至待办，发送给资产管理员
		}else if("5".equals(t.getZcglsplc().getCode())){
			zsf.setCzmc("确认");
			if("2".equals(null == t.getSqlx()?"":t.getSqlx().getCode())){
				//销毁
				z.setCode("2");
			}else{
				z.setCode("6");
			}
			zcglSqjl.setZcglsplc(z);
		}else if("6".equals(t.getZcglsplc().getCode())){
			zsf.setCzmc("审批");
			z.setCode("8");
			zcglSqjl.setZcglsplc(z);
		}else if("8".equals(t.getZcglsplc().getCode())){
			zsf.setCzmc("审批");
			z.setCode("2");
			zcglSqjl.setZcglsplc(z);
		}
 		//是否通过
 		Tgzt tgzt = new Tgzt();
		tgzt.setCode(t.getTgzt().getCode());
		zsf.setTgzt(tgztDao.queryByCode(t.getTgzt().getCode()).getName());
		zcglSqjl.setTgzt(tgzt);
 		if("2".equals(zcglSqjl.getTgzt().getCode())){
 			z.setCode("9");
 		}
 		
 		//若流程状态为已审批且通过状态为通过，更新库存数据,记录出库
 		if("2".equals(zcglSqjl.getZcglsplc().getCode())&&"1".equals(zcglSqjl.getTgzt().getCode())){
 			//更新数量
 			Zcgl zcgl = zcglService.getDao().findOne(zcglSqjl.getZcglId());
			zcgl.setSl(zcgl.getSl() - zcglSqjl.getSl());
			zcglService.save(zcgl);
			//插入盘点数据中
			Zcgl newZcgl = zcglDao.queryZcglInfo(zcglSqjl.getSqbm().getCode(),zcglSqjl.getSqrUserName(),t.getZcmc(),t.getPp(),t.getXh());
			if(newZcgl==null){
				Zcgl newZcgl1 = new Zcgl();
				newZcgl1.setZcmc(zcgl.getZcmc());
				newZcgl1.setPp(zcgl.getPp());
				newZcgl1.setXh(zcgl.getXh());
				newZcgl1.setDw(zcgl.getDw());
				newZcgl1.setSl(zcglSqjl.getSl());
				newZcgl1.setYjsysm(zcgl.getYjsysm());
				newZcgl1.setBz(zcgl.getBz());
				Bmdw bm = new Bmdw();
				bm.setCode(zcglSqjl.getSqbm().getCode());
				newZcgl1.setBm(bm);
				newZcgl1.setSyr(zcglSqjl.getSqrUserName());;
				zcglService.save(newZcgl1);
			}else{
				newZcgl.setSl(newZcgl.getSl()+zcglSqjl.getSl());
				zcglService.save(newZcgl);
			}
			
			ZcglRkjl zcglRkjl = new ZcglRkjl();
			Crk crk = new Crk();
			crk.setCode("2");//出库
			zcglRkjl.setCrk(crk);
			zcglRkjl.setZcglId(zcgl.getId());
			zcglRkjl.setDate(new Date());
			zcglRkjl.setPp(zcgl.getPp());
			zcglRkjl.setSl(t.getSl());
			zcglRkjl.setDw(zcgl.getDw());
			zcglRkjl.setUsername(t.getSqrUserName());
			zcglRkjlService.save(zcglRkjl);
			
 		}
 		zcglSqFlowService.save(zsf);
		save(zcglSqjl);
		return new Result(true);
	}

	/**
	 * 局长grid
	 * @param map
	 * @return
	 * @author yinchao
	 * @date 2017年9月22日
	 */
	public DataGrid jzGrid(Map<String, Object> map) {
		String a = " ";
		if(!"".equals(StringKit.toString(map.get("zcmc")))){
			a += " AND t_zcgl_sqjl.zcmc like '%"+StringKit.toString(map.get("zcmc"))+"%'";
		}
		String sqrqs = StringKit.toString(map.get("sqrqs"))+" 00:00:00";
		String sqrqe = StringKit.toString(map.get("sqrqe"))+" 23:59:59";
		if (!"".equals(StringSimple.nullToEmpty(map.get("sqrqs")))) {
			a += " AND  t_zcgl_sqjl.sqrq >=  '"+sqrqs+"'";
		}
		if (!"".equals(StringSimple.nullToEmpty(map.get("sqrqe")))) {
			a += " AND  t_zcgl_sqjl.sqrq <=  '"+sqrqe+"'";
		}
		//当前页面、每页数据量
		int page = Integer.parseInt((String) map.get("page"));
		int rows = Integer.parseInt((String) map.get("rows"));
		Txl txl = yhglDao.getTxlByUser(userService.getCurrentUser().getId());
		String fqbms = null == txl ? "-1" : txl.getFgbms();//分管领导和局长包含的部门
		String sql = "SELECT * FROM ( SELECT RowNumber = ROW_NUMBER() OVER (ORDER BY id ASC),* FROM t_zcgl_sqjl WHERE (t_zcgl_sqjl.zcglsplc_code = '2' OR t_zcgl_sqjl.zcglsplc_code = '6' OR ( t_zcgl_sqjl.zcglsplc_code = '4' AND t_zcgl_sqjl.sqbm_code IN ("+fqbms+"))) "+a+") a"
				   + " WHERE a.RowNumber >= "+page+" AND a.RowNumber <="+rows+" ORDER BY create_time DESC ";
		//查询总量
		String total_sql = "SELECT count(*) total "
					+ "FROM t_zcgl_sqjl WHERE t_zcgl_sqjl.zcglsplc_code = '2' OR t_zcgl_sqjl.zcglsplc_code = '6' OR ( t_zcgl_sqjl.zcglsplc_code = '4' AND t_zcgl_sqjl.sqbm_code IN ("+fqbms+"))";
//		System.out.println(sql);
		MapSqlParameterSource parameters = new MapSqlParameterSource(); 
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		List<Map<String, Object>> queryForList = namedParameterJdbcTemplate.queryForList(sql, parameters);
		List<Map<String, Object>> total = namedParameterJdbcTemplate.queryForList(total_sql, parameters);
		//datagrid的数据源
		List<Map<String, Object>> ja = new ArrayList<Map<String,Object>>();
		for(Map<String, Object>  tmap : queryForList){
			Map<String, Object> jo = new HashMap<String, Object>();
			jo.put("id", tmap.get("id"));
			jo.put("sqr", tmap.get("sqr"));
			Bmdw sqbm = bmdwDao.queryByCode(StringKit.toString(tmap.get("sqbm_code")));
			jo.put("sqbm", sqbm.getName());
			jo.put("lxdh", tmap.get("lxdh"));
			jo.put("sqrq", new SimpleDateFormat("yyyy-MM-dd").format(tmap.get("sqrq")));
			Sqlx sqlx = sqlxDao.queryByCode(StringKit.toString(tmap.get("sqlx_code")));
			jo.put("sqlx", sqlx.getName());
			jo.put("zcmc", tmap.get("zcmc"));
			jo.put("sl", tmap.get("sl"));
			Zcglsplc zcglsplc = zcglsplcDao.queryByCode(StringKit.toString(tmap.get("zcglsplc_code")));
			jo.put("zcglsplc", zcglsplc.getCode());
			jo.put("zcglsplcName", zcglsplc.getName());
			ja.add(jo);
		}
		DataGrid dg = new DataGrid();
		dg.setRows(ja);
		String totalStr = StringKit.toString(total.get(0).get("total"));
		dg.setTotal(Long.parseLong(totalStr));
		return dg;
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
		List<ZcglSqFlow> list = zcglSqFlowService.getDao().queryByZcglSqjlId(Long.parseLong(id));
		for (ZcglSqFlow t : list) {
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("pUserName", t.getCzr());
			m.put("cnode", t.getCzmc());
			m.put("czsj", t.getCzsj());
			m.put("tgztName", t.getTgzt());
			m.put("suggestion", t.getSuggestion());
			res.add(m);
		}
		return res;
	}
}
