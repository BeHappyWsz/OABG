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
import simple.project.oabg.dao.FwngDao;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.dic.model.Fwzt;
import simple.project.oabg.dic.model.SF;
import simple.project.oabg.dic.model.Zt;
import simple.project.oabg.dto.FwngDto;
import simple.project.oabg.entities.Fwng;
import simple.project.oabg.entities.FwngFlow;
import simple.project.oabg.entities.Txl;
import simple.system.simpleweb.module.system.file.dao.FileInfoDao;
import simple.system.simpleweb.module.system.file.model.FileInfo;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;

/**
 * 发文
 * @author sxm
 * @created 2017年8月28日
 */
@Service
public class FwngService extends AbstractService<FwngDao, Fwng, Long>{
	
	@Autowired
	public FwngService(FwngDao dao) {
		super(dao);
	}
	@Autowired
	private FileInfoDao fileInfoDao;
	@Autowired
	private FwngFlowService fwngFlowService;
	@Autowired
	private Txl_service txl_service;
	@Autowired
	private UserService userService;
	@Autowired
	private YhglDao yhglDao;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private WddbService wddbService;
	
	/**
	 * 返回grid列表
	 * @author sxm
	 * @created 2017年8月29日
	 * @param map
	 * @return
	 */
	public DataGrid grid(Map<String,Object> map){
		
		try {
			User user=userService.getCurrentUser();
			String username=user.getUsername();
			map.put("ngr", username);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			String times="";
			String timee="";
			//拟稿日期起
			times=StringSimple.nullToEmpty(map.get("times"));
			//拟稿日期止
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
		
			
		Page<Fwng> page = queryForPage(FwngDto.class, map);
		return new CommonConvert<Fwng>(page, new Cci<Fwng>() {
			@Override
			public Map<String, Object> convertObj(Fwng t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("bt", t.getBt());
				map.put("mj", t.getMj()==null?"":t.getMj());
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				map.put("rq", t.getNgrq()==null?"":sdf.format(t.getNgrq()));
				Long userid=userService.getCurrentUser().getId();
				Txl txl=yhglDao.getTxlByUser(userid);
				map.put("gzbm",txl.getBmdw()==null?"":txl.getBmdw().getName());
				map.put("fwzt", t.getFwzt()==null?"":t.getFwzt().getCode());
				map.put("ddsj", t.getUpdateTime()==null?"":sdf.format(t.getUpdateTime()));
				map.put("fqr", t.getNgr());
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
				return map;
			}
		}).getDataGrid();
	}
	
	/**
	 * 返回grid列表
	 * @author sxm
	 * @created 2017年8月29日
	 * @param map
	 * @return
	 */
	public DataGrid grid1(Map<String,Object> map){
		try {
			User user=userService.getCurrentUser();
			String username=user.getUsername();
			map.put("username", username);
			List<String> list=new ArrayList<String>();
			list.add("1");
			list.add("2");
			list.add("3");
			list.add("4");
			list.add("5");
			list.add("6");
			list.add("7");
			list.add("8");
			list.add("10");
			list.add("11");
			map.put("fwzt", list);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			String times="";
			String timee="";
			//拟稿日期起
			times=StringSimple.nullToEmpty(map.get("times"));
			//拟稿日期止
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
		
		
		Page<Fwng> page = queryForPage(FwngDto.class, map);
		return new CommonConvert<Fwng>(page, new Cci<Fwng>() {
			@Override
			public Map<String, Object> convertObj(Fwng t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("bt", t.getBt());
				map.put("fqr", t.getNgr());
				User user=userService.getUserByUsername( t.getNgr());
				map.put("fqrName", user==null?"":user.getRealname());
				map.put("mj", t.getMj()==null?"":t.getMj());
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				map.put("rq", t.getNgrq()==null?"":sdf.format(t.getNgrq()));
				Long userid=userService.getUserByUsername(t.getNgr()).getId();
				Txl txl=yhglDao.getTxlByUser(userid);
				map.put("gzbm",txl.getBmdw()==null?"":txl.getBmdw().getName());
				map.put("fwzt", t.getFwzt()==null?"":t.getFwzt().getCode());
				map.put("fwztName", t.getFwzt() == null ? "" : t.getFwzt().getName());
				map.put("ddsj", t.getUpdateTime()==null?"":sdf.format(t.getUpdateTime()));
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
				return map;
			}
		}).getDataGrid();
	}
	
	/**
	 * 获得详情
	 * @author sxm
	 * @created 2017年8月29日
	 * @param id
	 * @return
	 */
	public Map<String,Object> getInfo(String id){
		Fwng fwng=getDao().findOne(Long.valueOf(id));
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id",fwng.getId() );
		map.put("bt", fwng.getBt());
		String zs=fwng.getZs();
		map.put("zs", zs);
		List<Txl> zsdws = fwng.getZsdw();
		String zsdwid="";
		for(Txl txl:zsdws){
			zsdwid+=txl.getId()+",";
		}
		if(!"".equals(StringSimple.nullToEmpty(zsdwid))){
			zsdwid=zsdwid.substring(0, zsdwid.length()-1);
		}
		
		map.put("zsdws", zsdwid);
		List<Txl> csdws = fwng.getCsdw();
		if(!csdws.isEmpty()){
			String csdwid="";
			for(Txl txl:csdws){
				csdwid+=txl.getId()+",";
			}
			csdwid=csdwid.substring(0, csdwid.length()-1);
			map.put("csdws", csdwid);
		}
		map.put("cs", fwng.getCs());
		map.put("fsdx", fwng.getFsdx()==null?"":fwng.getFsdx().getCode());
		map.put("sfgk", fwng.getSfgk()==null?"":fwng.getSfgk().getCode());
		map.put("sfgkname", fwng.getSfgk()==null?"":fwng.getSfgk().getName());
		map.put("ngr", fwng.getNgr());
		String ngrRealname=userService.getUserByUsername(fwng.getNgr()).getRealname();
		map.put("ngrRealname", ngrRealname);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		map.put("ngrq", sdf.format(fwng.getNgrq()));
		map.put("mj", fwng.getMj()==null?"":fwng.getMj());
		map.put("fs", fwng.getFs());
		map.put("zw", fwng.getZw());
		String fjs="";
		List<FileInfo> fjList=fwng.getFileInfo();
		if(!fjList.isEmpty()){
			for(FileInfo fj:fjList){
			String uuid=fj.getId();
			fjs+=uuid+",";
			}
			fjs=fjs.substring(0, fjs.length()-1);
		}
		map.put("fjs", fjs);
		map.put("djbh", fwng.getDjbh());
		map.put("dzqz", fwng.getSfdzqz()==null?"":fwng.getSfdzqz().getCode());
		String dzqzs="";
		List<FileInfo> dzqzList=fwng.getDzqz();
		if(!dzqzList.isEmpty()){
			for(FileInfo dzqz:dzqzList){
			String uuid=dzqz.getId();
			dzqzs+=uuid+",";
			}
			dzqzs=dzqzs.substring(0, dzqzs.length()-1);
		}
		map.put("dzqzs", dzqzs);
		map.put("fwzt", fwng.getFwzt()==null?"":fwng.getFwzt().getCode());
		if("10".equals(fwng.getFwzt()==null?"":fwng.getFwzt().getCode()) || "11".equals(fwng.getFwzt()==null?"":fwng.getFwzt().getCode())){
			map.put("tgzt", "1");
		}
		map.put("bz", fwng.getBz());
		
		return map;
	}
	
	/**
	 * 保存发文拟稿信息
	 * @author sxm
	 * @created 2017年8月29日
	 * @param fwng
	 * @param zsdws
	 * @param csdws
	 */
	public void save(Fwng fwng,String zsdws,String csdws,String fjs){
		if(!"".equals(StringSimple.nullToEmpty(zsdws))){
			String[] zsdwArray=zsdws.split(",");
			List<Txl> zsdwList=new ArrayList<Txl>();
			for(String zsdwid:zsdwArray){
				Txl txl=txl_service.getDao().findOne(Long.valueOf(zsdwid));
				zsdwList.add(txl);
			}
			fwng.setZsdw(zsdwList);
		}
		if(!"".equals(StringSimple.nullToEmpty(csdws))){
			String[] csdwArray=csdws.split(",");
			List<Txl> csdwList=new ArrayList<Txl>();
			for(String csdwid:csdwArray){
				Txl txl=txl_service.getDao().findOne(Long.valueOf(csdwid));
				csdwList.add(txl);
			}
			fwng.setCsdw(csdwList);
		}
		if(!"".equals(StringSimple.nullToEmpty(fjs))){
			String[] fjArray=fjs.split(",");
			List<FileInfo> fjList=new ArrayList<FileInfo>();
			for(String fj:fjArray){
				FileInfo fileInfo=fileInfoDao.findOne(fj);
				fjList.add(fileInfo);
			}
			fwng.setFileInfo(fjList);
		}
		
		//默认发文状态为在途
		Zt zt=new Zt();
		zt.setCode("2");
		fwng.setZt(zt);
		//发文状态
		Fwzt fwzt=new Fwzt();
		
		
		//如果是处长拟稿
		boolean czng=false;
		User user=userService.getCurrentUser();
		List<Role> roles=user.getRoles();
		for(Role role:roles){
			if("CZ".equals(role.getRoleCode())){
				czng=true;
				break;
			}
		}
		boolean isBgs=false;
		for(Role role:roles){
			if("JYWYSZR".equals(role.getRoleCode())){
				isBgs=true;
				break;
			}else if("BGSZR".equals(role.getRoleCode())){
				isBgs=true;
				break;
			}else if("XXGLY".equals(role.getRoleCode())){
				isBgs=true;
				break;
			}
		}
		if(czng){
			User fgld = yhgl_service.getOneFgld(user.getId());
			String names = "#"+fgld.getUsername()+"#";
			fwng.setUsername(names);
			fwzt.setCode("4");
			fwng.setFwzt(fwzt);
		}else{
			//拟稿后发给本部门处长
			if(isBgs){
				User bgsfzr=yhgl_service.getBgsfzr();
				String names = "#"+bgsfzr.getUsername()+"#";
				fwng.setUsername(names);
			}else{
				User cz=yhgl_service.getCz();
				//设置核稿的的username
				String names = "#"+cz.getUsername()+"#";
				fwng.setUsername(names);
			}
			fwzt.setCode("1");
			fwng.setFwzt(fwzt);
		}
		
		save(fwng);
		
		String fwngId=StringSimple.nullToEmpty(fwng.getId());
		
		/**提交时生成一条流程记录表**/

		//关联发文拟稿流程记录表，拟稿后各处室处长核稿
		FwngFlow fwngFlow=new FwngFlow();
		fwngFlow.setFwngid(fwngId);
		
		fwngFlow.setCnode("拟稿");
		//当前用户
		fwngFlow.setPuser(user.getUsername());
		//当前用户真实姓名
		fwngFlow.setRealName(user.getRealname());
		
		if(czng){
			fwngFlow.setNuser(yhgl_service.getOneFgld(user.getId()).getUsername());
			String name = "#"+yhgl_service.getOneFgld(user.getId()).getUsername()+"#";
			wddbService.saveNewWddb(fwng.getId(), "90",fwng.getBt(),name,"4");
		}else{
			//通过当前用户查询到部门负责人
			if(isBgs){
				fwngFlow.setNuser(yhgl_service.getBgsfzr().getUsername());
				String name = "#"+yhgl_service.getBgsfzr().getUsername()+"#";
				wddbService.saveNewWddb(fwng.getId(), "90",fwng.getBt(),name,"1");
			}else{
				fwngFlow.setNuser(yhgl_service.getCz().getUsername());
				String name = "#"+yhgl_service.getCz().getUsername()+"#";
				wddbService.saveNewWddb(fwng.getId(), "90",fwng.getBt(),name,"1");
			}
			
		}
		
		fwngFlow.setLcName("发文流程");
		fwngFlowService.save(fwngFlow);
	}
	
	
	/**
	 * 查看流程
	 * @author sxm
	 * @created 2017年9月5日
	 * @param id
	 * @return
	 */
	public List<Map<String,Object>> queryFlowById(String id){
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		List<FwngFlow> list = fwngFlowService.getDao().queryByGlid(id);
		for (FwngFlow t : list) {
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("realName", t.getRealName());
			m.put("cnode", t.getCnode());
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			m.put("clsj", sdf.format(t.getCreateTime()));
			m.put("tgzt", null == t.getTgzt()? "" : t.getTgzt().getName());
			m.put("suggestion", t.getSuggestion());
			res.add(m);
		}
		return res;
	}
	
	/**
	 * 保存审批结果
	 * @param t
	 * @return
	 */
	public void saveObj(FwngFlow fwngFlow,String fwngid,String fwzt,String sflz,String qtcz,String sfqf,String bhqr,String djbh,String dzqz,String dzqzs) {
		//提交时生成流程状态
		fwngFlow.setFwngid(fwngid);
		fwngFlow.setLcName("发文流程");
		User user=userService.getCurrentUser();
		fwngFlow.setPuser(user.getUsername());
		fwngFlow.setRealName(user.getRealname());
		//通过状态
		String tgztCode=fwngFlow.getTgzt()==null?"":fwngFlow.getTgzt().getCode();
		
		Fwng fwng=getDao().findOne(Long.valueOf(fwngid));
		
		//判断拟稿人是否为办公室
		String ngr=fwng.getNgr();
		User bgs=userService.getUserByUsername(ngr);
		List<Role> roles=bgs.getRoles();
		boolean isBgs=false;
		for(Role role:roles){
			if("JYWYSZR".equals(role.getRoleCode())){
				isBgs=true;
				break;
			}else if("BGSZR".equals(role.getRoleCode())){
				isBgs=true;
				break;
			}else if("XXGLY".equals(role.getRoleCode())){
				isBgs=true;
				break;
			}
		}
		User bgsfzr=yhgl_service.getBgsfzr();
		fwzt=StringSimple.nullToEmpty(fwzt);
		//根据发文状态判断节点
		if("1".equals(fwzt)){
			fwngFlow.setCnode("部门负责人审批");
			//当前用户为处长，下一个用户为发文管理员或者办公室负责人
			List<User> userList=yhgl_service.getUserByRoleCode("JYWYSZR");
			fwngFlow.setNuser(userList.get(0).getUsername());
			
			if("1".endsWith(tgztCode)){
				Fwzt zt=new Fwzt();
 				zt.setCode("2");
 				fwng.setFwzt(zt);
 				String names=fwng.getUsername();
				if(isBgs){
					String name = "#"+bgsfzr.getUsername()+"#";
	 				wddbService.saveNewWddb(fwng.getId(), "90",fwng.getBt(),name,"2");
	 				fwng.setUsername(names+"#"+bgsfzr.getUsername()+"#");
				}else{
					String name = "#"+userList.get(0).getUsername()+"#"+bgsfzr.getUsername()+"#";
	 				wddbService.saveNewWddb(fwng.getId(), "90",fwng.getBt(),name,"2");
	 				fwng.setUsername(names+userList.get(0).getUsername()+"#"+bgsfzr.getUsername()+"#");
				}
 				
			}else if("2".equals(tgztCode)){
				Fwzt zt=new Fwzt();
 				zt.setCode("9");
 				fwng.setFwzt(zt);
			}	
		}else if("2".equals(fwzt)){
			//当前用户为发文管理，如果需要流转，下一个用户为其他处室处长，不流转则为分管领导
			fwngFlow.setCnode("办公室审批");
			//判断是否流转审批
			if("1".endsWith(tgztCode)){
				if(!"".equals(sflz)){
					if("1".equals(sflz)){
						if(!"".equals(StringSimple.nullToEmpty(qtcz))){
							Fwzt zt=new Fwzt();
			 				zt.setCode("3");
			 				fwng.setFwzt(zt);
							fwngFlow.setNuser(qtcz);
							wddbService.saveNewWddb(fwng.getId(), "90", fwng.getBt(),"#"+qtcz+"#", "3");
						}
						String names=fwng.getUsername();
						fwng.setUsername(names+qtcz+"#");
					}else if("2".equals(sflz)){
						//不流转
						Fwzt zt=new Fwzt();
		 				zt.setCode("4");
		 				fwng.setFwzt(zt);
						String names=fwng.getUsername();
						User fgld=yhgl_service.getOneFgld(userService.getUserByUsername(ngr).getId());
						fwng.setUsername(names+fgld.getUsername()+"#");
						fwngFlow.setNuser(fgld.getUsername());
						String name = "#"+fgld.getUsername()+"#";
		 				wddbService.saveNewWddb(fwng.getId(), "90", fwng.getBt(),name, "4");
					}
				}else{
					Fwzt zt=new Fwzt();
	 				zt.setCode("4");
	 				fwng.setFwzt(zt);
					String names=fwng.getUsername();
					User fgld=yhgl_service.getOneFgld(userService.getUserByUsername(ngr).getId());
					fwng.setUsername(names+fgld.getUsername()+"#");
					fwngFlow.setNuser(fgld.getUsername());
					String name = "#"+fgld.getUsername()+"#";
	 				wddbService.saveNewWddb(fwng.getId(), "90", fwng.getBt(),name, "4");
				}
			}else if("2".endsWith(tgztCode)){
				Fwzt zt=new Fwzt();
 				zt.setCode("9");
 				fwng.setFwzt(zt);
			}
			
		}else if("3".equals(fwzt)){
			
			if("1".endsWith(tgztCode)){
				Fwzt zt=new Fwzt();
 				zt.setCode("4");
 				fwng.setFwzt(zt);
				String names=fwng.getUsername();
				User fgld=yhgl_service.getOneFgld(userService.getUserByUsername(ngr).getId());
				fwng.setUsername(names+fgld.getUsername()+"#");
				String name = "#"+fgld.getUsername()+"#";
 				wddbService.saveNewWddb(fwng.getId(), "90", fwng.getBt(), name, "4");
			}else if("2".endsWith(tgztCode)){
				Fwzt zt=new Fwzt();
 				zt.setCode("9");
 				fwng.setFwzt(zt);
			}
			User fgld=yhgl_service.getOneFgld(userService.getUserByUsername(ngr).getId());
			//当前用户为其他处室处长，下一个用户为分管领导
			fwngFlow.setCnode("其他部门审批");
			fwngFlow.setNuser(fgld.getUsername());
		}else if("4".equals(fwzt)){
			
			List<User> userList=yhgl_service.getUserByRoleCode("JYWYSZR");
			fwngFlow.setNuser(userList.get(0).getUsername());
			
			if("1".endsWith(tgztCode)){
				String names=fwng.getUsername();
				if(isBgs){
					fwng.setUsername(names+bgsfzr.getName()+"#");
					String name = "#"+bgsfzr.getName()+"#";
					wddbService.saveNewWddb(fwng.getId(), "90", fwng.getBt(), name, "5");
				}else{
					fwng.setUsername(names+userList.get(0).getUsername()+"#");
					String name = "#"+userList.get(0).getUsername()+"#";
					wddbService.saveNewWddb(fwng.getId(), "90", fwng.getBt(), name, "5");
				}
 				Fwzt zt=new Fwzt();
 				zt.setCode("5");
 				fwng.setFwzt(zt);
 				
			}else if("2".endsWith(tgztCode)){
				Fwzt zt=new Fwzt();
 				zt.setCode("9");
 				fwng.setFwzt(zt);
			}
		
			//当前用户为分管领导，下一个用户为发文管理员
			fwngFlow.setCnode("分管领导审批");
			
		}else if("5".equals(fwzt)){
			List<User> userList=yhgl_service.getUserByRoleCode("JYWYSZR");
			fwngFlow.setNuser(userList.get(0).getUsername());
			
			if("1".endsWith(tgztCode)){
				String names=fwng.getUsername();
				if(isBgs){
					fwng.setUsername(names+bgsfzr.getUsername()+"#");
					String name = "#"+bgsfzr.getUsername()+"#";
					wddbService.saveNewWddb(fwng.getId(), "90", fwng.getBt(), name, "6");
				}else{
					fwng.setUsername(names+userList.get(0).getUsername()+"#");
					String name = "#"+userList.get(0).getUsername()+"#";
					wddbService.saveNewWddb(fwng.getId(), "90", fwng.getBt(), name, "6");
				}
							
				Fwzt zt=new Fwzt();
 				zt.setCode("8");
 				fwng.setFwzt(zt);
				bhqr=StringSimple.nullToEmpty(bhqr);
				if(!"".equals(bhqr)){
					SF bh=new SF();
					bh.setCode(bhqr);
					fwng.setBhqr(bh);
					fwng.setDjbh(djbh);
				}
				
				
				
				
			}else if("2".endsWith(tgztCode)){
				Fwzt zt=new Fwzt();
 				zt.setCode("9");
 				fwng.setFwzt(zt);
			}
			
			//当前用户为发文管理员，下一个用户为发文管理员
			fwngFlow.setCnode("办公室审批");

		}else if("8".equals(fwzt)){
			
			Fwzt zt=new Fwzt();
			zt.setCode("10");
			fwng.setFwzt(zt);
 				
			//是否需要电子签章
			dzqz=StringSimple.nullToEmpty(dzqz);
			if(!"".equals(dzqz)){
				SF qz=new SF();
				qz.setCode(dzqz);
				fwng.setSfdzqz(qz);
			}
			//电子签章
			if(!"".equals(StringSimple.nullToEmpty(dzqzs))){
				String[] dzqzArray=dzqzs.split(",");
				List<FileInfo> dzqzList=new ArrayList<FileInfo>();
				for(String qz:dzqzArray){
					FileInfo fileInfo=fileInfoDao.findOne(qz);
					dzqzList.add(fileInfo);
				}
				fwng.setDzqz(dzqzList);
			}
						
			fwngFlow.setCnode("办公室审批");
		}
		
		fwngFlowService.getDao().save(fwngFlow);
		
		save(fwng);
		
	}
}


