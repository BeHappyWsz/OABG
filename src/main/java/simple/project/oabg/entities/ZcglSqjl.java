package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Bmdw;
import simple.project.oabg.dic.model.Sqlx;
import simple.project.oabg.dic.model.Tgzt;
import simple.project.oabg.dic.model.Zcglsplc;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 资产管理 申请记录
 * @author yinchao
 * @date 2017年9月6日
 */
@Entity
@Table(name="t_zcgl_sqjl")
public class ZcglSqjl extends AutoIDModel{
	
	private static final long serialVersionUID = 1L;
	
	@Des("资产管理id")
	@Setter
	@Getter
	@Column(name="zcglId")
	private Long zcglId;
	
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
	
	@Des("申请日期")
	@Setter
	@Getter
	@Column(name="sqrq")
	private Date sqrq;
	
	@Des("申请部门")
	@Setter
	@Getter
	@ManyToOne
	private Bmdw sqbm;
	
	@Des("联系电话")
	@Setter
	@Getter
	@Column(name="lxdh",length=50)
	private String lxdh;
	
	@Des("资产名称")
	@Setter
	@Getter
	@Column(name="zcmc",length=50)
	private String zcmc;
	
	@Des("品牌")
	@Setter
	@Getter
	@Column(name="pp",length=50)
	private String pp;
	
	@Des("数量")
	@Setter
	@Getter
	@Column(name="sl")
	private Integer sl;
	
	@Des("型号")
	@Setter
	@Getter
	@Column(name="xh",length=50)
	private String xh;
	
	@Des("申请类型")
	@Setter
	@Getter
	@ManyToOne
	private Sqlx sqlx;
	
	@Des("审批流程")
	@Setter
	@Getter
	@ManyToOne
	private Zcglsplc zcglsplc;
	
	@Des("申请原因")
	@Setter
	@Getter
	@Column(name="sqyy",length=500)
	private String sqyy;
	
	@Des("销毁原因")
	@Setter
	@Getter
	@Column(name="xhyy",length=500)
	private String xhyy;
	
	@Des("审批通过与否")
	@Setter
	@Getter
	@ManyToOne
	private Tgzt tgzt;
	
	@Des("审批意见")
	@Setter
	@Getter
	@Column(name="suggestion",length=500)
	private String suggestion;
	
}
