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
import simple.project.oabg.dao.GdpsblDao;
import simple.project.oabg.dao.GdpsblFlowDao;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.dic.dao.BmdwDao;
import simple.project.oabg.dic.model.Bmdw;
import simple.project.oabg.dic.model.Gdpsblzt;
import simple.project.oabg.dic.model.Tgzt;
import simple.project.oabg.dto.GdpsblDto;
import simple.project.oabg.dto.YhglDto;
import simple.project.oabg.entities.Gdpsbl;
import simple.project.oabg.entities.GdpsblFlow;
import simple.project.oabg.entities.Txl;
import simple.system.simpleweb.module.system.file.dao.FileInfoDao;
import simple.system.simpleweb.module.system.file.model.FileInfo;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;
import simple.system.simpleweb.platform.util.StringKit;

/**
 * 工单派送办理之工单派送Service
 * @author wsz
 * @date 2017年9月27日
 */
@Service
public class GdpsService extends AbstractService<GdpsblDao, Gdpsbl, Long>{
	@Autowired
	public GdpsService(GdpsblDao dao) {
		super(dao);
	}

	@Autowired
	private UserService userService;
	@Autowired
	private GdpsblFlowDao gdpsblFlowDao;
	@Autowired
	private BmdwDao bmdwDao;
	@Autowired
	private FileInfoDao fileInfoDao;
	@Autowired
	private YhglDao yhglDao;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private GdpsblFlowService gdpsblFlowService;
	@Autowired
	private WddbService wddbService;
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	/**
	 * 返回grid列表
	 * @author wsz
	 * @created 2017年9月27日
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
		String sldw = StringSimple.nullToEmpty(map.get("sldw"));
		if(!StringKit.isEmpty(sldw)){
			Bmdw queryByCode = bmdwDao.queryByCode(sldw);
			if(!StringKit.isEmpty(queryByCode)){
				map.put("sldw", queryByCode.getName());
			}
		}
		Page<Gdpsbl> page = queryForPage(GdpsblDto.class, map);
		return new CommonConvert<Gdpsbl>(page, new Cci<Gdpsbl>(){
			@Override
			public Map<String, Object> convertObj(Gdpsbl t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("gdpslx", t.getGdpslx() == null ? "" : t.getGdpslx().getCode());
				map.put("gdpslxName", t.getGdpslx() == null ? "" : t.getGdpslx().getName());
				map.put("sldw", t.getBlbmNames());
				map.put("pssj", t.getPssj() == null ? "" : sdf1.format(t.getPssj()));
				map.put("bt", t.getBt());
				map.put("files", "");
				map.put("gdpszt", t.getGdpszt() == null ? "" : t.getGdpszt().getCode());
				map.put("gdpsztName", t.getGdpszt() == null ? "" : t.getGdpszt().getName());
				map.put("blqxs", t.getBlqxs() == null ? "" : sdf1.format(t.getBlqxs()));
				map.put("blqxe", t.getBlqxe() == null ? "" : sdf1.format(t.getBlqxe()));
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
	 * 提交工单派送办理对象
	 * @author wsz
	 * @param sldws 受理单位
	 * @created 2017年9月27日
	 * @return
	 */
	public Result saveGdpsbl(Gdpsbl g,String fjs,String sldws){
		Gdpsblzt  zt = new Gdpsblzt();
		zt.setCode("1");//工单派送状态1待办理2已办理
		//附件
	    if(!"".equals(StringSimple.nullToEmpty(fjs))){
	    	String[] fjArray=fjs.split(",");
	    	List<FileInfo> fjList=new ArrayList<FileInfo>();
	    	for(String fj:fjArray){
	    		FileInfo fileInfo=fileInfoDao.findOne(fj);
	    		fjList.add(fileInfo);
	    	}
	    	g.setFileInfo(fjList);
	    }
	    //受理单位->获取单位中的人
	    g.setBlr(getTxlsBySldws(sldws));
	    String blbmName = "";
	    String[] bmdws = sldws.split(",");//部门单位code
	    for(String bmdw : bmdws){
	    	Bmdw queryByCode = bmdwDao.queryByCode(bmdw);
	    	if(!StringKit.isEmpty(queryByCode)){
	    		blbmName += queryByCode.getName()+",";
	    	}
		}
	    if(!StringKit.isEmpty(blbmName)){
	    	blbmName = blbmName.substring(0, blbmName.length()-1);
	    }
	    g.setBlbmNames(blbmName);
	    g.setBlcs(0);
	    g.setGdpszt(zt);
	    save(g);
	    
	    GdpsblFlow gf = new GdpsblFlow();
	    Tgzt tgzt = new Tgzt();
		tgzt.setCode("1");
		gf.setGdps_id(StringSimple.nullToEmpty(g.getId()));
		gf.setCnode("派送");
		gf.setTgzt(tgzt);
		gf.setPuser(userService.getCurrentUser().getUsername());
		gf.setPuserName(userService.getCurrentUser().getRealname());
		gf.setNuser("");
		gf.setSuggestion("");	
		gf.setCzsj(new Date());
		gdpsblFlowService.save(gf);
		
		return new Result(true);
	}
	
