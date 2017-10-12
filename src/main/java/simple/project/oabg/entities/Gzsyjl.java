package simple.project.oabg.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Bmdw;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 公章使用情况
 * @author yzw
 * @created 2017年8月21日
 */
@Des("公章使用记录")
@Entity
@Table(name="t_GZSYJL")
public class Gzsyjl extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("公章名称")
	@Setter
	@Getter
	private String gzname;
	
	@Des("公章使用部门")
	@Setter
	@Getter
	@ManyToOne
	private Bmdw bmdw;
	
	@Des("公章使用人")
	@Setter
	@Getter
	private String name;
	
	@Des("盖章数量")
	@Setter
	@Getter
	private String gzsl;
	
}
