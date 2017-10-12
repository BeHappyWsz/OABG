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
 * 劳务用品入库记录
 * 2017年9月4日
 * @author yc
 */
@Entity
@Table(name="t_lwyp_rkjl")
public class LwypRkjl extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("名称")
	@Setter
	@Getter
	@ManyToOne
	@JoinColumn(nullable = true)
	private Lwyp lwyp;
	
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
	
	@Des("备注")
	@Setter
	@Getter
	@Column(name="bz",length=500)
	private String bz;
}
