package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.GdpsblFlow;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.dao.Dao;

public interface GdpsblFlowDao extends Dao<Long,GdpsblFlow>{
	@Des("查询")
	@Query("select u from GdpsblFlow u where u.deleted = 0 and u.gdps_id = :gdps_id order by u.createTime asc")
	public List<GdpsblFlow> queryByGdpsId(@Param("gdps_id") String gdps_id);
}
