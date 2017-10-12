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
import simple.project.oabg.dao.ZlzxDao;
import simple.project.oabg.dto.ZlzxDto;
import simple.project.oabg.entities.Zlzx;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;

/**
 * 资料中心
 * 2017年9月5日
 * @author yc
 */
@Service
public class ZlzxService extends AbstractService<ZlzxDao, Zlzx, Long>{
	
	@Autowired
	private UserService userService;
	@Autowired
	public ZlzxService(ZlzxDao dao) {
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!"".equals(StringSimple.nullToEmpty(map.get("scsj_s")))){
			try {
				map.put("scsj_s", sdf.parse(StringSimple.nullToEmpty(map.get("scsj_s"))+" 00:00:00"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(!"".equals(StringSimple.nullToEmpty(map.get("scsj_e")))){
			try {
				map.put("scsj_e", sdf.parse(StringSimple.nullToEmpty(map.get("scsj_e"))+" 23:59:59"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Page<Zlzx> page = queryForPage(ZlzxDto.class, map);
		return new CommonConvert<Zlzx>(page, new Cci<Zlzx>() {
			@Override
			public Map<String, Object> convertObj(Zlzx t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("title", t.getTitle());
				map.put("scsj", null == t.getScsj() ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(t.getScsj()));
				User user = userService.getUserByUsername(StringSimple.nullToEmpty(t.getUserName()));
				map.put("scyh", null == user ? "" : user.getRealname());
				map.put("userName", t.getUserName());
				map.put("fileInfo", null == t.getFileInfo() ? "" : t.getFileInfo().getId());
				map.put("fileInfoUrl", null == t.getFileInfo() ? "" : t.getFileInfo().getUrl());
				map.put("fileName", null == t.getFileInfo() ? "" : t.getFileInfo().getFileName());
				map.put("zllx", null == t.getZllx() ? "" : t.getZllx().getCode());
				map.put("zllxName", null == t.getZllx() ? "" : t.getZllx().getName());
				return map;
			}
		}).getDataGrid();
	}
	
	/**
	 * 根据id获取详情
	 * 2017年9月5日
	 * yc
	 * @param id
	 * @return
	 */
	public Map<String,Object> getInfo(String id){
		Zlzx t=getDao().findOne(Long.valueOf(id));
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id", t.getId());
		map.put("title", t.getTitle());
		map.put("fileInfo", null == t.getFileInfo() ? "" : t.getFileInfo().getId());
		map.put("zllx", null == t.getZllx() ? "" : t.getZllx().getCode());
		map.put("zllxName", null == t.getZllx() ? "" : t.getZllx().getName());
		map.put("wjsm", t.getWjsm());
		return map;
	}
	
	/**
	 * 保存
	 * 2017年9月4日
	 * yc
	 * @param t
	 * @return
	 */
	public Result saveObj(Zlzx t){
		if("".equals(StringSimple.nullToEmpty(t.getId()))){
			t.setUserName(userService.getCurrentUser().getUsername());
			t.setUserRealName(userService.getCurrentUser().getRealname());
			t.setScsj(new Date());
			save(t);
		}else{
			Zlzx old = getDao().findOne(t.getId());
			old.setTitle(t.getTitle());
			old.setFileInfo(t.getFileInfo());
			old.setZllx(t.getZllx());
			old.setWjsm(t.getWjsm());
			save(old);
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
	
}
