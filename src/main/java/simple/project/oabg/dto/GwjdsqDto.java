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
 * 公务接待申请
 * 2017年9月19日
 * @author yc
 */
@SearchBean
@Paged
@Sorted({"lczt.code asc","createTime desc"})
public class GwjdsqDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1514862321418778487L;
	
	@Des("申请人")
	@CondtionExpression(value = "sqrUserName", type = CondtionType.equal)
	private String sqrUserName;
	
	@Des("流程状态")
	@CondtionExpression(value = "lczt.code", type = CondtionType.equal)
	private String lczt;
	
	@Des("流程状态")
	@CondtionExpression(prameType=String.class,value = "lczt.code", type = CondtionType.in)
	private List<String> lczts;
	
	@Des("接待事由")
	@CondtionExpression(value = "jdsy", type = CondtionType.like)
	private String jdsy;
	
	@Des("部门单位")
	@CondtionExpression(value = "sqrbmcode", type = CondtionType.equal)
	private String sqrbmcode;
	
	@Des("部门单位")
	@CondtionExpression(prameType=String.class,value = "sqrbmcode", type = CondtionType.in)
	private List<String> sqrbmcodein;
	
	@Des("申请日期起")
	@CondtionExpression(value = "sqrq", type = CondtionType.greaterthanOrequal)
	private Date sqrq_s;
	
	@Des("申请日期止")
	@CondtionExpression(value = "sqrq", type = CondtionType.lessthanOrequal)
	private Date sqrq_e;
	
	@Des("接待时间起")
	@CondtionExpression(value = "jdsj", type = CondtionType.greaterthanOrequal)
	private Date jdsj_s;
	
	@Des("接待时间止")
	@CondtionExpression(value = "jdsj", type = CondtionType.lessthanOrequal)
	private Date jdsj_e;
}
