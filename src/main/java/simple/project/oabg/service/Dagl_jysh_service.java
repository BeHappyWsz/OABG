package simple.project.oabg.service;

import java.text.ParseException;
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
import simple.project.oabg.dao.WdjySqFlowDao;
import simple.project.oabg.dao.WdjySqjlDao;
import simple.project.oabg.dic.model.Gh;
import simple.project.oabg.dic.model.Tgzt;
import simple.project.oabg.dic.model.Tylc;
import simple.project.oabg.dto.WdjySqjlDto;
import simple.project.oabg.entities.WdjySqFlow;
import simple.project.oabg.entities.WdjySqjl;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;

/**
 * 借阅审核
 * @author yinchao
 * @date 2017年9月9日
 */
@Service
public class Dagl_jysh_service  extends AbstractService<WdjySqjlDao, WdjySqjl, Long>{
	
	@Autowired
	public Dagl_jysh_service(WdjySqjlDao dao) {
		super(dao);
	}
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private UserService userService;
	@Autowired
	private WdjySqFlowDao wdjySqFlowDao;

	/**
	 * 待审核grid
	 * @param map
	 * @return
	 * @author yinchao
	 * @date 2017年9月9日
	 */
	public DataGrid grid(Map<String, Object> map) {
		if(!"".equals(StringSimple.nullToEmpty(map.get("sqrq_s")))){
			try {
				map.put("sqrq_s", sdf.parseObject(StringSimple.nullToEmpty(map.get("sqrq_s"))+" 00:00:00"));
			} catch (ParseException e) {}
		}
		if(!"".equals(StringSimple.nullToEmpty(map.get("sqrq_e")))){
			try {
				map.put("sqrq_e", sdf.parseObject(StringSimple.nullToEmpty(map.get("sqrq_e"))+" 23:59:59"));
			} catch (ParseException e) {}
		}
//		map.put("tg", " ");
		Page<WdjySqjl> page = queryForPage(WdjySqjlDto.class, map);
		return new CommonConvert<WdjySqjl>(page, new Cci<WdjySqjl>() {
			@Override
			public Map<String, Object> convertObj(WdjySqjl t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("sqr", t.getSqr());
				map.put("sqbm", t.getSqbm());
				map.put("lxdh", t.getLxdh());
				map.put("sqrq",null == t.getSqrq() ? "" :new SimpleDateFormat("yyyy-MM-dd").format(t.getSqrq()));
				map.put("wdbt", t.getWdbt());
				map.put("wdbh", t.getWdbh() == null ? "" : t.getWdbh());
				map.put("lczt", null == t.getLczt() ? "" : t.getLczt().getCode());
				map.put("lcztName", null == t.getLczt() ? "" : t.getLczt().getName());
				map.put("gh", null == t.getGh() ? "" : t.getGh().getCode());
				map.put("ghName", null == t.getGh() ? "" : t.getGh().getName());
				return map;
			}
		}).getDataGrid();
	}

	/**
	 * 查看详细
	 * @param id
	 * @return
	 * @author yinchao
	 * @date 2017年9月9日
	 */
	public Map<String, Object> getInfo(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		WdjySqjl t = getDao().findOne(Long.valueOf(id));
		map.put("id", t.getId());
		map.put("sqr", t.getSqr());
		map.put("sqbm", t.getSqbm());
		map.put("sqrq", null == t.getSqrq() ? "" : sdf.format(t.getSqrq()));
		map.put("lxdh", t.getLxdh());
		map.put("wdbt", t.getWdbt());
		map.put("wdbh", t.getWdbh());
		map.put("lcztCode", t.getLczt().getCode());
		map.put("sqyy",t.getSqyy());
		map.put("jysjs", t.getJysjs() == null ? "" : sdf.format(t.getJysjs()));
		map.put("jysje", t.getJysje() == null ? "" : sdf.format(t.getJysje()));
		return map;
	}

	/**
	 * 保存审核
	 * @param t
	 * @return
	 * @author yinchao
	 * @date 2017年9月9日
	 */
	public Result saveObj(long id, String tgzt, String suggestion) {
		WdjySqjl t = getDao().findOne(id);
		Tgzt ttt = new Tgzt();
		ttt.setCode(tgzt);
		t.setTgzt(ttt);
		t.setSuggestion(suggestion);
		Tylc lczt = new Tylc();
		WdjySqFlow lcjl = new WdjySqFlow();
		if("1".equals(tgzt)){
			Gh gh = new Gh();
			gh.setCode("2");
			t.setGh(gh);
			lcjl.setTgzt("通过");
			lczt.setCode("2");
		}else{
			lcjl.setTgzt("不通过");
			lczt.setCode("9");
		}
		t.setLczt(lczt);
		save(t);
		/*计入流程表*/
		lcjl.setGlid(StringSimple.nullToEmpty(id));
		lcjl.setCnode("审核");
		lcjl.setCzsj(new Date());
		lcjl.setPuser(userService.getCurrentUser().getUsername());
		lcjl.setPuserName(userService.getCurrentUser().getRealname());
		lcjl.setSuggestion(suggestion);
		wdjySqFlowDao.save(lcjl);
		return new Result(true);
	}

	/**
	 * 归还
	 * @param id
	 * @return
	 * @author yinchao
	 * @date 2017年9月14日
	 */
	public Result gh(long id) {
		WdjySqjl t = getDao().findOne(id);
		Gh g = new Gh();
		g.setCode("1");
		t.setGh(g);
		save(t);
		return new Result(true);
	}

}
