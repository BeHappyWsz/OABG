package simple.project.oabg.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import simple.system.simpleweb.module.user.model.Org;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.dao.Dao;

/**
 * 自定义的orgDao
 * @author wm
 * @date 2016年3月9日
 */
public interface DeOrgDao extends Dao<Long, Org>{
	
	@Des("根据pid查询子节点")
	@Query("select u from Org u where u.parent.id=:pid and u.deleted=0")
	public List<Org> queryByPid(@Param("pid") Long pid);
	
	
	@Des("根据code查询组织")
	@Query("select u from Org u where u.orgCode=:code and u.deleted=0")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	public Org queryByCode(@Param("code") String code);
	
	@Des("根据名称查询组织")
	@Query("select u from Org u where u.orgName=:name and u.deleted=0")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	public Org queryByName(@Param("name") String name);
}
