package simple.project.oabg.dto;

import simple.system.simpleweb.platform.annotation.search.Paged;
import simple.system.simpleweb.platform.annotation.search.SearchBean;
import simple.system.simpleweb.platform.annotation.search.Sorted;
import simple.system.simpleweb.platform.dao.jpa.SearchIsdeleteDto;

/**
 * 资产入库记录
 * @author yinchao
 * @date 2017年9月6日
 */
@SearchBean
@Paged
@Sorted("createTime desc")
public class ZcglRkjlDto extends SearchIsdeleteDto{
	private static final long serialVersionUID = 1L;
}
