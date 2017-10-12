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
 * 资产申请记录
 * @author yinchao
 * @date 2017年9月6日
 */
@SearchBean
@Paged
@Sorted({"zcglsplc.orderNum asc","createTime desc"})
public class ZcglSqjlDto extends SearchIsdeleteDto{
	private static final long serialVersionUID = 1L;
	
	@Des("申请日期起")
	@CondtionExpression(value = "sqrq", type = CondtionType.greaterthanOrequal)
	private Date sqrqs;
	
	@Des("申请日期止")
	@CondtionExpression(value = "sqrq", type = CondtionType.lessthanOrequal)
	private Date sqrqe;
	
	
	@Des("申请类型")
	@CondtionExpression(value = "sqlx.code", type = CondtionType.equal)
	private String sqlx;
	
	@Des("通过状态")
	@CondtionExpression(value = "tgzt.code", type = CondtionType.notequal)
	private String tgzt;
	
	@Des("申请类型")
	@CondtionExpression(value = "zcglsplc.code", type = CondtionType.equal)
	private String zcglsplc;
	
	@Des("当前username")
	@CondtionExpression(value = "sqrUserName", type = CondtionType.equal)
	private String sqrUserName;
	
	@Des("所属部门")
	@CondtionExpression(value = "sqbm.code", type = CondtionType.equal)
	private String sqbm;
	
	@Des("所属分管部门")
	@CondtionExpression(prameType=String.class, value = "sqbm.code", type = CondtionType.in)
	private List<String> fqbmsList;
	
	@Des("可查看的状态")
	@CondtionExpression(prameType=String.class, value = "zcglsplc.code", type = CondtionType.in)
	private List<String> zcglsplcJz;
	
	@Des("资产名称")
	@CondtionExpression(value = "zcmc", type = CondtionType.like)
	private String zcmc;

}
