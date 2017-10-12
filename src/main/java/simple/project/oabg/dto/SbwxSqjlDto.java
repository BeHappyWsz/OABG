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
 * 设备维修申请记录
 * @author wsz
 * @date 2017年9月20日
 */
@SearchBean
@Paged
@Sorted({"sbwxLczt.code asc","createTime desc"})
public class SbwxSqjlDto extends SearchIsdeleteDto{
	
	private static final long serialVersionUID = 1L;
	
	@Des("申请人username")
	@CondtionExpression(value = "sqrUserName", type = CondtionType.equal)
	private String sqrUserName;
	
	@Des("报修日期(起)")
	@CondtionExpression(value = "bxrq", type = CondtionType.greaterthanOrequal)
	private Date bxrqs;
	
	@Des("报修日期(止)")
	@CondtionExpression(value = "bxrq", type = CondtionType.lessthanOrequal)
	private Date bxrqe;
	
	@Des("经手人jsr")
	@CondtionExpression(value = "jsr", type = CondtionType.like)
	private String jsr;
	
	@Des("部门负责人")
	@CondtionExpression(value = "bmfzr", type = CondtionType.like)
	private String bmfzr;
	
	@Des("报修部门")
	@CondtionExpression(value = "bxbm.name", type = CondtionType.like)
	private String bxbm;
	
	@Des("报修部门")
	@CondtionExpression(prameType=String.class,value = "bxbm.code", type = CondtionType.in)
	private List<String> bxbms;
	
	@Des("设备维修流程状态")
	@CondtionExpression(value = "sbwxLczt.code", type = CondtionType.equal)
	private String sbwxLczt;
	
	@Des("设备维修流程状态")
	@CondtionExpression(prameType=String.class , value = "sbwxLczt.code", type = CondtionType.in)
	private List<String> sbwxLczts;
	
	@Des("故障原因")
	@CondtionExpression(value = "gzyy", type = CondtionType.like)
	private String gzyy;
	
}
