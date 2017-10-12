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
 * 公章申请dto
 * @author YZW
 * @created 2017年9月18日
 */
@SearchBean
@Paged
@Sorted({"lczt.code asc","createTime desc"})
public class GzsqDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1L;
	
	@Des("姓名查询")
	@CondtionExpression(value="sqname",type=CondtionType.like)
	private String sqname;
	
	@Des("部门")
	@CondtionExpression(prameType=String.class,value = "bmdw.code", type = CondtionType.in)
	private List<String> bmdws;
	
	@Des("流程状态")
	@CondtionExpression(prameType=String.class,value = "lczt.code", type = CondtionType.in)
	private List<String> lczts;
	
	@Des("用户名查询")
	@CondtionExpression(value="squsername",type=CondtionType.like)
	private String squsername;
	
	@Des("盖章时间起")
	@CondtionExpression(value="gztime",type=CondtionType.greaterthanOrequal)
	private Date gztimeStr;
	
	@Des("盖章时间止")
	@CondtionExpression(value="gztime",type=CondtionType.lessthanOrequal)
	private Date gztimeEnd;
	
}