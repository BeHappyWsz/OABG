package simple.project.oabg.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import simple.base.utils.StringSimple;
import simple.project.communal.convert.Cci;
import simple.project.communal.convert.CommonConvert;
import simple.project.communal.util.Utils;
import simple.project.oabg.dao.GzapDao;
import simple.project.oabg.dic.model.SF;
import simple.project.oabg.dto.GzjhDto;
import simple.project.oabg.entities.Gzap;
import simple.project.oabg.entities.GzapYd;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.entities.Wddy;
import simple.project.oabg.entities.gzjhXQ;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;

@Service
public class Gzjhservice extends AbstractService<GzapDao, Gzap, Long>{
	@Autowired
	private UserService userService;
	@Autowired
	private GzjhxxService gzjhservice;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private GzjhydService gzjhydService;
	@Autowired
	private WddyService wddyService;
	@Autowired
	public Gzjhservice(GzapDao dao) {
		super(dao);
	}
	/**
	 * 工作安排表
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月21日
	 */
	public DataGrid grid(Map<String,Object> map){
		List<Role> roles = userService.getCurrentUser().getRoles();
		List<String> role = new ArrayList<String>();
		for(Role r :roles){
			role.add(r.getRoleCode());
		}
		if(role.contains("BGSZR")){
			map.put("name", userService.getCurrentUser().getUsername());
		}else{
			map.put("sftj", "1");
		}
			Page<Gzap> page = queryForPage(GzjhDto.class, map);
			return new CommonConvert<Gzap>(page, new Cci<Gzap>() {
				@Override
				public Map<String, Object> convertObj(Gzap t) {
					Map<String, Object> map=new HashMap<String,Object>();
					map.put("id", t.getId());
					map.put("gzjh", t.getGzjh()==null?"":t.getGzjh().getName());
					map.put("kssj", t.getKssj());
					map.put("jssj", t.getJssj());
					map.put("jhbt", t.getJhbt());
					map.put("lczt", t.getSftj()==null?"":t.getSftj().getCode());
					map.put("lcztName", t.getSftj()==null?"":t.getSftj().getName());
					GzapYd gzapYd = gzjhydService.getDao().queryByGzjhIDandName(t.getId(), userService.getCurrentUser().getUsername());
					if(gzapYd!=null){
					map.put("sfyd", gzapYd.getSfyd()==null?"":gzapYd.getSfyd().getCode());
					map.put("sfydName", gzapYd.getSfyd()==null?"":gzapYd.getSfyd().getName());
					}
					return map;
				}
			}).getDataGrid();
		}
	
