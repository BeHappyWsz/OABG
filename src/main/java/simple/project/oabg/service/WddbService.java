package simple.project.oabg.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import simple.project.oabg.dao.SwngFlowDao;
import simple.project.oabg.dao.WddbDao;
import simple.project.oabg.dic.model.SF;
import simple.project.oabg.dic.model.Wddbsjly;
import simple.project.oabg.dto.WddbDto;
import simple.project.oabg.entities.BgypSqjl;
import simple.project.oabg.entities.Fwng;
import simple.project.oabg.entities.Gwjdsq;
import simple.project.oabg.entities.Gzsq;
import simple.project.oabg.entities.Hypxsq;
import simple.project.oabg.entities.Kqgl;
import simple.project.oabg.entities.LwypSqjl;
import simple.project.oabg.entities.SbwxSqjl;
import simple.project.oabg.entities.Swng;
import simple.project.oabg.entities.SwngFlow;
import simple.project.oabg.entities.Wddb;
import simple.project.oabg.entities.WdjySqjl;
import simple.project.oabg.entities.Xxbs;
import simple.project.oabg.entities.ZcglSqjl;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;

/**
 * 我的待办
 * 2017年9月8日
 * @author yc
 */
@Service
public class WddbService extends AbstractService<WddbDao, Wddb, Long>{
	
	@Autowired
	private UserService userService;
	@Autowired
	private Swgl_swng_service swgl_swng_service;
	@Autowired
	private Qjsq_service qjsq_service;
	@Autowired
	private Ccsq_service ccsq_service;
	@Autowired
	private Gzsq_service gzsq_service;
	@Autowired
	private LwypSqjlService lwypSqjlService;
	@Autowired
	private BgypSqjlService bgypSqjlService;
	@Autowired
	private ZcglSqjlService zcglSqjlService;
	@Autowired
	private ZcglSpService zcglSpService;
	@Autowired
	private FwngService fwngService;
	@Autowired
	private Dagl_jysh_service dagl_jysh_service;
	@Autowired
	private SwngFlowDao swngFlowDao;
	@Autowired
	private Swgl_tssw_service swgl_tssw_service;
	@Autowired
	private GwjdsqService gwjdsqService;
	@Autowired
	private HypxsqService hypxsqService;
	@Autowired
	private SbwxSqService sbwxSqService;
	@Autowired
	private XxbsService xxbsService;
	@Autowired
	public WddbService(WddbDao dao) {
		super(dao);
	}
	
