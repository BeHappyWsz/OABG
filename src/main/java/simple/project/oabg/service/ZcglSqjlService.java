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
import simple.project.communal.util.Utils;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.dao.ZcglSqjlDao;
import simple.project.oabg.dic.model.Sqlx;
import simple.project.oabg.dic.model.Tgzt;
import simple.project.oabg.dic.model.Zcglsplc;
import simple.project.oabg.dto.ZcglSqjlDto;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.entities.Zcgl;
import simple.project.oabg.entities.ZcglSqFlow;
import simple.project.oabg.entities.ZcglSqjl;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;

/**
 * 资产管理申请记录
 * @author yinchao
 * @date 2017年9月6日
 */
@Service
public class ZcglSqjlService extends AbstractService<ZcglSqjlDao, ZcglSqjl, Long>{

	@Autowired
	public ZcglSqjlService(ZcglSqjlDao dao) {
		super(dao);
	}
	@Autowired
	private UserService userService;
	@Autowired
	private ZcglService zcglService;
	@Autowired
	private YhglDao yhglDao;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private ZcglSqFlowService zcglSqFlowService;
	@Autowired
	private Txl_service txl_service;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 查询返回grid
	 * @param map
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	public DataGrid gridLy(Map<String, Object> map) {
		map.put("sqlx", "1");
		map.put("sqrUserName", userService.getCurrentUser().getUsername());
		//时间格式化
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!"".equals(StringSimple.nullToEmpty(map.get("sqrqs")))){
			try {
				map.put("sqrqs", sdf1.parseObject(StringSimple.nullToEmpty(map.get("sqrqs"))));
			} catch (ParseException e) {
			}
		}
		if(!"".equals(StringSimple.nullToEmpty(map.get("sqrqe")))){
			
			try {
				map.put("sqrqe", sdf1.parseObject(StringSimple.nullToEmpty(map.get("sqrqe"))+" 23:59:59"));
			} catch (ParseException e) {
			}
		}
		Page<ZcglSqjl> page = queryForPage(ZcglSqjlDto.class, map);
		return new CommonConvert<ZcglSqjl>(page, new Cci<ZcglSqjl>() {
			@Override
			public Map<String, Object> convertObj(ZcglSqjl t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("sqr", t.getSqr());
				map.put("sqbm", t.getSqbm()==null?"":t.getSqbm().getName());
				map.put("lxdh", t.getLxdh());
				map.put("sqrq", sdf.format(t.getSqrq()));
				map.put("zcmc", t.getZcmc());
				map.put("sl", t.getSl());
				map.put("zt", t.getZcglsplc().getName());
				map.put("ztCode", t.getZcglsplc().getCode());
				return map;
			}
		}).getDataGrid();
	}

	/**
	 * 保存新增
	 * @param t
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	public Result saveObjLy(ZcglSqjl t) {
		Txl txl = txl_service.getDao().getTxlByUser(userService.getCurrentUser().getId());
		Zcglsplc zcglsplc = new Zcglsplc();
		if ("102".equals(txl.getBmdw().getCode())) {
			zcglsplc.setCode("7");//办公室无处长，办公室负责人审批
		}else{
			zcglsplc.setCode("3");
		}
		t.setZcglsplc(zcglsplc);
		Sqlx sqlx = new Sqlx();
		sqlx.setCode("1");
		t.setSqlx(sqlx);
		Tgzt tgzt = new Tgzt();
		tgzt.setCode("1");
		t.setTgzt(tgzt);
		t.setSqrq(new Date());
		t.setSqrUserName(userService.getCurrentUser().getName());
		ZcglSqjl z = save(t);
		/** 记录流程表 */
		ZcglSqFlow zsf = new ZcglSqFlow();
		zsf.setCzr(userService.getCurrentUser().getRealname());
		zsf.setCzsj(new SimpleDateFormat("yyyy-MM-dd HH:mm:dd").format(new Date()));
		zsf.setCzmc("提交");
		zsf.setZcglSqjlId(z.getId());
		zcglSqFlowService.save(zsf);
		return new Result(true);
	}

	/**
	 * 删除
	 * @param ids
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	public Result deleteByIds(String ids) {
		List<Long> idList = Utils.stringToLongList(ids, ",");
		logicDelete(idList);
		return new Result(true);
	}

	public DataGrid gridXh(Map<String, Object> map) {
		map.put("sqlx", "2");
		map.put("sqrUserName", userService.getCurrentUser().getUsername());
		//时间格式化
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!"".equals(StringSimple.nullToEmpty(map.get("sqrqs")))){
			try {
				map.put("sqrqs", sdf1.parseObject(StringSimple.nullToEmpty(map.get("sqrqs"))));
			} catch (ParseException e) {
			}
		}
		if(!"".equals(StringSimple.nullToEmpty(map.get("sqrqe")))){
			
			try {
				map.put("sqrqe", sdf1.parseObject(StringSimple.nullToEmpty(map.get("sqrqe"))+" 23:59:59"));
			} catch (ParseException e) {
			}
		}
		Page<ZcglSqjl> page = queryForPage(ZcglSqjlDto.class, map);
		return new CommonConvert<ZcglSqjl>(page, new Cci<ZcglSqjl>() {
			@Override
			public Map<String, Object> convertObj(ZcglSqjl t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("sqr", t.getSqr());
				map.put("sqbm", t.getSqbm()==null?"":t.getSqbm().getName());
				map.put("lxdh", t.getLxdh());
				map.put("sqrq", sdf.format(t.getSqrq()));
				map.put("zcmc", t.getZcmc());
				map.put("sl", t.getSl());
				map.put("zt", t.getZcglsplc().getName());
				map.put("ztCode", t.getZcglsplc().getCode());
				return map;
			}
		}).getDataGrid();
	}

	public Result saveObjXh(ZcglSqjl t) {
		Txl txl = txl_service.getDao().getTxlByUser(userService.getCurrentUser().getId());
		Zcglsplc zcglsplc = new Zcglsplc();
		
		System.out.println(txl.getBmdw().getCode());
		if ("102".equals(txl.getBmdw().getCode())) {
			zcglsplc.setCode("5");//办公室无处长，待办公室负责人审批
		}else{
			zcglsplc.setCode("5");
		}
		t.setZcglsplc(zcglsplc);
		Sqlx sqlx = new Sqlx();
		sqlx.setCode("2");
		t.setSqlx(sqlx);
		Tgzt tgzt = new Tgzt();
		tgzt.setCode("1");
		t.setTgzt(tgzt);
		t.setSqrq(new Date());
		t.setSqrUserName(userService.getCurrentUser().getName());
		ZcglSqjl z = save(t);
		/** 记录流程表 */
		ZcglSqFlow zsf = new ZcglSqFlow();
		zsf.setCzr(userService.getCurrentUser().getRealname());
		zsf.setCzsj(new SimpleDateFormat("yyyy-MM-dd HH:mm:dd").format(new Date()));
		zsf.setCzmc("销毁");
		zsf.setZcglSqjlId(z.getId());
		zcglSqFlowService.save(zsf);
		return new Result(true);
	}

	/**
	 * 查看
	 * @param id
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	public Map<String, Object> getInfoLy(String id) {
		Map<String,Object> map=new HashMap<String, Object>();
		if("".equals(StringSimple.nullToEmpty(id))){
			map.putAll(getCurUserInfo());
		}else{
			ZcglSqjl t=getDao().findOne(Long.valueOf(id));
			map.put("id", t.getId());
			map.put("sqr", t.getSqr());
			map.put("sqrq", null == t.getSqrq() ? "" : new SimpleDateFormat("yyyy-MM-dd").format(t.getSqrq()));
			map.put("sqbm", t.getSqbm().getCode());
			map.put("lxdh", t.getLxdh());
			map.put("zcmc", t.getZcmc());
			map.put("sl", t.getSl());
			map.put("pp", t.getPp());
			map.put("lcCode", null == t.getZcglsplc() ? "" : t.getZcglsplc().getCode());
			map.put("xh", t.getXh());
			map.put("sqyy", t.getSqyy());
			map.put("zcglId", t.getZcglId());
			Zcgl zcgl = zcglService.getDao().findOne(t.getZcglId());
			map.put("leftnum", null == zcgl ? "" : zcgl.getSl());
		}
		return map;
	}

	/**
	 * 查看
	 * @param id
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	public Map<String, Object> getInfoXh(String id) {
		Map<String,Object> map=new HashMap<String, Object>();
		if("".equals(StringSimple.nullToEmpty(id))){
			map.putAll(getCurUserInfo());
		}else{
			ZcglSqjl t=getDao().findOne(Long.valueOf(id));
			map.put("id", t.getId());
			map.put("sqr", t.getSqr());
			map.put("sqrq", null == t.getSqrq() ? "" : new SimpleDateFormat("yyyy-MM-dd").format(t.getSqrq()));
			map.put("sqbm", t.getSqbm().getCode());
			map.put("lxdh", t.getLxdh());
			map.put("zcmc", t.getZcmc());
			map.put("sl", t.getSl());
			map.put("pp", t.getPp());
			map.put("xh", t.getXh());
			map.put("xhyy", t.getXhyy());
			map.put("zcglId", t.getZcglId());
			map.put("lcCode", null == t.getZcglsplc() ? "" : t.getZcglsplc().getCode());
			Zcgl zcgl = zcglService.getDao().findOne(t.getZcglId());
			map.put("leftnum", null == zcgl ? "" : zcgl.getSl());
		}		
		return map;
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
			info.put("lxdh","".equals(StringSimple.nullToEmpty(t.getBgdh()))?t.getMobile():t.getBgdh());
		}
		info.put("sqrq", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		return info;
	}
	
}
