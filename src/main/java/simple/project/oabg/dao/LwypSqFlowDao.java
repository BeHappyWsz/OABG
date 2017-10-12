package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.LwypSqFlow;
import simple.system.simpleweb.platform.dao.Dao;

public interface LwypSqFlowDao extends Dao<Long, LwypSqFlow>{

	@Query("select u from LwypSqFlow u where u.deleted = 0 and u.glid = :glid")
	public List<LwypSqFlow> queryByGlid(@Param("glid") String glid);
}
