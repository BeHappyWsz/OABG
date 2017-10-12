package simple.project.oabg.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Bmdw;
import simple.project.oabg.dic.model.SF;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;


/**
 * 资产管理
 * @author yinchao
 * @date 2017年9月6日
 */
@Entity
@Table(name="t_zcgl")
public class Zcgl extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("资产名称")
	@Setter
	@Getter
	@Column(name="zcmc",length=50)
	private String zcmc;
	
	@Des("品牌")
	@Setter
	@Getter
	@Column(name="pp",length=50)
	private String pp;
	
	@Des("型号")
	@Setter
	@Getter
	@Column(name="xh",length=50)
	private String xh;
	
	@Des("数量")
	@Setter
	@Getter
	@Column(name="sl")
	private int sl;
	
	@Des("单位")
	@Setter
	@Getter
	@Column(name="dw",length=10)
	private String dw;
	
	@Des("预计使用寿命")
	@Setter
	@Getter
	@Column(name="yjsysm",length=50)
	private String yjsysm;
	
	@Des("备注")
	@Setter
	@Getter
	@Column(name="bz",length=500)
	private String bz;
	
	@Des("部门")
	@Setter
	@Getter
	@ManyToOne
	private Bmdw bm;
	
	@Des("是否销毁")
	@Setter
	@Getter
	@ManyToOne
	private SF sfxh;

	@Des("使用人")
	@Setter
	@Getter
	@Column(name="syr",length=20)
	private String syr;
	
	@Des("数量警戒线")
	@Setter
	@Getter
	@Column(name="jjx")
	private int jjx;
	
}
