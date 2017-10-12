package simple.project.oabg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import simple.project.oabg.dao.SwngFlowDao;
import simple.project.oabg.entities.SwngFlow;
import simple.system.simpleweb.module.system.file.dao.FileInfoDao;
import simple.system.simpleweb.platform.service.AbstractService;

/**
 * 收文拟稿流程
 * @author wsz
 * @created 2017年9月5日
 */
@Service
public class Swgl_swngFlow_service extends AbstractService<SwngFlowDao, SwngFlow, Long>{
	@Autowired
	public Swgl_swngFlow_service(SwngFlowDao dao) {
		super(dao);
	}

	@Autowired
	private FileInfoDao fileInfoDao;
}
