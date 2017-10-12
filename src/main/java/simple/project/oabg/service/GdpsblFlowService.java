package simple.project.oabg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import simple.project.oabg.dao.GdpsblFlowDao;
import simple.project.oabg.entities.GdpsblFlow;
import simple.system.simpleweb.platform.service.AbstractService;

/**
 * 工单派送办理流程
 * @author wsz
 * @created 2017年9月27日
 */
@Service
public class GdpsblFlowService extends AbstractService<GdpsblFlowDao, GdpsblFlow, Long>{
	@Autowired
	public GdpsblFlowService(GdpsblFlowDao dao) {
		super(dao);
	}

}
