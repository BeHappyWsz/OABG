package simple.project.oabg.dto;

import java.util.Date;
import java.util.List;

import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.annotation.search.CondtionExpression;
import simple.system.simpleweb.platform.annotation.search.CondtionType;
import simple.system.simpleweb.platform.annotation.search.Paged;
import simple.system.simpleweb.platform.annotation.search.SearchBean;
import simple.system.simpleweb.platform.annotation.search.Sorted;
import simple.system.simpleweb.platform.dao.jpa.SearchIsdeleteDto;

/**
 * 劳务用品--申请记录
 * 2017年9月4日
 * @author yc
 */
@SearchBean
@Paged
@Sorted({"lczt.code asc","createTime desc"})
public class LwypSqjlDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1514862321418778487L;
	
	@Des("物品名称")
	@CondtionExpression(value = "name", type = CondtionType.like)
	private String name;
	
	@Des("申请人")
	@CondtionExpression(value = "sqr", type = CondtionType.like)
	private String sqr;
	
	@Des("申请部门")
	@CondtionExpression(prameType=String.class, value = "sqbm", type = CondtionType.in)
	private List<String> sqbms;
	
	@Des("流程状态")
	@CondtionExpression(value = "lczt.code", type = CondtionType.equal)
	private String lczt;
	
	@Des("流程状态")
	@CondtionExpression(prameType=String.class, value = "lczt.code", type = CondtionType.in)
	private List<String> lczts;
	
	@Des("申请人账号")
	@CondtionExpression(value = "sqrUserName", type = CondtionType.equal)
	private String sqrUserName;
	
	@Des("申请日期起")
	@CondtionExpression(value = "createTime", type = CondtionType.greaterthanOrequal)
	private Date sqrq_s;
	
	@Des("申请日期止")
	@CondtionExpression(value = "createTime", type = CondtionType.lessthanOrequal)
	private Date sqrq_e;
}
