package simple.project.oabg.dto;

import java.util.List;

import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.annotation.search.CondtionExpression;
import simple.system.simpleweb.platform.annotation.search.CondtionType;
import simple.system.simpleweb.platform.annotation.search.Paged;
import simple.system.simpleweb.platform.annotation.search.SearchBean;
import simple.system.simpleweb.platform.annotation.search.Sorted;
import simple.system.simpleweb.platform.dao.jpa.SearchIsdeleteDto;
/**
 * 用户管理dto
 * @author wsz
 * @created 2017年8月21日
 */
@SearchBean
@Paged
@Sorted("id asc")
public class GzsyjlDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1L;
	
	@Des("姓名查询")
	@CondtionExpression(value="name",type=CondtionType.like)
	private String name;
	
	@Des("部门")
	@CondtionExpression(prameType=String.class,value = "bmdw.code", type = CondtionType.in)
	private List<String> bmdws;
	
	@Des("公章名称查询")
	@CondtionExpression(value="gzname",type=CondtionType.equal)
	private String gzname;
	
}