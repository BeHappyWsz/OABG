package simple.project.oabg.entities;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import simple.project.oabg.dic.model.Tgzt;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 劳务用品申请流程记录表
 * 2017年9月4日
 * @author yc
 */
@Entity
@Table(name = "t_lwypsq_flow")
@Cacheable
public class LwypSqFlow extends AutoIDModel {
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
	@Des("当前节点处理人(保存用户的realName)")
	@Column(length = 200)
	private String realName;
	
	@Getter
	@Setter
	@Des("下一步处理人(保存用户的username)")
	@Column(length = 20)
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
	@Column(length = 500)
	private String suggestion;
}
