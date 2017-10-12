package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.kqglSqFlow;
import simple.system.simpleweb.platform.dao.Dao;

public interface kqSqFlowDao extends Dao<Long, kqglSqFlow>{

	@Query("select u from kqglSqFlow u where u.deleted = 0 and u.glid = :glid")
	public List<kqglSqFlow> queryByGlid(@Param("glid") String glid);
}
