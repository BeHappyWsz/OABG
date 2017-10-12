package simple.project.oabg.service;

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
import simple.project.oabg.dao.BgypDao;
import simple.project.oabg.dic.model.Crk;
import simple.project.oabg.dto.BgypDto;
import simple.project.oabg.entities.Bgyp;
import simple.project.oabg.entities.BgypRkjl;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;

/**
 * 办公用品
 * @author sxm
 * @created 2017年9月5日
 */
@Service
public class BgypService extends AbstractService<BgypDao, Bgyp, Long>{
	
	@Autowired
	public BgypService(BgypDao dao) {
		
		super(dao);
	}

	@Autowired
	private BgypRkjlService bgypRkjlService;
	@Autowired
	private UserService userService;
	
	
	/**
	 * 返回grid列表
	 * @author sxm
	 * @created 2017年9月5日
	 * @param map
	 * @return
	 */
	public DataGrid grid(Map<String,Object> map){
		Page<Bgyp> page = queryForPage(BgypDto.class, map);
		return new CommonConvert<Bgyp>(page, new Cci<Bgyp>() {
			@Override
			public Map<String, Object> convertObj(Bgyp t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("name", t.getName());
				map.put("sl", t.getSl());
				map.put("bgypfl", null == t.getBgypfl() ? "" : t.getBgypfl().getCode());
				map.put("bgypflName", null == t.getBgypfl() ? "" : t.getBgypfl().getName());
				map.put("jjsl", t.getJjsl());
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
		Bgyp t=getDao().findOne(Long.valueOf(id));
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id", t.getId());
		map.put("name", t.getName());
		map.put("sl", t.getSl());
		map.put("bgypfl", null == t.getBgypfl() ? "" : t.getBgypfl().getCode());
		map.put("bgypflName", null == t.getBgypfl() ? "" : t.getBgypfl().getName());
		map.put("pp", t.getPp());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		map.put("rksj", sdf.format(t.getRksj()));
		map.put("jjsl", t.getJjsl());
		map.put("bz", t.getBz());
		return map;
	}
	
	/**
	 * 保存
	 * @author sxm
	 * @created 2017年9月5日
	 * @param t
	 * @return
	 */
	public Result saveObj(Bgyp t){
		if("".equals(StringSimple.nullToEmpty(t.getId()))){
			save(t);
			if(0 != t.getSl()){
				//如果不为0，新增入库记录
				BgypRkjl rkjl = new BgypRkjl();
				rkjl.setBgyp(t);
				Crk crk = new Crk();
				crk.setCode("1");
				rkjl.setCrk(crk);
				rkjl.setSl(t.getSl());
				rkjl.setBz(t.getBz());
				rkjl.setDate(new Date());
				rkjl.setUsername(userService.getCurrentUser().getUsername());
				bgypRkjlService.save(rkjl);
			}
		}else{
			Bgyp old = getDao().findOne(t.getId());
			int oldsl = old.getSl();
			int tsl = t.getSl();
			old.setName(t.getName());
			old.setSl(t.getSl());
			old.setPp(t.getPp());
			old.setJjsl(t.getJjsl());
			old.setBz(t.getBz());
			old.setBgypfl(t.getBgypfl());
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
			BgypRkjl rkjl = new BgypRkjl();
			rkjl.setBgyp(old);
			rkjl.setCrk(crk);
			rkjl.setSl(sl);
			rkjl.setDate(new Date());
			rkjl.setUsername(userService.getCurrentUser().getUsername());
			rkjl.setBz(t.getBz());
			bgypRkjlService.save(rkjl);
		}
		return new Result(true);
	}
	
	/**
	 * 根据id删除
	 * 2017年9月4日
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
	 * @param lwypId
	 * @return
	 */
	public List<Map<String,Object>> queryRkjlByBgypId(Long bgypId){
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		List<BgypRkjl> list = bgypRkjlService.getDao().queryByBgypId(bgypId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (BgypRkjl t : list) {
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("id", t.getId());
			m.put("crk", null == t.getCrk() ? "" : t.getCrk().getCode());
			m.put("crkName", null == t.getCrk() ? "" : t.getCrk().getName());
			m.put("sldw", t.getSl());
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
	 * 2017年9月9日
	 * yc
	 * @param map
	 * @return
	 */
	public DataGrid jjGrid(Map<String,Object> map){
		if(!"".equals(StringSimple.nullToEmpty(map.get("ids")))){
			List<String> idList = Utils.stringToStringList(StringSimple.nullToEmpty(map.get("ids")), ",");
			map.put("idList", idList);
		}
		Page<Bgyp> page = queryForPage(BgypDto.class, map);
		return new CommonConvert<Bgyp>(page, new Cci<Bgyp>() {
			@Override
			public Map<String, Object> convertObj(Bgyp t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("name", t.getName());
				map.put("sl", t.getSl());
				map.put("jjsl", t.getJjsl());
				map.put("bgypfl", null == t.getBgypfl() ? "" : t.getBgypfl().getCode());
				map.put("bgypflName", null == t.getBgypfl() ? "" : t.getBgypfl().getName());
				return map;
			}
		}).getDataGrid();
	}
	
	/**
	 * 查询是否有库存数量低于警戒线的记录
	 * 2017年9月9日
	 * yc
	 * @return
	 */
	public Result checkHasJjjl(){
		List<Bgyp> list = getDao().countJjjl();
		StringBuilder idsb = new StringBuilder();
		for (Bgyp bgyp : list) {
			idsb.append(bgyp.getId()+",");
		}
		String ids = idsb.toString();
		ids = "".equals(ids) ? "" : ids.substring(0, ids.length()-1);
		return new Result(0 != list.size(),"",ids);
	}
	
}
