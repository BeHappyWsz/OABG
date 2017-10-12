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
 * 档案管理之归档登记dto
 * @author wsz
 * @created 2017年9月4日
 */

@SearchBean
@Paged
@Sorted("createTime desc")
public class GddjDto extends SearchIsdeleteDto {

	private static final long serialVersionUID = 1L;
	
	@Des("归档类型1正常收文2发文3特殊收文")
	@CondtionExpression(value="gdlx.code",type=CondtionType.equal)
	private String gdlx;
	
	@Des("归档日期(起)")
	@CondtionExpression(value = "gdrq", type = CondtionType.greaterthanOrequal)
	private Date times;
	
	@Des("归档日期(止)")
	@CondtionExpression(value = "gdrq", type = CondtionType.lessthanOrequal)
	private Date timee;
	
	@Des("文件名称")
	@CondtionExpression(value="bt",type=CondtionType.like)
	private String bt;
	
	@Des("存档人")
	@CondtionExpression(value="cdr",type=CondtionType.like)
	private String cdr;
	
	@Des("档案编号")
	@CondtionExpression(value="dabh",type=CondtionType.like)
	private String dabh;
	
	
	@Des("备注")
	@CondtionExpression(value="bz",type=CondtionType.like)
	private String bz;
}
