package simple.project.oabg.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import simple.base.utils.StringSimple;
import simple.project.communal.convert.Cci;
import simple.project.communal.convert.CommonConvert;
import simple.project.communal.util.Utils;
import simple.project.oabg.dao.LwypDao;
import simple.project.oabg.dao.LwypRkjlDao;
import simple.project.oabg.dic.model.Crk;
import simple.project.oabg.dto.LwypDto;
import simple.project.oabg.entities.Lwyp;
import simple.project.oabg.entities.LwypRkjl;
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
public class LwypService extends AbstractService<LwypDao, Lwyp, Long>{
	
	@Autowired
	private LwypRkjlDao lwypRkjlDao;
	@Autowired
	private UserService userService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	public LwypService(LwypDao dao) {
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
		Page<Lwyp> page = queryForPage(LwypDto.class, map);
		return new CommonConvert<Lwyp>(page, new Cci<Lwyp>() {
			@Override
			public Map<String, Object> convertObj(Lwyp t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("name", t.getName());
				map.put("sl", t.getSl());
				map.put("jjsl", t.getJjsl());
				map.put("dw", t.getDw());
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
		Lwyp t=getDao().findOne(Long.valueOf(id));
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id", t.getId());
		map.put("name", t.getName());
		map.put("sl", t.getSl());
		map.put("dw", t.getDw());
		map.put("bz", t.getBz());
		map.put("jjsl", t.getJjsl());
		return map;
	}
	
	/**
	 * 保存
	 * 2017年9月4日
	 * yc
	 * @param t
	 * @return
	 */
	public Result saveObj(Lwyp t){
		if("".equals(StringSimple.nullToEmpty(t.getId()))){
			save(t);
			if(0 != t.getSl()){
				//如果不为0，新增入库记录
				LwypRkjl rkjl = new LwypRkjl();
				rkjl.setLwyp(t);
				Crk crk = new Crk();
				crk.setCode("1");
				rkjl.setCrk(crk);
				rkjl.setSl(t.getSl());
				rkjl.setDw(t.getDw());
				rkjl.setBz(t.getBz());
				rkjl.setDate(new Date());
				rkjl.setUsername(userService.getCurrentUser().getUsername());
				lwypRkjlDao.save(rkjl);
			}
		}else{
			Lwyp old = getDao().findOne(t.getId());
			int oldsl = old.getSl();
			int tsl = t.getSl();
			old.setName(t.getName());
			old.setDw(t.getDw());
			old.setSl(t.getSl());
			old.setBz(t.getBz());
			old.setJjsl(t.getJjsl());
			save(old);
			//保存出入库记录
			int sl = 0;
			Crk crk = new Crk();
			if(oldsl > tsl){
				//出库
				crk.setCode("2");
				sl = oldsl-tsl;
			}else if(oldsl < tsl){
				//入库
				crk.setCode("1");
				sl = tsl-oldsl;
			}else if(oldsl == tsl){
				//未变
				crk.setCode("9");
				sl = oldsl;
			}
			//如果和之前不相等，新增出入库记录
			LwypRkjl rkjl = new LwypRkjl();
			rkjl.setLwyp(old);
			rkjl.setCrk(crk);
			rkjl.setSl(sl);
			rkjl.setDw(old.getDw());
			rkjl.setDate(new Date());
			rkjl.setUsername(userService.getCurrentUser().getUsername());
			rkjl.setBz(t.getBz());
			lwypRkjlDao.save(rkjl);
		}
		return new Result(true);
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
	
	/**
	 * 根据劳务用品id查询出入库记录
	 * 2017年9月4日
	 * yc
	 * @param lwypId
	 * @return
	 */
	public List<Map<String,Object>> queryRkjlByLwypId(Long lwypId){
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		List<LwypRkjl> list = lwypRkjlDao.queryByLwypId(lwypId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (LwypRkjl t : list) {
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("id", t.getId());
			m.put("crk", null == t.getCrk() ? "" : t.getCrk().getCode());
			m.put("crkName", null == t.getCrk() ? "" : t.getCrk().getName());
			m.put("sldw", t.getSl()+StringSimple.nullToEmpty(t.getDw()));
			m.put("date", null == t.getDate() ? "" : sdf.format(t.getDate()));
			User user = userService.getUserByUsername(StringSimple.nullToEmpty(t.getUsername()));
			m.put("userName", null == user ? "" : user.getRealname());
			m.put("bz", t.getBz());
			res.add(m);
		}
		return res;
	}
	
	/**
	 * 库存警报
	 * 2017年9月6日
	 * yc
	 * @param map
	 * @return
	 */
	public DataGrid jjGrid(Map<String,Object> map){
		if(!"".equals(StringSimple.nullToEmpty(map.get("ids")))){
			List<String> idList = Utils.stringToStringList(StringSimple.nullToEmpty(map.get("ids")), ",");
			map.put("idList", idList);
		}
		Page<Lwyp> page = queryForPage(LwypDto.class, map);
		return new CommonConvert<Lwyp>(page, new Cci<Lwyp>() {
			@Override
			public Map<String, Object> convertObj(Lwyp t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("name", t.getName());
				map.put("sl", t.getSl());
				map.put("jjsl", t.getJjsl());
				map.put("dw", t.getDw());
				return map;
			}
		}).getDataGrid();
	}
	
	/**
	 * 查询是否有库存数量低于警戒线的记录
	 * 2017年9月6日
	 * yc
	 * @return
	 */
	public Result checkHasJjjl(){
		List<Lwyp> list = getDao().countJjjl();
		StringBuilder idsb = new StringBuilder();
		for (Lwyp lwyp : list) {
			idsb.append(lwyp.getId()+",");
		}
		String ids = idsb.toString();
		ids = "".equals(ids) ? "" : ids.substring(0, ids.length()-1);
		return new Result(0 != list.size(),"",ids);
	}
	
}
