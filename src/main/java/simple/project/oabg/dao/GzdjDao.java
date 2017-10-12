package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.Gzdj;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.dao.Dao;

public interface GzdjDao extends Dao<Long, Gzdj>{
	
	@Des("查询所有公章")
	@Query("select u from Gzdj u where u.deleted=0")
	public List<Gzdj> queryAll();
	
	@Des("根据id查询公章")
	@Query("select u from Gzdj u where u.deleted=0 and u.id=:id")
	public Gzdj queryById(@Param("id")Long id);
	
	@Des("根据name查询公章")
	@Query("select u from Gzdj u where u.deleted=0 and u.gzname=:gzname")
	public Gzdj queryByName(@Param("gzname")String gzname);
}
