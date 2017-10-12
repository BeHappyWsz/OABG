package simple.project.oabg.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.Zcgl;
import simple.system.simpleweb.platform.dao.Dao;

/**
 * 资产管理自定义Dao
 * @author yinchao
 * @date 2017年9月6日
 */
public interface ZcglDao extends Dao<Long, Zcgl>{
	
	@Query("select u from Zcgl u where u.bm.code = :bmcode and syr = :sqrUserName and zcmc = :zcmc and pp = :pp and xh = :xh and u.deleted = 0 order by u.createTime desc")
	public Zcgl queryZcglInfo(@Param("bmcode") String bmcode, @Param("sqrUserName") String sqrUserName, @Param("zcmc") String zcmc,@Param("pp") String pp, @Param("xh")  String xh);

}
