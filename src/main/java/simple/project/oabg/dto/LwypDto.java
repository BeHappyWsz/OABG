package simple.project.oabg.dto;

import java.util.List;

import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.annotation.search.CondtionExpression;
import simple.system.simpleweb.platform.annotation.search.CondtionType;
import simple.system.simpleweb.platform.annotation.search.Paged;
import simple.system.simpleweb.platform.annotation.search.SearchBean;
import simple.system.simpleweb.platform.annotation.search.Sorted;
import simple.system.simpleweb.platform.dao.jpa.SearchIsdeleteDto;

/**
 * 劳务用品
 * 2017年9月4日
 * @author yc
 */
@SearchBean
@Paged
@Sorted("createTime desc")
public class LwypDto extends SearchIsdeleteDto{

	private static final long serialVersionUID = 1514862321418778487L;

	@Des("物品名称")
	@CondtionExpression(value = "name", type = CondtionType.like)
	private String name;
	
	@Des("id列表")
	@CondtionExpression(prameType=String.class,value="id",type=CondtionType.in)
	private List<String> idList;
	
}
