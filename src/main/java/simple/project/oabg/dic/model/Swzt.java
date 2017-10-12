package simple.project.oabg.dic.model;

import groovy.transform.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import org.hibernate.annotations.Subselect;

import simple.system.simpleweb.module.system.dic.model.DicSelectModel;
import simple.system.simpleweb.platform.annotation.Des;

@Des("收文状态1拟办2批阅3传阅与分办4已传阅5待分办9提交")
@Entity
@Immutable
@Subselect("select * from v_dicitems where dcode='"+Swzt.DICCODE+"'")
@Cacheable
public class Swzt extends DicSelectModel{

	private static final long serialVersionUID = 1L;
	protected static final String  DICCODE="swzt";
}
