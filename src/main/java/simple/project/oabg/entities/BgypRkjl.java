package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Crk;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 办公用品入库记录
 * @author sxm
 * @created 2017年9月5日
 */
@Entity
@Table(name="t_bgyp_rkjl")
public class BgypRkjl extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("名称")
	@Setter
	@Getter
	@ManyToOne
	@JoinColumn(nullable = true)
	private Bgyp bgyp;
	
	@Des("出入库 1为入库  2为出库 9为未变")
	@Setter
	@Getter
	@ManyToOne
	private Crk crk;
	
	@Des("数量")
	@Setter
	@Getter
	@Column(name="sl")
	private Integer sl;
	
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
	@Column(name="bz",length=500)
	private String bz;
}
