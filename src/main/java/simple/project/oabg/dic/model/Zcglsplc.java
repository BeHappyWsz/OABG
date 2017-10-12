package simple.project.oabg.dic.model;

import groovy.transform.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import org.hibernate.annotations.Subselect;

import simple.system.simpleweb.module.system.dic.model.DicSelectModel;
import simple.system.simpleweb.platform.annotation.Des;

@Des("资产管理审批流程")
@Entity
@Immutable
@Subselect("select * from v_dicitems where dcode='"+Zcglsplc.DICCODE+"'")
@Cacheable
public class Zcglsplc extends DicSelectModel{

	private static final long serialVersionUID = 1L;
	protected static final String  DICCODE="ZCGLSPLC";

}
