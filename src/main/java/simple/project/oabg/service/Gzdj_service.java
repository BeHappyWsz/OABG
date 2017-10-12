package simple.project.oabg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import simple.base.utils.StringSimple;
import simple.project.communal.convert.Cci;
import simple.project.communal.convert.CommonConvert;
import simple.project.oabg.dao.DeDicItemDao;
import simple.project.oabg.dao.GzdjDao;
import simple.project.oabg.dto.GzsyjlDto;
import simple.project.oabg.entities.Gzdj;
import simple.project.oabg.entities.Gzsyjl;
import simple.system.simpleweb.module.system.dic.model.DicItem;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;

@Service
public class Gzdj_service extends AbstractService<GzdjDao, Gzdj, Long>{
	@Autowired
	private UserService userService;
	@Autowired
	private DeDicItemDao deDicItemDao;
	@Autowired
	private Gzsyjl_service gzsyjl_service;
	@Autowired
	public Gzdj_service(GzdjDao dao) {
		super(dao);
	}
	/**
	 * 公章登记列表
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月21日
	 */
	/*public DataGrid grid(Map<String,Object> map){
			Page<Gzdj> page = queryForPage(GzdjDto.class, map);
			return new CommonConvert<Gzdj>(page, new Cci<Gzdj>() {
				@Override
				public Map<String, Object> convertObj(Gzdj t) {
					Map<String, Object> map=new HashMap<String,Object>();
					map.put("id", t.getId());
					map.put("gzname", t.getGzname());
					map.put("glyname", t.getGlyname());
					map.put("titles", t.getTitles());
					return map;
				}
			}).getDataGrid();
		}
	*/
	/**
	 * 获取单个用户信息
	 * @param id
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	/*public Map<String, Object> getInfo(Long id) {
		Map<String,Object> map = new HashMap<String,Object>();
		Gzdj g = getDao().findOne(id);
		map.put("id",g.getId());
		map.put("gzname", g.getGzname());
		map.put("glyname", g.getGlyname());
		map.put("titles",g.getTitles());
		return map;
	}*/
	/**
	 * 删除公章
	 * @param id
	 * @author yzw
	 * @created 2017年8月23日
	 */
	/*public Result deleteByIds(String ids) {
		List<Long> idList = Utils.stringToLongList(ids, ",");
		logicDelete(idList);
		return new Result(true, "删除成功!");
	}*/
	/**
	 * 判断公章是否重复
	 * @param id
	 * @author yzw
	 * @created 2017年8月23日
	 */
	/*s*/
	
	/**
	 * 公章使用情况
	 * @param id
	 * @author yzw
	 * @created 2017年8月23日
	 */
	public DataGrid querygrid(Map<String,Object> map){
		if(!"".equals(StringSimple.nullToEmpty( map.get("bmdw")))){
			String name = "%"+StringSimple.nullToEmpty( map.get("bmdw"))+"%";
			List<DicItem> diclist = deDicItemDao.findItemByDicCodeAndName(name, "BMDW");
			List<String> codeList = new ArrayList<String>();
			if(!diclist.isEmpty()){
				for(DicItem d : diclist){
					codeList.add(d.getCode());
				}
				map.put("bmdws", codeList);
				map.remove("bmdw");
			}
		}
		Page<Gzsyjl> page = gzsyjl_service.queryForPage(GzsyjlDto.class, map);
		return new CommonConvert<Gzsyjl>(page, new Cci<Gzsyjl>() {
			@Override
			public Map<String, Object> convertObj(Gzsyjl t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("gzname", t.getGzname());
				map.put("sybm", t.getBmdw() == null?"":t.getBmdw().getName());
				map.put("syname", t.getName());
				map.put("gzsl", t.getGzsl());
				return map;
			}
		}).getDataGrid();
	}
	
	
	/**
	 * 保存公章
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 *//*
	@ResponseBody
	@RequestMapping("djsave")
	public Result saveObj(Gzdj gzdj){
		String name = userService.getCurrentUser().getRealname();
		if(!SfCf(gzdj)){
			return new Result(false,"该公章已存在");
		}
		if("".equals(StringSimple.nullToEmpty(gzdj.getId()))){
			gzdj.setGlyname(name);
			save(gzdj);
		}else{
			Gzdj old = getDao().findOne(gzdj.getId());
			old.setBmdw(gzdj.getBmdw());
			old.setGlyname(name);
			old.setGzname(gzdj.getGzname());
			save(old);
		}
		return new Result(true);
	}*/
	
}