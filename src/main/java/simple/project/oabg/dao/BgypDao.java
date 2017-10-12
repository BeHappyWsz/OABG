package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import simple.project.oabg.entities.Bgyp;
import simple.system.simpleweb.platform.dao.Dao;

public interface BgypDao extends Dao<Long, Bgyp>{
	@Query("select u from Bgyp u where u.sl <= u.jjsl and u.deleted = 0")
	public List<Bgyp> countJjjl();
}
