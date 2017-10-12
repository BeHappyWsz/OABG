package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Tgzt;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 工单派送办理流程记录表
 * 2017年9月27日
 * @author wsz
 */
@Entity
@Table(name = "t_gdpsbl_flow")
@Cacheable
public class GdpsblFlow extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	@Des("工单派送id")
	@Column(name="gdpsid",length = 50)
	private String gdps_id;
	
	@Getter
	@Setter
	@Des("操作时间")
	@Column(name="czsj")
	private Date czsj;
	
	@Getter
	@Setter
	@Des("当前节点")
	@Column(name="cnode",length = 50)
	private String cnode;
	
	@Getter
	@Setter
	@Des("当前节点处理人(保存用户的username)")
	@Column(name="puser",length = 50)
	private String puser;
	
	@Getter
	@Setter
	@Des("当前节点处理人(保存用户的realname)")
	@Column(name="puserName",length = 50)
	private String puserName;
	
	@Getter
	@Setter
	@Des("下一步处理人(保存用户的username)")
	@Column(name="nuser",length = 50)
	private String nuser;
	
	
	@Getter
	@Setter
	@Des("通过状态 (1为通过，2为未通过)")
	@ManyToOne
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Tgzt tgzt;
	
	@Getter
	@Setter
	@Des("处理意见")
	@Column(name="suggestion",length = 500)
	private String suggestion;
}
