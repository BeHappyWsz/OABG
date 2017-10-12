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
import simple.project.oabg.dao.SwngDao;
import simple.project.oabg.dao.SwngFlowDao;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.dic.model.Swlx;
import simple.project.oabg.dic.model.Tgzt;
import simple.project.oabg.dic.model.Tsswzt;
import simple.project.oabg.dto.SwngDto;
import simple.project.oabg.entities.Swng;
import simple.project.oabg.entities.SwngFlow;
import simple.project.oabg.entities.Txl;
import simple.system.simpleweb.module.system.file.dao.FileInfoDao;
import simple.system.simpleweb.module.system.file.model.FileInfo;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;
import simple.system.simpleweb.platform.util.StringKit;

/**
 * 收文管理之特殊收文,特殊收文回复
 * @author wsz
 * @created 2017年9月5日
 */
@Service
public class Swgl_tssw_service extends AbstractService<SwngDao, Swng, Long>{
	
	@Autowired
	public Swgl_tssw_service(SwngDao dao) {
		super(dao);
	}
	@Autowired
	private UserService userService;
	@Autowired
	private FileInfoDao fileInfoDao;
	@Autowired
	private SwngFlowDao swngFlowDao;
	@Autowired
	private YhglDao yhglDao;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private WddbService wddbService;
	@Autowired
	private Swgl_swngFlow_service swgl_swngFlow_service;
	@Autowired
	private Dagl_gddj_service dagl_gddj_service;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	/**
	 * 特殊收文grid列表
	 * @author wsz
	 * @created 2017年9月7日
	 * @param map
	 * @return
	 */
	public DataGrid grid(Map<String, Object> map) {
		try {
			String times="";
			String timee="";
			//收文日期起
			times=StringSimple.nullToEmpty(map.get("times"));
			//收文日期止
			timee=StringSimple.nullToEmpty(map.get("timee"));
			if(!"".equals(times)){
				times+=" 00:00:00.000";
				map.put("times", sdf.parse(times));
			}
			if(!"".equals(timee)){
				timee+=" 23:59:59.999";
				map.put("timee", sdf.parse(timee));
			}	
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		map.put("swr", userService.getCurrentUser().getUsername());
		map.put("swlx", "2");//收文类型1正常收文2特殊收文
		
		Page<Swng> page = queryForPage(SwngDto.class, map);
		return new CommonConvert<Swng>(page, new Cci<Swng>(){
			@Override
			public Map<String, Object> convertObj(Swng t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("swrq",sdf1.format(t.getSwrq()));
				map.put("tsswlxCode", t.getTsswlx().getCode());
				map.put("tsswlxName", t.getTsswlx().getName());
				map.put("bt", t.getBt());
				map.put("hfrqs", sdf2.format(t.getHfrqs()));
				map.put("hfrqe", sdf2.format(t.getHfrqe()));
				map.put("tsswztCode", t.getTsswzt().getCode());
				map.put("tsswztName", t.getTsswzt().getName());
				List<FileInfo> list = t.getFileInfo();
				String files = "";
				for(int i=0; i < list.size(); i++){
					if(i == list.size()-1){
						files += list.get(i).getUrl()+"#"+list.get(i).getFileName();
					}else{
						files += list.get(i).getUrl()+"#"+list.get(i).getFileName()+"&";
					}
				}
				map.put("files", files);
				return map;
			}
		}).getDataGrid();
	}
	
	/**
	 * 保存特殊收文
	 * @param s
	 * @param fjs 
	 * @param sjrs 代收件人的username
	 * @return
	 * @author wsz
	 * @created 2017年9月7日
	 */
	public Result saveTsswng(Swng s, String fjs, String sjrs) {
		Tsswzt tsswzt = new Tsswzt();
		tsswzt.setCode("1");//1待回复2待处理
		s.setTsswzt(tsswzt);
		Swlx swlx = new Swlx();
		swlx.setCode("2");//1正常收文2特殊收文
		s.setSwlx(swlx);
		//附件
	    if(!"".equals(StringSimple.nullToEmpty(fjs))){
	    	String[] fjArray=fjs.split(",");
	    	List<FileInfo> fjList=new ArrayList<FileInfo>();
	    	for(String fj:fjArray){
	    		FileInfo fileInfo=fileInfoDao.findOne(fj);
	    		fjList.add(fileInfo);
	    	}
	    	s.setFileInfo(fjList);
	    }
	    //收件人
	    if(!"".equals(StringSimple.nullToEmpty(sjrs))){
	    	String[] sjrArray = sjrs.split(",");
	    	List<Txl> sjr = new ArrayList<Txl>();
	    	for(String name : sjrArray){
	    		sjr.add(yhglDao.getTxlByUserName(name));
	    	}
	    	s.setSjr(sjr);
	    }
	    s.setHfcs(0);
	    this.save(s);
	  //流程	
	  SwngFlow sf = new SwngFlow();
	  Tgzt tgzt = new Tgzt();
	  tgzt.setCode("1");
	  sf.setSwng_id(StringSimple.nullToEmpty(s.getId()));
	  sf.setCnode("提交");
	  sf.setTgzt(tgzt);
	  sf.setPuser(userService.getCurrentUser().getUsername());
	  sf.setNuser("");
	  sf.setSuggestion("");	
	  sf.setCzsj(new Date());
	  swgl_swngFlow_service.save(sf);
	  
	  //收件人
	   if(!"".equals(StringSimple.nullToEmpty(sjrs))){
	   		String[] sjrArray = sjrs.split(",");
	   		for(String name : sjrArray){
	   			//放入我的代办  特殊收文100
	   			wddbService.saveNewWddb(s.getId(), "100", s.getBt(), "#"+name+"#", "1");
	   		}
	   }
	  //自动归档登记特殊收文信息1正常收文2发文3特殊收文
	  dagl_gddj_service.gddj(s.getId(),"3", userService.getCurrentUser().getUsername(), s.getBt());
	  return new Result(true);
	}
	
	/**
	 * 获取详情
	 * @author wsz
	 * @created 2017年9月7日
	 * @param id
	 * @return
	 */
	public Map<String, Object> getInfo(Long id) {
		Swng swng = getDao().findOne(id);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id",swng.getId() );
		map.put("swr", swng.getSwr());
		map.put("swrq", swng.getSwrq() == null ? "":sdf1.format(swng.getSwrq()));
		map.put("tsswlxName",swng.getTsswlx() == null ? "" : swng.getTsswlx().getName());
		map.put("tsswlx", swng.getTsswlx() == null ? "" : swng.getTsswlx().getCode());
		map.put("bt", swng.getBt());
		map.put("bz", swng.getBz());
		map.put("zw", swng.getZw());
		String fjs="";
		List<FileInfo> fjList=swng.getFileInfo();
		if(!fjList.isEmpty()){
			for(FileInfo fj:fjList){
				String uuid=fj.getId();
				fjs+=uuid+",";
			}
			fjs=fjs.substring(0, fjs.length()-1);
		}
		map.put("fjs", fjs);
		map.put("sjrnames", swng.getSjrnames());
		map.put("hfrqs", swng.getHfrqs() == null ? "" : sdf2.format(swng.getHfrqs()));
		map.put("hfrqe", swng.getHfrqe() == null ? "" : sdf2.format(swng.getHfrqe()));
		
		map.put("isHf", "0");
		List<SwngFlow> sfs = swngFlowDao.queryBySwngId(swng.getId().toString());
		String username = userService.getCurrentUser().getUsername();
		for(SwngFlow sf : sfs){
			String puser = sf.getPuser();
			if(username.equals(puser) && "回复".equals(sf.getCnode())){
				map.put("isHf", "1");
				break;
			}
		}
		return map;
	}

	/**
	 * 特殊收文查询流程记录
	 * 2017年9月7日
	 * wsz
	 * @param id
	 * @return
	 */
	public List<Map<String,Object>> queryFlowById(String id){
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		List<SwngFlow> list = swngFlowDao.queryBySwngId(id);
		for (SwngFlow t : list) {
			Map<String,Object> m = new HashMap<String, Object>();
			User user = userService.getUserByUsername(t.getPuser());
			m.put("pUserName", null == user ? "" : user.getRealname());
			m.put("cnode", t.getCnode());
			m.put("czsj", t.getCzsj());
			m.put("tgztName", null == t.getTgzt() ? "" : t.getTgzt().getName());
			m.put("suggestion", t.getSuggestion());
			res.add(m);
		}
		return res;
	}

	/**
	 * 返回待回复人员能看到的特殊收文grid列表
	 * @author wsz
	 * @created 2017年9月9日
	 * @param map
	 * @return
	 */
	public DataGrid hfGrid(Map<String, Object> map) {
		try {
			String times="";
			String timee="";
			//收文日期起
			times=StringSimple.nullToEmpty(map.get("times"));
			//收文日期止
			timee=StringSimple.nullToEmpty(map.get("timee"));
			if(!"".equals(times)){
				times+=" 00:00:00.000";
				map.put("times", sdf.parse(times));
			}
			if(!"".equals(timee)){
				timee+=" 23:59:59.999";
				map.put("timee", sdf.parse(timee));
			}	
		} catch (ParseException e) {
			e.printStackTrace();
		}
		map.put("swlx", "2");//收文类型1正常收文2特殊收文
		map.put("sjr", userService.getCurrentUser().getUsername());
		Page<Swng> page = queryForPage(SwngDto.class, map);
		return new CommonConvert<Swng>(page, new Cci<Swng>(){
			@Override
			public Map<String, Object> convertObj(Swng t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("swrq",sdf1.format(t.getSwrq()));
				map.put("tsswlxCode", t.getTsswlx().getCode());
				map.put("tsswlxName", t.getTsswlx().getName());
				map.put("bt", t.getBt());
				map.put("hfrqs", sdf2.format(t.getHfrqs()));
				map.put("hfrqe", sdf2.format(t.getHfrqe()));
				map.put("tsswztCode", t.getTsswzt().getCode());
				map.put("tsswztName", t.getTsswzt().getName());
				
				List<FileInfo> list = t.getFileInfo();
				String files = "";
				for(int i=0; i < list.size(); i++){
					if(i == list.size()-1){
						files += list.get(i).getUrl()+"#"+list.get(i).getFileName();
					}else{
						files += list.get(i).getUrl()+"#"+list.get(i).getFileName()+"&";
					}
				}
				map.put("files", files);
				
				List<SwngFlow> sfs = swngFlowDao.queryBySwngId(t.getId().toString());
				String username = userService.getCurrentUser().getUsername();
				int tj = 0;
				int py = 0;
				for(SwngFlow sf : sfs){
					if("提交".equals(sf.getCnode())){
						tj ++;
					}
					if(username.equals(sf.getPuser()) && "回复".equals(sf.getCnode())){
						py ++;
					}
				}
				if(tj > py){
					map.put("isHf", "0");
				}else{
					map.put("isHf", "1");
				}
				
				return map;
			}
		}).getDataGrid();
	}

	/**
	 * 保存回复
	 * @author wsz
	 * @created 2017年9月9日
	 * @param id 特殊收文id
	 * @param suggestion 回复意见
	 * @return
	 */
	public Result doSave(Long id, String suggestion) {
		Swng swng = getDao().findOne(id);
		int size = swng.getSjr().size();
		int hfcs = swng.getHfcs();
		++hfcs;
		SwngFlow sf = new SwngFlow();
		sf.setSwng_id(swng.getId().toString());
		Tgzt tgzt = new Tgzt();//1通过2不通过
		tgzt.setCode("1");
		sf.setTgzt(tgzt);
		sf.setCnode("回复");
		sf.setPuser(userService.getCurrentUser().getUsername());
		sf.setSuggestion(suggestion);
		sf.setNuser("");
		sf.setCzsj(new Date());
		swng.setHfcs(hfcs);
		if(hfcs >= size){
			Tsswzt zt = new Tsswzt();//特殊收文状态1待回复2待处理
			zt.setCode("2");
			swng.setTsswzt(zt);
			//已全部回复 保存到我的待办
			wddbService.saveNewWddb(swng.getId(), "10", swng.getBt(), "#"+swng.getSwr()+"#", "2");
		}
		save(swng);
		swgl_swngFlow_service.save(sf);
		return new Result(true);
	}

	/**
	 * 时间控制
	 * @param timeStr
	 * @param timeEnd
	 * @return
	 * @author wsz
	 * @created 2017年9月13日
	 */
	public Result TimeStr(Date timeStr, Date timeEnd) {
		Calendar calendar = Calendar.getInstance();  
        calendar.setTime(new Date());  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  
        Date time = calendar.getTime();  
		if(timeStr!=null){
			if(timeStr.getTime() < time.getTime()){
				return new Result(false, "回复起始时间不能小于当日时间！");
			}
		}
		if((!"".equals(StringSimple.nullToEmpty(timeStr)))&&(!"".equals(StringSimple.nullToEmpty(timeEnd)))){
			if(timeStr.getTime() > timeEnd.getTime()){
				return new Result(false, "回复起始时间不能大于回复截止时间！");
			}
		}
		return new Result(true);
	}
	
	
	/**
	 * 时间校验
	 * @author wsz
	 * @created 2017年9月13日
	 */
	public Result TimeEnd(Date timeStr,Date timeEnd){
		if(timeEnd!=null){
			if(timeEnd.getTime() < new Date().getTime()){
				return new Result(false, "回复截止时间不能小于当前时间");
			}
		}
		if((!"".equals(StringSimple.nullToEmpty(timeStr)))&&(!"".equals(StringSimple.nullToEmpty(timeEnd)))){
			if(timeStr.getTime() > timeEnd.getTime()){
				return new Result(false, "回复起始时间不能大于回复截止时间");
			}
		}
		return new Result(true);
	}

	/**
	 * 判断当前回复日期是否超过回复截止日期
	 * 2017年9月7日
	 * wsz
	 * @param id 特殊收文id
	 * @return 1未超过 2超过
	 */
	public Map<String,Object> checkHfrq(Long id) {
		Swng swng = getDao().findOne(id);
		Map<String,Object> map = new HashMap<String,Object>();
		if(StringKit.isEmpty(swng)){
			map.put("isIn", "false");
			return map;
		}
		Date hfrqe = swng.getHfrqe();
		if(hfrqe.getTime() > new Date().getTime()){
			map.put("isIn", "1");
			return map;
		}
		map.put("isIn", "2");
		return map;
	}
}
