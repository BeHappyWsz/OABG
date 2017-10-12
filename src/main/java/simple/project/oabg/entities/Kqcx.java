package simple.project.oabg.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Bmdw;
import simple.project.oabg.dic.model.Qjlx;
import simple.project.oabg.dic.model.SF;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 考勤管理
 * @author yzw
 * @created 2017年8月21日
 */
@Des("考勤查询")
@Entity
@Table(name="t_Kqcx")
public class Kqcx extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("名字")
	@Setter
	@Getter
	@Column(name="name",length=20)
	private String name;
	
	@Des("部门")
	@Setter
	@Getter
	@ManyToOne
	private Bmdw bmdw;
	
	@Des("请假类型")
	@Setter
	@Getter
	@ManyToOne
	private Qjlx qjlx;
	
	
	@Des("是否出差")
	@Setter
	@Getter
	@ManyToOne
	private SF sfcc;
	
	@Des("出差地点")
	@Setter
	@Getter
	@Column(name="ccaddress",length=100)
	private String ccaddress;
}
