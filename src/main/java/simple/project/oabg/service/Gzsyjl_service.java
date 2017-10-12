package simple.project.oabg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import simple.project.oabg.dao.GzsyjlDao;
import simple.project.oabg.entities.Gzsyjl;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.service.AbstractService;

@Service
public class Gzsyjl_service extends AbstractService<GzsyjlDao, Gzsyjl, Long>{
	@Autowired
	private UserService userService;
	@Autowired
	public Gzsyjl_service(GzsyjlDao dao) {
		super(dao);
	}
	
}