package simple.project.oabg.dto;

import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.annotation.search.CondtionExpression;
import simple.system.simpleweb.platform.annotation.search.CondtionType;
import simple.system.simpleweb.platform.annotation.search.Paged;
import simple.system.simpleweb.platform.annotation.search.SearchBean;
import simple.system.simpleweb.platform.annotation.search.Sorted;
import simple.system.simpleweb.platform.dao.jpa.SearchIsdeleteDto;

/**
 * 发文拟稿流程dto
 * @author sxm
 * @created 2017年8月28日
 */

@SearchBean
@Paged
@Sorted("createTime desc")
public class FwngFlowDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1514862321418778487L;
	
	@Des("用户名查询")
	@CondtionExpression(value="nuser",type=CondtionType.like)
	private String nuser;
	
	@Des("通过状态")
	@CondtionExpression(value="tgzt.code",type=CondtionType.equal)
	private String tgzt;
	
	@Des("操作状态")
	@CondtionExpression(value="state",type=CondtionType.like)
	private String state;
}
