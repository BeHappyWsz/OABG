package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 文档借阅申请流程记录表
 * @author wsz
 * @created 2017年9月8日
 */
@Entity
@Table(name = "t_wdjy_flow")
public class WdjySqFlow extends AutoIDModel{
	
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	@Des("关联id")
	@Column(length = 50)
	private String glid;
	
	@Getter
	@Setter
	@Des("处理时间")
	@Column(length = 50)
	private Date czsj;
	
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
	@Des("下一步处理人(保存用户的username)")
	@Column(length = 20)
	private String nuser;
	
	@Getter
	@Setter
	@Des("通过状态 ")
	@Column(length = 20)
	private String tgzt;
	
	@Getter
	@Setter
	@Des("处理意见")
	@Column(length = 500)
	private String suggestion;
}
