package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.WdjySqFlow;
import simple.system.simpleweb.platform.dao.Dao;

public interface WdjySqFlowDao extends Dao<Long, WdjySqFlow>{

	@Query("select u from WdjySqFlow u where u.deleted = 0 and u.glid = :glid order by u.createTime asc")
	public List<WdjySqFlow> queryByGlid(@Param("glid") String glid);
}
