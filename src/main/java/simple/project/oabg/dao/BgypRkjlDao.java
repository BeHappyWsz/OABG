package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.BgypRkjl;
import simple.system.simpleweb.platform.dao.Dao;

public interface BgypRkjlDao extends Dao<Long, BgypRkjl>{

	@Query("select u from BgypRkjl u where u.bgyp.id = :bgypId and u.deleted = 0 order by u.date asc")
	public List<BgypRkjl> queryByBgypId(@Param("bgypId") Long bgypId);
}
