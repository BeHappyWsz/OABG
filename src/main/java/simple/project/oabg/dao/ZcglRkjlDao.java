package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.ZcglRkjl;
import simple.system.simpleweb.platform.dao.Dao;

/**
 * 资产入库自定义Dao
 * @author yinchao
 * @date 2017年9月6日
 */
public interface ZcglRkjlDao extends Dao<Long, ZcglRkjl>{
	
	@Query("select u from ZcglRkjl u where u.zcglId = :zcglId and u.deleted = 0 order by u.createTime desc")
	public List<ZcglRkjl> queryZcglById(@Param("zcglId") Long zcglId);

}
