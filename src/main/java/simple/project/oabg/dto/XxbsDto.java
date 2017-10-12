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
public class XxbsDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1L;
	@Des("报送人")
	@CondtionExpression(value="bsxm",type=CondtionType.like)
	private String bsxm;
	
	@Des("报送时间起")
	@CondtionExpression(value="bssj",type=CondtionType.greaterthanOrequal)
	private Date bssjstart;
	
	@Des("报送时间起")
	@CondtionExpression(value="bssj",type=CondtionType.lessthanOrequal)
	private Date bssjend;
	
	@Des("报送部门")
	@CondtionExpression(value="bsbm.code",type=CondtionType.equal)
	private String  bsbm;
	
	@Des("部门单位")
	@CondtionExpression(prameType=String.class,value = "bsbm.code", type = CondtionType.in)
	private List<String> bmdws;
	
	@Des("报送部门")
	@CondtionExpression(prameType=String.class,value="lczt.code",type=CondtionType.in)
	private List<String>  lczts;
	
	@Des("是否录用")
	@CondtionExpression(value="sfly.code",type=CondtionType.equal)
	private String  sfly;

}
