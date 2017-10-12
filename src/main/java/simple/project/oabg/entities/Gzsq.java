package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Bmdw;
import simple.project.oabg.dic.model.Tylc;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 公章登记
 * @author yzw
 * @created 2017年8月21日
 */
@Des("公章申请")
@Entity
@Table(name="t_GZSQ")
public class Gzsq extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("公章名称")
	@Setter
	@Getter
	private String gzname;
	
	@Des("申请人姓名")
	@Setter
	@Getter
	private String sqname;
	
	@Des("申请人用户名")
	@Setter
	@Getter
	private String squsername;
	
	@Des("申请人部门")
	@Setter
	@Getter
	@ManyToOne
	private Bmdw bmdw;
	
	@Des("盖章时间")
	@Setter
	@Getter
	@Temporal(TemporalType.TIMESTAMP)
	private Date gztime;
	
	@Des("申请事由")
	@Setter
	@Getter
	private String sqreason;
	
	@Des("盖章数量")
	@Setter
	@Getter
	private String gzsl;
	
	@Des("流程状态 1:待审批 2：已审批 9:未通过")
	@Setter
	@Getter
	@ManyToOne
	private Tylc lczt;
	
}
