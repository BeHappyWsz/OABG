package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.SbwxSqFlow;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.dao.Dao;

/**
 * 设备维修流程自定义Dao
 * @author wsz
 * @date 2017年9月20日
 */
public interface SbwxSqFlowDao extends Dao<Long,SbwxSqFlow>{

	@Des("查询某一流程 order by u.createTime desc")
	@Query("select u from SbwxSqFlow u where u.glid =:glid and u.deleted =0 order by u.createTime asc")
	public List<SbwxSqFlow> queryByGlid(@Param("glid") String glid);
}
