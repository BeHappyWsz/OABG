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
 * 用户管理dto
 * @author wsz
 * @created 2017年8月21日
 */
@SearchBean
@Paged
@Sorted("createTime desc")
public class HytzDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1L;

	@Des("会议时间起")
	@CondtionExpression(value="huiyitime",type=CondtionType.greaterthanOrequal)
	private Date timestart;
	
	@Des("会议时间起")
	@CondtionExpression(value="huiyitime",type=CondtionType.lessthanOrequal)
	private Date timeend;
	
	@Des("收件人")
	@CondtionExpression(joinName = "sjr", value = "id", type = CondtionType.equal)
	private String sjr;
}
