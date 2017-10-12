package simple.project.oabg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import simple.project.oabg.dao.ZcglSqFlowDao;
import simple.project.oabg.entities.ZcglSqFlow;
import simple.system.simpleweb.platform.service.AbstractService;

@Service
public class ZcglSqFlowService extends AbstractService<ZcglSqFlowDao, ZcglSqFlow, Long>{
	@Autowired
	public ZcglSqFlowService(ZcglSqFlowDao dao) {
		super(dao);
	}

	
	
}
