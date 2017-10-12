package simple.project.oabg.dic.model;

import groovy.transform.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import org.hibernate.annotations.Subselect;

import simple.system.simpleweb.module.system.dic.model.DicSelectModel;
import simple.system.simpleweb.platform.annotation.Des;

@Des("收文管理之收文类型1正常收文2特殊收文")
@Entity
@Immutable
@Subselect("select * from v_dicitems where dcode='"+Swlx.DICCODE+"'")
@Cacheable
public class Swlx extends DicSelectModel{
	private static final long serialVersionUID = 1L;
	protected static final String  DICCODE="swlx";
}
