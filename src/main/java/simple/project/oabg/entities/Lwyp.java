package simple.project.oabg.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 劳务用品
 * 2017年9月4日
 * @author yc
 */
@Entity
@Table(name="t_lwyp")
public class Lwyp extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("名称")
	@Setter
	@Getter
	@Column(name="name",length=50)
	private String name;
	
	@Des("单位")
	@Setter
	@Getter
	@Column(name="dw",length=50)
	private String dw;
	
	@Des("数量")
	@Setter
	@Getter
	@Column(name="sl")
	private Integer sl;
	
	@Des("警戒数量")
	@Setter
	@Getter
	@Column(name="jjsl")
	private Integer jjsl;
	
	@Des("备注")
	@Setter
	@Getter
	@Column(name="bz",length=500)
	private String bz;
}
