package simple.project.oabg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import simple.project.oabg.dao.FwngFlowDao;
import simple.project.oabg.entities.FwngFlow;
import simple.system.simpleweb.platform.service.AbstractService;

/**
 * @author sxm
 * @created 2017年9月9日
 */
@Service
public class FwngFlowService extends AbstractService<FwngFlowDao, FwngFlow, Long>{
	
	@Autowired
	public FwngFlowService(FwngFlowDao dao) {
	
		super(dao);
	}

}
