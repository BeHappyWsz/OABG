package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.ZcglSqFlow;
import simple.system.simpleweb.platform.dao.Dao;

/**
 * 资产管理流程自定义Dao
 * @author yinchao
 * @date 2017年9月7日
 */
public interface ZcglSqFlowDao extends Dao<Long, ZcglSqFlow>{
	
	@Query("select u from ZcglSqFlow u where u.zcglSqjlId = :zcglSqjlId and u.deleted = 0 order by u.createTime asc")
	public List<ZcglSqFlow> queryByZcglSqjlId(@Param("zcglSqjlId") Long zcglSqjlId);

}
