package simple.project.oabg.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.Wddy;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.dao.Dao;

public interface WddyDao extends Dao<Long, Wddy>{
	
	@Des("查询我的待阅")
	@Query("select u from Wddy u where u.glId =:glid and u.deleted =0 and u.userName =:userName and u.sjly.code=:sjly order by u.createTime desc")
	public Wddy queryByGlid(@Param("glid") String glid,@Param("userName") String userName,@Param("sjly") String sjly);
}
