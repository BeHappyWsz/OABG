package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Tylc;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 劳务用品--申请记录
 * 2017年9月4日
 * @author yc
 */
@Entity
@Table(name="t_lwyp_sqjl")
public class LwypSqjl extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("劳务用品id")
	@Setter
	@Getter
	@Column(name="lwypId")
	private Long lwypId;
	
	@Des("申请人")
	@Setter
	@Getter
	@Column(name="sqr",length=100)
	private String sqr;
	
	@Des("申请人(账户名)")
	@Setter
	@Getter
	@Column(name="sqrUserName",length=100)
	private String sqrUserName;
	
	@Des("申请部门")
	@Setter
	@Getter
	@Column(name="sqbm",length=100)
	private String sqbm;
	
	@Des("联系电话")
	@Setter
	@Getter
	@Column(name="lxdh",length=50)
	private String lxdh;
	
	@Des("申请日期")
	@Setter
	@Getter
	@Column(name="sqrq")
	private Date sqrq;
	
	@Des("物品名称")
	@Setter
	@Getter
	@Column(name="name",length=50)
	private String name;
	
	@Des("数量")
	@Setter
	@Getter
	@Column(name="sl")
	private Integer sl;

	@Des("单位")
	@Setter
	@Getter
	@Column(name="dw",length=50)
	private String dw;
	
	@Des("申请原因")
	@Setter
	@Getter
	@Column(name="sqyy",length=500)
	private String sqyy;
	
	@Des("流程状态 1:待审批 2：已审批 9:未通过")
	@Setter
	@Getter
	@ManyToOne
	private Tylc lczt;
}
