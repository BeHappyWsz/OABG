package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Gzjh;
import simple.project.oabg.dic.model.SF;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 工作计划
 * @author yzw
 * @created 2017年9月25日
 */
@Des("工作计划")
@Entity
@Table(name="t_GZJH")
public class Gzap extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("工作计划分类")
	@Setter
	@Getter
	@ManyToOne
	private Gzjh gzjh;
	
	@Des("名字")
	@Setter
	@Getter
	private String name;
	
	@Des("计划标题")
	@Setter
	@Getter
	private String jhbt;
	
	@Des("开始时间")
	@Setter
	@Getter
	@Temporal(TemporalType.DATE)
	private Date kssj;
	
	@Des("结束时间")
	@Setter
	@Getter
	@Temporal(TemporalType.DATE)
	private Date jssj;
	
	@Des("是否提交")
	@Setter
	@Getter
	@ManyToOne
	private SF sftj;
	
}