	/**
	 * 数据列表
	 * 2017年9月5日
	 * yc
	 * @param map
	 * @return
	 */
	public DataGrid grid(Map<String,Object> map){
		String curUserName = userService.getCurrentUser().getUsername();
		map.put("sjr", "#"+curUserName+"#");
		//查询grid前刷新状态
		Map<String,Object> tmap1 = new HashMap<String,Object>();
		tmap1.put("sfyb", "2");
		tmap1.put("sjr", map.get("sjr"));
		List<Wddb> list1 = query(WddbDto.class, tmap1);
		for (Wddb wddb : list1) {
			changeIfHasBl(wddb);
		}
		Map<String,Object> tmap2 = new HashMap<String,Object>();
		tmap2.put("sfxh", "2");
		tmap2.put("sjr", map.get("sjr"));
		List<Wddb> list2 = query(WddbDto.class, tmap2);
		for (Wddb wddb : list2) {
			changeIfHasBl(wddb);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!"".equals(StringSimple.nullToEmpty(map.get("sjsj_s")))){
			try {
				map.put("sjsj_s", sdf.parse(StringSimple.nullToEmpty(map.get("sjsj_s"))+" 00:00:00"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(!"".equals(StringSimple.nullToEmpty(map.get("sjsj_e")))){
			try {
				map.put("sjsj_e", sdf.parse(StringSimple.nullToEmpty(map.get("sjsj_e"))+" 23:59:59"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		Page<Wddb> page = queryForPage(WddbDto.class, map);
		return new CommonConvert<Wddb>(page, new Cci<Wddb>() {
			@Override
			public Map<String, Object> convertObj(Wddb t) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("glid", t.getGlid());
				map.put("title", t.getTitle());
				map.put("sjsj", null == t.getSjsj() ? "" : sdf.format(t.getSjsj()));
				map.put("blsj", null == t.getBlsj() ? "" : sdf.format(t.getBlsj()));
				map.put("sjly",null == t.getSjly() ? "" : t.getSjly().getCode());
				map.put("sjlyName", null == t.getSjly() ? "" : t.getSjly().getName());
				map.put("lczt", t.getLczt());
				map.put("sfyb", null == t.getSfyb() ? "" : t.getSfyb().getCode());
				map.put("sfybName", null == t.getSfyb() ? "" : t.getSfyb().getName());
				map.put("sfxh", null == t.getSfxh() ? "" : t.getSfxh().getCode());
				map.put("sfxhName", null == t.getSfxh() ? "" : t.getSfxh().getName());
				return map;
			}
		}).getDataGrid();
	}
	
	/**
	 * 校验该id是否已经办理，如果已办理，则修改状态
	 * 2017年9月9日
	 * yc
	 * @param id
	 * @return
	 */
	public Result changeIfHasBl(Wddb t ){
		if(null == t)
			return new Result(false);
		String sfxhy = null == t.getSfxh() ? "" : t.getSfxh().getCode();
		if("1".equals(sfxhy)){
			return new Result(false);
		}
		String sfxh = "";
		String sfyby = null == t.getSfyb() ? "" : t.getSfyb().getCode();
		String sfyb = "";
		String sjly = null == t.getSjly() ? "" : t.getSjly().getCode();
		if("10".equals(sjly)){
			Swng swng = swgl_swng_service.getDao().getOne(t.getGlid());
			if(null == swng || swng.getDeleted()){
				sfxh = "1";
			}else{
				if("2".equals(t.getLczt())){
					List<SwngFlow> sfs = swngFlowDao.queryBySwngId(swng.getId().toString());
					String username = userService.getCurrentUser().getUsername();
					int tj = 0;
					int py = 0;
					for(SwngFlow sf : sfs){
						if("提交".equals(sf.getCnode())){
							tj ++;
						}
						if(username.equals(sf.getPuser()) && "批阅".equals(sf.getCnode())){
							py ++;
						}
					}
					System.out.println("用户名："+username+"，提交数："+tj+"，批阅数："+py);
					if(tj <= py){
						sfyb = "1";
					}
				}else{
					if(!t.getLczt().equals(StringSimple.nullToEmpty(null == swng.getSwzt() ? "" : swng.getSwzt().getCode()))){
						sfyb = "1";
					}
				}
				
			}
			
		}else if("20".equals(sjly)){
			Kqgl kqgl = qjsq_service.getDao().getOne(t.getGlid());
			if(null == kqgl || kqgl.getDeleted()){
				sfxh = "1";
			}else{
				String lcztcode = null == kqgl ? "" : (null == kqgl.getLczt() ? "" : kqgl.getLczt().getCode());
				if(!lcztcode.equals(t.getLczt())){
					sfyb = "1";
				}
			}
		}else if("30".equals(sjly)){
			Kqgl kqgl = ccsq_service.getDao().getOne(t.getGlid());
			if(null == kqgl || kqgl.getDeleted()){
				sfxh = "1";
			}else{
				String lcztcode = null == kqgl ? "" : (null == kqgl.getLczt() ? "" : kqgl.getLczt().getCode());
				if(!lcztcode.equals(t.getLczt())){
					sfyb = "1";
				}
			}
		}else if("40".equals(sjly)){
			ZcglSqjl sqjl = zcglSqjlService.getDao().getOne(t.getGlid());
			if(null == sqjl|| sqjl.getDeleted()){
				sfxh = "1";
			}else{
				String lcztcode = null == sqjl ? "" : (null == sqjl.getZcglsplc() ? "" : sqjl.getZcglsplc().getCode());
				if(!lcztcode.equals(t.getLczt())){
					sfyb = "1";
				}
			}
		}else if("50".equals(sjly)){
			BgypSqjl sqjl = bgypSqjlService.getDao().getOne(t.getGlid());
			if(null == sqjl || sqjl.getDeleted()){
				sfxh = "1";
			}else{
				String lcztcode = null == sqjl ? "" : (null == sqjl.getLczt() ? "" : sqjl.getLczt().getCode());
				if(!lcztcode.equals(t.getLczt())){
					sfyb = "1";
				}
			}
		}else if("60".equals(sjly)){
			LwypSqjl sqjl = lwypSqjlService.getDao().getOne(t.getGlid());
			if(null == sqjl || sqjl.getDeleted()){
				sfxh = "1";
			}else{
				String lcztcode = null == sqjl ? "" : (null == sqjl.getLczt() ? "" : sqjl.getLczt().getCode());
				if(!lcztcode.equals(t.getLczt())){
					sfyb = "1";
				}
			}
		}else if("70".equals(sjly)){
			Gzsq gzsq = gzsq_service.getDao().getOne(t.getGlid());
			if(null == gzsq || gzsq.getDeleted()){
				sfxh = "1";
			}else{
				String lcztcode = null == gzsq ? "" : (null == gzsq.getLczt() ? "" : gzsq.getLczt().getCode());
				if(!lcztcode.equals(t.getLczt())){
					sfyb = "1";
				}
			}
		}else if("80".equals(sjly)){
			WdjySqjl sqjl = dagl_jysh_service.getDao().getOne(t.getGlid());
			if(null == sqjl || sqjl.getDeleted()){
				sfxh = "1";
			}else{
				String lcztcode = null == sqjl ? "" : (null == sqjl.getLczt() ? "" : sqjl.getLczt().getCode());
				if(!lcztcode.equals(t.getLczt())){
					sfyb = "1";
				}
			}
		}else if("90".equals(sjly)){
			Fwng fwng = fwngService.getDao().getOne(t.getGlid());
			if(null == fwng || fwng.getDeleted()){
				sfxh = "1";
			}else{
				String lcztcode = null == fwng ? "" : (null == fwng.getFwzt() ? "" : fwng.getFwzt().getCode());
				if(!lcztcode.equals(t.getLczt())){
					sfyb = "1";
				}
			}
		}else if("100".equals(sjly)){
			Swng swng = swgl_tssw_service.getDao().getOne(t.getGlid());
			if(null == swng || swng.getDeleted()){
				sfxh = "1";
			}else{
				if("2".equals(StringSimple.nullToEmpty(t.getLczt()))){
					List<SwngFlow> sfs = swngFlowDao.queryBySwngId(swng.getId().toString());
					String username = userService.getCurrentUser().getUsername();
					int tj = 0;
					int py = 0;
					for(SwngFlow sf : sfs){
						if("提交".equals(sf.getCnode())){
							tj ++;
						}
						if(username.equals(sf.getPuser()) && "回复".equals(sf.getCnode())){
							py ++;
						}
					}
					if(tj <= py){
						sfyb = "1";
					}
				}else{
					if(!t.getLczt().equals(null == swng.getTsswzt() ? "" : swng.getTsswzt().getCode())){
						sfyb = "1";
					}
				}
				
			}
		}else if("110".equals(sjly)){
			Gwjdsq sq = gwjdsqService.getDao().getOne(t.getGlid());
			if(null == sq || sq.getDeleted()){
				sfxh = "1";
			}else{
				String lcztcode = null == sq ? "" : (null == sq.getLczt() ? "" : sq.getLczt().getCode());
				if(!lcztcode.equals(t.getLczt())){
					sfyb = "1";
				}
			}
		}else if("120".equals(sjly)){
			Hypxsq sq = hypxsqService.getDao().getOne(t.getGlid());
			if(null == sq || sq.getDeleted()){
				sfxh = "1";
			}else{
				String lcztcode = null == sq ? "" : (null == sq.getLczt() ? "" : sq.getLczt().getCode());
				if(!lcztcode.equals(t.getLczt())){
					sfyb = "1";
				}
			}
		}else if("130".equals(sjly)){
			SbwxSqjl sq = sbwxSqService.getDao().getOne(t.getGlid());
			if(null == sq || sq.getDeleted()){
				sfxh = "1";
			}else{
				String lcztcode = null == sq ? "" : (null == sq.getSbwxLczt() ? "" : sq.getSbwxLczt().getCode());
				if(!lcztcode.equals(t.getLczt())){
					sfyb = "1";
				}
			}
		}else if("140".equals(sjly)){
			Xxbs bs = xxbsService.getDao().getOne(t.getGlid());
			if(null == bs || bs.getDeleted()){
				sfxh = "1";
			}else{
				String lcztcode = null == bs ? "" : (null == bs.getLczt() ? "" : bs.getLczt().getCode());
				if(!lcztcode.equals(t.getLczt())){
					sfyb = "1";
				}
			}
		}
		if("1".equals(sfxh) && !"1".equals(sfxhy)){
			//已销毁
			SF sf = new SF();
			sf.setCode("1");
			t.setSfxh(sf);
			save(t);
			return new Result(true);
		}else{
			//未销毁
			//如果已经办理则改变状态
			if("1".equals(sfyb) && !"1".equals(sfyby)){
				SF sf = new SF();
				sf.setCode(sfyb);
				t.setSfyb(sf);
				t.setBlsj(new Date());
				save(t);
				return new Result(true);
			}
		}
			
		return new Result(false);
	}
	
	/**
	 * 根据id获取详情
	 * 2017年9月5日
	 * yc
	 * @param id
	 * @return
	 */
	public Map<String,Object> getInfo(String id){
		Wddb t=getDao().findOne(Long.valueOf(id));
		Map<String,Object> map=new HashMap<String, Object>();
		String sjly = t.getSjly().getCode();
		if("10".equals(sjly)){
			map.putAll(swgl_swng_service.getInfo(t.getGlid()));
		}else if("40".equals(sjly)){
			map.putAll(zcglSpService.getInfo(StringSimple.nullToEmpty(t.getGlid())));
		}else if("80".equals(sjly)){
			map.putAll(dagl_jysh_service.getInfo(t.getGlid()));
		}else if("90".equals(sjly)){
			map.putAll(fwngService.getInfo(String.valueOf(t.getGlid())));
		}else if("100".equals(sjly)){
			map.putAll(swgl_tssw_service.getInfo(t.getGlid()));
		}
		return map;
	}
	
	/**
	 * 新增我的待办
	 * 2017年9月8日
	 * yc
	 * @param id 原id
	 * @param sjlycode 数据来源 10:收文 20:请假 30:出差
	 * @param title 标题
	 * @param sjr 收件人 #username1#username2#
	 * @param lcztcode 流程状态
	 * @return
	 */
	public Result saveNewWddb(Long id,String sjlycode,String title,String sjr,String lcztcode){
		Wddb t = new Wddb();
		Wddbsjly sjly = new Wddbsjly();
		sjly.setCode(sjlycode);
		t.setSjly(sjly);
		t.setTitle(title);
		t.setGlid(id);
		t.setSjr(sjr);
		t.setLczt(lcztcode);
		SF sf = new SF();
		sf.setCode("2");
		t.setSfxh(sf);
		SF sfyb = new SF();
		sfyb.setCode("2");
		t.setSfyb(sfyb);
		t.setSjsj(new Date());
		save(t);
		return new Result(true);
	}
	
}
