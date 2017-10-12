package simple.project.oabg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import simple.project.oabg.dao.GzapxxDao;
import simple.project.oabg.entities.gzjhXQ;
import simple.system.simpleweb.platform.service.AbstractService;

@Service
public class GzjhxxService extends AbstractService<GzapxxDao, gzjhXQ, Long>{
	@Autowired
	public GzjhxxService(GzapxxDao dao) {
		super(dao);
	}
}