	/**
	 * 保存
	 * @param result
	 * @return
	 * @author yzw
	 * @created 2017年8月21日
	 */
	public Result saveObj(Gzap gzap,gzjhXQ gzjhXQ,String idd){
		String [] rq = gzjhXQ.getRqstart().split(",", -1);
		String [] rqend = gzjhXQ.getRqEnd().split(",",-1);
		String [] week = gzjhXQ.getWeek().split(",", -1);
		String [] sj = gzjhXQ.getSj().split(",", -1);
		String [] text = gzjhXQ.getText().split(",", -1);
		String [] ld = gzjhXQ.getLd().split(",", -1);
		String [] zbbm = gzjhXQ.getZbbm().split(",", -1);
		String [] bz = gzjhXQ.getBz().split(",", -1);
		String [] address = gzjhXQ.getAddress().split(",", -1);
		int length = rq.length;
		if("".equals(StringSimple.nullToEmpty(gzap.getId()))){
			SF sftj = new SF();
			sftj.setCode("2");
			gzap.setSftj(sftj);
			gzap.setName(userService.getCurrentUser().getUsername());
			Gzap g =  save(gzap);
			for(int i=0;i<length;i++){
				gzjhXQ xq = new gzjhXQ();
				xq.setGZJHiD(g.getId().toString());
				xq.setBz(bz[i]);
				xq.setRqstart(rq[i]);
				xq.setRqEnd(rqend[i]);
				xq.setWeek(week[i]);
				xq.setSj(sj[i]);
				xq.setText(text[i]);
				xq.setLd(ld[i]);
				xq.setZbbm(zbbm[i]);
				xq.setBz(bz[i]);
				xq.setAddress(address[i]);
				gzjhservice.save(xq);
			}
		}else{
			String [] id = idd.split(",",-1);
			Gzap old = getDao().findOne(gzap.getId());
			old.setGzjh(gzap.getGzjh());
			old.setJssj(gzap.getJssj());
			old.setKssj(gzap.getKssj());
			old.setJhbt(gzap.getJhbt());
			old.setSftj(gzap.getSftj());
			SF sftj = new SF();
			sftj.setCode("2");
			old.setSftj(sftj);
			old =save(old);
			for(int i=0;i<bz.length;i++){
				gzjhXQ xq;
				if("".equals(id[i])){
					xq = new gzjhXQ();
				}else{
					xq = gzjhservice.getDao().findOne(Long.valueOf(id[i]));
				}
				xq.setBz(bz[i]);
				xq.setWeek(week[i]);
				xq.setZbbm(zbbm[i]);
				xq.setGZJHiD(old.getId().toString());
				xq.setLd(ld[i]);
				xq.setRqstart(rq[i]);
				xq.setRqEnd(rqend[i]);
				xq.setSj(sj[i]);
				xq.setText(text[i]);
				xq.setAddress(address[i]);
				gzjhservice.save(xq);
			}
			
		}
		return new Result(true);
	}
	/**
	 * 查询单个信息
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月21日
	 */
	public Map<String, Object> getInfo(Long id){
		Gzap gzap  = getDao().findOne(id);
		Map<String, Object> AllMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", gzap.getId());
		map.put("gzjh", gzap.getGzjh()==null?"":gzap.getGzjh().getCode());
		map.put("gzjhName", gzap.getGzjh()==null?"":gzap.getGzjh().getName());
		map.put("kssj", gzap.getKssj());
		map.put("jssj", gzap.getJssj());
		map.put("jhbt", gzap.getJhbt());
		List<Map<String, Object>> listMsp = new ArrayList<Map<String,Object>>();
		List<gzjhXQ>  gzjhXQ= gzjhservice.getDao().queryByGzjhID(gzap.getId().toString());
		for(gzjhXQ g :gzjhXQ){
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", g.getId());
			m.put("rqstart", g.getRqstart());
			m.put("rqEnd", g.getRqEnd());
			m.put("week", g.getWeek());
			m.put("sj", g.getSj());
			m.put("text", g.getText());
			m.put("ld", g.getLd());
			m.put("zbbm", g.getZbbm());
			m.put("bz", g.getBz());
			m.put("address", g.getAddress());
			listMsp.add(m);
		}
		AllMap.put("map", map);
		AllMap.put("listMsp", listMsp);
		return AllMap;
	}
	/**
	 *	提交
	 * @param result
	 * @return
	 * @author yzw
	 * @created 2017年8月21日
	 */
	public Result tickObj(Gzap gzap,gzjhXQ gzjhXQ,String idd){
		String [] rq = gzjhXQ.getRqstart().split(",", -1);
		String [] rqend = gzjhXQ.getRqEnd().split(",",-1);
		String [] week = gzjhXQ.getWeek().split(",", -1);
		String [] sj = gzjhXQ.getSj().split(",", -1);
		String [] text = gzjhXQ.getText().split(",", -1);
		String [] ld = gzjhXQ.getLd().split(",", -1);
		String [] zbbm = gzjhXQ.getZbbm().split(",", -1);
		String [] bz = gzjhXQ.getBz().split(",", -1);
		String [] address = gzjhXQ.getAddress().split(",", -1);
		int length = rq.length;
		if("".equals(StringSimple.nullToEmpty(gzap.getId()))){
			SF sftj = new SF();
			sftj.setCode("1");
			gzap.setSftj(sftj);
			gzap.setName(userService.getCurrentUser().getUsername());
			Gzap g =  save(gzap);
			for(int i=0;i<length;i++){
				gzjhXQ xq = new gzjhXQ();
				xq.setGZJHiD(g.getId().toString());
				xq.setBz(bz[i]);
				xq.setRqstart(rq[i]);
				xq.setRqEnd(rqend[i]);
				xq.setWeek(week[i]);
				xq.setSj(sj[i]);
				xq.setText(text[i]);
				xq.setLd(ld[i]);
				xq.setZbbm(zbbm[i]);
				xq.setBz(bz[i]);
				xq.setAddress(address[i]);
				gzjhservice.save(xq);
			}
		}else{
			String [] id = idd.split(",",-1);
			Gzap old = getDao().findOne(gzap.getId());
			old.setGzjh(gzap.getGzjh());
			old.setJssj(gzap.getJssj());
			old.setKssj(gzap.getKssj());
			old.setJhbt(gzap.getJhbt());
			SF sftj = new SF();
			sftj.setCode("1");
			old.setSftj(sftj);
			old =save(old);
			for(int i=0;i<bz.length;i++){
				gzjhXQ xq;
				if("".equals(id[i])){
					xq = new gzjhXQ();
				}else{
					xq = gzjhservice.getDao().findOne(Long.valueOf(id[i]));
				}
				xq.setBz(bz[i]);
				xq.setWeek(week[i]);
				xq.setZbbm(zbbm[i]);
				xq.setGZJHiD(old.getId().toString());
				xq.setLd(ld[i]);
				xq.setRqstart(rq[i]);
				xq.setRqEnd(rqend[i]);
				xq.setSj(sj[i]);
				xq.setText(text[i]);
				xq.setAddress(address[i]);
				gzjhservice.save(xq);
			}
			
		}
		
		final Gzap newgzap = gzap;
		Thread t = new Thread(new Runnable(){
		String Username = userService.getCurrentUser().getUsername();
			@Override
			public void run() {
				List<Txl> txls = yhgl_service.getDao().getJcs();
				String name = "";
				for(Txl txl :txls){
					if(!txl.getUser().getUsername().equals(Username)){
						GzapYd gzapYd =new GzapYd();
						SF sf = new SF();
						sf.setCode("2");
						gzapYd.setSfyd(sf);
						gzapYd.setGzjh(newgzap.getId());
						gzapYd.setName(txl.getUser().getUsername());
						gzapYd=gzjhydService.save(gzapYd);
						name+=txl.getUser().getUsername()+",";
					}
				}
				wddyService.save("4", newgzap.getId().toString(), name);
			}
		});
		t.start();
		return new Result(true);
	}
	
