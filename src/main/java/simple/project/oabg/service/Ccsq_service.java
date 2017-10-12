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
import simple.project.oabg.dao.KqglDao;
import simple.project.oabg.dao.kqSqFlowDao;
import simple.project.oabg.dic.model.Kqlcsp;
import simple.project.oabg.dic.model.SF;
import simple.project.oabg.dic.model.Tgzt;
import simple.project.oabg.dto.KqglDto;
import simple.project.oabg.dto.YhglDto;
import simple.project.oabg.entities.Kqgl;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.entities.kqglSqFlow;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;
import simple.system.simpleweb.platform.util.StringKit;

@Service
public class Ccsq_service extends AbstractService<KqglDao, Kqgl, Long>{
	@Autowired
	private kqSqFlowDao KqSqFlowDao; 
	@Autowired
	private UserService userService; 
	@Autowired
	private Txl_service txl_service;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private WddbService wddbService;
	@Autowired
	public Ccsq_service(KqglDao dao) {
		super(dao);
	}
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 请假列表
	 * @param map
	 * @return
	 * @author yzw
	 * @throws ParseException 
	 * @created 2017年8月21日
	 */
	public DataGrid grid(Map<String,Object> map){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(!StringKit.isEmpty(map.get("timeStr"))){
				String timeStr = map.get("timeStr")+" 00:00:00";
				Date str = sdf.parse(timeStr); 
				map.put("timeStr", str);
			}
			if(!StringKit.isEmpty(map.get("timeEnd"))){
				String timeEnd = map.get("timeEnd")+" 23:59:59";
				Date str = sdf.parse(timeEnd); 
				map.put("timeEnd", str);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("sfcc", "1");
		//map.put("qjname", userService.getCurrentUser().getRealname());
		Txl txl = txl_service.getDao().getTxlByUserId(userService.getCurrentUser().getId());
		if(txl!=null){
			map.put("ccry", txl.getId());
		}
		Page<Kqgl> page = queryForPage(KqglDto.class, map);
		return new CommonConvert<Kqgl>(page, new Cci<Kqgl>() {
			@Override
			public Map<String, Object> convertObj(Kqgl k) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", k.getId());
				map.put("ccname", k.getName());
				map.put("ccbm", k.getBmdw()==null?"":k.getBmdw().getName());
				map.put("lxdh", k.getLxfs());
				map.put("ccaddress", k.getCcaddress());
				map.put("ccstr", k.getTimeStr());
				map.put("ccend", k.getTimeEnd());
				map.put("ccsy", k.getSy());
				map.put("lczt", null == k.getLczt() ? "" : k.getLczt().getCode());
				map.put("lcztName", null == k.getLczt() ? "" : k.getLczt().getName());
				return map;
			}
		}).getDataGrid();
	}

	/**
	 * 查询流程记录
	 * 2017年9月4日
	 * yzw
	 * @param id
	 * @return
	 */
	public List<Map<String,Object>> queryFlowById(String id){
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		List<kqglSqFlow> list = KqSqFlowDao.queryByGlid(id);
		for (kqglSqFlow t : list) {
			Map<String,Object> m = new HashMap<String, Object>();
			User user = userService.getUserByUsername(t.getPuser());
			m.put("pUserName", null == user ? "" : user.getRealname());
			m.put("cnode", t.getCnode());
			m.put("tgztName", null == t.getTgzt() ? "" : t.getTgzt().getName());
			m.put("suggestion", t.getSuggestion());
			m.put("clsj", sdf.format(t.getClsj()));
			res.add(m);
		}
		return res;
	}
	
