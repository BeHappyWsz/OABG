package simple.project.oabg.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.Gzsq;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.dao.Dao;

public interface GzsqDao extends Dao<Long, Gzsq>{
	
	@Des("根据id查询公章")
	@Query("select u from Gzsq u where u.deleted=0 and u.id=:id")
	public Gzsq queryById(@Param("id")Long id);
}
