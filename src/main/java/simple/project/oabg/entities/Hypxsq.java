package simple.project.oabg.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.HypxLczt;
import simple.project.oabg.dic.model.Hypxjhqk;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 会议培训申请
 * 2017年9月19日
 * @author yc
 */
@Entity
@Table(name="t_hypxsq")
public class Hypxsq extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("申请人")
	@Setter
	@Getter
	@Column(name="sqr",length=100)
	private String sqr;
	
	@Des("申请人(账户名)")
	@Setter
	@Getter
	@Column(name="sqrUserName",length=100)
	private String sqrUserName;
	
	@Des("申请部门code")
	@Setter
	@Getter
	@Column(name="sqrbmcode",length=100)
	private String sqrbmcode;
	
	@Des("申请日期")
	@Setter
	@Getter
	@Column(name="sqrq")
	private Date sqrq;
	
	@Des("会议名称")
	@Setter
	@Getter
	@Column(name="mc",length=200)
	private String mc;
	
	@Des("会议地点")
	@Setter
	@Getter
	@Column(name="hydd",length=200)
	private String hydd;
	
	@Des("会议天数")
	@Setter
	@Getter
	@Column(name="hyts")
	private Double hyts;
	
	@Des("计划情况")
	@Setter
	@Getter
	@ManyToOne
	private Hypxjhqk jhqk;
	
	@Des("会场人数")
	@Setter
	@Getter
	@Column(name="hcrs")
	private Integer hcrs;
	
	@Des("会场金额")
	@Setter
	@Getter
	@Column(name="hcje")
	private BigDecimal hcje;
	
	@Des("就餐人数")
	@Setter
	@Getter
	@Column(name="jcrs")
	private Integer jcrs;
	
	@Des("就餐标准")
	@Setter
	@Getter
	@Column(name="jcbz",length=200)
	private String jcbz;
	
	@Des("就餐金额")
	@Setter
	@Getter
	@Column(name="jcje")
	private BigDecimal jcje;
	
	@Des("住宿人数")
	@Setter
	@Getter
	@Column(name="zsrs")
	private Integer zsrs;
	
	@Des("住宿标准")
	@Setter
	@Getter
	@Column(name="zsbz",length=200)
	private String zsbz;
	
	@Des("住宿金额")
	@Setter
	@Getter
	@Column(name="zsje")
	private BigDecimal zsje;
	
	@Des("预算大写")
	@Setter
	@Getter
	@Column(name="ysdx",length=200)
	private String ysdx;
	
	@Des("预算小写")
	@Setter
	@Getter
	@Column(name="ysxx",length=200)
	private String ysxx;
	
	@Des("其他费用")
	@Setter
	@Getter
	@Column(name="qtfy")
	private BigDecimal qtfy;
	
	@Des("申请流程状态")
	@Setter
	@Getter
	@ManyToOne
	private HypxLczt lczt;
	
	@Des("备注")
	@Setter
	@Getter
	@Column(name="bz",length=500)
	private String bz;

}
