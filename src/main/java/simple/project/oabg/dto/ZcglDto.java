package simple.project.oabg.dto;

import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.annotation.search.CondtionExpression;
import simple.system.simpleweb.platform.annotation.search.CondtionType;
import simple.system.simpleweb.platform.annotation.search.Paged;
import simple.system.simpleweb.platform.annotation.search.SearchBean;
import simple.system.simpleweb.platform.annotation.search.Sorted;
import simple.system.simpleweb.platform.dao.jpa.SearchIsdeleteDto;

/**
 * 资产管理Dto
 * @author yinchao
 * @date 2017年9月6日
 */
@SearchBean
@Paged
@Sorted("createTime desc")
public class ZcglDto extends SearchIsdeleteDto{
	private static final long serialVersionUID = 1L;
	
	@Des("资产名称")
	@CondtionExpression(value = "zcmc", type = CondtionType.like)
	private String zcmc;
	
	@Des("部门单位下拉树")
	@CondtionExpression(value="bm.code",type=CondtionType.equal)
	private String bm;
	
	@Des("是否销毁")
	@CondtionExpression(value="sfxh.code",type=CondtionType.equal)
	private String sfxh;
	
	@Des("使用人")
	@CondtionExpression(value="syr",type=CondtionType.isnullorempty)
	private String syrLy;
	
	@Des("使用人")
	@CondtionExpression(value="syr",type=CondtionType.isnotnullandempty)
	private String syrXh;
	
}
