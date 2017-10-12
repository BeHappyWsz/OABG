package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.gzjhXQ;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.dao.Dao;

public interface GzapxxDao extends Dao<Long, gzjhXQ>{
	
	@Des("根据id查询")
	@Query("select u from gzjhXQ u where u.deleted=0 and u.id=:id")
	public gzjhXQ queryById(@Param("id")Long id);
	
	@Des("根据工作计划查询")
	@Query("select u from gzjhXQ u where u.deleted=0 and u.GZJHiD=:GZJHiD")
	public List<gzjhXQ> queryByGzjhID(@Param("GZJHiD")String GZJHiD);
}
