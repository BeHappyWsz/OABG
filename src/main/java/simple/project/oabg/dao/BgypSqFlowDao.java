package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.BgypSqFlow;
import simple.system.simpleweb.platform.dao.Dao;

public interface BgypSqFlowDao extends Dao<Long, BgypSqFlow>{

	@Query("select u from BgypSqFlow u where u.deleted = 0 and u.glid = :glid")
	public List<BgypSqFlow> queryByGlid(@Param("glid") String glid);
}
