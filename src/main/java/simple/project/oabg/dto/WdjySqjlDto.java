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
 * 文档借阅--申请记录
 * 2017年9月5日
 */
@SearchBean
@Paged
@Sorted("createTime desc")
public class WdjySqjlDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1L;
	
	
	@Des("文档标题")
	@CondtionExpression(value = "wdbt", type = CondtionType.like)
	private String wdbt;
	
	@Des("申请人")
	@CondtionExpression(value = "sqr", type = CondtionType.like)
	private String sqr;
	
	@Des("流程状态")
	@CondtionExpression(value = "lczt.code", type = CondtionType.equal)
	private String lczt;
	
	@Des("流程状态-图标右上角未办提示需要")
	@CondtionExpression(value = "lczt.code", type = CondtionType.in)
	private String lczts;
	
	@Des("申请人账号")
	@CondtionExpression(value = "sqrUserName", type = CondtionType.equal)
	private String sqrUserName;
	
	@Des("申请日期(起)")
	@CondtionExpression(value = "sqrq", type = CondtionType.greaterthanOrequal)
	private Date sqrq_s;
	
	@Des("申请日期(止)")
	@CondtionExpression(value = "sqrq", type = CondtionType.lessthanOrequal)
	private Date sqrq_e;
	
	@Des("通过状态")
	@CondtionExpression(value = "tgzt.code", type = CondtionType.isnullorempty)
	private String tg;
	
}
