package simple.project.oabg.dic.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.dic.model.Bmdw;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.dao.Dao;

public interface BmdwDao extends Dao<String,Bmdw>{

	@Des("根据code查找")
	@Query("select g from Bmdw g where g.code =:code  and g.deleted =0")
	public Bmdw queryByCode(@Param("code") String code);
}
