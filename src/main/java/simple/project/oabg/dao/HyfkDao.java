package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.Hyfk;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.dao.Dao;

public interface HyfkDao extends Dao<Long, Hyfk>{
	
	@Des("根据会议id查询")
	@Query("select u from Hyfk u where u.hyid=:hyid and u.deleted=0")
	public List<Hyfk> queryByPid(@Param("hyid") Long hyid );
	
	@Des("根据收件人名字")
	@Query("select u from Hyfk u where u.hyid=:hyid and u.name=:name and u.deleted=0")
	public Hyfk queryByName(@Param("name") String name,@Param("hyid") Long hyid );
}
