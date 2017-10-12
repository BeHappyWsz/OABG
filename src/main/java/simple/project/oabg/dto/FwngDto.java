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
 * 发文拟稿dto
 * @author sxm
 * @created 2017年8月28日
 */

@SearchBean
@Paged
@Sorted("createTime desc")
public class FwngDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1514862321418778487L;
	
	@Des("机密程度")
	@CondtionExpression(value="mj",type=CondtionType.like)
	private String jmcd;
	
	@Des("发文状态")
	@CondtionExpression(value="zt.code",type=CondtionType.equal)
	private String zt;
	
	@Des("发文日期(起)")
	@CondtionExpression(value = "ngrq", type = CondtionType.greaterthanOrequal)
	private Date times;
	
	@Des("发文日期(止)")
	@CondtionExpression(value = "ngrq", type = CondtionType.lessthanOrequal)
	private Date timee;
	
	@Des("登入用户")
	@CondtionExpression(value="username",type=CondtionType.like)
	private String username;
	
	@Des("通过状态")
	@CondtionExpression(value="tgzt.code",type=CondtionType.like)
	private String tgzt;
	
	@Des("登入用户")
	@CondtionExpression(value="ngr",type=CondtionType.equal)
	private String ngr;
	
	@Des("流程状态")
	@CondtionExpression(value="fwzt.code",type=CondtionType.in)
	private String fwzt;
	
}
