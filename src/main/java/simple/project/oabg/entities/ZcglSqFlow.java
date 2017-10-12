package simple.project.oabg.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 资产申请流程表
 * 
 * @author yinchao
 * @date 2017年9月7日
 */
@Entity
@Table(name="t_zcgl_sqflow")
public class ZcglSqFlow extends AutoIDModel{
	
	private static final long serialVersionUID = 1L;
	
	@Des("操作人")
	@Setter
	@Getter
	@Column(name="czr" , length=20)
	private String czr;
	
	@Des("操作时间")
	@Setter
	@Getter
	@Column(name="czsj" , length=50)
	private String czsj;
	
	@Des("操作名称")
	@Setter
	@Getter
	@Column(name="czmc" , length=50)
	private String czmc;
	
	@Des("是否通过")
	@Setter
	@Getter
	@Column(name="tgzt" , length=50)
	private String tgzt;
	
	@Des("申请记录ID")
	@Setter
	@Getter
	@Column(name="zcglSqjlId")
	private long zcglSqjlId;
	
	@Getter
	@Setter
	@Des("处理意见")
	@Column(length = 500)
	private String suggestion;

}
