package simple.project.oabg.dto;

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
@Sorted("createTime desc")
public class GzjhydDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1L;

	
	
	
}
