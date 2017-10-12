package simple.project.communal.convert;

import java.util.Map;

/**
 * CommonConvertInf简称
 * @param <T>	
 * @author wm
 * @date 2016年8月9日
 */
public interface Cci<T> {
	Map<String,Object> convertObj(T t);
}
