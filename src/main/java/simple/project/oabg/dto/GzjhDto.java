package simple.project.oabg.dto;

import java.util.Date;

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
@Sorted("createTime desc")
public class GzjhDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1L;

	@Des("开始时间")
	@CondtionExpression(value="kssj",type=CondtionType.greaterthanOrequal)
	private Date kssj;
	
	@Des("会议时间起")
	@CondtionExpression(value="jssj",type=CondtionType.lessthanOrequal)
	private Date jssj;
	
	@Des("姓名")
	@CondtionExpression(value="name",type=CondtionType.like)
	private String name;
	
	@Des("姓名")
	@CondtionExpression(value="sftj.code",type=CondtionType.like)
	private String sftj;
	
}
