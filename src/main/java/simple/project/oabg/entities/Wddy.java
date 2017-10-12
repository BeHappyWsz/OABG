package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.SF;
import simple.project.oabg.dic.model.Wddysjly;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 发文拟稿
 * @author sxm
 * @created 2017年9月4日
 */
@Des("我的待阅")
@Entity
@Table(name="t_wddy")
public class Wddy extends AutoIDModel{

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Des("关联id")
	@Column(name="glid",length = 50)
	private String glId;
	
	@Getter
	@Setter
	@Des("标题")
	@Column(name="bt",length = 50)
	private String bt;
	
	
	@Des("阅读时间")
    @Setter
    @Getter
    @Column(name="ydsj")
    private Date ydsj;
	
	@Des("发起时间")
    @Setter
    @Getter
    @Column(name="fqsj")
    private Date fqsj;
	
	@Getter
	@Setter
	@Des("处理状态 1：已阅读 2：未阅读")
	@ManyToOne
	private SF state;
	
	@Des("当前用户")
    @Setter
    @Getter
    @Column(name="username",length=100)
    private String userName;
	
	@Des("数据来源  1.发文 2.传阅")
    @Setter
    @Getter
    @ManyToOne
	private Wddysjly sjly;
	
	@Des("发文人")
    @Setter
    @Getter
    @Column(name="fwr",length=100)
    private String fwr;
	
}
