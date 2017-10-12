package simple.project.oabg.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import simple.project.oabg.dao.SwngDao;
import simple.project.oabg.dao.SwngFlowDao;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.dic.dao.BmdwDao;
import simple.project.oabg.dic.model.Swlx;
import simple.project.oabg.dic.model.Swzt;
import simple.project.oabg.dic.model.Tgzt;
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
 * 收文管理之收文拟稿Service
 * @author wsz
 * @date 2017年9月4日
 */
@Service
public class Swgl_swng_service extends AbstractService<SwngDao, Swng, Long>{
	@Autowired
	public Swgl_swng_service(SwngDao dao) {
		super(dao);
	}
	@Autowired
	private BmdwDao bmdwDao;
	@Autowired
	private YhglDao yhglDao;
	@Autowired
	private WddyService wddyService;
	@Autowired
	private WddbService wddbService;
	@Autowired
	private UserService userService;
	@Autowired
	private SwngFlowDao swngFlowDao;
	@Autowired
	private FileInfoDao fileInfoDao;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private Swgl_swngFlow_service swgl_swngFlow_service;
	@Autowired
	private Dagl_gddj_service dagl_gddj_service;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 返回grid列表
	 * @author wsz
	 * @created 2017年9月4日
	 * @param map
	 * @return
	 */
	public DataGrid grid(Map<String,Object> map){
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
				timee+=" 23:59:59.998";
				map.put("timee", sdf.parse(timee));
			}	
		} catch (ParseException e) {
			e.printStackTrace();
		}
		map.put("swlx", "1");//收文类型1正常收文2特殊收文
