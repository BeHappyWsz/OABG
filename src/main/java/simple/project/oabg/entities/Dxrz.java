package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Dxfszt;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 短信发送日志
 * 2017年9月4日
 * @author yc
 */
@Entity
@Table(name = "t_dxrz")
public class Dxrz extends AutoIDModel {
	
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	@Des("发送时间")
	@Column(length=50)
	private Date fssj;
	
	@Getter
	@Setter
	@Des("接收人")
	@Column(length=50)
	private String jsr;
	
	@Getter
	@Setter
	@Des("接收人电话号码")
	@Column(length=50)
	private String tel;
	
	@Getter
	@Setter
	@Des("内容")
	@Column(length=500)
	private String nr;
	
	@Getter
	@Setter
	@Des("发送原因")
	@Column(length=100)
	private String fsyy;
	
	@Des("发送结果 1：成功，2：失败")
	@Setter
	@Getter
	@ManyToOne
	private Dxfszt fszt;
}
