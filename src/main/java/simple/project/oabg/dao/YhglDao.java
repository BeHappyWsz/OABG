package simple.project.oabg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import simple.project.oabg.entities.Txl;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.dao.Dao;

public interface YhglDao extends Dao<Long, Txl>{

	@Des("查询局长/分管领导的分管部门")
	@Query("select t from Txl t where t.fgbms != null and t.fgbms != '' and t.deleted = 0")
	public List<Txl> getFgbms();
	
	@Des("查询当前用户的上级,部门单位+工作职位")
	@Query("select t from Txl t where t.bmdw.code =:bmdw and t.gzzw.code =:gzzw and t.deleted =0")
	public List<Txl> getSj(@Param("bmdw") String gzbm,@Param("gzzw") String gzzw);
	
	@Des("根据用户查找")
	@Query("select t from Txl t where t.user.id =:uid and t.deleted =0 ")
	public Txl getTxlByUser(@Param("uid") Long uid);
	
	@Des("局处室下所有用户")
	@Query("select t from Txl t where t.user != null and t.bmdw.pitemid = 35 and t.deleted =0")
	public List<Txl> getJcs();
	
	@Des("直属单位下所有用户")
	@Query("select t from Txl t where t.user != null and t.bmdw.pitemid = 36 and t.deleted =0")
	public List<Txl> getZsdw();
	
	@Des("辖市区下所有用户")
	@Query("select t from Txl t where t.user != null and t.bmdw.pitemid = 37 and t.deleted =0")
	public List<Txl> getXsq();
	
	@Des("根据用户txlid查找--用下面的方法查询")
	@Query("select t from Txl t where t.id =:id and t.deleted =0 ")
	public Txl getTxlByUserName(@Param("id") Long id);
	
	
	@Des("根据用户username查找")
	@Query("select t from Txl t where t.name =:uname and t.user.username =:uname and t.deleted =0 ")
	public Txl getTxlByUserName(@Param("uname") String uname);

	
	@Des("根据用户userid查找--用这个查询")
	@Query("select t from Txl t where t.user.id =:uid and t.deleted =0 and t.user.deleted = 0")
	public Txl getTxlByUserId(@Param("uid") Long uid);
	
	@Des("获取所有处长")
	@Query("select t from Txl t where t.name =:uname and t.user.username =:uname and t.deleted =0 ")
	public Txl getAllCz(@Param("uname") String uname);
	
	@Des("获取某部门所有人员")
	@Query("select t from Txl t where t.bmdw.code =:bmdw and t.deleted =0 and t.user.deleted =0")
	public List<Txl> getBmryByBmdw(@Param("bmdw") String bmdw);
}
