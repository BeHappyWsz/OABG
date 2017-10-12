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

import simple.project.communal.convert.Cci;
import simple.project.communal.convert.CommonConvert;
import simple.project.communal.util.Utils;
import simple.project.oabg.dao.DeOrgDao;
import simple.project.oabg.dao.DeRoleDao;
import simple.project.oabg.dao.HytzDao;
import simple.project.oabg.dic.model.FK;
import simple.project.oabg.dic.model.SF;
import simple.project.oabg.dto.HytzDto;
import simple.project.oabg.entities.Hyfk;
import simple.project.oabg.entities.Hytz;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.entities.Wddy;
import simple.system.simpleweb.module.system.file.model.FileInfo;
import simple.system.simpleweb.module.system.file.service.FileService;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;
import simple.system.simpleweb.platform.util.StringKit;

import com.alibaba.fastjson.JSONObject;

@Service
public class Hytz_service extends AbstractService<HytzDao, Hytz, Long>{

	@Autowired
	private DeOrgDao deOrgDao;
	@Autowired
	private DeRoleDao roleDao;
	@Autowired
	private UserService userService;
	@Autowired
	private FileService fileService;
	@Autowired
	private Hyfk_service hyfk_service;
	@Autowired
	private Txl_service txl_service;
	@Autowired
	private WddyService wddyService;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	public Hytz_service(HytzDao dao) {
		super(dao);
	}

