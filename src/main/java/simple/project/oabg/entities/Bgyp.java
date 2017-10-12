package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import simple.project.oabg.dic.model.Bgypfl;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 办公用品
 * @author sxm
 * @created 2017年9月5日
 */
@Entity
@Table(name="t_bgyp")
public class Bgyp extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("名称")
	@Setter
	@Getter
	@Column(name="name",length=50)
	private String name;
	
	@Getter
	@Setter
	@Des("办公用品分类(1办公用品，2耗材，3其它)")
	@ManyToOne
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Bgypfl bgypfl;
	
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
	
	@Des("品牌")
	@Setter
	@Getter
	@Column(name="pp",length=500)
	private String pp;
	
	@Des("备注")
	@Setter
	@Getter
	@Column(name="bz",length=500)
	private String bz;
	
	@Des("入库时间")
	@Setter
	@Getter
	@Column(name="rksj")
	private Date rksj;
	
}