	/**
	 * 自动回填姓名，部门，联系方式
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	public Map<String, Object> info(){
		Map<String, Object> map = new HashMap<String, Object>();
		Long id = userService.getCurrentUser().getId();
		Txl txl  = txl_service.getDao().getTxlByUser(id);
		if(txl!=null){
			map.put("name", txl.getRealname());
			map.put("bmName", txl.getBmdw()==null?"":txl.getBmdw().getName());
			map.put("bmCode", txl.getBmdw()==null?"":txl.getBmdw().getCode());
			map.put("lxfs", txl.getMobile());
		}
		return map;
	}
	
	/**
	 * 出差申请
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	public Result saveObj(Kqgl kqgl){
		String name = "";
		String code = "";
		User user = userService.getCurrentUser();
		List< Role> u = user.getRoles();
		List<String> CodeList = new ArrayList<String>();
		for(Role r :u){
			CodeList.add(r.getRoleCode());
		}
		Txl txl = txl_service.getDao().getTxlByUser(userService.getCurrentUser().getId());
		if("".equals(StringSimple.nullToEmpty(kqgl.getId()))){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.MINUTE,-1);
			Date time = calendar.getTime();
			if(kqgl.getTimeStr().getTime()>kqgl.getTimeEnd().getTime()){
				return new Result(false,"出差开始不能大于出差结束时间");
			}
			if(kqgl.getTimeStr().getTime() < time.getTime()){
				return new Result(false,"出差开始时间填写有误");
			}
			if(kqgl.getTimeEnd().getTime() < time.getTime()){
				return new Result(false,"出差结束时间填写有误");
			}
			Kqlcsp lczt = new Kqlcsp();
			if(txl!=null){
				if(!CodeList.contains("CZ")&&!CodeList.contains("FGLD")&&!CodeList.contains("JZ")&&!txl.getBmdw().getCode().equals("102")&&!CodeList.contains("BGSFZR")){
					lczt.setCode("1");
					User cz = yhgl_service.getCz();
					name="#"+cz.getUsername()+"#";
					kqgl.setCzName(cz.getUsername());
				}
				if(!CodeList.contains("CZ")&&!CodeList.contains("FGLD")&&!CodeList.contains("JZ")&&txl.getBmdw().getCode().equals("102")&&!CodeList.contains("BGSFZR")){
					lczt.setCode("0");
					User cz = yhgl_service.getCz();
					name="#"+cz.getUsername()+"#";
					kqgl.setCzName(cz.getUsername());
				}
				if(CodeList.contains("CZ")&&txl.getGzzw().getCode().equals("4")){
					lczt.setCode("1");
					User cz = yhgl_service.getCz();
					kqgl.setCzName(cz.getUsername());
					name="#"+cz.getUsername()+"#";
				}
				if(CodeList.contains("CZ")&&!txl.getGzzw().getCode().equals("4")){
					lczt.setCode("2");
					User user2 = getFgld();
					List<String> rolecode = new ArrayList<String>();
					for(Role role2 :user2.getRoles()){
						rolecode.add(role2.getRoleCode());
					}
					if(rolecode.contains("JZ")){
						kqgl.setJzName(user2.getUsername());
						kqgl.setFgldName(user2.getUsername());
					}else{
						kqgl.setFgldName(user2.getUsername());
					}
					name="#"+user2.getUsername()+"#";
				}
				if(txl.getBmdw().getCode().equals("102")&&CodeList.contains("BGSFZR")){
					lczt.setCode("2");
					User user2 = getFgld();
					name="#"+user2.getUsername()+"#";
					List<String> rolecode = new ArrayList<String>();
					for(Role role2 :user2.getRoles()){
						rolecode.add(role2.getRoleCode());
					}
					if(rolecode.contains("JZ")){
						kqgl.setJzName(user2.getUsername());
						kqgl.setFgldName(user2.getUsername());
					}else{
						kqgl.setFgldName(user2.getUsername());
					}
				}
			}
			SF sf = new SF();
			sf.setCode("1");
			kqgl.setCcry(list(kqgl));
			kqgl.setSfcc(sf);
			kqgl.setLczt(lczt);
			save(kqgl);
			code = kqgl.getLczt().getCode();
		}else{
			Kqgl old = getDao().findOne(kqgl.getId());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.MINUTE,-1);
			Date time = calendar.getTime();
			if(kqgl.getTimeStr().getTime()>kqgl.getTimeEnd().getTime()){
				return new Result(false,"出差开始不能大于出差结束时间");
			}
			if(kqgl.getTimeStr().getTime() < time.getTime()){
				return new Result(false,"出差开始时间填写有误");
			}
			if(kqgl.getTimeEnd().getTime() < time.getTime()){
				return new Result(false,"出差结束时间填写有误");
			}
			old.setBmdw(kqgl.getBmdw());
			old.setName(kqgl.getName());
			old.setLxfs(kqgl.getLxfs());
			old.setCcaddress(kqgl.getCcaddress());
			SF sf = new SF();
			sf.setCode("1");
			old.setSfcc(sf);
			old.setCcry(list(kqgl));
			old.setSy(kqgl.getSy());
			old.setBz(kqgl.getBz());
			Kqlcsp lczt = new Kqlcsp();
			if(txl!=null){
				
				if(!CodeList.contains("CZ")&&!CodeList.contains("FGLD")&&!CodeList.contains("JZ")&&!txl.getBmdw().getCode().equals("102")&&!CodeList.contains("BGSFZR")){
					lczt.setCode("1");
					User cz = yhgl_service.getCz();
					name="#"+cz.getUsername()+"#";
					kqgl.setCzName(cz.getUsername());
				}
				if(!CodeList.contains("CZ")&&!CodeList.contains("FGLD")&&!CodeList.contains("JZ")&&txl.getBmdw().getCode().equals("102")&&!CodeList.contains("BGSFZR")){
					lczt.setCode("0");
					User cz = yhgl_service.getCz();
					name="#"+cz.getUsername()+"#";
					kqgl.setCzName(cz.getUsername());
				}
				if(CodeList.contains("CZ")&&txl.getGzzw().getCode().equals("4")){
					lczt.setCode("1");
					User cz = yhgl_service.getCz();
					kqgl.setCzName(cz.getUsername());
					name="#"+cz.getUsername()+"#";
				}
				if(CodeList.contains("CZ")&&!txl.getGzzw().getCode().equals("4")&&!CodeList.contains("BGSFZR")){
					lczt.setCode("2");
					User user2 = getFgld();
					List<String> rolecode = new ArrayList<String>();
					for(Role role2 :user2.getRoles()){
						rolecode.add(role2.getRoleCode());
					}
					if(rolecode.contains("JZ")){
						kqgl.setJzName(user2.getUsername());
						kqgl.setFgldName(user2.getUsername());
					}else{
						kqgl.setFgldName(user2.getUsername());
					}
					name="#"+user2.getUsername()+"#";
				}
				if(txl.getBmdw().getCode().equals("102")&&CodeList.contains("BGSFZR")){
					lczt.setCode("2");
					User user2 = getFgld();
					name="#"+user2.getUsername()+"#";
					List<String> rolecode = new ArrayList<String>();
					for(Role role2 :user2.getRoles()){
						rolecode.add(role2.getRoleCode());
					}
					if(rolecode.contains("JZ")){
						kqgl.setJzName(user2.getUsername());
						kqgl.setFgldName(user2.getUsername());
					}else{
						kqgl.setFgldName(user2.getUsername());
					}
				}
			}
			old.setSfyc(kqgl.getSfyc());
			lczt.setCode("0");
			old.setLczt(lczt);
			old=save(old);
			code = old.getLczt().getCode();
		}
		kqglSqFlow lcjl = new kqglSqFlow();
		lcjl.setGlid(StringSimple.nullToEmpty(kqgl.getId()));
		lcjl.setCnode("提交");
		Tgzt tgzt = new Tgzt();
		tgzt.setCode("1");
		lcjl.setClsj(new Date());
		lcjl.setTgzt(tgzt);
		lcjl.setPuser(userService.getCurrentUser().getUsername());
		lcjl.setSuggestion("");
		KqSqFlowDao.save(lcjl);
		return wddbService.saveNewWddb(Long.parseLong(StringSimple.nullToEmpty(kqgl.getId())), "30",kqgl.getName()+"的出差申请", name, code);
	}
	/**
	 * 回填信息
	 * @author yzw
	 * @created 2017年8月21日
	 * @return
	 */
	public Map<String, Object> Getinfo(Long id){
		Kqgl kqgl = getDao().findOne(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("name", kqgl.getName());
		map.put("bm", kqgl.getBmdw()==null?"":kqgl.getBmdw().getName());
		map.put("bmValue", kqgl.getBmdw()==null?"":kqgl.getBmdw().getCode());
		map.put("lxfs", kqgl.getLxfs());
		map.put("ccaddress",kqgl.getCcaddress());
		map.put("timeStr", kqgl.getTimeStr());
		map.put("timeEnd", kqgl.getTimeEnd());
		map.put("sy", kqgl.getSy());
		map.put("bz", kqgl.getBz());
		map.put("ccrname", kqgl.getCcrname());
		map.put("sfyc", kqgl.getSfyc());
		return map;
	}
	/**
	 * 删除申请
	 * @param id
	 * @author yzw
	 * @created 2017年8月23日
	 */
	public Result deleteByIds(String ids) {
		List<Long> idList = Utils.stringToLongList(ids, ",");
		logicDelete(idList);
		return new Result(true, "删除成功!");
	}
	
	/**
	 * 获取本部门的分管领导
	 * @author wsz
	 * @created 2017年9月5日
	 */
	public User getFgld(){
		Map<String, Object> map=new HashMap<String,Object>();
		Txl txl = yhgl_service.getDao().getTxlByUser(userService.getCurrentUser().getId());
		String code = txl.getBmdw().getCode();
		map.put("fgbm", code);
		List<Txl> list =yhgl_service.query(YhglDto.class, map);
		return list.get(0).getUser();
	}
	
	/**
	 * 时间校验
	 * @author wsz
	 * @created 2017年9月5日
	 */
	public Result TimeStr(Date timeStr,Date timeEnd){
		if((!"".equals(StringSimple.nullToEmpty(timeStr)))){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.MINUTE,-1);
			Date time = calendar.getTime();
			if(timeStr.getTime() < time.getTime()){
				return new Result(false, "出差开始时间不能小于当前时间！");
			}
		}
		if((!"".equals(StringSimple.nullToEmpty(timeStr)))&&(!"".equals(StringSimple.nullToEmpty(timeEnd)))){
			if(timeStr.getTime() > timeEnd.getTime()){
				return new Result(false, "出差开始时间不能大于出差结束时间！");
			}
		}
		return new Result(true);
	}
	
