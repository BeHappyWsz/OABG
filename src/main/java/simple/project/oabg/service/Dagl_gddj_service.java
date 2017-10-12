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
import simple.project.oabg.dao.GddjDao;
import simple.project.oabg.dic.model.Gdlx;
import simple.project.oabg.dto.GddjDto;
import simple.project.oabg.entities.Fwng;
import simple.project.oabg.entities.Gddj;
import simple.project.oabg.entities.Swng;
import simple.project.oabg.entities.Txl;
import simple.system.simpleweb.module.system.file.model.FileInfo;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;
import simple.system.simpleweb.platform.util.StringKit;
/**
 * 档案管理之归档登记
 * @author wsz
 * @date 2017年9月4日
 */
@Service
public class Dagl_gddj_service extends AbstractService<GddjDao, Gddj, Long>{

	@Autowired
	public Dagl_gddj_service(GddjDao dao) {
		super(dao);
	}
	@Autowired
	private UserService userService;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private Swgl_swng_service swgl_swng_service;
	@Autowired
	private FwngService fwngService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("MM.dd");
	
	
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
				times+=" 00:00:00";
				map.put("times", sdf.parse(times));
			}
			if(!"".equals(timee)){
				timee+=" 23:59:59";
				map.put("timee", sdf.parse(timee));
			}	
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Page<Gddj> page = queryForPage(GddjDto.class, map);
		return new CommonConvert<Gddj>(page,new Cci<Gddj>(){
			@Override
			public Map<String, Object> convertObj(Gddj t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("bt", t.getBt());
				map.put("dabh", t.getDabh());
				map.put("gdrq", t.getGdrq() == null ? "" : sdf1.format(t.getGdrq()));
				map.put("gdlx", t.getGdlx() == null ? "" : t.getGdlx().getName());
				map.put("files", getFileByGddj(t.getWdid(),t.getGdlx() == null ? "" :t.getGdlx().getCode()));
				String gdlx = StringSimple.nullToEmpty(t.getGdlx().getCode());
				if("1".equals(gdlx)){//1正常收文归档
					Swng swng = swgl_swng_service.getDao().findOne(t.getWdid());
					map.put("swrq", swng.getSwrq() == null ? "" : sdf1.format(swng.getSwrq()));
					map.put("fwzh", swng.getFwzh());
					map.put("fwjg", swng.getFwjg());
					map.put("fs", swng.getFs());
				}else if("2".equals(gdlx)){//2发文归档
					Fwng fwng = fwngService.getDao().findOne(t.getWdid());
					map.put("swrq", fwng.getNgrq() == null ? "" : sdf1.format(fwng.getNgrq()));
					map.put("fwzh", fwng.getDjbh());
					Txl txl = yhgl_service.getDao().getTxlByUserName(fwng.getNgr());
					map.put("fwjg", txl.getBmdw() == null ? "" : txl.getBmdw().getName());
					map.put("fs", fwng.getFs());
				}else if("3".equals(gdlx)){//特殊收文
					Swng swng = swgl_swng_service.getDao().findOne(t.getWdid());
					map.put("swrq", swng.getSwrq() == null ? "" : sdf1.format(swng.getSwrq()));
				}
				return map;
			}
		}).getDataGrid();
	}
	
	/**
	 * 获取归档文档的附件信息
	 * @author wsz
	 * @created 2017年9月4日
	 * @param wdid 文档id
	 * @param gdlx 归档类型
	 * @return
	 */
	public String getFileByGddj(Long wdid,String gdlx){
		List<FileInfo> list;
		if("1".equals(gdlx)){//1收文归档
			Swng swng = swgl_swng_service.getDao().findOne(wdid);
			list = swng.getFileInfo();
		}else if("2".equals(gdlx)){//2发文归档
			Fwng fwng = fwngService.getDao().findOne(wdid);
			list =fwng.getFileInfo();
		}else{
			list = new ArrayList<FileInfo>();
		}
		String files ="";
		for(int i = 0; i <list.size() ;i++){
			FileInfo fi = list.get(i);
			if(i == list.size() -1){
				files += fi.getUrl()+"#"+fi.getFileName();
			}else{
				files += fi.getUrl()+"#"+fi.getFileName()+"&";
			}
		}
		return files;
	}
	
	/**
	 * 获取详情
	 * @author wsz
	 * @created 2017年9月4日
	 * @param id
	 * @return
	 */
	public Map<String, Object> getInfo(Long id) {
		Gddj gddj = getDao().findOne(id);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id",gddj.getId() );
		map.put("cdrid", gddj.getCdrid());
		map.put("gdlx", gddj.getGdlx() == null ? "" : gddj.getGdlx().getCode());
		map.put("gdrq", gddj.getGdrq() == null ? "" :sdf.format(gddj.getGdrq()) );
		map.put("bt", gddj.getBt());
		map.put("cdr", userService.getUserByUsername(gddj.getCdr()).getRealname());
		map.put("ys", gddj.getYs());
		map.put("dabh",gddj.getDabh());
		map.put("bz", gddj.getBz() == null ? "" : gddj.getBz());
		return map;
	}
	
	/**
	 * 文件自动归档
	 * @param id 文档id
	 * @param gdlx 文档类型1正常收文2发文3特殊收文
	 * @param username  用户username
	 * @param bt 文件标题
	 * @author wsz
	 * @created 2017年9月8日
	 */
	public void gddj(Long id,String gdlx,String username,String bt){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Gddj gddj = new Gddj();
		Gdlx lx = new Gdlx();
		lx.setCode(gdlx);
		gddj.setGdlx(lx);
		gddj.setWdid(id);
		gddj.setBt(bt);
		gddj.setCdr(username);
		gddj.setCdrid(userService.getUserByUsername(username).getId().toString());
		gddj.setGdrq(new Date());
		gddj.setDabh(sdf.format(new Date()));
		save(gddj);
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
		//前一天
		Calendar calendar = Calendar.getInstance();  
        calendar.setTime(new Date());  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  
        Date time = calendar.getTime();
        
		if(timeStr!=null){
			if(timeStr.getTime() < time.getTime()){
				return new Result(false, "借阅起始时间不能小于当日时间！");
			}
		}
		if((!"".equals(StringSimple.nullToEmpty(timeStr)))&&(!"".equals(StringSimple.nullToEmpty(timeEnd)))){
			if(timeStr.getTime() > timeEnd.getTime()){
				return new Result(false, "借阅起始时间不能大于借阅截止时间！");
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
				return new Result(false, "借阅截止时间不能小于当前时间！");
			}
		}
		if((!"".equals(StringSimple.nullToEmpty(timeStr)))&&(!"".equals(StringSimple.nullToEmpty(timeEnd)))){
			if(timeStr.getTime() > timeEnd.getTime()){
				return new Result(false, "借阅起始时间不能大于借阅截止时间！");
			}
		}
		return new Result(true);
	}
	
	/**
	 * 获取所有正常收文登记记录列表
	 * @author wsz
	 * @created 2017年9月25日
	 * @return
	 */
	public List<Map<String,Object>> getSwList(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("gdlx", "1");//正常收文
		List<Gddj> query = query(GddjDto.class, map);
		for(Gddj g : query){
			Map<String,Object> m = new HashMap<String, Object>();
			Swng swng = swgl_swng_service.getDao().findOne(g.getWdid());
			if(StringKit.isEmpty(swng)){
				continue;
			}
			m.put("swrq", swng.getSwrq() == null ? "" : sdf2.format(swng.getSwrq()));
			m.put("ywrq", swng.getFwrq() == null ? "" : sdf2.format(swng.getFwrq()));
			m.put("ywh",  swng.getFwzh() == null ? "" : swng.getFwzh());
			m.put("fwjg", swng.getFwjg() == null ? "" : swng.getFwjg());
			m.put("wjbt", swng.getBt()   == null ? "" : swng.getBt());
			m.put("fs",   swng.getFs()   == null ? "" : swng.getFs());
			list.add(m);
		}
		return list;
	}
}
