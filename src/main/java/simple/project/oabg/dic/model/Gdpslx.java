package simple.project.oabg.dic.model;

import groovy.transform.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import org.hibernate.annotations.Subselect;

import simple.system.simpleweb.module.system.dic.model.DicSelectModel;
import simple.system.simpleweb.platform.annotation.Des;

@Des("工单派送类型-1:12345服务平台2:阳光信访3:市长公开电话")
@Entity
@Immutable
@Subselect("select * from v_dicitems where dcode='"+Gdpslx.DICCODE+"'")
@Cacheable
public class Gdpslx extends DicSelectModel{
	private static final long serialVersionUID = 1L;
	protected static final String  DICCODE="gdpslx";
}
