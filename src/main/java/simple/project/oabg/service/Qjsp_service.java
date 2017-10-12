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
import simple.project.oabg.dao.DeDicItemDao;
import simple.project.oabg.dao.KqglDao;
import simple.project.oabg.dao.kqSqFlowDao;
import simple.project.oabg.dic.model.Kqlcsp;
import simple.project.oabg.dic.model.Tgzt;
import simple.project.oabg.dto.KqglDto;
import simple.project.oabg.entities.Kqcx;
import simple.project.oabg.entities.Kqgl;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.entities.kqglSqFlow;
import simple.system.simpleweb.module.system.dic.model.DicItem;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;
import simple.system.simpleweb.platform.util.StringKit;

@Service
public class Qjsp_service extends AbstractService<KqglDao, Kqgl, Long>{
	@Autowired
	private kqSqFlowDao KqSqFlowDao; 
	@Autowired
	private UserService userService; 
	@Autowired
	private Qjsq_service qjsq_service; 
	@Autowired
	private Txl_service txl_service;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private WddbService wddbService; 
	@Autowired
	private kqcx_service kqcx_service; 
	@Autowired
	private DeDicItemDao deDicItemDao;
	@Autowired
	public Qjsp_service(KqglDao dao) {
		super(dao);
	}
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 请假列表
	 * @param map
	 * @return
	 * @author yzw
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
			e.printStackTrace();
		}
		if(!"".equals(StringSimple.nullToEmpty( map.get("bmdw")))){
			String name = "%"+StringSimple.nullToEmpty( map.get("bmdw"))+"%";
			List<DicItem> diclist = deDicItemDao.findItemByDicCodeAndName(name, "BMDW");
			List<String> codeList = new ArrayList<String>();
			if(!diclist.isEmpty()){
				for(DicItem d : diclist){
					codeList.add(d.getCode());
				}
			}else{
				codeList.add("-1");
			}
			map.put("bmdws", codeList);
		}
		map.put("sfcc", "2");
		Txl txl = txl_service.getDao().getTxlByUser(userService.getCurrentUser().getId());
		User user = userService.getCurrentUser();
		List< Role> u = user.getRoles();
		List<String> lczts = new ArrayList<String>();
		List<String> role = new ArrayList<String>();
		for(Role r :u){
			role.add(r.getRoleCode());
		}
		
		if(txl!=null){
			if(role.contains("CZ")){
				map.put("czName",user.getUsername());
				lczts.add("1");
				lczts.add("2");
				lczts.add("4");
				map.put("lczts", lczts);
			}
			if(role.contains("BGSFZR")){
				map.put("czName",user.getUsername());
				lczts.add("0");
				lczts.add("2");
				lczts.add("4");
				map.put("lczts", lczts);
			}
			if(role.contains("FGLD")){
				lczts.add("2");
				lczts.add("3");
				lczts.add("4");
				map.put("lczts", lczts);
				map.put("fgname",user.getUsername());
			}
			if(role.contains("JZ")){
				lczts.add("2");
				lczts.add("3");
				lczts.add("4");
				map.put("lczts", lczts);
				map.put("jzName",user.getUsername());
			}
			if(!role.contains("JZ")&&!role.contains("FGLD")&&!role.contains("BGSFZR")&&!role.contains("CZ")
				){
				map.put("jzName","-1");
				map.put("fgname","-1");
				map.put("czName","-1");
			}
			}
		Page<Kqgl> page = queryForPage(KqglDto.class, map);
		return new CommonConvert<Kqgl>(page, new Cci<Kqgl>() {
			@Override
			public Map<String, Object> convertObj(Kqgl k) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", k.getId());
				map.put("qjname", k.getName());
				map.put("qjbm", k.getBmdw()==null?"":k.getBmdw().getName());
				map.put("lxdh", k.getLxfs());
				map.put("qjlx", k.getQjlx()==null?"":k.getQjlx().getName());
				map.put("qjstr", k.getTimeStr());
				map.put("qjend", k.getTimeEnd());
				map.put("qjsy", k.getSy());
				map.put("lczt", null == k.getLczt() ? "" : k.getLczt().getCode());
				map.put("lcztName", null == k.getLczt() ? "" : k.getLczt().getName());
				User user = userService.getCurrentUser();
				List< Role> u = user.getRoles();
				String role = "";
				for(Role r : u){
					role+=r.getRoleCode()+",";
				}
				map.put("role", role);
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
	 * 执行审批
	 * 2017年9月4日
	 * yc
	 * @param id
	 * @param sftg
	 * @param suggestion
	 * @return
	 */
	
	public Result doSp(String id,String sftg,String suggestion){
		String name = "";
		Kqgl t = getDao().findOne(Long.parseLong(id));
		Kqcx kqcx = new Kqcx();
		Kqlcsp lczt = new Kqlcsp();
		List<Role> role = userService.getCurrentUser().getRoles();
		List<String> r = new ArrayList<String>();
		for(Role ro :role){
			r.add(ro.getRoleCode());
		}
		kqglSqFlow lcjl = new kqglSqFlow();
		lcjl.setGlid(id);
		if(r.contains("BGSFZR")){
			if("1".equals(StringSimple.nullToEmpty(sftg))){
				lczt.setCode("2");
				lcjl.setCnode("审批");
				User user = qjsq_service.getFgld(); 
				List<String> rolecode = new ArrayList<String>();
				for(Role role1 :(user.getRoles())){
					rolecode.add(role1.getRoleCode());
				}
				if(rolecode.contains("JZ")){
					t.setJzName(user.getUsername());
					t.setFgldName(user.getUsername());
				}else{
					t.setFgldName(user.getUsername());
				}
				t.setTimes("1");
				name="#"+user.getUsername()+"#";
			}else{
				lczt.setCode("5");
				lcjl.setCnode("审批");
			}
		}
		if(r.contains("CZ")){
			if("1".equals(StringSimple.nullToEmpty(sftg))){
				lczt.setCode("2");
				lcjl.setCnode("审批");
				User user = qjsq_service.getFgld(); 
				List<String> rolecode = new ArrayList<String>();
				for(Role role1 :(user.getRoles())){
					rolecode.add(role1.getRoleCode());
				}
				if(rolecode.contains("JZ")){
					t.setJzName(user.getUsername());
					t.setFgldName(user.getUsername());
				}else{
					t.setFgldName(user.getUsername());
				}
				t.setTimes("1");
				t.setJzspcs("1");
				name="#"+user.getUsername()+"#";
			}else{
				lczt.setCode("5");
				lcjl.setCnode("审批");
			}
		}
		if(r.contains("FGLD")&&r.contains("JZ")){
			if("1".equals(StringSimple.nullToEmpty(sftg))){
					lczt.setCode("4");
					lcjl.setCnode("审批");
					kqcx.setBmdw(t.getBmdw());
					kqcx.setName(t.getName());
					kqcx.setSfcc(t.getSfcc());
					kqcx.setQjlx(t.getQjlx());
					kqcx_service.save(kqcx);
			}else{
				lczt.setCode("5");
				lcjl.setCnode("审批");
			}
		}
		if(r.contains("FGLD")&&!r.contains("JZ")){
			if("1".equals(StringSimple.nullToEmpty(sftg))){
				if(!StringKit.isEmpty(t.getTimes())){
					lczt.setCode("4");
					lcjl.setCnode("审批");
					kqcx.setBmdw(t.getBmdw());
					kqcx.setName(t.getName());
					kqcx.setSfcc(t.getSfcc());
					kqcx.setQjlx(t.getQjlx());
					kqcx_service.save(kqcx);
				}else{
					lczt.setCode("3");
					lcjl.setCnode("审批");
					User user = yhgl_service.getJz();
					t.setJzName(user.getUsername());
					name="#"+user.getUsername()+"#";
				}
			}else{
				lczt.setCode("5");
				lcjl.setCnode("审批");
			}
		}
		if(r.contains("JZ")&&!r.contains("FGLD")){
			if("1".equals(StringSimple.nullToEmpty(sftg))){
				lczt.setCode("4");
				lcjl.setCnode("审批");
				kqcx.setBmdw(t.getBmdw());
				kqcx.setName(t.getName());
				kqcx.setSfcc(t.getSfcc());
				kqcx.setQjlx(t.getQjlx());
				kqcx_service.save(kqcx);
			}else{
				lczt.setCode("5");
				lcjl.setCnode("审批");
			}
			/*	if(StringSimple.nullToEmpty(t.getJzspcs()).equals("")){
					if("1".equals(StringSimple.nullToEmpty(sftg))){
						lczt.setCode("3");
						lcjl.setCnode("分管领导审批");
						kqcx.setBmdw(t.getBmdw());
						kqcx.setName(t.getName());
						kqcx.setSfcc(t.getSfcc());
						kqcx.setQjlx(t.getQjlx());
						t.setJzspcs("2");
						kqcx_service.save(kqcx);
					}else{
						lczt.setCode("5");
						lcjl.setCnode("分管领导审批");
					}
				}else{
					if(StringSimple.nullToEmpty(t.getJzspcs()).equals("1")){
						if("1".equals(StringSimple.nullToEmpty(sftg))){
							lczt.setCode("4");
							lcjl.setCnode("分管领导审批");
							kqcx.setBmdw(t.getBmdw());
							kqcx.setName(t.getName());
							kqcx.setSfcc(t.getSfcc());
							kqcx.setQjlx(t.getQjlx());
							kqcx_service.save(kqcx);
						}else{
							lczt.setCode("5");
							lcjl.setCnode("分管领导审批");
						}
					}else{
						if("1".equals(StringSimple.nullToEmpty(sftg))){
							lczt.setCode("4");
							lcjl.setCnode("局长审批");
							kqcx.setBmdw(t.getBmdw());
							kqcx.setName(t.getName());
							kqcx.setSfcc(t.getSfcc());
							kqcx.setQjlx(t.getQjlx());
							kqcx_service.save(kqcx);
						}else{
							lczt.setCode("5");
							lcjl.setCnode("局长审批");
						}
					}
				}*/
			}
		t.setLczt(lczt);
		save(t);
		//保存流程记录
		Tgzt tgzt = new Tgzt();
		tgzt.setCode("1".equals(StringSimple.nullToEmpty(sftg)) ? "1" : "2");
		lcjl.setTgzt(tgzt);
		lcjl.setClsj(new Date());
		lcjl.setRealname(userService.getCurrentUser().getRealname());
		lcjl.setPuser(userService.getCurrentUser().getUsername());
		lcjl.setSuggestion(suggestion);
		KqSqFlowDao.save(lcjl);
		return wddbService.saveNewWddb(Long.parseLong(id), "20",t.getName()+"的"+(t.getQjlx()==null?"":t.getQjlx().getName())+ "请假申請", name, lczt.getCode());
	}
	
}
