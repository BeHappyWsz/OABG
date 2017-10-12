package simple.project.oabg.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Bmdw;
import simple.project.oabg.dic.model.Kqlcsp;
import simple.project.oabg.dic.model.Qjlx;
import simple.project.oabg.dic.model.SF;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 考勤管理
 * @author yzw
 * @created 2017年8月21日
 */
@Des("请假申请")
@Entity
@Table(name="t_KQGL")
public class Kqgl extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("名字")
	@Setter
	@Getter
	@Column(name="name",length=20)
	private String name;
	
	@Des("部门")
	@Setter
	@Getter
	@ManyToOne
	private Bmdw bmdw;
	
	@Des("联系方式")
	@Setter
	@Getter
	@Column(name="lxfs",length=25)
	private String lxfs;
	
	@Des("请假类型")
	@Setter
	@Getter
	@ManyToOne
	private Qjlx qjlx;
	
	@Des("出差/请假时间起")
	@Setter
	@Getter
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStr;
	
	@Des("出差/请假时间止")
	@Setter
	@Getter
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeEnd;
	
	@Des("出差/请假事由")
	@Setter
	@Getter
	@Column(name="sy",length=100)
	private String sy;
	
	@Des("是否出差")
	@Setter
	@Getter
	@ManyToOne
	private SF sfcc;
	
	@Des("出差地点")
	@Setter
	@Getter
	@Column(name="ccaddress",length=100)
	private String ccaddress;
	
	@Des("备注")
	@Setter
	@Getter
	@Column(name="bz",length=100)
	private String bz;
	
	@Des("流程状态 1:待审批 2：已审批 9:未通过")
	@Setter
	@Getter
	@ManyToOne
	private Kqlcsp lczt;
	
	@Des("该部门处长名字")
	@Setter
	@Getter
	@Column(name="czName",length=50)
	private String czName;
	
	@Des("分管部门领导名字")
	@Setter
	@Getter
	@Column(name="fgldName",length=50)
	private String fgldName;
	
	@Des("局长名字")
	@Setter
	@Getter
	@Column(name="jzName",length=50)
	private String jzName;
	
	@Des("审批次数")
	@Setter
	@Getter
	@Column(name="times",length=50)
	private String times;
	
	@Des("局长审批次数")
	@Setter
	@Getter
	@Column(name="jzspcs",length=50)
	private String jzspcs;
	
	@Des("出差人员姓名")
	@Setter
	@Getter
	@Lob
	@Column(name="ccrname")
	private String ccrname;
	
	@Des("是否用车")
	@Setter
	@Getter
	@Lob
	@Column(name="sfyc")
	private String sfyc;
	
	@Getter
	@Setter
	@Des("出差人员")
	@ManyToMany
	@JoinTable(
			name ="t_ccsq_ccry",
			joinColumns = {@JoinColumn(name="hyId")},
			inverseJoinColumns = {@JoinColumn(name="txlID")}
			)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<Txl> ccry; 
}
