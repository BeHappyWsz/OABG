package simple.project.oabg.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.FK;
import simple.project.oabg.dic.model.SF;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 会议反馈
 * @author yzw
 * @created 2017年8月21日
 */
@Des("会议通知")
@Entity
@Table(name="t_HYFK")
public class Hyfk extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("会议id")
	@Setter
	@Getter
	private Long hyid;
	
	@Des("是否参加")
	@Setter
	@Getter
	@ManyToOne
	private SF sfcj;
	
	@Des("不参加理由")
	@Setter
	@Getter
	@Column(name="reason",length=100)
	private String reason;
	
	@Des("收件人")
	@Setter
	@Getter
	private String name;
	
	@Des("收件人部门")
	@Setter
	@Getter
	private String bmname;
	
	@Des("参会具体人员")
	@Setter
	@Getter
	@Lob
	private String chjtry;
	
	@Des("fkzt")
	@Setter
	@Getter
	@ManyToOne
	private FK hyfk;
}
