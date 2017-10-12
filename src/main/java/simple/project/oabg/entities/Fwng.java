package simple.project.oabg.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import simple.project.oabg.dic.model.Fwzt;
import simple.project.oabg.dic.model.SF;
import simple.project.oabg.dic.model.Zt;
import simple.system.simpleweb.module.system.file.model.FileInfo;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 发文拟稿
 * @author sxm
 * @created 2017年8月28日
 */
@Des("发文拟稿")
@Entity
@Table(name="t_fwng")
public class Fwng extends AutoIDModel{

	private static final long serialVersionUID = 5784633439754466016L;
	
	@Des("标题")
    @Setter
    @Getter
    @Column(name="bt",length=100)
    private String bt;
	
	
	@Des("密级")
	@Setter
	@Getter
	@Column(name="mj",length=100)
	private String mj;

	@Des("拟稿日期")
    @Setter
    @Getter
    @Column(name="ngrq")
    private Date ngrq;
	
	@Getter
	@Setter
	@Des("主送单位")
	@ManyToMany
	@JoinTable(
			name ="t_fwng_zsdw",
			joinColumns = {@JoinColumn(name="fwngid")},
			inverseJoinColumns = {@JoinColumn(name="txlid")}
			)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<Txl> zsdw; 
	
	@Getter
	@Setter
	@Des("抄送单位")
	@ManyToMany
	@JoinTable(
			name ="t_fwng_csdw",
			joinColumns = {@JoinColumn(name="fwngid")},
			inverseJoinColumns = {@JoinColumn(name="txlid")}
			)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<Txl> csdw;
	
	@Des("发送短信")
	@Setter
	@Getter
	@ManyToOne
	private SF fsdx;
	
	@Des("是否公开")
	@Setter
	@Getter
	@ManyToOne
	private SF sfgk;
	
	@Des("拟稿人")
	@Setter
	@Getter
	@Column(name="ngr",length=50)
	private String ngr;
	
	@Des("份数")
	@Setter
	@Getter
	@Column(name="fs",length=50)
	private String fs;
	
	@Des("正文")
	@Lob
	@Setter
	@Getter
	@Column(name="zw")
	private String zw;
	
	@Getter
	@Setter
	@Des("上传记录表")
	@ManyToMany
	@JoinTable(
			name ="t_fwng_fjs",
			joinColumns = {@JoinColumn(name="fwngid")},
			inverseJoinColumns = {@JoinColumn(name="id")}
			)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<FileInfo> fileInfo;
	
	@Des("判断发文是否走完流程 1：办结 2：在途")
	@Setter
	@Getter
	@ManyToOne
	private Zt zt;
	
	@Getter
	@Setter
	@Des("登记编号/发文字号")
	@Column(name="djbh",length = 100)
	private String djbh;
	
	@Getter
	@Setter
	@Des("电子签章")
	@ManyToMany
	@JoinTable(
			name ="t_fwng_dzqz",
			joinColumns = {@JoinColumn(name="fwngid")},
			inverseJoinColumns = {@JoinColumn(name="dzqzid")}
			)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<FileInfo> dzqz;
	
	@Des("通讯录name")
	@Setter
	@Getter
	@Column(name="zs",length=500)
	private String zs;
	
	@Des("通讯录name")
	@Setter
	@Getter
	@Column(name="cs",length=500)
	private String cs;
	
	@Des("是否签发")
	@Setter
	@Getter
	@ManyToOne
	private SF sfqf;
	
	@Getter
	@Setter
	@Des("流程状态")
	@ManyToOne
	private Fwzt fwzt;
	
	@Des("过程用户")
	@Setter
	@Lob
	@Getter
	@Column(name="username")
	private String username;
	
	@Des("备注")
	@Setter
	@Lob
	@Getter
	@Column(name="bz")
	private String bz;
	
	@Des("编号确认")
	@Setter
	@Getter
	@ManyToOne
	private SF bhqr;
	
	@Des("是否需要电子签章")
	@Setter
	@Getter
	@ManyToOne
	private SF sfdzqz;
	
	@Des("是否流转")
	@Setter
	@Getter
	@ManyToOne
	private SF sflz;
	
}
