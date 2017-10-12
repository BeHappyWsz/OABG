package simple.project.oabg.dto;

import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.annotation.search.CondtionExpression;
import simple.system.simpleweb.platform.annotation.search.CondtionType;
import simple.system.simpleweb.platform.annotation.search.Paged;
import simple.system.simpleweb.platform.annotation.search.SearchBean;
import simple.system.simpleweb.platform.annotation.search.Sorted;
import simple.system.simpleweb.platform.dao.jpa.SearchIsdeleteDto;
/**
 * 用户管理dto
 * @author wsz
 * @created 2017年8月21日
 */
@SearchBean
@Paged
@Sorted({"bmdw.code asc","createTime desc"})
public class YhglDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1L;

	@Des("初次查询用户名时,只显示不为空的")
	@CondtionExpression(value = "user.username", type = CondtionType.isnotnullandempty)
	private String username_;
	
	@Des("用户名")
	@CondtionExpression(value = "user.username", type = CondtionType.like)
	private String username;
	
	@Des("真实姓名")
	@CondtionExpression(value = "user.realname", type = CondtionType.like)
	private String realname;
	
	@Des("姓名-用于通讯录中查询")
	@CondtionExpression(value="realname",type=CondtionType.like)
	private String name;
	
//	@Des("组织名称")
//	@CondtionExpression(value = "user.org.orgName", type = CondtionType.like)
//	private String orgname;
//	
//	@Des("组织代号")
//	@CondtionExpression(value = "user.org.orgCode", type = CondtionType.equal)
//	private String orgcode;

	@Des("角色code--查询的为Txl中单独保存的数据信息")
	@CondtionExpression(value = "roleCode", type = CondtionType.like)
	private String rolecode;
	
	@Des("性别")
	@CondtionExpression(value="xb.code",type=CondtionType.equal)
	private String xb;
	
	/**通讯录查询 **/
	@Des("部门单位下拉树")
	@CondtionExpression(value="bmdw.code",type=CondtionType.equal)
	private String bmdw;
	
	@Des("部门单位下拉树")
	@CondtionExpression(value="bmdw.name",type=CondtionType.like)
	private String bmdwName;
	
	@Des("部门职位")
	@CondtionExpression(value="bmzw.code",type=CondtionType.equal)
	private String bmzw;
	
	@Des("分管部门")
	@CondtionExpression(value="fgbms",type=CondtionType.like)
	private String fgbm;
	
	@Des("过滤部门单位的人员")
	@CondtionExpression(value="bmdw.pitemid",type=CondtionType.lessthanOrequal)
	private int pitemid;
	/**用户管理模块  **/
	@Des("工作职位")
	@CondtionExpression(value="gzzw.code",type=CondtionType.equal)
	private String gzzw;
	/**通讯录部分信息 **/
	@Des("手机号码")
	@CondtionExpression(value="mobile",type=CondtionType.like)
	private String mobile;
	
	@Des("办公电话")
	@CondtionExpression(value="bgdh",type=CondtionType.like)
	private String bgdh;
	
	@Des("宅电")
	@CondtionExpression(value="telphone",type=CondtionType.like)
	private String telphone;
	
	@Des("办公地址")
	@CondtionExpression(value="dz",type=CondtionType.like)
	private String dz;
	
	@Des("备注")
	@CondtionExpression(value="bz",type=CondtionType.like)
	private String bz;
}
