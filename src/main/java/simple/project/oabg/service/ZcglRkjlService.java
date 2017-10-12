package simple.project.oabg.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import simple.base.utils.StringSimple;
import simple.project.oabg.dao.ZcglRkjlDao;
import simple.project.oabg.entities.ZcglRkjl;
import simple.system.simpleweb.platform.service.AbstractService;

/**
 * 资产入库记录
 * 
 * @author yinchao
 * @date 2017年9月6日
 */
@Service
public class ZcglRkjlService extends AbstractService<ZcglRkjlDao, ZcglRkjl, Long>{

	@Autowired
	public ZcglRkjlService(ZcglRkjlDao dao) {
		super(dao);
	}

	public Object queryRkjlByZcglId(Long zcglId) {
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		List<ZcglRkjl> zcglRkjlList = getDao().queryZcglById(zcglId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (ZcglRkjl t : zcglRkjlList) {
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("id", t.getId());
			m.put("crk", t.getCrk()==null ? "" : t.getCrk().getCode());
			m.put("crkName", t.getCrk()==null ? "" : t.getCrk().getName());
			m.put("sldw", t.getSl()+StringSimple.nullToEmpty(t.getDw()));
			m.put("date", t.getDate()==null ? "" : sdf.format(t.getDate()));
			m.put("userName", t.getUsername());
			res.add(m);
		}
		return res;
	}

}
