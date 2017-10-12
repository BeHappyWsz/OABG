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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import simple.project.oabg.dic.model.Hj;
import simple.project.oabg.dic.model.Swlx;
import simple.project.oabg.dic.model.Swzt;
import simple.project.oabg.dic.model.Tsswlx;
import simple.project.oabg.dic.model.Tsswzt;
import simple.project.oabg.dic.model.Wjly;
import simple.system.simpleweb.module.system.file.model.FileInfo;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.annotation.search.Distincted;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 收文拟稿:
 * 	普通收文(swrq+fwrq+fwzh+zrbm+bt+fwjg+wjly+mj+hj+fs+ys+fjs+bz)
 * 	特殊收文(swr+swrq+tsswlx+zw+fj+sjr(ids)+sjrnames+hfrqs+hfrqe)
 * @author wsz
 * @created 2017年9月4日
 */
@Des("收文拟稿")
@Entity
@Table(name="t_swng")
@Distincted
public class Swng extends AutoIDModel{

	private static final long serialVersionUID = 1L;
	
	@Des("收文日期")
    @Setter
    @Getter
    @Column(name="swrq")
    private Date swrq;
	
	@Des("发文日期")
    @Setter
    @Getter
    @Column(name="fwrq")
    private Date fwrq;
	
	@Des("发文字号")
	@Setter
	@Getter
	@Column(name="fwzh",length=100)
	private String fwzh;
	
	@Des("责任部门")
	@Setter
	@Getter
	@Column(name="zrbms",length=50)
	private String zrbms;
	
	@Des("标题")
    @Setter
    @Getter
    @Column(name="bt",length=100)
    private String bt;
	
	@Des("发文机关")
	@Setter
	@Getter
	@Column(name="fwjg",length=100)
	private String fwjg;
	
	@Des("文件来源")
	@Setter
	@Getter
	@ManyToOne
	private Wjly wjly;
	
	@Des("收文管理之收文类型1正常收文2特殊收文")
	@Setter
	@Getter
	@ManyToOne
	private Swlx swlx;
	
	
	@Des("收文人id")
	@Setter
	@Getter
	@Column(name="swrid",length=50)
	private String swrid;
	
	@Des("收文人")
	@Setter
	@Getter
	@Column(name="swr",length=50)
	private String swr;
	
//	@Des("密级")
//	@Setter
//	@Getter
//	@ManyToOne
//	private Mj mj;
	
	@Des("密级")
	@Setter
	@Getter
	@Column(name="mj",length=50)
	private String mj;
	
	@Des("缓急")
	@Setter
	@Getter
	@ManyToOne
	private Hj hj;
	
	@Des("份数")
	@Setter
	@Getter
	@Column(name="fs",length=50)
	private String fs;
	
	@Des("页数")
	@Setter
	@Getter
	@Column(name="ys",length=50)
	private String ys;
	
	@Des("备注")
	@Lob
	@Setter
	@Getter
	@Column(name="bz",length=500)
	private String bz;
	
	@Des("上传记录表")
	@Setter
	@Getter
	@OneToMany(targetEntity = FileInfo.class)
	@JoinColumn(name="swngid")
	private List<FileInfo> fileInfo;
	
	@Des("收文状态1拟办2批阅3传阅与分办4已传阅5待分办9提交")
	@Setter
	@Getter
	@ManyToOne
	private Swzt swzt;
	
	
	
//	@Des("批阅分管领导")
//    @Setter
//    @Getter
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//    private List<Txl> fgld;
	
	@Getter
	@Setter
	@Des("分发批阅给分管领导")
	@ManyToMany
	@JoinTable(
			name ="t_swng_fgld",
			joinColumns = {@JoinColumn(name="swngId")},
			inverseJoinColumns = {@JoinColumn(name="fgld")}
			)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<Txl> fgld;
	
	
	@Des("分管领导已批阅次数")
	@Setter
	@Getter
	@Column(name="pycs",length=5)
	private Integer pycs;
	
	@Getter
	@Setter
	@Des("被传阅人")
	@ManyToMany
	@JoinTable(
			name ="t_swng_cyr",
			joinColumns = {@JoinColumn(name="swngId")},
			inverseJoinColumns = {@JoinColumn(name="cyr")}
			)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<Txl> cyr;
	
	/**  特殊收文字段       **/
	
	@Des("备注")
	@Lob
	@Setter
	@Getter
	@Column(name="zw",length=500)
	private String zw;
	
	
	@Des("特殊收文类型1人大提案2信访处理")
	@Setter
	@Getter
	@ManyToOne
	private Tsswlx tsswlx;
	
	@Des("特殊收文状态1待回复2待处理")
	@Setter
	@Getter
	@ManyToOne
	private Tsswzt tsswzt;
	
	@Des("收件人展示信息")
	@Setter
	@Getter
	@Column(name="sjrname",length=500)
	private String sjrnames;
	
	@Getter
	@Setter
	@Des("收件人")
	@ManyToMany
	@JoinTable(
			name ="t_tssw_sjr",
			joinColumns = {@JoinColumn(name="tsswId")},
			inverseJoinColumns = {@JoinColumn(name="sjrname")}
			)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<Txl> sjr;
	
	@Des("收件人回复次数")
	@Setter
	@Getter
	@Column(name="hfcs",length=5)
	private Integer hfcs;
	
	@Des("回复日期起")
    @Setter
    @Getter
    @Column(name="hfrqs")
    private Date hfrqs;
	
	@Des("回复日期止")
    @Setter
    @Getter
    @Column(name="hfrqe")
    private Date hfrqe;
}
