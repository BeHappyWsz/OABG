package simple.project.oabg.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import simple.system.simpleweb.module.system.dic.model.DicItem;
import simple.system.simpleweb.platform.dao.Dao;

/**
 * 自定义的DicItemDao
 * @author yzw
 * @date 2017年8月16日
 */
public interface DeDicItemDao extends Dao<Long, DicItem>{
	/**
	 * 通过名字
	 * @param name
	 * @param dcode
	 * @return
	 */
	
	@Query("select d from DicItem d where d.dic.code =:dcode and d.actived=1 and d.deleted=0 and d.name like :name")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	public List<DicItem> findItemByDicCodeAndName(@Param("name") String name,@Param("dcode") String dcode);
	
}
