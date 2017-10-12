package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Tgzt;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 设备维修申请流程表
 * @author wsz
 * @date 2017年9月20日
 */
@Entity
@Table(name="t_sbwx_sqflow")
public class SbwxSqFlow extends AutoIDModel{
	
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	@Des("关联id")
	@Column(length = 50)
	private String glid;
	
	@Getter
	@Setter
	@Des("当前节点")
	@Column(length = 20)
	private String cnode;
	
	@Getter
	@Setter
	@Des("当前节点处理人(保存用户的username)")
	@Column(length = 20)
	private String puser;
	
	@Getter
	@Setter
	@Des("当前节点处理人(保存用户的realname)")
	@Column(length = 20)
	private String puserName;
	
	
	@Getter
	@Setter
	@Des("下一个节点处理人(保存用户的username)")
	@Column(length = 20)
	private String nuser;
	
	@Getter
	@Setter
	@Des("下一个节点处理人(保存用户的realname)")
	@Column(length = 20)
	private String nuserName;
	
	@Getter
	@Setter
	@Des("通过状态 (1为通过，2为未通过)")
	@ManyToOne
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Tgzt tgzt;
	
	@Getter
	@Setter
	@Des("处理意见")
	@Column(length = 500)
	private String suggestion;
	
	@Getter
	@Setter
	@Des("处理时间")
	@Temporal(TemporalType.TIMESTAMP)
	private Date clsj;
}
