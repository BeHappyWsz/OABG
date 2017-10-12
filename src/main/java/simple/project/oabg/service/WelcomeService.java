package simple.project.oabg.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import simple.base.utils.StringSimple;

/**
 * @author sxm
 * @created 2017年9月20日
 */
@Service
public class WelcomeService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Map<String,Object> getBarData(Map<String,Object> map){
		String time=StringSimple.nullToEmpty(map.get("time"));
		Calendar cale = Calendar.getInstance(); 
		//当前年
        String year = cale.get(Calendar.YEAR)+""; 
        //当年月
        String month = cale.get(Calendar.MONTH) +1+"";
        List<Integer> fwList=new ArrayList<Integer>();
        List<Integer> swList=new ArrayList<Integer>();
        List<Integer> gzList=new ArrayList<Integer>();
        List<Integer> hytzList=new ArrayList<Integer>();
        Map<String,Object> tmap=new HashMap<String, Object>();
		if(!"".equals(time)){
			if(time.equals(year)){
				for(int i=1;i<=Integer.valueOf(month);i++){
					int fwsl=getFw(time+"-"+i+"-01");
					int swsl=getSw(time+"-"+i+"-01");
					int gzsl=getGz(time+"-"+i+"-01");
					int hysl=getHytz(time+"-"+i+"-01");
					fwList.add(fwsl);
					swList.add(swsl);
					gzList.add(gzsl);
					hytzList.add(hysl);
				}
			}else{
				for(int i=1;i<=12;i++){
					int fwsl=getFw(time+"-"+i+"-01");
					int swsl=getSw(time+"-"+i+"-01");
					int gzsl=getGz(time+"-"+i+"-01");
					int hysl=getHytz(time+"-"+i+"-01");
					fwList.add(fwsl);
					swList.add(swsl);
					gzList.add(gzsl);
					hytzList.add(hysl);
				}
			}
		}else{
			for(int i=1;i<=Integer.valueOf(month);i++){
				int fwsl=getFw(year+"-"+i+"-01");
				int swsl=getSw(year+"-"+i+"-01");
				int gzsl=getGz(year+"-"+i+"-01");
				int hysl=getHytz(year+"-"+i+"-01");
				fwList.add(fwsl);
				swList.add(swsl);
				gzList.add(gzsl);
				hytzList.add(hysl);
			}
		}
		
		
		tmap.put("fw", fwList);
		tmap.put("sw", swList);
		tmap.put("gz", gzList);
		tmap.put("hy", hytzList);
		return tmap;
		
		
	}
	
		
		/**
		 * 发文数量
		 * @author sxm
		 * @created 2017年9月20日
		 * @param date
		 * @return
		 */
		public int getFw(String date){
			String sql="SELECT r.* from t_gddj r left JOIN s_dicitem s ON s.code = r.gdlx_code and s.dic_id = '19' where DATEDIFF(month, r.create_time, '"+date+"')=0 and r.gdlx_code='2' ORDER BY r.update_time,r.create_time";
			return jdbcTemplate.queryForList(sql).size();
		}
		
		/**
		 * 收文数量
		 * @author sxm
		 * @created 2017年9月20日
		 * @param date
		 * @return
		 */
		public int getSw(String date){
			String sql="SELECT r.* from t_gddj r left JOIN s_dicitem s ON s.code = r.gdlx_code and s.dic_id = '19' where DATEDIFF(month, r.create_time, '"+date+"')=0 and r.gdlx_code in('1','3') ORDER BY r.update_time,r.create_time";
			return jdbcTemplate.queryForList(sql).size();
		}
		
		/**
		 * 公章申请数量
		 * @author sxm
		 * @created 2017年9月21日
		 * @param date
		 * @return
		 */
		public int getGz(String date){
			String sql="select r.* from t_gzsyjl r where DATEDIFF(month, r.create_time, '"+date+"')=0 ORDER BY r.update_time,r.create_time";
			return jdbcTemplate.queryForList(sql).size();
		}
		
		/**
		 * 会议通知数量
		 * @author sxm
		 * @created 2017年9月21日
		 * @param date
		 * @return
		 */
		public int getHytz(String date){
			String sql="select r.* from t_hytz r where DATEDIFF(month, r.create_time, '"+date+"')=0 ORDER BY r.update_time,r.create_time";
			return jdbcTemplate.queryForList(sql).size();
		}
}
