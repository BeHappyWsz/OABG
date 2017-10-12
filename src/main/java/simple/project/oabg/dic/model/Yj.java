package simple.project.oabg.dic.model;

import groovy.transform.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import org.hibernate.annotations.Subselect;

import simple.system.simpleweb.module.system.dic.model.DicSelectModel;
import simple.system.simpleweb.platform.annotation.Des;

@Des("意见")
@Entity
@Immutable
@Subselect("select * from v_dicitems where dcode='"+Yj.DICCODE+"'")
@Cacheable
public class Yj extends DicSelectModel{

	private static final long serialVersionUID = 1L;
	protected static final String  DICCODE="YJ";
}
