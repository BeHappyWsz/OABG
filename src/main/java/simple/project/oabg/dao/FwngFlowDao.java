package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.FwngFlow;
import simple.system.simpleweb.platform.dao.Dao;

/**
 * @author sxm
 * @created 2017年9月9日
 */
public interface FwngFlowDao extends Dao<Long, FwngFlow>{
	
	@Query("select u from FwngFlow u where u.deleted = 0 and u.fwngid = :glid")
	public List<FwngFlow> queryByGlid(@Param("glid") String glid);
}
