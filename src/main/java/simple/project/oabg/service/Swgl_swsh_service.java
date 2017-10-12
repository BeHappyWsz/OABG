package simple.project.oabg.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import simple.project.oabg.dto.SwngDto;
import simple.project.oabg.entities.Swng;
import simple.project.oabg.entities.SwngFlow;
import simple.project.oabg.entities.Wddy;
import simple.system.simpleweb.module.system.file.dao.FileInfoDao;
import simple.system.simpleweb.module.system.file.model.FileInfo;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;
import simple.system.simpleweb.platform.util.StringKit;

/**
 * 收文管理之收文审核Service
 * @author wsz
 * @date 2017年9月5日
 */
@Service
public class Swgl_swsh_service extends AbstractService<SwngDao, Swng, Long>{
	@Autowired
	private UserService userService;
	@Autowired
	private WddyService wddyService;
	@Autowired
	private FileInfoDao fileInfoDao;
	@Autowired
	private SwngFlowDao swngFlowDao;
	@Autowired
	private Swgl_swng_service swgl_swng_service;
	@Autowired
	public Swgl_swsh_service(SwngDao dao) {
		super(dao);
	}
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 审核页面返回grid列表
	 * @author wsz
	 * @created 2017年9月5日
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
		List<Role> roles = userService.getCurrentUser().getRoles();
		for(Role role : roles){
			if("BGSFZR".equals(role.getRoleCode())){//办公室负责人1拟办2批阅3传阅与分办
//				map.put("swzt", "1");
			}else if("FGLD".equals(role.getRoleCode())){//分管领导
				String username = userService.getCurrentUser().getUsername();
//				map.put("swzt", "2");
				map.put("fgld", username);
			}
		}
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");
		map.put("swzts", list);
		map.put("swlx", "1");//收文类型1正常收文2特殊收文
		Page<Swng> page = queryForPage(SwngDto.class, map);
		return new CommonConvert<Swng>(page, new Cci<Swng>(){
			@Override
			public Map<String, Object> convertObj(Swng t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("swr", userService.getUserByUsername(t.getSwr()).getRealname());
				map.put("bt", t.getBt());
				map.put("fwjg", t.getFwjg());
				map.put("bt", t.getBt());
				map.put("wjly", t.getWjly() == null ? "" : t.getWjly().getName());
				map.put("mj", t.getMj()==null? "" : t.getMj());
				map.put("hj", t.getHj()==null? "" : t.getHj().getName());
				map.put("ddrq", sdf1.format(t.getCreateTime()));
				map.put("swrq", sdf1.format(t.getSwrq()));
				map.put("fwrq", t.getFwrq() == null ? "" : sdf1.format(t.getFwrq()));
				map.put("fwzh", t.getFwzh() == null ? "" : t.getFwzh());
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
		}).getDataGrid();
	}
	
	
	/**
	 * 审核页面返回grid列表
	 * @author wsz
	 * @created 2017年9月5日
	 * @param map
	 * @return
	 */
	public DataGrid cyGrid(Map<String,Object> map){
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
		final String username = userService.getCurrentUser().getUsername();
		map.put("cyr", username);
		map.put("swzt", "4");//只能查看已传阅的
		map.put("swlx", "1");//收文类型1正常收文2特殊收文
		Page<Swng> page = queryForPage(SwngDto.class, map);
		return new CommonConvert<Swng>(page, new Cci<Swng>(){
			@Override
			public Map<String, Object> convertObj(Swng t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("swr", userService.getUserByUsername(t.getSwr()).getRealname());
				map.put("bt", t.getBt());
				map.put("fwjg", t.getFwjg());
				map.put("mj", t.getMj()==null? "" : t.getMj());
				map.put("hj", t.getHj()==null? "" : t.getHj().getName());
				
				map.put("swrq", t.getSwrq() == null ? "" : sdf1.format(t.getSwrq()));
				map.put("fwrq", t.getFwrq() == null ? "" : sdf1.format(t.getFwrq()));
				map.put("fwzh", t.getFwzh() == null ? "" : t.getFwzh());
				map.put("fwjg", t.getFwjg() == null ? "" : t.getFwjg());
				map.put("bt",   t.getBt()   == null ? "" : t.getBt());
				map.put("wjly", t.getWjly() == null ? "" : t.getWjly().getName());
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
				
				//判断是否已阅读
				map.put("isYd", "0");
				Wddy wddy = wddyService.getDao().queryByGlid(String.valueOf(t.getId()), username, "2");
				if(!StringKit.isEmpty(wddy)){
					map.put("state", wddy.getState().getCode());
					map.put("ydsj", wddy.getYdsj());
				}
				return map;
			}
		}).getDataGrid();
	}
}
