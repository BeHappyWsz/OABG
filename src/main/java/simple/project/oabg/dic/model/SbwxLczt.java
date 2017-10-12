package simple.project.oabg.dic.model;

import groovy.transform.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import org.hibernate.annotations.Subselect;

import simple.system.simpleweb.module.system.dic.model.DicSelectModel;
import simple.system.simpleweb.platform.annotation.Des;

@Des("设备维修流程状态1待审批2已审批9未通过")
@Entity
@Immutable
@Subselect("select * from v_dicitems where dcode='"+SbwxLczt.DICCODE+"'")
@Cacheable
public class SbwxLczt extends DicSelectModel{

	private static final long serialVersionUID = 1L;
	
	protected static final String  DICCODE="SBWXLCZT";
}