	/**
	 * 时间校验
	 * @author wsz
	 * @created 2017年9月5日
	 */
	public Result TimeEnd(Date timeStr,Date timeEnd){
		if(!"".equals(StringSimple.nullToEmpty(timeEnd))){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.MINUTE,-1);
			Date time = calendar.getTime();
			if(timeEnd.getTime() < time.getTime()){
				return new Result(false, "请假结束时间不能小于当前时间！");
			}
		}
		if((!"".equals(StringSimple.nullToEmpty(timeStr)))&&(!"".equals(StringSimple.nullToEmpty(timeEnd)))){
			if(timeStr.getTime() > timeEnd.getTime()){
				return new Result(false, "请假开始时间不能大于请假结束时间！");
			}
		}
		return new Result(true);
	}
	
	/**
	 * 发件人去重
	 * @param id
	 * @author yzw
	 * @created 2017年8月23日
	 */
	public List<Txl> list(Kqgl kqgl){
		List<Txl> oldList = kqgl.getCcry();
		List<Txl> newList = new ArrayList<Txl>();
		if(!oldList.isEmpty()){
			for(Txl n : oldList){
				if(n!=null){
					if(!newList.contains(n)) {    
						newList.add(n);    
			        }  
				}
			}
		}
		return newList;
	}
}
