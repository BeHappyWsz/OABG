package simple.project.oabg.dic.model;

import groovy.transform.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import org.hibernate.annotations.Subselect;

import simple.system.simpleweb.module.system.dic.model.DicSelectModel;
import simple.system.simpleweb.platform.annotation.Des;

@Des("工单派送办理状态-1待办理2已办理")
@Entity
@Immutable
@Subselect("select * from v_dicitems where dcode='"+Gdpsblzt.DICCODE+"'")
@Cacheable
public class Gdpsblzt extends DicSelectModel{
	
	private static final long serialVersionUID = 1L;
	
	protected static final String  DICCODE="GDPSBLZT";
}
