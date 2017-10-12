package simple.project.oabg.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import simple.project.oabg.dic.model.Gdpsblzt;
import simple.project.oabg.dic.model.Gdpslx;
import simple.system.simpleweb.module.system.file.model.FileInfo;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 工单派送办理
 * @author wsz
 * @created 2017年9月27日
 */
@Des("工单派送办理")
@Entity
@Table(name="t_gdpsbl")
public class Gdpsbl extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("派送人username")
	@Setter
	@Getter
	@Column(name="swr",length=50)
	private String psr;
	
	@Des("派送时间")
    @Setter
    @Getter
    @Column(name="pssj")
    private Date pssj;
	
	@Des("工单派送类型-1:12345服务平台2:阳光信访3:市长公开电话")
	@Setter
	@Getter
	@ManyToOne
	private Gdpslx gdpslx;
	
	@Des("标题")
    @Setter
    @Getter
    @Column(name="bt",length=100)
    private String bt;
	
	@Des("工单内容")
	@Setter
	@Getter
	@Column(name="gdnr",length=500)
	private String gdnr;
	
	
	@Des("附件上传记录表")
	@Setter
	@Getter
	@OneToMany(targetEntity = FileInfo.class)
	@JoinColumn(name="gdpsblId")
	private List<FileInfo> fileInfo;
	
	@Des("办理期限起")
    @Setter
    @Getter
    @Column(name="blqxs")
    private Date blqxs;
	
	@Des("办理期限止")
    @Setter
    @Getter
    @Column(name="blqxe")
    private Date blqxe;
	
	@Getter
	@Setter
	@Des("办理人")
	@ManyToMany
	@JoinTable(
			name ="t_gdpsbl_blr",
			joinColumns = {@JoinColumn(name="gdpsblId")},
			inverseJoinColumns = {@JoinColumn(name="blr")}
			)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<Txl> blr;
	
	
	@Des("办理部门展示信息")
	@Setter
	@Getter
	@Column(name="blbmNames",length=250)
	private String blbmNames;
	
	@Des("办理人办理次数")
	@Setter
	@Getter
	@Column(name="blcs",length=5)
	private Integer blcs;
	
	@Des("备注")
    @Setter
    @Getter
    @Column(name="bz",length=500)
    private String bz;
	
	
	@Des("工单派送状态1待办理2已办理")
	@Setter
	@Getter
	@ManyToOne
	private Gdpsblzt gdpszt;
}
