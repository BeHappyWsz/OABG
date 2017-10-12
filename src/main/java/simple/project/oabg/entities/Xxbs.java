package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Bmdw;
import simple.project.oabg.dic.model.SF;
import simple.project.oabg.dic.model.Xxbsly;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 会议通知
 * @author yzw
 * @created 2017年8月21日
 */
@Des("会议通知")
@Entity
@Table(name="t_xxbs")
public class Xxbs extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Setter
	@Getter
	@Des("报送部门")
	@ManyToOne
	private Bmdw bsbm;
	
	@Setter
	@Getter
	@Des("报送时间")
	@Temporal(TemporalType.TIMESTAMP)
	private Date bssj;
	
	@Setter
	@Getter
	@Des("报送标题")
	@Column(name="bsbt",length = 50)
	private String bsbt;
	
	@Setter
	@Getter
	@Des("报送人姓名")
	@Column(name="bsxm",length = 20)
	private String bsxm;
	
	@Setter
	@Getter
	@Des("录用人姓名")
	@Column(name="lyrxm",length = 20)
	private String lyrxm;
	
	@Setter
	@Getter
	@Des("报送内容")
	@Lob
	private String bsnr;
	
	@Setter
	@Getter
	@Des("是否录用")
	@ManyToOne
	private SF sfly;
	
	@Des("流程状态 1:待审批 2：已审批 9:未通过")
	@Setter
	@Getter
	@ManyToOne
	private Xxbsly lczt;
	
	@Setter
	@Getter
	@Des("录用分数")
	@Column(name="score",columnDefinition="float default 0")
	private Double score;
}
