package simple.project.oabg.dic.model;

import groovy.transform.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import org.hibernate.annotations.Subselect;

import simple.system.simpleweb.module.system.dic.model.DicSelectModel;
import simple.system.simpleweb.platform.annotation.Des;

@Des("通过状态")
@Entity
@Immutable
@Subselect("select * from v_dicitems where dcode='"+Tgzt.DICCODE+"'")
@Cacheable
public class Tgzt extends DicSelectModel{

	private static final long serialVersionUID = 1L;
	
	protected static final String  DICCODE="TGZT";
}
