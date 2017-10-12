package simple.project.oabg.dto;

import simple.system.simpleweb.platform.annotation.search.Paged;
import simple.system.simpleweb.platform.annotation.search.SearchBean;
import simple.system.simpleweb.platform.annotation.search.Sorted;
import simple.system.simpleweb.platform.dao.jpa.SearchIsdeleteDto;

/**
 * 资产申请流程
 * @author yinchao
 * @date 2017年9月7日
 */
@SearchBean
@Paged
@Sorted("createTime desc")
public class ZcglSqFlowDto extends SearchIsdeleteDto{
	
	private static final long serialVersionUID = 1L;

}