	/**
	 * 通过受理单位获取受理人
	 * @created 2017年9月27日
	 * wsz
	 */
	public List<Txl> getTxlsBySldws(String sldws){
		List<Txl> list = new ArrayList<Txl>();
		String[] bmdws = sldws.split(",");//部门单位code
		for(String bmdw : bmdws){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("bmdw", bmdw);
//			map.put("rolecode", "CZ");
			List<Txl> txls = yhgl_service.query(YhglDto.class, map);
			list.addAll(txls);
		}
		return list;
	}
	/**
	 * 获取详情
	 * @author wsz
	 * @created 2017年9月27日
	 * @param id
	 * @return
	 */
	public Map<String, Object> getInfo(Long id) {
		Gdpsbl findOne = getDao().findOne(id);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id", findOne.getId());
		map.put("psr", findOne.getPsr());
		map.put("pssj", findOne.getPssj() == null ? "" : sdf1.format(findOne.getPssj()));
		map.put("gdpslx", findOne.getGdpslx() == null ? "" : findOne.getGdpslx().getName());
		map.put("bt", findOne.getBt());
		map.put("gdnr", findOne.getGdnr());
		String fjs="";
		List<FileInfo> fjList=findOne.getFileInfo();
		if(!fjList.isEmpty()){
			for(FileInfo fj:fjList){
				String uuid=fj.getId();
				fjs+=uuid+",";
			}
			fjs=fjs.substring(0, fjs.length()-1);
		}
		map.put("fjs", fjs);
		
		map.put("blbmNames", findOne.getBlbmNames());
		map.put("blqxs", findOne.getBlqxs() == null ? "" : sdf1.format(findOne.getBlqxs()));
		map.put("blqxe", findOne.getBlqxe() == null ? "" : sdf1.format(findOne.getBlqxe()));
		map.put("bz", findOne.getBz());
		return map;
	}
	
	/**
	 * 工单派送办理查询流程记录
	 * 2017年9月27日
	 * wsz
	 * @param id
	 * @return
	 */
	public List<Map<String,Object>> queryFlowById(String id){
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		List<GdpsblFlow> list = gdpsblFlowDao.queryByGdpsId(id);
		for(GdpsblFlow gf : list){
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("pUserName", gf.getPuserName());
			m.put("cnode", gf.getCnode());
			m.put("czsj", gf.getCzsj());
			m.put("tgztName", null == gf.getTgzt() ? "" : gf.getTgzt().getName());
			m.put("suggestion", gf.getSuggestion());
			res.add(m);
		}
		return res;
	}
	
	
	/**
	 * 时间控制
	 * @param timeStr
	 * @param timeEnd
	 * @return
	 * @author wsz
	 * @created 2017年9月27日
	 */
	public Result TimeStr(Date timeStr, Date timeEnd) {
		Calendar calendar = Calendar.getInstance();  
        calendar.setTime(new Date());  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  
        Date time = calendar.getTime();  
		if(timeStr!=null){
			if(timeStr.getTime() < time.getTime()){
				return new Result(false, "办理起始时间不能小于当日时间！");
			}
		}
		if((!"".equals(StringSimple.nullToEmpty(timeStr)))&&(!"".equals(StringSimple.nullToEmpty(timeEnd)))){
			if(timeStr.getTime() > timeEnd.getTime()){
				return new Result(false, "办理起始时间不能大于截止时间！");
			}
		}
		return new Result(true);
	}
	
	
	/**
	 * 时间校验
	 * @author wsz
	 * @created 2017年9月27日
	 */
	public Result TimeEnd(Date timeStr,Date timeEnd){
		if(timeEnd!=null){
			if(timeEnd.getTime() < new Date().getTime()){
				return new Result(false, "办理截止时间不能小于当前时间");
			}
		}
		if((!"".equals(StringSimple.nullToEmpty(timeStr)))&&(!"".equals(StringSimple.nullToEmpty(timeEnd)))){
			if(timeStr.getTime() > timeEnd.getTime()){
				return new Result(false, "办理起始时间不能大于截止时间");
			}
		}
		return new Result(true);
	}
}