	/**
	 * 查询单个信息
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月21日
	 */
	public Map<String, Object> Info(Long id){
		Gzap gzap  = getDao().findOne(id);
		Map<String, Object> AllMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", gzap.getId());
		map.put("gzjh", gzap.getGzjh()==null?"":gzap.getGzjh().getCode());
		map.put("gzjhName", gzap.getGzjh()==null?"":gzap.getGzjh().getName());
		map.put("kssj", gzap.getKssj());
		map.put("jssj", gzap.getJssj());
		map.put("jhbt", gzap.getJhbt());
		List<Map<String, Object>> listMsp = new ArrayList<Map<String,Object>>();
		List<gzjhXQ>  gzjhXQ= gzjhservice.getDao().queryByGzjhID(gzap.getId().toString());
		for(gzjhXQ g :gzjhXQ){
			Map<String, Object> m = new HashMap<String, Object>();
			if(!"".equals(g.getRqstart())){
				m.put("rqstart", g.getRqstart());
			}
			if(!"".equals(g.getRqEnd())){
				m.put("rqEnd", g.getRqEnd());
			}
			if(!"".equals(g.getWeek())){
				m.put("week", g.getWeek());
			}
			if(!"".equals(g.getSj())){
				m.put("sj", g.getSj());
			}
			if(!"".equals(g.getText())){
				m.put("text", g.getText());
			}
			if(!"".equals(g.getLd())){
				m.put("ld", g.getLd());
			}
			if(!"".equals(g.getZbbm())){
				m.put("zbbm", g.getZbbm());
			}
			if(!"".equals(g.getBz())){
				m.put("bz", g.getBz());
			}
			if(!"".equals(g.getAddress())){
				m.put("address", g.getAddress());
			}
			if(m.size()!=0){
				listMsp.add(m);
			}
		}
		AllMap.put("map", map);
		AllMap.put("listMsp", listMsp);
		return AllMap;
	}
	
