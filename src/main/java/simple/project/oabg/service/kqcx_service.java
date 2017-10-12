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
import simple.project.oabg.dao.KqcxDao;
import simple.project.oabg.dto.KqcxDto;
import simple.project.oabg.entities.Kqcx;
import simple.system.simpleweb.module.system.dic.model.DicItem;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;

@Service
public class kqcx_service extends AbstractService<KqcxDao, Kqcx, Long>{
	@Autowired
	private DeDicItemDao deDicItemDao;
	@Autowired
	public kqcx_service(KqcxDao dao) {
		super(dao);
	}

	/**
	 * 考勤列表
	 * @param map
	 * @return
	 * @author yzws
	 * @created 2017年8月21日
	 */
	public DataGrid grid(Map<String,Object> map){
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
		Page<Kqcx> page = queryForPage(KqcxDto.class, map);
		return new CommonConvert<Kqcx>(page, new Cci<Kqcx>() {
			@Override
			public Map<String, Object> convertObj(Kqcx k) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", k.getId());
				map.put("name", k.getName());
				map.put("bm", k.getBmdw()==null?"":k.getBmdw().getName());
				map.put("qjlx", k.getQjlx()==null?"":k.getQjlx().getName());
				map.put("sfcc", k.getSfcc()==null?"":k.getSfcc().getName());
				map.put("ccaddress", k.getCcaddress());
				return map;
			}
		}).getDataGrid();
	}

}
