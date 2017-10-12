package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.SF;
import simple.project.oabg.dic.model.Wddbsjly;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.annotation.search.Distincted;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 我的待办
 * 2017年9月8日
 * @author yc
 */
@Des("我的待办")
@Entity
@Table(name="t_wddb")
@Distincted
public class Wddb extends AutoIDModel{

	private static final long serialVersionUID = 1L;
	
	@Des("数据来源 10：收文  20：请假  30：出差")
	@Setter
	@Getter
	@ManyToOne
	private Wddbsjly sjly;
	
	@Des("关联原表id")
	@Setter
	@Getter
	@Column(name="glid")
	private Long glid;
	
	@Des("标题")
	@Setter
	@Getter
	@Column(name="title",length=200)
	private String title;
	
	@Des("收件人")
	@Setter
	@Getter
	@Lob
	@Column(name="sjr")
	private String sjr;
	
	@Des("收件时间")
	@Setter
	@Getter
	@Column(name="sjsj")
	private Date sjsj;
	
	@Des("是否已办1:是2:否")
	@Setter
	@Getter
	@ManyToOne
	private SF sfyb;
	
	@Des("办理时间")
	@Setter
	@Getter
	@Column(name="blsj")
	private Date blsj;
	
	@Des("流程状态")
	@Setter
	@Getter
	@Column(name="lczt",length=50)
	private String lczt;
	
	@Des("是否销毁")
	@Setter
	@Getter
	@ManyToOne
	private SF sfxh;
	
}
