package simple.project.oabg.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import simple.base.utils.StringSimple;
import simple.project.communal.convert.Cci;
import simple.project.communal.convert.CommonConvert;
import simple.project.oabg.dao.DxrzDao;
import simple.project.oabg.dto.DxrzDto;
import simple.project.oabg.entities.Dxrz;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;

/**
 * 短信日志
 * 2017年9月4日
 * @author yc
 */
@Service
public class DxrzService extends AbstractService<DxrzDao, Dxrz, Long>{
	
	@Autowired
	private UserService userService;
	@Autowired
	public DxrzService(DxrzDao dao) {
		super(dao);
	}
	
	/**
	 * 数据列表
	 * 2017年9月6日
	 * yc
	 * @param map
	 * @return
	 */
	public DataGrid grid(Map<String,Object> map){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!"".equals(StringSimple.nullToEmpty(map.get("fssj_s")))){
			try {
				map.put("fssj_s", sdf.parse(StringSimple.nullToEmpty(map.get("fssj_s"))+" 00:00:00"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(!"".equals(StringSimple.nullToEmpty(map.get("fssj_e")))){
			try {
				map.put("fssj_e", sdf.parse(StringSimple.nullToEmpty(map.get("fssj_e"))+" 23:59:59"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Page<Dxrz> page = queryForPage(DxrzDto.class, map);
		return new CommonConvert<Dxrz>(page, new Cci<Dxrz>() {
			@Override
			public Map<String, Object> convertObj(Dxrz t) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("fssj", null == t.getFssj() ? "" : sdf.format(t.getFssj()));
				map.put("jsr", t.getJsr());
				map.put("tel", t.getTel());
				map.put("nr", t.getNr());
				map.put("fsyy", t.getFsyy());
				map.put("fszt", null == t.getFszt() ? "" : t.getFszt().getCode());
				map.put("fsztName", null == t.getFszt() ? "" : t.getFszt().getName());
				return map;
			}
		}).getDataGrid();
	}
	
	/**
	 * 根据id获取详情
	 * 2017年9月6日
	 * yc
	 * @param id
	 * @return
	 */
	public Map<String,Object> getInfo(String id){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String,Object> map = new HashMap<String, Object>();
		Dxrz t = getDao().findOne(Long.parseLong(id));
		map.put("id", t.getId());
		map.put("fssj", null == t.getFssj() ? "" : sdf.format(t.getFssj()));
		map.put("jsr", t.getJsr());
		map.put("tel", t.getTel());
		map.put("nr", t.getNr());
		map.put("fsyy", t.getFsyy());
		map.put("fszt", null == t.getFszt() ? "" : t.getFszt().getCode());
		map.put("fsztName", null == t.getFszt() ? "" : t.getFszt().getName());
		return map;
	}
}
