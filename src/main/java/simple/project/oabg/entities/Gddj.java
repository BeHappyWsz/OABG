package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Gdlx;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 归档登记记录表
 * @author wsz
 * @created 2017年9月4日
 */
@Des("归档登记")
@Entity
@Table(name="t_gddj")
public class Gddj extends AutoIDModel{

	private static final long serialVersionUID = 1L;
	
	@Des("归档类型1发文2收文")
	@Setter
	@Getter
	@ManyToOne
	private Gdlx gdlx;
	
	@Des("归档文档id")
    @Setter
    @Getter
    @Column(name="wdid")
    private Long wdid;
	
	@Des("归档日期")
    @Setter
    @Getter
    @Column(name="gdrq")
    private Date gdrq;
	
	@Des("文档标题")
    @Setter
    @Getter
    @Column(name="bt",length=100)
    private String bt;
	
	@Des("存档人ID")
    @Setter
    @Getter
    @Column(name="cdrid")
    private String cdrid;
	
	@Des("存档人")
    @Setter
    @Getter
    @Column(name="cdr",length=50)
    private String cdr;
	
	@Des("页数")
	@Setter
	@Getter
	@Column(name="ys",length=50)
	private String ys;
	
	@Des("档案编号")
	@Setter
	@Getter
	@Column(name="dabh",length=50)
	private String dabh;
	
	@Des("备注")
	@Setter
	@Getter
	@Column(name="bz",length=100)
	private String bz;
}
