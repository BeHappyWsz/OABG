package simple.project.oabg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import simple.project.oabg.dao.BgypRkjlDao;
import simple.project.oabg.entities.BgypRkjl;
import simple.system.simpleweb.platform.service.AbstractService;

/**
 * 办公用品入库记录
 * @author sxm
 * @created 2017年9月5日
 */
@Service
public class BgypRkjlService extends AbstractService<BgypRkjlDao, BgypRkjl, Long>{

	@Autowired
	public BgypRkjlService(BgypRkjlDao dao) {
		super(dao);
	}

}
