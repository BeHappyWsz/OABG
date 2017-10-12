package simple.project.oabg.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 工作计划
 * @author yzw
 * @created 2017年9月25日
 */
@Des("工作计划详情")
@Entity
@Table(name="t_GZJHXQ")
public class gzjhXQ extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("工作计划工作计划ID")
	@Setter
	@Getter
	private String GZJHiD;
	
	@Des("计划标题")
	@Setter
	@Getter
	@Column(name="jhbt",length=50)
	private String jhbt;
	
	@Des("日期起")
	@Setter
	@Getter
	@Column(name="rqstart",length=20)
	private String rqstart;
	
	@Des("日期止")
	@Setter
	@Getter
	@Column(name="rqEnd",length=20)
	private String rqEnd;
	
	@Des("星期")
	@Setter
	@Getter
	@Column(name="week",length=10)
	private String week;
	
	@Des("时间")
	@Setter
	@Getter
	@Column(name="sj",length=40)
	private String sj;
	
	@Des("活动（会议）内容")
	@Setter
	@Getter
	@Lob
	
	private String text;
	
	@Des("地点")
	@Setter
	@Getter
	@Column(name="address",length=100)
	private String address;
	
	@Des("领导")
	@Setter
	@Getter
	@Column(name="ld",length=20)
	private String ld;
	
	@Des("主办部门")
	@Setter
	@Getter
	@Column(name="zbbm",length=40)
	private String zbbm;
	
	@Des("备注")
	@Setter
	@Getter
	@Column(name="bz",length=100)
	private String bz;
	
}
