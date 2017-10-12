package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import simple.project.oabg.entities.Lwyp;
import simple.system.simpleweb.platform.dao.Dao;

public interface LwypDao extends Dao<Long, Lwyp>{

	@Query("select u from Lwyp u where u.sl <= u.jjsl and u.deleted = 0")
	public List<Lwyp> countJjjl();
}
