package simple.project.oabg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import simple.project.oabg.dao.HyfkDao;
import simple.project.oabg.entities.Hyfk;
import simple.system.simpleweb.platform.service.AbstractService;

@Service
public class Hyfk_service extends AbstractService<HyfkDao, Hyfk, Long>{
	@Autowired
	public Hyfk_service(HyfkDao dao) {
		super(dao);
	}
}
