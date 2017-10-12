package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.GwjdsqFlow;
import simple.system.simpleweb.platform.dao.Dao;

public interface GwjdsqFlowDao extends Dao<Long, GwjdsqFlow>{
	
	@Query("select u from GwjdsqFlow u where u.deleted = 0 and u.glid = :glid")
	public List<GwjdsqFlow> queryByGlid(@Param("glid") String glid);
	
}
