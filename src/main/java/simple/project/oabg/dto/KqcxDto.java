package simple.project.oabg.dto;

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
public class KqcxDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1L;
	
	@Des("请假类型")
	@CondtionExpression(value = "qjlx.code", type = CondtionType.equal)
	private String qjlx;
	
	@Des("部门单位")
	@CondtionExpression(value = "bmdw.code", type = CondtionType.equal)
	private String bmdw;
	
	
	@Des("是否出差")
	@CondtionExpression(value = "sfcc.code", type = CondtionType.equal)
	private String sfcc;
	
}