	/**
	 * 删除申请
	 * @param id
	 * @author yzw
	 * @created 2017年8月23日
	 */
	public Result deleteByIds(String ids) {
		List<Long> idList = Utils.stringToLongList(ids, ",");
		
		for(Long list :idList){
			List<Long> id = new ArrayList<Long>();
			List<gzjhXQ> g = gzjhservice.getDao().queryByGzjhID(list.toString());
			for(gzjhXQ gz :g){
				id.add(gz.getId());
			}
			gzjhservice.logicDelete(id);
		}
		logicDelete(idList);
		return new Result(true, "删除成功!");
	}
	/**
	 * 自动回填week
	 * @param id
	 * @author yzw
	 * @created 2017年8月23日
	 */
	 public  Result dateToWeek(String start,String end) {
	        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
	        String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
	        Calendar cal = Calendar.getInstance(); // 获得一个日历
	        Date dateStart = null;
	        Date dateEnd = null;
	        String startWeek="";
	        String endWeek="";
	        if(!"".equals(start)){
	        	try {
					dateStart = f.parse(start);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	cal.setTime(new Date());
	        	cal.add(Calendar.DATE, -1);
	        	Date time = cal.getTime();
	        	if(dateStart.getTime()<time.getTime()){
	        		return new Result(false,"日期起始时间不能小于当前时间");
	        	}
	        	cal.setTime(dateStart);
	        	 int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
	 	        if (w < 0)
	 	            w = 0;
	 	       startWeek = weekDays[w];
	        }
	        if(!"".equals(end)){
	        	try {
					dateEnd = f.parse(end);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	cal.setTime(new Date());
	        	cal.add(Calendar.DATE, -1);
	        	Date time = cal.getTime();
	        	if(dateEnd.getTime()<time.getTime()){
	        		return new Result(false,"日期结束时间不能小于当前时间");
	        	}
	        	cal.setTime(dateEnd);
	        	 int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
	 	        if (w < 0)
	 	            w = 0;
	 	       endWeek = weekDays[w];
	        }
	        if(!"".equals(start)&&!"".equals(end)){
	        	if(dateStart.getTime()>dateEnd.getTime()){
	        		return new Result(false,"日期开始时间不能大于结束时间");
	        	}
	        }	
	        String week = "";
	        if("".equals(startWeek)&&!"".equals(endWeek)){
	        	week = endWeek;
	        }else if(!"".equals(startWeek)&&"".equals(endWeek)){
	        	week = startWeek;
	        }else{
	        	week = startWeek+"-"+ endWeek;
	        }
	        if(startWeek.equals(endWeek)){
	        	week = startWeek;
	        }
	        return new Result(true, week);
	    }
	 /**
		 * 导出
		 * @param id
		 * @author yzw
		 * @created 2017年8月23日
		 */
	 public Map<String, Object> ExipotInfo(Long id) throws ParseException{
			Gzap gzap  = getDao().findOne(id);
			SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日");
			String sj = f.format(gzap.getKssj())+"-"+f.format(gzap.getJssj());
			String bt =  gzap.getJhbt();
			List<Map<String, Object>> listMsp = new ArrayList<Map<String,Object>>();
			List<gzjhXQ>  gzjhXQ= gzjhservice.getDao().queryByGzjhID(gzap.getId().toString());
			for(gzjhXQ g :gzjhXQ){
				Map<String, Object> m = new HashMap<String, Object>();
				if(!"".equals(StringSimple.nullToEmpty(g.getRqstart()))&&!"".equals(StringSimple.nullToEmpty(g.getRqEnd()))){
					String start = g.getRqstart();
					String end = g.getRqEnd();
					int result = 0;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				        Calendar c1 = Calendar.getInstance();
				        Calendar c2 = Calendar.getInstance();
				        try {
				        		c1.setTime(sdf.parse(start));
				        		c2.setTime(sdf.parse(end));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				        c1.set(Calendar.HOUR_OF_DAY, 0);   
				        c1.set(Calendar.MINUTE, 0);   
				        c1.set(Calendar.SECOND, 0);   
				        c2.set(Calendar.HOUR_OF_DAY, 0);   
				        c2.set(Calendar.MINUTE, 0);   
				        c2.set(Calendar.SECOND, 0);   
				        result = ((int) (c2.getTime().getTime() / 1000) - (int) (c1.getTime().getTime() / 1000)) / 3600 / 24;   
				        if(result >= 2){
				        	start = start.substring(5, start.length());
				        	start = start.replace("-", ".");
				        	end = end.substring(5, end.length());
				        	end = end.replace("-", ".");
				        	m.put("rq", start+"-"+end);
				        	m.put("week","");
				        	m.put("sj","");
				        }else if(result == 1){
				        	start = start.substring(5, start.length());
				        	start = start.replace("-", ".");
				        	end = end.substring(5, end.length());
				        	end = end.replace("-", ".");
				        	m.put("rq", start+"-"+end);
				        	m.put("week",g.getWeek());
				        	m.put("sj",g.getSj());
				        }
				}
		        	if(!"".equals(StringSimple.nullToEmpty(g.getRqstart()))&&"".equals(StringSimple.nullToEmpty(g.getRqEnd()))){
		        		String Rqstart = g.getRqstart().replace("-", ".");
		        		m.put("rq", Rqstart);
			        	m.put("week",g.getWeek());
			        	m.put("sj",g.getSj());
		        	}else if("".equals(StringSimple.nullToEmpty(g.getRqstart()))&&!"".equals(StringSimple.nullToEmpty(g.getRqEnd()))){
		        		String RqEnd = g.getRqEnd().replace("-", ".");
		        		m.put("rq", RqEnd);
			        	m.put("week",g.getWeek());
			        	m.put("sj",g.getSj());
		        	}
		        	if(StringSimple.nullToEmpty(g.getRqstart()).equals(StringSimple.nullToEmpty(g.getRqEnd()))&&
		        			!"".equals(StringSimple.nullToEmpty(g.getRqstart()))&&
		        			!"".equals(StringSimple.nullToEmpty(g.getRqEnd()))
		        			){
		        		String RqEnd = g.getRqEnd().replace("-", ".");
		        		m.put("rq", RqEnd);
			        	m.put("week",g.getWeek());
			        	m.put("sj",g.getSj());
		        	}
					m.put("text", g.getText());
					m.put("ld", g.getLd());
					m.put("zbbm", g.getZbbm());
					m.put("bz", g.getBz());
					m.put("address", g.getAddress());
				if("".equals(StringSimple.nullToEmpty(m.get("rq")))&&
					"".equals(StringSimple.nullToEmpty(m.get("week")))&&
					"".equals(StringSimple.nullToEmpty(m.get("sj")))&&
					"".equals(StringSimple.nullToEmpty(m.get("text")))&&
					"".equals(StringSimple.nullToEmpty(m.get("ld")))&&
					"".equals(StringSimple.nullToEmpty(m.get("zbbm")))&&
					"".equals(StringSimple.nullToEmpty(m.get("bz")))&&
					"".equals(StringSimple.nullToEmpty(m.get("address")))){
					
				}else{
					listMsp.add(m);
				}
			}
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("data", listMsp);
			result.put("bt", bt);
			result.put("sj", sj);
			return result;
		}
	 /**
		 * 时间校验
		 * @param id
		 * @author yzw
		 * @created 2017年8月23日
		 */
	 public Result TimeEnd(Date timeStr,Date timeEnd){
			if(!"".equals(StringSimple.nullToEmpty(timeEnd))){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DATE,-1);
				Date time = calendar.getTime();
				if(timeEnd.getTime() < time.getTime()){
					return new Result(false, "计划结束时间不能小于当前时间！");
				}
			}
			if((!"".equals(StringSimple.nullToEmpty(timeStr)))&&(!"".equals(StringSimple.nullToEmpty(timeEnd)))){
				if(timeStr.getTime() > timeEnd.getTime()){
					return new Result(false, "计划开始时间不能大于计划结束时间！");
				}
			}
			if(!"".equals(StringSimple.nullToEmpty(timeStr))){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DATE,-1);
				Date time = calendar.getTime();
				if(timeStr.getTime() < time.getTime()){
					return new Result(false, "计划开始时间不能小于当前时间！");
				}
			}
			if((!"".equals(StringSimple.nullToEmpty(timeStr)))&&(!"".equals(StringSimple.nullToEmpty(timeEnd)))){
				if(timeStr.getTime() > timeEnd.getTime()){
					return new Result(false, "计划开始时间不能大于计划结束时间！");
				}
			}
			return new Result(true);
		}
	 /**
		 * 保存代悦阅读信息
		 * @author wsz
		 * @created 2017年9月22日
		 */
		public void saveWdy(String id){
			Wddy wddy = wddyService.getDao().queryByGlid(id.toString(), userService.getCurrentUser().getUsername(),"4");
			if(wddy!=null){
				if(wddy.getState().getCode().equals("2")){
					SF state = new SF();
					state.setCode("1");
					wddy.setState(state);
					wddy.setYdsj(new Date());
					wddyService.save(wddy);
				}
			}
		}
}