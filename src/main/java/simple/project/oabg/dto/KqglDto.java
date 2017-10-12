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
 * 用户管理dto
 * @author wsz
 * @created 2017年8月21日
 */
@SearchBean
@Paged
@Sorted({"lczt.code asc","createTime desc"})
public class KqglDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1L;
	@Des("申请人")
	@CondtionExpression(value = "name", type = CondtionType.like)
	private String qjname;
	
	
	@Des("请假类型")
	@CondtionExpression(value = "qjlx.code", type = CondtionType.equal)
	private String qjlx;
	
	@Des("部门单位")
	@CondtionExpression(prameType=String.class,value = "bmdw.code", type = CondtionType.in)
	private List<String> bmdws;
	
	@Des("请假时间起")
	@CondtionExpression(value = "timeStr", type = CondtionType.greaterthan)
	private Date timeStr;
	
	@Des("请假时间止")
	@CondtionExpression(value = "timeEnd", type = CondtionType.lessthan)
	private Date timeEnd;
	
	@Des("请假时间止")
	@CondtionExpression(value = "lczt.code", type = CondtionType.equal)
	private String lczt;
	
	@Des("是否出差")
	@CondtionExpression(value = "sfcc.code", type = CondtionType.equal)
	private String sfcc;
	
	@Des("该部门处长名字")
	@CondtionExpression(value = "czName", type = CondtionType.like)
	private String czName;
	
	
	@Des("分管领导名字")
	@CondtionExpression(value = "fgldName", type = CondtionType.like)
	private String fgname;
	
	@Des("局长名字")
	@CondtionExpression(value = "jzName", type = CondtionType.like)
	private String jzName;
	
	@Des("出差人员")
	@CondtionExpression(joinName = "ccry", value = "id", type = CondtionType.equal)
	private String ccry;
	
	@Des("流程状态")
	@CondtionExpression(prameType=String.class,value = "lczt.code", type = CondtionType.in)
	private List<String> lczts;
}
