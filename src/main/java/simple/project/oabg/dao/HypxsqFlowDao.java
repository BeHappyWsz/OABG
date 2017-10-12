package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.HypxsqFlow;
import simple.system.simpleweb.platform.dao.Dao;

public interface HypxsqFlowDao extends Dao<Long, HypxsqFlow>{
	
	@Query("select u from HypxsqFlow u where u.deleted = 0 and u.glid = :glid")
	public List<HypxsqFlow> queryByGlid(@Param("glid") String glid);
	
}
