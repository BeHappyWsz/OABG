package simple.project.oabg.dic.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.dic.model.Tsswzt;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.dao.Dao;

public interface TsswztDao extends Dao<String,Tsswzt>{
	
	@Des("根据code查找")
	@Query("select g from Tsswzt g where g.code =:code  and g.deleted =0")
	public Tsswzt queryByCode(@Param("code") String code);
}
