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
 * 我的待办dto
 * 2017年9月8日
 * @author yc
 */
@SearchBean
@Paged
@Sorted("createTime desc")
public class WddbDto extends SearchIsdeleteDto {

	private static final long serialVersionUID = 1L;
	
	@Des("数据来源")
	@CondtionExpression(value="sjly.code",type=CondtionType.equal)
	private String sjly;
	
	@Des("是否已办")
	@CondtionExpression(value="sfyb.code",type=CondtionType.equal)
	private String sfyb;
	
	@Des("是否销毁")
	@CondtionExpression(value="sfxh.code",type=CondtionType.equal)
	private String sfxh;
	
	@Des("标题")
	@CondtionExpression(value="title",type=CondtionType.like)
	private String title;
	
	@Des("收件人")
	@CondtionExpression(value="sjr",type=CondtionType.like)
	private String sjr;
	
	@Des("收件日期(起)")
	@CondtionExpression(value = "sjsj", type = CondtionType.greaterthanOrequal)
	private Date sjsj_s;
	
	@Des("收件日期(止)")
	@CondtionExpression(value = "sjsj", type = CondtionType.lessthanOrequal)
	private Date sjsj_e;
	
}
