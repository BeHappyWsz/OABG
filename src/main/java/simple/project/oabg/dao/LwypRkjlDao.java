package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.LwypRkjl;
import simple.system.simpleweb.platform.dao.Dao;

public interface LwypRkjlDao extends Dao<Long, LwypRkjl>{

	@Query("select u from LwypRkjl u where u.lwyp.id = :lwypId and u.deleted = 0 order by u.date asc")
	public List<LwypRkjl> queryByLwypId(@Param("lwypId") Long lwypId);
}
