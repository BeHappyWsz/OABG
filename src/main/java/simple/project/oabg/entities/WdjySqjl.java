package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Gh;
import simple.project.oabg.dic.model.Tgzt;
import simple.project.oabg.dic.model.Tylc;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 文档借阅--申请记录
 * 2017年9月8日
 */
@Entity
@Table(name="t_wdjy_sqjl")
public class WdjySqjl extends AutoIDModel{
	
	private static final long serialVersionUID = 1L;
	
	@Des("文档id")
	@Setter
	@Getter
	@Column(name="wdId")
	private Long wdId;
	
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
	
	@Des("文档标题")
	@Setter
	@Getter
	@Column(name="wdbt",length=100)
	private String wdbt;
	
	@Des("文档编号")
	@Setter
	@Getter
	@Column(name="wdbh",length=50)
	private String wdbh;
	
	@Des("借阅原因")
	@Setter
	@Getter
	@Column(name="sqyy",length=500)
	private String sqyy;
	
	@Des("借阅时间(起)")
	@Setter
	@Getter
	@Column(name="jysjs")
	private Date jysjs;
	
	@Des("借阅时间(止)")
	@Setter
	@Getter
	@Column(name="jysje")
	private Date jysje;
	
	
	@Des("流程状态 1:待审批 2：已审批 9:未通过")
	@Setter
	@Getter
	@ManyToOne
	private Tylc lczt;
	
	@Des("通过状态 1:通过 2：不通过")
	@Setter
	@Getter
	@ManyToOne
	private Tgzt tgzt;
	
	@Des("通过建议")
	@Setter
	@Getter
	@Column(name="suggestion",length=500)
	private String suggestion;
	
	@Des("归还状态 1:已归还 2：未归还")
	@Setter
	@Getter
	@ManyToOne
	private Gh gh;
}
