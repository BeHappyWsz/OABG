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
import simple.project.oabg.dao.WdjySqFlowDao;
import simple.project.oabg.dao.WdjySqjlDao;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.dic.model.Tylc;
import simple.project.oabg.dto.WdjySqjlDto;
import simple.project.oabg.entities.WdjySqFlow;
import simple.project.oabg.entities.WdjySqjl;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;
import simple.system.simpleweb.platform.util.StringKit;

/**
 * 文档管理之文档借阅申请记录
 * 2017年9月8日
 * @author wsz
 */
@Service
public class Dagl_wdjySqjl_service extends AbstractService<WdjySqjlDao, WdjySqjl, Long>{
	@Autowired
	public Dagl_wdjySqjl_service(WdjySqjlDao dao) {
		super(dao);
	}
	@Autowired
	private YhglDao yhglDao;
	@Autowired
	private UserService userService;
	@Autowired
	private WdjySqFlowDao wdjySqFlowDao;
	@Autowired
	private WddbService wddbService;
	@Autowired
	private Yhgl_service yhgl_service;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	/**
	 * 申请借阅的数据列表
	 * 2017年9月8日
	 * wsz
	 * @param map
	 * @return
	 */
	public DataGrid grid(Map<String, Object> map) {
		if(!"".equals(StringSimple.nullToEmpty(map.get("sqrq_s")))){
			try {
				map.put("sqrq_s", sdf.parseObject(StringSimple.nullToEmpty(map.get("sqrq_s"))));
			} catch (ParseException e) {}
		}
		if(!"".equals(StringSimple.nullToEmpty(map.get("sqrq_e")))){
			try {
				map.put("sqrq_e", sdf.parseObject(StringSimple.nullToEmpty(map.get("sqrq_e")+" 23:59:59")));
			} catch (ParseException e) {}
		}
		map.put("sqrUserName", userService.getCurrentUser().getUsername());
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
				if(!StringKit.isEmpty(t.getGh())){//归还状态 1:已归还 2：未归还
					map.put("gh", t.getGh().getCode());
					map.put("ghName", t.getGh().getName());
				}
				return map;
			}
		}).getDataGrid();
	}
	
	/**
	 * 保存文档借阅申请信息
	 * @param w
	 * @return
	 * 2017年9月8日
	 * @author wsz
	 */
	public Result saveJySqjl(WdjySqjl w) {
		Tylc lczt = new Tylc();
		lczt.setCode("1"); //1:待审批 2：已审批 9:未通过
		w.setLczt(lczt);
		WdjySqjl wdjySqjl = save(w);
		//发送至待办
		List<User> userList= yhgl_service.getUserByRoleCode("JYWYSZR");
		String name="";
		if(!userList.isEmpty()){
			for(User u :userList){
				name+="#"+u.getUsername();
			}
		}
		name+="".equals(name)?"":"#";
		wddbService.saveNewWddb(wdjySqjl.getId(), "80", "文档借阅申请", name, "1");
		//记录流程
		WdjySqFlow lcjl = new WdjySqFlow();
		lcjl.setGlid(StringSimple.nullToEmpty(w.getId()));
		lcjl.setCnode("提交");
		lcjl.setTgzt("通过");
		lcjl.setPuser(userService.getCurrentUser().getUsername());
		lcjl.setPuserName(userService.getCurrentUser().getRealname());
		lcjl.setSuggestion("");
		lcjl.setCzsj(new Date());
		wdjySqFlowDao.save(lcjl);
		return new Result(true);
	}

	/**
	 * 获取文档借阅的申请记录
	 * @param id
	 * @return
	 * @author wsz
	 * 2017年9月8日
	 */
	public Map<String, Object> getInfo(Long id) {
		WdjySqjl jl = getDao().findOne(id);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("id", jl.getId());
		map.put("sqr", jl.getSqr());
		map.put("sqrq", jl.getSqrq());
		map.put("sqbm", jl.getSqbm());
		map.put("lxdh", jl.getLxdh());
		map.put("jysjs", jl.getJysjs() == null ? "" : jl.getJysjs());
		map.put("jysje", jl.getJysje() == null ? "" : jl.getJysje());
		map.put("wdbt", jl.getWdbt());
		map.put("sqyy", jl.getSqyy());
		map.put("lcztcode", null == jl.getLczt() ? "" : jl.getLczt().getCode());
		return map;
	}
	
	
	
}
