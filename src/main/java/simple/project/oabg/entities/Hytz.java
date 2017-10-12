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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import simple.project.oabg.dic.model.SF;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 会议通知
 * @author yzw
 * @created 2017年8月21日
 */
@Des("会议通知")
@Entity
@Table(name="t_HYTZ")
public class Hytz extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("通讯录")
	@Setter
	@Getter
	private Txl txl;
	
	@Des("收件人")
	@Setter
	@Getter
	@Lob
	private String name;
	
	@Des("发件人")
	@Setter
	@Getter
	private String fjrName;
	
	@Des("会议标题")
	@Setter
	@Getter
	@Column(name="titles",length=50)
	private String titles;
	
	@Des("会议时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Setter
	@Getter
	private Date huiyitime;
	
	@Des("会议地点")
	@Setter
	@Getter
	@Column(name="huiyiaddress",length=50)
	private String huiyiaddress;
	
	@Des("会议内容")
	@Setter
	@Getter
	@Lob
	@Column(name="huiyitext")
	private String huiyitext;
	
	@Des("是否发送短信")
	@Setter
	@Getter
	@ManyToOne
	private SF sfnote;
	
	@Getter
	@Setter
	@Des("上传的文件")
	private String hytzfile;
	
	@Getter
	@Setter
	@Des("收件人")
	@ManyToMany
	@JoinTable(
			name ="t_hytz_sjr",
			joinColumns = {@JoinColumn(name="hyId")},
			inverseJoinColumns = {@JoinColumn(name="sjrname")}
			)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<Txl> sjr; 
}
