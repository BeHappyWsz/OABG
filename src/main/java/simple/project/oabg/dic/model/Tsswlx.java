package simple.project.oabg.dic.model;

import groovy.transform.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import org.hibernate.annotations.Subselect;

import simple.system.simpleweb.module.system.dic.model.DicSelectModel;
import simple.system.simpleweb.platform.annotation.Des;

@Des("收文管理之特殊收文类型1人大提案2信访处理")
@Entity
@Immutable
@Subselect("select * from v_dicitems where dcode='"+Tsswlx.DICCODE+"'")
@Cacheable
public class Tsswlx extends DicSelectModel{

	private static final long serialVersionUID = 1L;
	protected static final String  DICCODE="tsswlx";
}
