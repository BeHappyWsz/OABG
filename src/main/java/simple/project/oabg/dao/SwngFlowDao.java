package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.SwngFlow;
import simple.system.simpleweb.platform.dao.Dao;

public interface SwngFlowDao extends Dao<Long, SwngFlow>{
	
	@Query("select u from SwngFlow u where u.deleted = 0 and u.swng_id = :swng_id order by u.createTime asc")
	public List<SwngFlow> queryBySwngId(@Param("swng_id") String swng_id);
}
