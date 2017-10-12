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
 * 短信日志
 * 2017年9月6日
 * @author yc
 */
@SearchBean
@Paged
@Sorted("createTime desc")
public class DxrzDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1514862321418778487L;
	

	@Des("发送时间起")
	@CondtionExpression(value = "fssj", type = CondtionType.greaterthanOrequal)
	private Date fssj_s;
	
	@Des("发送时间止")
	@CondtionExpression(value = "fssj", type = CondtionType.lessthanOrequal)
	private Date fssj_e;
	
	@Des("接收人")
	@CondtionExpression(value = "jsr", type = CondtionType.like)
	private String jsr;
	
	@Des("手机号")
	@CondtionExpression(value = "tel", type = CondtionType.like)
	private String tel;
	
	@Des("发送状态")
	@CondtionExpression(value = "fszt.code", type = CondtionType.equal)
	private String fszt;
	
}
