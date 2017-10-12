package simple.project.oabg.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.SF;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 工作计划
 * @author yzw
 * @created 2017年9月25日
 */
@Des("工作计划阅读")
@Entity
@Table(name="t_GZJHYD")
public class GzapYd extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("工作计划ID")
	@Setter
	@Getter
	private Long gzjh;
	
	@Des("阅读人")
	@Setter
	@Getter
	private String name;
	
	@Des("是否阅读")
	@Setter
	@Getter
	@ManyToOne
	private SF sfyd;
}
