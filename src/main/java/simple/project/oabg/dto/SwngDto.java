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
 * 收文拟稿dto
 * @author wsz
 * @created 2017年9月4日
 */
@SearchBean
@Paged
@Sorted("createTime desc")
public class SwngDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1L;
	
	@Des("收文人")
	@CondtionExpression(value="swr",type=CondtionType.equal)
	private String swr;
	
	@Des("文件(收文)来源")
	@CondtionExpression(value="wjly.code",type=CondtionType.equal)
	private String wjly;
	
	@Des("机密程度")
	@CondtionExpression(value="mj.code",type=CondtionType.equal)
	private String jmcd;
	
	@Des("缓急程度")
	@CondtionExpression(value="hj.code",type=CondtionType.equal)
	private String hjcd;
	
	@Des("收文状态")
	@CondtionExpression(value="swzt.code",type=CondtionType.equal)
	private String swzt;
	
	@Des("角色权限进行判断收文状态")
	@CondtionExpression(prameType=String.class,value="swzt.code",type=CondtionType.in)
	private List<String> swzts;
	
	@Des("收文日期(起)")
	@CondtionExpression(value = "swrq", type = CondtionType.greaterthanOrequal)
	private Date times;
	
	@Des("收文日期(止)")
	@CondtionExpression(value = "swrq", type = CondtionType.lessthanOrequal)
	private Date timee;
	
	@Des("分管领导")
	@CondtionExpression(joinName = "fgld" ,value = "name", type = CondtionType.equal)
	private String fgld;
	
	@Des("收文类型1正常收文2特殊收文")
	@CondtionExpression(value="swlx.code",type=CondtionType.equal)
	private String swlx;
	
	@Des("特殊收文类型1人大提案2信访处理")
	@CondtionExpression(value="tsswlx.code",type=CondtionType.equal)
	private String tsswlx;
	
	
	@Des("收件人查询")
	@CondtionExpression(joinName="sjr",value="user.username",type = CondtionType.like)
	private String sjr;
	
	
	@Des("传阅人查询")
	@CondtionExpression(joinName="cyr",value="user.username",type = CondtionType.like)
	private String cyr;
	
	@Des("收文字号")
	@CondtionExpression(value="fwzh",type=CondtionType.like)
	private String fwzh;
	
	@Des("收文机关")
	@CondtionExpression(value="fwjg",type=CondtionType.like)
	private String fwjg;
	
	@Des("文件标题")
	@CondtionExpression(value="bt",type=CondtionType.like)
	private String bt;
}
