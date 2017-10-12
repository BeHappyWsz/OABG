package simple.project.oabg.dao;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.dao.Dao;

/**
 * 自定义的roleDao
 * @author wm
 * @date 2016年10月10日
 */
public interface DeRoleDao extends Dao<Long, Role>{
	
	@Des("根据code查询角色")
	@Query("select u from Role u where u.roleCode=:roleCode and u.deleted=0")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	public Role queryByCode(@Param("roleCode") String roleCode);

}
