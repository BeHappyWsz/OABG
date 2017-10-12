package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Bmdw;
import simple.project.oabg.dic.model.SbwxLczt;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 设备维修 申请记录
 * @author wsz
 * @date 2017年9月20日
 */
@Des("设备维修申请")
@Entity
@Table(name="t_sbwx_sqjl")
public class SbwxSqjl extends AutoIDModel{
	
	private static final long serialVersionUID = 1L;
	
	@Des("申请人username")
	@Setter
	@Getter
	@Column(name="sqrUserName",length=20)
	private String sqrUserName;
	
	@Des("申请人realname")
	@Setter
	@Getter
	@Column(name="sqrRealName",length=20)
	private String sqrRealName;
	
	@Des("报修日期")
	@Setter
	@Getter
	@Column(name="bxrq")
	private Date bxrq;
	
	@Des("报修部门")
	@Setter
	@Getter
	@ManyToOne
	private Bmdw bxbm;
	
	@Des("经手人")
	@Setter
	@Getter
	@Column(name="jsr",length=20)
	private String jsr;
	
	@Des("部门负责人")
	@Setter
	@Getter
	@Column(name="bmfzr",length=20)
	private String bmfzr;
	
	@Des("故障原因")
	@Setter
	@Getter
	@Column(name="gzyy",length=200)
	private String gzyy;
	
	
	@Des("报修流程状态1待审批2已审批3已维修9未通过")
	@Setter
	@Getter
	@ManyToOne
	private SbwxLczt sbwxLczt;
}
