package simple.project.oabg.dic.model;

import groovy.transform.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import org.hibernate.annotations.Subselect;

import simple.system.simpleweb.module.system.dic.model.DicSelectModel;
import simple.system.simpleweb.platform.annotation.Des;

@Des("我的待阅数据来源")
@Entity
@Immutable
@Subselect("select * from v_dicitems where dcode='"+Wddysjly.DICCODE+"'")
@Cacheable
public class Wddysjly extends DicSelectModel{

	private static final long serialVersionUID = 1L;
	
	protected static final String  DICCODE="WDDYSJLY";
}
