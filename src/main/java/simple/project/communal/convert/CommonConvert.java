package simple.project.communal.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.domain.Page;

import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

/**
 * 通用转换器
 * @param <T>	
 * @author wm
 * @date 2016年8月9日
 */
public class CommonConvert<T>{
	@Getter
	@Setter
	private DataGrid dataGrid;
	
	public CommonConvert(){}
	
	public CommonConvert(Page<T> page,Cci<T> convertInf){
		DataGrid dg=new DataGrid();
		dg.setTotal(page.getTotalElements());
		dg.setRows(convertList(page,convertInf));
		this.setDataGrid(dg);
	}
	
	public List<Map<String,Object>> convertList(Page<T> page,Cci<T> convertInf){
		List<T> list=page.getContent();
		return convertList(list,convertInf);
	}
	
	public List<Map<String,Object>> convertTList(List<T> list,Cci<T> convertInf){
		return convertList(list,convertInf);
	}
	
	public List<Map<String,Object>> convertList(List<T> list,Cci<T> convertInf){
		List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
		for(T t:list){
			Map<String,Object> map=convertInf.convertObj(t);
			mapList.add(map);
		}
		return mapList;
	}
	
}
