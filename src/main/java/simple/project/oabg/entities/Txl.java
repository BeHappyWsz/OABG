package simple.project.oabg.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Bmdw;
import simple.project.oabg.dic.model.Bmzw;
import simple.project.oabg.dic.model.Gzzw;
import simple.project.oabg.dic.model.Xb;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 通讯录，即用户信息
 * @author wsz
 * @created 2017年8月21日
 */
@Des("用户信息")
@Entity
@Table(name="t_txl")
public class Txl extends AutoIDModel{
	private static final long serialVersionUID = 1L;

	@Des("用户Name")
	@Setter
	@Getter
	@Column(name="name",length=100)
	private String name;
	
	@Des("真实Name")
	@Setter
	@Getter
	@Column(name="realname",length=200)
	private String realname;
	
	@Des("用户对象")
	@Setter
	@Getter
	@OneToOne
	private User user;
	
	@Des("用户角色--简化查询,单独保存下来")
	@Setter
	@Getter
	@Column(name="roleCode",length=20)
	private String roleCode;
	
	
	@Des("性别 1男2女")
	@Setter
	@Getter
	@ManyToOne
	private Xb xb;
	
	/***      通讯录管理模块需要          ***/
	@Des("部门单位 ")
	@Getter
	@Setter
	@ManyToOne
	private Bmdw bmdw;
	
	@Des("职位类型:1局长2副局长3分管领导4办公室负责人5办公室负责人和处长6办公室主任7机要文印室主任8处长9科长10副科长11科员")
	@Getter
	@Setter
	@ManyToOne
	private Bmzw bmzw;
	
	
	/**      用户管理模块需要          ***/
	
	@Des("工作职位 1局长2分管领导3办公室负责人(处长)4办公室主任5机要文印室主任6处长7科员...20其他")
	@Setter
	@Getter
	@ManyToOne
	private Gzzw gzzw;
	
	@Des("分管领导管辖多个部门单位,其他为空")
	@Setter
	@Getter
	@Column(name="fgbms",length=100)
	private String fgbms;
	
//	@Des("分管领导管辖多个部门单位,其他为空")
//    @Setter
//    @Getter
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//    private List<Bmdw> fgbms;
	
	/**      下面为:通讯信息        **/
	@Des("手机号码")
	@Setter
	@Getter
	@Column(name="mobile",length=50)
	private String mobile;
	
	@Des("办公电话")
	@Setter
	@Getter
	@Column(name="bgdh",length=50)
	private String bgdh;
	
	@Des("宅电")
	@Setter
	@Getter
	@Column(name="telphone",length=50)
	private String telphone;
	
    @Des("工作地址")
    @Setter
    @Getter
    @Column(name="dz",length=100)
    private String dz;
    
    @Des("备注")
    @Setter
    @Getter
    @Column(name="bz",length=100)
    private String bz;
	
}
