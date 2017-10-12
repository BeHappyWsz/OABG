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
 * 工单派送办理dto
 * @author wsz
 * @created 2017年9月27日
 */
@SearchBean
@Paged
@Sorted("createTime desc")
public class GdpsblDto extends SearchIsdeleteDto{
	private static final long serialVersionUID = 1L;
	
	@Des("派送人")
	@CondtionExpression(value="psr",type=CondtionType.equal)
	private String psr;
	
	@Des("派送时间(起)")
	@CondtionExpression(value = "pssj", type = CondtionType.greaterthanOrequal)
	private Date times;
	
	@Des("派送时间(止)")
	@CondtionExpression(value = "pssj", type = CondtionType.lessthanOrequal)
	private Date timee;
	
	@Des("工单派送类型-1:12345服务平台2:阳光信访3:市长公开电话")
	@CondtionExpression(value="gdpslx.code",type=CondtionType.equal)
	private String gdpslx;
	
	@Des("受理部门")
	@CondtionExpression(value="blbmNames",type=CondtionType.like)
	private String sldw;
	
	
	@Des("办理人查询")
	@CondtionExpression(joinName="blr",value="user.username",type = CondtionType.like)
	private String blr;
}
