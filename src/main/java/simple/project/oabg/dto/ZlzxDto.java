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
 * 资料中心
 * 2017年9月5日
 * @author yc
 */
@SearchBean
@Paged
@Sorted("createTime desc")
public class ZlzxDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1514862321418778487L;
	

	@Des("文件标题")
	@CondtionExpression(value = "title", type = CondtionType.like)
	private String title;
	
	@Des("资料类型")
	@CondtionExpression(value = "zllx.code", type = CondtionType.equal)
	private String zllx;
	
	@Des("上传时间")
	@CondtionExpression(value = "scsj", type = CondtionType.greaterthanOrequal)
	private Date scsj_s;
	
	@Des("上传时间")
	@CondtionExpression(value = "scsj", type = CondtionType.lessthanOrequal)
	private Date scsj_e;
	
	@Des("上传时间")
	@CondtionExpression(value = "userRealName", type = CondtionType.like)
	private String scyh;
	
}
