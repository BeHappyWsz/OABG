package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.GzSqFlow;
import simple.system.simpleweb.platform.dao.Dao;

public interface gzSqFlowDao extends Dao<Long, GzSqFlow>{

	@Query("select u from GzSqFlow u where u.deleted = 0 and u.glid = :glid")
	public List<GzSqFlow> queryByGlid(@Param("glid") String glid);
}