//		map.put("swr", userService.getCurrentUser().getUsername());
		Page<Swng> page = queryForPage(SwngDto.class, map);
		return new CommonConvert<Swng>(page, new Cci<Swng>(){
			@Override
			public Map<String, Object> convertObj(Swng t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("wjly", t.getWjly() == null ? "" :t.getWjly().getName());
				map.put("fwjg", t.getFwjg());
				map.put("bt", t.getBt());
				map.put("fs", t.getFs());
				map.put("ys", t.getYs());
				map.put("mj", t.getMj());
				map.put("hj", t.getHj()==null?"":t.getHj().getName());
				map.put("swrq", sdf2.format(t.getSwrq()));
				map.put("swzt", t.getSwzt() == null ? "" : t.getSwzt().getCode());
				map.put("swztName", t.getSwzt() == null ? "" : t.getSwzt().getName());
				String files ="";
				List<FileInfo> list = t.getFileInfo();
				for(int i=0; i<list.size(); i++){
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
					if(username.equals(sf.getPuser()) && "批阅".equals(sf.getCnode())){
						py ++;
					}
				}
				if(tj > py){
					map.put("isPy", "0");
				}else{
					map.put("isPy", "1");
				}
				
				return map;
			}
		}).getDataGrid();
	}
	/**
	 * 新增/更新收文拟稿
	 * @author wsz
	 * @created 2017年9月5日
	 * @param s
	 * @param zrbms 
	 * @return
	 */
	public Result saveSwng(Swng s,String fjs){
	    Swzt swzt = new Swzt();
	    swzt.setCode("1");//收文状态1拟办2批阅3传阅与分办4已传阅5待分办9提交
	    s.setSwzt(swzt);
	    Swlx swlx = new Swlx();
	    swlx.setCode("1");//1正常收文2特殊收文
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
	    User u = userService.getCurrentUser();
	    s.setSwr(u.getUsername());
	    this.save(s);
	    //流程	
		SwngFlow sf = new SwngFlow();
		Tgzt tgzt = new Tgzt();
		tgzt.setCode("1");
		sf.setSwng_id(StringSimple.nullToEmpty(s.getId()));
		sf.setCnode("提交");
		sf.setTgzt(tgzt);
		sf.setPuser(u.getUsername());
		sf.setPuserName(u.getRealname());
		sf.setNuser(yhgl_service.getBgsfzr().getUsername());
		sf.setSuggestion("");	
		sf.setCzsj(new Date());
		swgl_swngFlow_service.save(sf);
		
		//保存到我的拟办  数据来源 10:收文 20:请假 30:出差       sjr 办公室负责人   收文状态1拟办2批阅3传阅与分办4已传阅5待分办9提交
//		wddbService.saveNewWddb(s.getId(), "10", s.getBt(), "#"+fzrname+"#", "1");
		return new Result(true);
	}
	
	/**
	 * 获取详情
	 * @author wsz
	 * @created 2017年9月4日
	 * @param id
	 * @return
	 */
	public Map<String, Object> getInfo(Long id) {
		Swng swng = getDao().findOne(id);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id",swng.getId() );
		map.put("wjly", swng.getWjly() == null ? "" :swng.getWjly().getCode());
		map.put("wjlyName", swng.getWjly() == null ? "" :swng.getWjly().getName());
		map.put("swrq", swng.getSwrq() == null ? "" :sdf2.format(swng.getSwrq()));
		map.put("fwrq", swng.getFwrq() == null ? "" :sdf2.format(swng.getFwrq()));
		map.put("zrbmNames", "");
		if(!StringKit.isEmpty(swng.getZrbms())){
			String zrbmNames = "";
			String[] zrbms = swng.getZrbms().split(",");
			int length = zrbms.length;
			for(int i=0; i< length ; i++){
				String name = bmdwDao.queryByCode(zrbms[i]).getName();
				if( i == length -1){
					zrbmNames += name;
				}
				else{
					zrbmNames += name+",";
				}
			}
			map.put("zrbmNames", zrbmNames);
		}
		map.put("swr", swng.getSwr());
		map.put("fwjg", swng.getFwjg());
		map.put("fwzh", swng.getFwzh());
		map.put("bt", swng.getBt());
		map.put("mj", swng.getMj() == null ? "" : swng.getMj());
		map.put("hj", swng.getHj()==null?"":swng.getHj().getCode());
		map.put("hjName", swng.getHj()==null?"":swng.getHj().getName());
		map.put("fs", swng.getFs()==null?"":swng.getFs());
		map.put("ys", swng.getYs()==null?"":swng.getYs());
		map.put("bz", swng.getBz());
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
		map.put("zrbms", swng.getZrbms());
		List<SwngFlow> sfs = swngFlowDao.queryBySwngId(swng.getId().toString());
		String username = userService.getCurrentUser().getUsername();
		int py = 0;
		for(SwngFlow sf : sfs){
			if(username.equals(sf.getPuser()) && "批阅".equals(sf.getCnode())){
				py ++;
			}
		}
		if(py > 0){
			map.put("isPy", "1");
		}else{
			map.put("isPy", "0");
		}
		return map;
	}
	
	
	/**
	 * 办公室负责人执行批量拟办
	 * 2017年9月6日
	 * wsz
	 * @param ids 所有待拟办的ids
	 * @param suggestion
	 * @return
	 */
	public Result doNbs(String ids,String suggestion,String fglds){
		List<Long> list = Utils.stringToLongList(ids, ",");
		try {
			for(Long id : list){
				doNb(id,suggestion,fglds);
			}
			return new Result(true,"已拟办");
		} catch (Exception e) {
			return new Result(false,"拟办出错");
		}
	}
	
	/**
	 * 执行单个拟办
	 * 2017年9月5日
	 * wsz
	 * @param id
	 * @param suggestion
	 * @return
	 */
	public void doNb(Long id,String suggestion,String fglds){
		Swng swng = getDao().findOne(id);
		//拟办给分管领导
		String[] fgld_names = fglds.split(",");
		String fgldRealNames = "";
		List<Txl> list = new ArrayList<Txl>();
		for(String fgldname : fgld_names){
			Txl txl = yhgl_service.getDao().getTxlByUserName(fgldname);
			list.add(txl);
			fgldRealNames += txl.getUser().getRealname()+",";
		}
		
		//流程记录
		SwngFlow sf = new SwngFlow();
		sf.setSwng_id(swng.getId().toString());
		sf.setCnode("拟办");
		Tgzt tgzt = new Tgzt();//1通过2不通过
		tgzt.setCode("1");
		sf.setTgzt(tgzt);
		sf.setCzsj(new Date());
		sf.setPuser(userService.getCurrentUser().getUsername());
		sf.setPuserName(userService.getCurrentUser().getRealname());
		sf.setSuggestion(suggestion+":"+fgldRealNames.substring(0, fgldRealNames.length()-1));
		
		swng.setFgld(list);
		swng.setPycs(0); //批阅次数
		Swzt swzt = new Swzt();
		swzt.setCode("2");//收文状态1待拟办2待批阅3传阅与分办4已传阅5待分办6销毁7已拟办9提交
		swng.setSwzt(swzt);
		
		save(swng);
		swgl_swngFlow_service.save(sf);
		//放进我的待办
//		wddbService.saveNewWddb(swng.getId(), "10", swng.getBt(),"#"+swng.getSwr()+"#", "2");
	}
	
	/**
	 * 批量分发批阅
	 * @param ids 收文id
	 * @param fglds 将要分发给的分管领导
	 * @return
	 * @author wsz
	 * @created 2017年9月7日
	 */
	public Result doFfpys(String ids,String fglds){
		List<Long> list = Utils.stringToLongList(ids, ",");
		for(Long id : list){
			doFfpy(id,fglds);
		}
		return new Result(true);
	}
	
	/**
	 * 单个分发批阅
	 * @param id 收文id
	 * @param fglds 将要分发给的分管领导
	 * @return
	 * @author wsz
	 * @created 2017年9月7日
	 */
	public void doFfpy(Long id,String fglds){
		Swng swng = getDao().findOne(id);
		
		SwngFlow sf = new SwngFlow();
		sf.setSwng_id(swng.getId().toString());
		sf.setCnode("分发批阅");
		Tgzt tgzt = new Tgzt();//1通过2不通过
		tgzt.setCode("1");
		sf.setTgzt(tgzt);
		sf.setCzsj(new Date());
		sf.setPuser(userService.getCurrentUser().getUsername());
		sf.setPuserName(userService.getCurrentUser().getRealname());
		
		String[] fgld_names = fglds.split(",");
		String fgldRealNames = "";
		List<Txl> list = new ArrayList<Txl>();
		for(String fgldname : fgld_names){
			Txl txl = yhgl_service.getDao().getTxlByUserName(fgldname);
			list.add(txl);
			fgldRealNames += txl.getUser().getRealname()+",";
//			String sjr = "#"+fgldname+"#";
//			//保存到我的代办   数据来源 10:收文 20:请假 30:出差     收文状态1拟办2批阅3传阅与销毁4已传阅5已分办6删除7已拟办9提交
//			wddbService.saveNewWddb(swng.getId(), "10", swng.getBt(),sjr, "2");
		}
		sf.setSuggestion(fgldRealNames.substring(0, fgldRealNames.length()-1));
		
		swng.setFgld(list);
		swng.setPycs(0); //批阅次数
		Swzt swzt = new Swzt();
		swzt.setCode("2");//收文状态1拟办2批阅3传阅与分办4已传阅5待分办6销毁7已拟办9提交
		swng.setSwzt(swzt);
		
		save(swng);
		swgl_swngFlow_service.save(sf);
	}
	
	/**
	 * 执行单个拟办--已失效
	 * 2017年9月5日
	 * wsz
	 * @param id
	 * @param sftg
	 * @param fgld
	 * @param suggestion
	 * @return
	 */
	public Result doNb(Long id,String sftg,String fgld,String suggestion){
		Swng swng = getDao().findOne(id);
		
		SwngFlow sf = new SwngFlow();
		sf.setSwng_id(swng.getId().toString());
		sf.setCnode("拟办");
		Tgzt tgzt = new Tgzt();//1通过2不通过
		tgzt.setCode("1".equals(StringSimple.nullToEmpty(sftg)) ? "1" : "2");
		sf.setTgzt(tgzt);
		sf.setCzsj(new Date());
		sf.setPuser(userService.getCurrentUser().getUsername());
		sf.setSuggestion(suggestion);
		if("1".equals(StringSimple.nullToEmpty(sftg))){//通过
			String[] fgld_names = fgld.split(",");
			List<Txl> list = new ArrayList<Txl>();
			for(String fgldname : fgld_names){
				Txl txl = yhgl_service.getDao().getTxlByUserName(fgldname);
				String sjr = "#"+fgldname+"#";
				list.add(txl);
				//保存到我的代办   数据来源 10:收文 20:请假 30:出差     收文状态1拟办2批阅3传阅与分办4已传阅5待分办9提交
				wddbService.saveNewWddb(swng.getId(), "10", swng.getBt(),sjr, "2");
			}
			
			swng.setFgld(list);
			swng.setPycs(0); //批阅次数
			Swzt swzt = new Swzt();
			swzt.setCode("2");//收文状态1拟办2批阅3传阅与分办4已传阅5待分办9提交
			swng.setSwzt(swzt);
			
			save(swng);
		}else{
			Swzt swzt = new Swzt();
			swzt.setCode("9");//收文状态1拟办2批阅3传阅与分办4已传阅5待分办9提交
			swng.setSwzt(swzt);
			save(swng);
			//不通过,返回给收文人,状态为9
//			wddbService.saveNewWddb(swng.getId(), "10", swng.getBt(),"#"+swng.getSwr()+"#", "9");
		}
		swgl_swngFlow_service.save(sf);
		return new Result(true);
	}
	
	
	/**
	 * 多位分管领导执行 批量 批阅
	 * 2017年9月6日
	 * wsz
	 * @param ids 
	 * @param suggestion
	 * @return
	 */
	public Result doPys(String ids,String suggestion){
		List<Long> list = Utils.stringToLongList(ids, ",");
		for(Long id : list){
			doPy(id,suggestion);
		}
		return new Result(true);
	}
	
	/**
	 * 执行批阅
	 * 2017年9月6日
	 * wsz
	 * @param id
	 * @param sftg
	 * @param suggestion
	 * @return
	 */
	public Result doPy(Long id,String suggestion){
		User user = userService.getCurrentUser();
		Swng swng = getDao().findOne(id);
		
		int size = swng.getFgld().size();
		int pycs = swng.getPycs();
		++pycs;
		
		SwngFlow sf = new SwngFlow();
		sf.setSwng_id(swng.getId().toString());
		sf.setCnode("批阅");
		Tgzt tgzt = new Tgzt();//1通过2不通过
		tgzt.setCode("1");
		sf.setTgzt(tgzt);
		sf.setPuser(user.getUsername());
		sf.setPuserName(user.getRealname());
		sf.setSuggestion(suggestion);
		sf.setNuser("");
		sf.setCzsj(new Date());
		swng.setPycs(pycs);
		if(pycs >= size){ //已全部批阅
			Swzt swzt = new Swzt();
			swzt.setCode("3");//1拟办2批阅3传阅与分办4 5 6已销毁7已拟办9提交
			swng.setSwzt(swzt);
			swng.setPycs(0);
			//已全部批阅,
			//保存到我的拟办  数据来源 10:收文 20:请假 30:出差     收文状态1拟办2批阅3传阅与分办4已传阅5待分办9提交
//			wddbService.saveNewWddb(swng.getId(), "10", swng.getBt(), "#"+swng.getSwr()+"#", "3");
		}
		save(swng);
		swgl_swngFlow_service.save(sf);
		
		return new Result(true);
	}
	
	/**
	 * 查询流程记录
	 * 2017年9月5日
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
	 * 批量传阅收文->自动保存归档登记收文信息
	 * 2017年9月7日
	 * wsz
	 * @param ids 批量收文id
	 * @param sjr 通讯录usernames
	 * @param name 选择人员展示信息realnames
	 * @return
	 */
	public Result cySws(String ids, String sjr, String name) {
		List<Long> list = Utils.stringToLongList(ids, ",");
		for(Long id : list){
			cySw(id,sjr,name);
		}
		return new Result(true);
	}
	
	/**
	 * 单个传阅收文->自动保存归档登记收文信息
	 * 2017年9月7日
	 * wsz
	 * @param id 收文id
	 * @param sjr 通讯录usernames
	 * @param name 选择人员展示信息
	 * @return
	 */
	public Result cySw(Long id, String sjr, String name) {
		Swng swng = getDao().findOne(id);
		//进行传阅
		wddyService.save("2", swng.getId().toString(),sjr);
		//被传阅人 //新传阅 2017年9月26日09:58:00
	    if(!"".equals(StringSimple.nullToEmpty(sjr))){
	    	String[] sjrArray = sjr.split(",");
	    	List<Txl> cyr = new ArrayList<Txl>();
	    	for(String cyrname :sjrArray){
	    		Txl txlByUserName = yhglDao.getTxlByUserName(cyrname);
	    		if(!StringKit.isEmpty(txlByUserName)){
	    			cyr.add(txlByUserName);
	    		}
	    	}
	    	swng.setCyr(cyr);
	    }
		
		Swzt swzt = new Swzt();
		swzt.setCode("4");//收文状态1拟办2批阅3传阅与分办4已传阅5待分办9提交
		swng.setSwzt(swzt);
		save(swng);
		//保存流程
		SwngFlow sf = new SwngFlow();
		sf.setSwng_id(swng.getId().toString());
		sf.setCnode("传阅");
		Tgzt tgzt = new Tgzt();//1通过2不通过
		tgzt.setCode("1");
		sf.setTgzt(tgzt);
		sf.setPuser(userService.getCurrentUser().getUsername());
		sf.setPuserName(userService.getCurrentUser().getRealname());
		sf.setSuggestion("传阅给:"+name);
		sf.setNuser("");
		sf.setCzsj(new Date());
		swgl_swngFlow_service.save(sf);
		
		//自动归档登记传阅收文信息1正常收文2发文3特殊收文
		dagl_gddj_service.gddj(swng.getId(),"1", userService.getCurrentUser().getUsername(), swng.getBt());
		return new Result(true);
	}
	
	
	/**
	 * 分办收文->调用我的代办进行保存
	 * 2017年9月7日
	 * wsz
	 * @param name 选择人员的展示字段
	 * @param id,sjr
	 * @return
	 */
	public Result fbSw(Long id, String sjr, String name) {
		String sjly = "10";//数据来源 10:收文 20:请假 30:出差
		Swng swng = getDao().findOne(id);
		String bt = swng.getBt();
		String txl_usernames = sjr.replaceAll(",", "#");
		wddbService.saveNewWddb(id, sjly, bt, "#"+txl_usernames+"#","5");//收文状态1拟办2批阅3传阅与分办4已传阅5待分办9提交
		Swzt swzt = new Swzt();
		swzt.setCode("5");//已分办
		swng.setSwzt(swzt);
		save(swng);
		
		//保存流程
		SwngFlow sf = new SwngFlow();
		sf.setSwng_id(swng.getId().toString());
		sf.setCnode("分办");
		Tgzt tgzt = new Tgzt();//1通过2不通过
		tgzt.setCode("1");
		sf.setTgzt(tgzt);
		sf.setPuser(userService.getCurrentUser().getUsername());
		sf.setPuserName(userService.getCurrentUser().getRealname());
		sf.setSuggestion("分办人员:"+name);
		sf.setNuser("");
		sf.setCzsj(new Date());
		swgl_swngFlow_service.save(sf);
		
		//自动归档登记分办信息1收文2发文
		dagl_gddj_service.gddj(swng.getId(),"1", userService.getCurrentUser().getUsername(), bt);
		return new Result(true);
	}
	
	/**
	 * 批量销毁
	 * 2017年9月14日
	 * wsz
	 * @param ids 收文ids
	 * @return
	 */
	public Result xhs(String ids) {
		List<Long> list = Utils.stringToLongList(ids, ",");
		for(Long id : list){
			xh(id);
		}
		return new Result(true);
	}
	
	/**
	 * 销毁
	 * 2017年9月14日
	 * wsz
	 * @param id
	 * @return
	 */
	public Result xh(Long id) {
		Swng swng = getDao().findOne(id);
		if(StringKit.isEmpty(swng)){
			return new Result(false,"销毁失败");
		}
		Swzt swzt = new Swzt();
		swzt.setCode("6");
		swng.setSwzt(swzt);
		save(swng);
		
		//保存流程
		SwngFlow sf = new SwngFlow();
		sf.setSwng_id(swng.getId().toString());
		sf.setCnode("销毁");
		Tgzt tgzt = new Tgzt();//1通过2不通过
		tgzt.setCode("1");
		sf.setTgzt(tgzt);
		sf.setPuser(userService.getCurrentUser().getUsername());
		sf.setPuserName(userService.getCurrentUser().getRealname());
		sf.setSuggestion("销毁人员:"+userService.getCurrentUser().getRealname());
		sf.setNuser("");
		sf.setCzsj(new Date());
		swgl_swngFlow_service.save(sf);
		return new Result(true,"销毁成功");
	}
	
	/**
	 * 收文拟稿之发文日期控制(只能选择当前日期及以前)
	 * @param 
	 * @return
	 * @author wsz
	 * @created 2017年9月21日
	 */
	public Result fwrqCheck(Date fwrq) {
		if(!StringKit.isEmpty(fwrq)){
			if(fwrq.getTime() > new Date().getTime()){
				return new Result(false, "收文日期不能大于当前日期");
			}
		}
		return new Result(true);
	}

}
