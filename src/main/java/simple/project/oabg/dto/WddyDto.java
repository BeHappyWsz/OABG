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
 * 我的待阅dto
 * @author sxm
 * @created 2017年9月4日
 */
@SearchBean
@Paged
@Sorted("createTime desc")
public class WddyDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1L;
	
	@Des("当前用户")
	@CondtionExpression(value = "userName", type = CondtionType.equal)
	private String username;
	
	@Des("阅读状态")
	@CondtionExpression(value = "state.code", type = CondtionType.equal)
	private String state;
	
	@Des("数据来源")
	@CondtionExpression(value="sjly.code",type=CondtionType.in)
	private String sjly;
	
	@Des("标题")
	@CondtionExpression(value="bt",type=CondtionType.like)
	private String bt;
	
	
	@Des("收件日期(起)")
	@CondtionExpression(value = "fqsj", type = CondtionType.greaterthanOrequal)
	private Date times;
	
	@Des("收件日期(止)")
	@CondtionExpression(value = "fqsj", type = CondtionType.lessthanOrequal)
	private Date timee;
}
