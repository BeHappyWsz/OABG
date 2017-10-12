package simple.project.oabg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import simple.project.oabg.dao.GzapydjDao;
import simple.project.oabg.entities.GzapYd;
import simple.system.simpleweb.platform.service.AbstractService;

@Service
public class GzjhydService extends AbstractService<GzapydjDao, GzapYd, Long>{
	@Autowired
	public GzjhydService(GzapydjDao dao) {
		super(dao);
	}
	
}