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
 * 公章登记
 * @author yzw
 * @created 2017年8月21日
 */
@Des("公章登记")
@Entity
@Table(name="t_GZDJ")
public class Gzdj extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("公章名称")
	@Setter
	@Getter
	private String gzname;
	
	@Des("公章管理员")
	@Setter
	@Getter
	private String glyname;
	
	@Des("联系方式")
	@Setter
	@Getter
	private String titles;
	
	@Des("公章使用部门")
	@Setter
	@Getter
	@ManyToOne
	private Bmdw bmdw;
	
	
}
