package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Crk;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 资产管理入库记录
 * @author yinchao
 * @date 2017年9月6日
 */
@Entity
@Table(name="t_zcgl_rkjl")
public class ZcglRkjl extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("资产管理id")
	@Setter
	@Getter
	@Column(name="zcglId")
	private Long zcglId;
	
	@Des(" 1入库  2出库 9未变 5盘点")
	@Setter
	@Getter
	@ManyToOne
	private Crk crk;
	
	@Des("数量")
	@Setter
	@Getter
	@Column(name="sl")
	private int sl;
	
	@Des("单位")
	@Setter
	@Getter
	@Column(name="dw",length=50)
	private String dw;
	
	@Des("日期")
	@Setter
	@Getter
	@Column(name="date")
	private Date date;
	
	@Des("操作人")
	@Setter
	@Getter
	@Column(name="username",length=100)
	private String username;
	
	@Des("品牌")
	@Setter
	@Getter
	@Column(name="pp",length=500)
	private String pp;
}
