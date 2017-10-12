package simple.project.oabg.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.GwjdLczt;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 公务接待申请
 * 2017年9月19日
 * @author yc
 */
@Entity
@Table(name="t_gwjdsq")
public class Gwjdsq extends AutoIDModel{
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
	
	/*************************** 接待 **************************/
	@Des("接待时间")
	@Setter
	@Getter
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="jdsj")
	private Date jdsj;
	
	@Des("接待对象")
	@Setter
	@Getter
	@Column(name="jddx",length=200)
	private String jddx;
	
	@Des("接待事由")
	@Setter
	@Getter
	@Column(name="jdsy",length=200)
	private String jdsy;
	
	@Des("接待级别")
	@Setter
	@Getter
	@Column(name="jdjb",length=200)
	private String jdjb;
	
	@Des("人数")
	@Setter
	@Getter
	@Column(name="jdrs")
	private Integer jdrs;
	
	@Des("陪客人数")
	@Setter
	@Getter
	@Column(name="pkrs")
	private Integer pkrs;
	
	/*************************** 会场 **************************/
	
	@Des("会场地点")
	@Setter
	@Getter
	@Column(name="hcdd",length=200)
	private String hcdd;
	
	@Des("人数")
	@Setter
	@Getter
	@Column(name="hcrs")
	private Integer hcrs;
	
	@Des("标准")
	@Setter
	@Getter
	@Column(name="hcbz",length=200)
	private String hcbz;
	
	/*************************** 住宿 **************************/
	
	@Des("地点")
	@Setter
	@Getter
	@Column(name="zsdd",length=200)
	private String zsdd;
	
	@Des("人数")
	@Setter
	@Getter
	@Column(name="zsrs")
	private Integer zsrs;
	
	@Des("标准")
	@Setter
	@Getter
	@Column(name="zsbz",length=200)
	private String zsbz;
	
	/*************************** 餐饮 **************************/
	
	@Des("地点")
	@Setter
	@Getter
	@Column(name="cydd",length=200)
	private String cydd;
	
	@Des("人数")
	@Setter
	@Getter
	@Column(name="cyrs")
	private Integer cyrs;
	
	@Des("标准")
	@Setter
	@Getter
	@Column(name="cybz",length=200)
	private String cybz;
	
	/*************************** 其它 **************************/
	
	@Des("其他金额")
	@Setter
	@Getter
	@Column(name="qtje")
	private BigDecimal qtje;
	
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
	
	@Des("申请流程状态")
	@Setter
	@Getter
	@ManyToOne
	private GwjdLczt lczt;
	
	@Des("备注")
	@Setter
	@Getter
	@Column(name="bz",length=500)
	private String bz;

}
