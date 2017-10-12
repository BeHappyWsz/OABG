package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.XxbsSqFlow;
import simple.system.simpleweb.platform.dao.Dao;

public interface XxbsSqFlowDao extends Dao<Long, XxbsSqFlow>{

	@Query("select u from XxbsSqFlow u where u.deleted = 0 and u.glid = :glid")
	public List<XxbsSqFlow> queryByGlid(@Param("glid") String glid);
}
