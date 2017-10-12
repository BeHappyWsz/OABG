package simple.project.oabg.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import simple.base.utils.StringSimple;
import simple.project.communal.convert.Cci;
import simple.project.communal.convert.CommonConvert;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.dao.ZcglDao;
import simple.project.oabg.dic.model.Crk;
import simple.project.oabg.dic.model.SF;
import simple.project.oabg.dto.ZcglDto;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.entities.Zcgl;
import simple.project.oabg.entities.ZcglRkjl;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;

/**
 * 资产管理
 * @author yinchao
 * @date 2017年9月6日
 */
@Service
public class ZcglService extends AbstractService<ZcglDao, Zcgl, Long>{

	@Autowired
	public ZcglService(ZcglDao dao) {
		super(dao);
	}
	
	@Autowired
	private UserService userService;
	@Autowired
	private ZcglRkjlService zcglRkjlService;
	@Autowired
	private YhglDao yhglDao;

	/**
	 * 查询返回grid列表
	 * @param map
	 * @return
	 * @author yinchao
	 * @date 2017年9月6日
	 */
	public DataGrid grid(Map<String, Object> map) {
		Page<Zcgl> page = queryForPage(ZcglDto.class, map);
		return new CommonConvert<Zcgl>(page, new Cci<Zcgl>() {
			@Override
			public Map<String, Object> convertObj(Zcgl t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("zcmc", t.getZcmc());
				map.put("pp", t.getPp());
				map.put("xh", t.getXh());
				map.put("bm", t.getBm()==null?"":t.getBm().getName());
				map.put("syr", t.getSyr());
				map.put("sl", t.getSl());
				map.put("dw", t.getDw());
				return map;
			}
		}).getDataGrid();
	}
	
	/**
	 * 领用grid
	 * @param map
	 * @return
	 * @author yinchao
	 * @date 2017年9月8日
	 */
	public DataGrid gridLy(Map<String, Object> map) {
		map.put("syrLy", " ");
		Page<Zcgl> page = queryForPage(ZcglDto.class, map);
		return new CommonConvert<Zcgl>(page, new Cci<Zcgl>() {
			@Override
			public Map<String, Object> convertObj(Zcgl t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("zcmc", t.getZcmc());
				map.put("pp", t.getPp());
				map.put("xh", t.getXh());
				map.put("bm", t.getBm()==null?"":t.getBm().getName());
				map.put("syr", t.getSyr());
				map.put("sl", t.getSl());
				map.put("dw", t.getDw());
				return map;
			}
		}).getDataGrid();
	}
	
	/**
	 * 销毁grid
	 * @param map
	 * @return
	 * @author yinchao
	 * @date 2017年9月8日
	 */
	public DataGrid gridXh(Map<String, Object> map) {
		map.put("syrXh", " ");
		Page<Zcgl> page = queryForPage(ZcglDto.class, map);
		return new CommonConvert<Zcgl>(page, new Cci<Zcgl>() {
			@Override
			public Map<String, Object> convertObj(Zcgl t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("zcmc", t.getZcmc());
				map.put("pp", t.getPp());
				map.put("xh", t.getXh());
				map.put("bm", t.getBm()==null?"":t.getBm().getName());
				map.put("syr", t.getSyr());
				map.put("sl", t.getSl());
				map.put("dw", t.getDw());
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
	public Result saveObj(Zcgl t) {
		ZcglRkjl z = new ZcglRkjl();
		Crk crk = new Crk();
		SF sfxh = new SF();//是否销毁 1是 2否  默认2
		sfxh.setCode("2");
		t.setSfxh(sfxh);
		if(t.getId()!=null){
			Zcgl zcglXg = getDao().findOne(t.getId());
			z.setSl(t.getSl()-zcglXg.getSl());
		}else{
			z.setSl(t.getSl());
		}
		save(t);
		//判断是盘点还是入库
		if(t.getBm()==null){
			crk.setCode("1");//入库
		}else{
			crk.setCode("5");//盘点
		}
		z.setCrk(crk);
		z.setZcglId(t.getId());
		z.setDate(new Date());
		z.setPp(t.getPp());
		z.setDw(t.getDw());
		z.setUsername(userService.getCurrentUser().getRealname());
		zcglRkjlService.save(z);
		return new Result(true);
	}
	
	/**
	 * 查看
	 * @param id
	 * @return
	 * @author yinchao
	 * @date 2017年9月7日
	 */
	public Map<String, Object> getInfo(String id) {
		Map<String,Object> map=new HashMap<String, Object>();
		if("".equals(StringSimple.nullToEmpty(id))){
			map.putAll(getCurUserInfo());
		}else{
			Zcgl t=getDao().findOne(Long.valueOf(id));
			map.put("id", t.getId());
			map.put("zcmc", t.getZcmc());
			map.put("pp", t.getPp());
			map.put("xh", t.getXh());
			map.put("yjsysm", t.getYjsysm());
			map.put("dw", t.getDw());
			map.put("sl", t.getSl());
			map.put("bm", t.getBm()==null?"":t.getBm().getName());
			map.put("syr", t.getSyr());
			map.put("bz", t.getBz());
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
			info.put("bm",null ==  t.getBmdw() ? "" : t.getBmdw().getCode());
			info.put("syr",t.getRealname());
			info.put("lxdh","".equals(StringSimple.nullToEmpty(t.getBgdh()))?t.getMobile():t.getBgdh());
		}
		info.put("sqrq", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		return info;
	}

}
