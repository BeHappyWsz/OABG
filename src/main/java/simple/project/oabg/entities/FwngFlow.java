package simple.project.oabg.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Tgzt;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 发文拟稿流程记录表
 * @author sxm
 * @created 2017年8月30日
 */
@Des("发文拟稿流程记录表")
@Entity
@Table(name = "t_fwng_flow")
public class FwngFlow extends AutoIDModel {
	
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	@Des("发文拟稿id")
	@Column(name="fwngid",length = 50)
	private String fwngid;
	
	@Getter
	@Setter
	@Des("流程")
	@Column(name="lc",length = 50)
	private String lcName;
	
	@Getter
	@Setter
	@Des("当前节点")
	@Column(name="cnode",length = 20)
	private String cnode;
	
	@Getter
	@Setter
	@Des("当前节点处理人(保存用户的username)")
	@Column(name="puser",length = 20)
	private String puser;
	
	@Getter
	@Setter
	@Des("当前节点处理人(保存用户的realname)")
	@Column(name="realname",length = 20)
	private String realName;
	
	@Getter
	@Setter
	@Des("下一步处理人(保存用户的username)")
	@Column(name="nuser",length = 20)
	private String nuser;
	
	@Getter
	@Setter
	@Des("通过状态")
	@ManyToOne
	private Tgzt tgzt;
	
	@Getter
	@Setter
	@Des("处理意见")
	@Column(name="suggestion",length = 500)
	private String suggestion;
	
	

}
