package simple.project.oabg.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.Xxbs;
import simple.system.simpleweb.platform.dao.Dao;


	
/** 
 * @author  YZW: 
 * @date 创建时间：2017年9月19日 下午1:33:05 
 * @parameter  
 */

public interface XxbsDao extends Dao<Long, Xxbs>{

	@Query("select sum(t.score) from Xxbs t where t.deleted = 0 and t.lczt.code = 2 and t.bsbm.code = :bmdw and t.bssj between :bssjs and :bssje")
	public Double queryYearScoreByBmdw(@Param("bmdw")String bmdw,@Param("bssjs")Date bssjs,@Param("bssje") Date bssje);
	
}
