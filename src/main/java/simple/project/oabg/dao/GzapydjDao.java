package simple.project.oabg.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.GzapYd;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.dao.Dao;

public interface GzapydjDao extends Dao<Long, GzapYd>{
	
	@Des("根据工作计计划和用户差选阅读情况")
	@Query("select u from GzapYd u where u.deleted=0 and u.gzjh=:gzjhid and u.name=:name")
	public GzapYd queryByGzjhIDandName(@Param("gzjhid")Long gzjhid,@Param("name")String name);
}