	/**
	 * 会议列表
	 * @param map
	 * @return
	 * @author yzw
	 * @created 2017年8月21日
	 */
	public DataGrid grid(Map<String,Object> map){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(!StringKit.isEmpty(map.get("timestart"))){
				String timeStr = map.get("timestart")+" 00:00:00";
				Date str = sdf.parse(timeStr); 
				map.put("timestart", str);
			}
			if(!StringKit.isEmpty(map.get("timeend"))){
				String timeEnd = map.get("timeend")+" 23:59:59";
				Date str = sdf.parse(timeEnd); 
				map.put("timeend", str);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Long name = userService.getCurrentUser().getId();
		Txl txl = txl_service.getDao().getTxlByUserId(name);
		if(txl!=null){
			map.put("sjr", txl.getId());
		}else{
			map.put("sjr", name);
		}
		Page<Hytz> page = queryForPage(HytzDto.class, map);
		return new CommonConvert<Hytz>(page, new Cci<Hytz>() {
			@Override
			public Map<String, Object> convertObj(Hytz t) {
				Map<String, Object> map=new HashMap<String,Object>();
				String name = userService.getCurrentUser().getRealname();
				Hyfk hyfk = hyfk_service.getDao().queryByName(name,t.getId());
					map.put("id", t.getId());
					map.put("titles", t.getTitles());
					map.put("huiyitext", t.getHuiyitext());
					map.put("sfnote", t.getSfnote() == null?"":t.getSfnote().getName());
					map.put("huiyitime", t.getHuiyitime());
					map.put("huiyiaddress", t.getHuiyiaddress());
					map.put("fjr",t.getFjrName());
					if(hyfk!=null){
						map.put("hyfk", hyfk.getHyfk() == null?"":hyfk.getHyfk().getName());
					}
				return map;
			}
		}).getDataGrid();
	}


	/**
	 * 获取单个会议信息
	 * @param id
	 * @return
	 * @author yzw
	 * @created 2017年8月22日
	 */
	public Map<String, Object> getInfo(String id) {
		Map<String,Object> map = new HashMap<String,Object>();
		Hytz h = getDao().findOne(Long.parseLong(id));
		map.put("id", id);
		map.put("name",h.getName());
		map.put("titles",h.getTitles());
		map.put("huiyiaddress",h.getHuiyiaddress());
		map.put("huiyitime",h.getHuiyitime());
		map.put("huiyitext", h.getHuiyitext());
		if(!StringKit.isEmpty(h.getHytzfile())){
			map.put("hytzfile",h.getHytzfile());
		}
		map.put("sfnote", h.getSfnote() == null?"":h.getSfnote().getCode());
		String str = "";
		for(Txl t :h.getSjr()){
			if(t!=null){
				if(str.indexOf(t.getId().toString())==-1){
					str+=t.getId()+",";
				}
			}
		}
		map.put("sjr",str);
		return map;
	}


	/**
	 * 删除会议
	 * @param id
	 * @author yzw
	 * @created 2017年8月23日
	 */
	/*public Result deleteByIds(String ids) {
		List<Long> idList = Utils.stringToLongList(ids, ",");
		String name = userService.getCurrentUser().getUsername();
		for(Long id:idList){
			Hytz hytz = getDao().findOne(id);
			if(!hytz.getFjrName().equals(name)){
				return new Result(false, "只能删除您本人发送的邮件!");
			}
		}
		logicDelete(idList);
		return new Result(true, "删除成功!");
	}*/
	
	/**
	 * 发送短信
	 * @param id
	 * @author yzw
	 * @created 2017年8月23日
	 */
	public void fsdx(Hytz hytz){
		String name = userService.getCurrentUser().getUsername();
		if(hytz.getSfnote().getCode().equals("1")){
			List<Txl> TList = new ArrayList<Txl>();
			TList = hytz.getSjr();
			for(Txl n:TList){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
				JSONObject message = new JSONObject();
				message.put("hysj", sdf.format(hytz.getHuiyitime()));
				message.put("hydd", hytz.getHuiyiaddress().toString());
				message.put("hybt", hytz.getTitles().toString());
				if(n!=null){
					Txl txl = txl_service.getDao().getTxlByUserName(n.getId());
					if(txl!=null&&!txl.getUser().getUsername().equals(name)&&!StringKit.isEmpty(txl.getMobile())){
						Utils.dosend("常州民政局", " SMS_96765046 ",n.getMobile(), null);
					}
					
				}
				
			}
		}
	}
	
	/**
	 * 发件人去重
	 * @param id
	 * @author yzw
	 * @created 2017年8月23日
	 */
	public List<Txl> list(Hytz hytz){
		List<Txl> oldList = hytz.getSjr();
		List<Txl> newList = new ArrayList<Txl>();
		for(Txl n : oldList){
			if(n!=null){
				if(!newList.contains(n)) {    
					newList.add(n);    
		        }  
			}
		}
		return newList;
	}
	
	/**
	 * 保存
	 * @param id
	 * @author yzw
	 * @created 2017年8月23日
	 */
	public Result saveObj(Hytz hytz){
		String name = userService.getCurrentUser().getRealname();
		if(hytz.getHuiyitime().getTime()<new Date().getTime()){
			return new Result(false);
		}
		hytz.setSjr(list(hytz));
		hytz.setFjrName(name);
		String sjrName ="";
		for(Txl list :list(hytz)){
			if(!list.getUser().getUsername().equals(userService.getCurrentUser().getUsername())){
				sjrName+=list.getUser().getUsername()+",";
			}
		}
		save(hytz);
		fsdx(hytz);
		wddyService.save("3", hytz.getId().toString(), sjrName);
		return new Result(true);
	}
	
	/**
	 * 时间校验
	 * @author wsz
	 * @created 2017年9月5日
	 */
	public Result Time(Date huiyitime){
		if(huiyitime!=null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.MINUTE,0);
			Date time = calendar.getTime();
			if(huiyitime.getTime() < time.getTime()){
				return new Result(false, "会议时间填写有误");
			}
		}
		return new Result(true);
	}
	/**
	 * 根据用户名和会议ID，查找会议反馈
	 * @author wsz
	 * @created 2017年9月22日
	 */
	public Hyfk getHyfk(Long id){
		Txl txl = txl_service.getDao().getTxlByUserId(userService.getCurrentUser().getId());
		String bmname= "";
		String name = userService.getCurrentUser().getRealname();
		if(txl!=null){
			if(txl.getBmdw()!=null){
				if(txl.getBmdw().getCode().equals("101")){
					bmname = name;
				}else{
					bmname = txl.getBmdw().getName();
				}
			}
		}
		Hyfk hyfk = hyfk_service.getDao().queryByName(bmname, id);
		return hyfk;
	}
	/**
	 * 保存代悦阅读信息
	 * @author wsz
	 * @created 2017年9月22日
	 */
	public void saveWdy(String id){
		Wddy wddy = wddyService.getDao().queryByGlid(id.toString(), userService.getCurrentUser().getUsername(),"3");
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
	/**
	 * 获得文件集合
	 * @author wsz
	 * @created 2017年9月22日
	 */
	public Object fileList(Long id){
		Hytz h = getDao().findOne(id);
		List<String> fileList = new ArrayList<String>();
		List<FileInfo> urlList = new ArrayList<FileInfo>();
		if(!StringKit.isEmpty(h.getHytzfile())){
			String [] fileSZ = null;
			fileSZ=h.getHytzfile().split(",");
			for(int i=0;i<fileSZ.length;i++){
				fileList.add(fileSZ[i]);
			}
			for(String str:fileList){
				FileInfo file = fileService.getDao().findOne(str);
				urlList.add(file);
			}
			
		}
		return urlList;
	}
	/**
	 * 获得局处室list
	 * @author wsz
	 * @created 2017年9月22日
	 */
	public List<Long> JcsList(){
		List<Txl> txlList = yhgl_service.getDao().getJcs();
		List<Long> idlist = new ArrayList<Long>();
		List <Txl> DWBMlist = new ArrayList<Txl>();
		if(!txlList.isEmpty()){
			for(Txl l :txlList){
				List<Role> roleList = l.getUser().getRoles();
				if(!roleList.isEmpty()){
					for(Role r :roleList){
						if(r.getRoleCode().equals("DWBM")){
							DWBMlist.add(l);
						}
					}
				}
			}
		}
		for(Txl txl : DWBMlist){
			idlist.add(txl.getId());
		}
		return idlist;
	}
	/**
	 * 获得直属单位list
	 * @author wsz
	 * @created 2017年9月22日
	 */
	public List<Long> ZsdwList(){
		List<Txl> txlList = yhgl_service.getDao().getZsdw();
		List<Long> idlist = new ArrayList<Long>();
		List <Txl> DWBMlist = new ArrayList<Txl>();
		if(!txlList.isEmpty()){
			for(Txl l :txlList){
				List<Role> roleList = l.getUser().getRoles();
				if(!roleList.isEmpty()){
					for(Role r :roleList){
						if(r.getRoleCode().equals("DWBM")){
							DWBMlist.add(l);
						}
					}
				}
			}
		}
		for(Txl txl : DWBMlist){
			idlist.add(txl.getId());
		}
		return idlist;
	}
	/**
	 * 获得辖市区局list
	 * @author wsz
	 * @created 2017年9月22日
	 */
	public List<Long> XsjqList(){
		List<Txl> txlList = yhgl_service.getDao().getXsq();
		List<Long> idlist = new ArrayList<Long>();
		List <Txl> DWBMlist = new ArrayList<Txl>();
		if(!txlList.isEmpty()){
			for(Txl l :txlList){
				List<Role> roleList = l.getUser().getRoles();
				if(!roleList.isEmpty()){
					for(Role r :roleList){
						if(r.getRoleCode().equals("DWBM")){
							DWBMlist.add(l);
						}
					}
				}
			}
		}
		for(Txl txl : DWBMlist){
			idlist.add(txl.getId());
		}
		return idlist;
	}
	/**
	 * 反馈情况list
	 * @author wsz
	 * @created 2017年9月22日
	 */
	public List<Map<String, Object>> fkList(Long id){
		List<Hyfk> hyfklist = new ArrayList<Hyfk>();
		hyfklist = hyfk_service.getDao().queryByPid(id);
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		if(!hyfklist.isEmpty()){
			for(Hyfk h : hyfklist){
				Map< String, Object> map = new HashMap<String, Object>();
				if(h.getSfcj().getCode().equals("1")){
					map.put("fkname", h.getBmname());
					map.put("chry",h.getChjtry());
					map.put("fkqk", "已报名");
				}else{
					map.put("fkname", h.getBmname());
					map.put("fkqk", "未报名");
				}
				listMap.add(map);
			}
		}
		return listMap;
	}
	/**
	 * 保存反馈信息
	 * @author wsz
	 * @created 2017年9月22日
	 */
	public Result SaveFk(Hyfk hyfk){
		FK fk = new FK();
		if(hyfk.getSfcj().getCode().equals("1")){
			fk.setCode("1");
		}else{
			fk.setCode("3");
		}
		hyfk.setHyfk(fk);
		Txl txl = txl_service.getDao().getTxlByUserId(userService.getCurrentUser().getId());
		String name = userService.getCurrentUser().getRealname();
		String bmname= "";
		if(txl!=null){
			if(txl.getBmdw()!=null){
				if(txl.getBmdw().getCode().equals("101")){
					bmname = name;
				}else{
					bmname = txl.getBmdw().getName();
				}
			}
		}
		hyfk.setBmname(bmname);
		hyfk.setName(name);
		hyfk = hyfk_service.save(hyfk);
		saveWdy(hyfk.getHyid().toString());
		if(hyfk.getSfcj().getCode().equals("1")){
			return new Result(true, "感谢您的报名，谢谢！");
		}
		return new Result(true, "反馈成功！");
	}
}
