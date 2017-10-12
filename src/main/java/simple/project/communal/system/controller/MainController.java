package simple.project.communal.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.base.utils.StringSimple;
import simple.project.communal.service.DeMenuService;
import simple.project.communal.util.Utils;
import simple.project.oabg.dao.GdpsblFlowDao;
import simple.project.oabg.dao.SwngFlowDao;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.dto.BgypSqjlDto;
import simple.project.oabg.dto.FwngDto;
import simple.project.oabg.dto.GdpsblDto;
import simple.project.oabg.dto.GwjdsqDto;
import simple.project.oabg.dto.GzsqDto;
import simple.project.oabg.dto.HypxsqDto;
import simple.project.oabg.dto.KqglDto;
import simple.project.oabg.dto.LwypSqjlDto;
import simple.project.oabg.dto.SbwxSqjlDto;
import simple.project.oabg.dto.SwngDto;
import simple.project.oabg.dto.WddyDto;
import simple.project.oabg.dto.WdjySqjlDto;
import simple.project.oabg.dto.XxbsDto;
import simple.project.oabg.dto.ZcglSqjlDto;
import simple.project.oabg.entities.BgypSqjl;
import simple.project.oabg.entities.Fwng;
import simple.project.oabg.entities.Gdpsbl;
import simple.project.oabg.entities.GdpsblFlow;
import simple.project.oabg.entities.Gwjdsq;
import simple.project.oabg.entities.Gzsq;
import simple.project.oabg.entities.Hypxsq;
import simple.project.oabg.entities.Kqgl;
import simple.project.oabg.entities.LwypSqjl;
import simple.project.oabg.entities.SbwxSqjl;
import simple.project.oabg.entities.Swng;
import simple.project.oabg.entities.SwngFlow;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.entities.Wddy;
import simple.project.oabg.entities.WdjySqjl;
import simple.project.oabg.entities.Xxbs;
import simple.project.oabg.entities.ZcglSqjl;
import simple.project.oabg.service.BgypSqjlService;
import simple.project.oabg.service.Ccsp_service;
import simple.project.oabg.service.Dagl_wdjySqjl_service;
import simple.project.oabg.service.FwngService;
import simple.project.oabg.service.GdblService;
import simple.project.oabg.service.GwjdsqService;
import simple.project.oabg.service.Gzsq_service;
import simple.project.oabg.service.HypxsqService;
import simple.project.oabg.service.LwypSqjlService;
import simple.project.oabg.service.Qjsp_service;
import simple.project.oabg.service.SbwxSqService;
import simple.project.oabg.service.Swgl_swng_service;
import simple.project.oabg.service.Swgl_tssw_service;
import simple.project.oabg.service.WddyService;
import simple.project.oabg.service.XxbsLyService;
import simple.project.oabg.service.Yhgl_service;
import simple.project.oabg.service.ZcglSpService;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.OnlineUserService;
import simple.system.simpleweb.module.user.service.UserLoginLogService;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.AbstractController;

@Controller("QYRKMainController")
public class MainController extends AbstractController{
    @Autowired
	private OnlineUserService onlineUserService;
    @Autowired
	private UserService userService;
    @Autowired
    private UserLoginLogService userLoginLogService;
    @Autowired
    private DeMenuService deMenuService;
    @Autowired
    private WddyService wddyService;
    @Autowired
    private FwngService fwngService;
    @Autowired
    private Gzsq_service gzsqService;
    @Autowired
    private Swgl_swng_service swngService;
    @Autowired
    private Swgl_tssw_service tsswService;
    @Autowired
    private BgypSqjlService bgypSqjlService;
    @Autowired
    private LwypSqjlService lwypSqjlService;
    @Autowired
    private SbwxSqService sbwxSqjlService;
    @Autowired
    private HypxsqService hypxsqService;
    @Autowired
    private GwjdsqService gwjdsqService;
    @Autowired
    private Yhgl_service yhgl_service;
    @Autowired
    private SwngFlowDao swngFlowDao;
    @Autowired
    private Dagl_wdjySqjl_service wdjySqjl_service;
    @Autowired
	private Qjsp_service qjsp_service;
    @Autowired
    private Ccsp_service ccsp_service;
    @Autowired
    private XxbsLyService  xxbxLyService;
    @Autowired
    private GdblService gdblService;
    @Autowired
	private GdpsblFlowDao gdpsblFlowDao;
    @Autowired
    private YhglDao yhglDao;
    @Autowired
    private ZcglSpService zcglSpService;
    
	@RequestMapping("/system.shtml")
	public ModelAndView index(HttpSession session,HttpServletRequest request){
		
//	   	String sessionid = session.getId();
//    	OnlineUser onlineUser  =  onlineUserService.getDao().getBySessioniId(sessionid);
//    	if(onlineUser != null && onlineUser.getUser()==null){
//    		//保存登录用户在线信息
//        	User tuser = userService.getCurrentUser();
//        	onlineUser.setUser(tuser);
//        	onlineUserService.save(onlineUser);
//        	//保存登录日志
//        	UserLoginLog userLoginLog = new UserLoginLog();
//        	userLoginLog.setSessionId(sessionid);
//        	userLoginLog.setIp(NetKit.getIp(request));
//        	userLoginLog.setUserId(tuser.getId());
//        	userLoginLog.setUsername(tuser.getUsername());
//        	userLoginLog.setDes("登入");
//        	userLoginLogService.save(userLoginLog);
//    	}
		ModelAndView model = new ModelAndView();
		model.setViewName("system/system");
		model.addObject("indexMainRoleMenus", deMenuService.getMainRoleMenus());
		
		return model;
	}
	
	/**
	 * 获取左部菜单树
	 * 2017年8月9日
	 * yc
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/system/menutree")
	public List<Map<String,Object>> menutree(String menucodes){
		return deMenuService.menutree(menucodes);
	}
	
	/**
	 * 获取未办数据
	 * @author sxm
	 * @created 2017年9月28日
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/system/getNum")
	public Map<String,Object> getNum(){
		Map<String,Object> hytz=new HashMap<String, Object>();
		User user=userService.getCurrentUser();
		Txl txl = yhglDao.getTxlByUser(user.getId());
		//String fqbms = null == txl ? "-1" : txl.getFgbms();//分管领导和局长包含的部门
		String bmdw="";
		if(txl==null){
			bmdw="-1";
		}else{
			bmdw=txl.getBmdw()==null ?"":txl.getBmdw().getCode();
		}
		
		
		List<Role> roles=user.getRoles();
		//党政文件
		Map<String,Object> dzwj=new HashMap<String, Object>(); 
		//公章申请
		Map<String,Object> gzsq=new HashMap<String, Object>();
		//办公用品
		Map<String,Object> bgyp=new HashMap<String, Object>();
		//会务培训
		Map<String,Object> hwpx=new HashMap<String, Object>();
		//信息报送
		Map<String,Object> xxbs=new HashMap<String, Object>();
		//考勤管理
		Map<String,Object> kqgl=new HashMap<String, Object>();
		//人大信访-特殊收文
		Map<String,Object> rdxf=new HashMap<String, Object>();
		//工单派送办理
		Map<String,Object> gdpsbl=new HashMap<String, Object>();
		//资产管理
		Map<String,Object> zcgl=new HashMap<String, Object>();
		//工作安排
		Map<String,Object> gzap=new HashMap<String, Object>();
		//资产销毁
		Map<String,Object> zcxh=new HashMap<String, Object>();
		
		
		List<String> fwztList=new ArrayList<String>();
		List<String> swztList=new ArrayList<String>();
		List<String> jyshList=new ArrayList<String>();
		List<String> gzsqList=new ArrayList<String>();
		List<String> bgypList=new ArrayList<String>();
		List<String> hwpxList=new ArrayList<String>();
		List<String> xxbsList=new ArrayList<String>();
		List<String> kqglList=new ArrayList<String>();
		List<String> zcglList=new ArrayList<String>();
		List<String> zcxhList=new ArrayList<String>();
		//资产销毁
		List<ZcglSqjl> xhList =new ArrayList<ZcglSqjl>();
		
		
		int total=0;
		int count = 0;
		
		boolean isCz = false;
		boolean isCw = false;
		String roleCode="";
		for(Role role:roles){
			roleCode+=role.getRoleCode()+",";
		}	
		
		roleCode=roleCode.substring(0, roleCode.length()-1);
		
		if(roleCode.contains("CZ") || roleCode.contains("DWBM")){
			if(roleCode.contains("BGSFZR")){
				fwztList.add("1");
				fwztList.add("2");
				fwztList.add("5");
				fwztList.add("8");
				swztList.add("1");
				hwpxList.add("2");
				bgypList.add("-1");
				gzsqList.add("-1");
				jyshList.add("-1");
				kqglList.add("-1");
				xxbsList.add("-1");
				zcgl.put("zcglsplc","7");
				zcxhList.add("-1");
				
				String username = userService.getCurrentUser().getUsername();
				rdxf.put("swlx", "2");//收文类型1正常收文2特殊收文
				rdxf.put("sjr", username);
				List<Swng> query = tsswService.query(SwngDto.class, rdxf);//该用户接收到的所有信息
				for(Swng tssw : query){
					//特殊收文流程记录
					List<SwngFlow> sfs = swngFlowDao.queryBySwngId(tssw.getId().toString());
					int hf = 0;
					for(SwngFlow sf : sfs){
						if(username.equals(sf.getPuser())){
							hf ++;
						}
					}
					if(hf == 0){//未回复
						total++;
					}
				}
				
				//工单派送办理
				gdpsbl.put("blr", username);
				List<Gdpsbl> query2 = gdblService.query(GdpsblDto.class, gdpsbl);
				
				for(Gdpsbl g : query2){
					List<GdpsblFlow> gfs = gdpsblFlowDao.queryByGdpsId(g.getId().toString());
					int bl = 0;
					for(GdpsblFlow gf : gfs){
						if(username.equals(gf.getPuser())){
							bl++;
						}
					}
					if(bl == 0){//未办理
						count++;
					}
				}
			}else if(roleCode.contains("GHCWC")){
				hwpxList.add("3");
				fwztList.add("-1");
				gzsqList.add("-1");
				bgypList.add("-1");
				swztList.add("-1");
				jyshList.add("-1");
				kqglList.add("-1");
				xxbsList.add("-1");
				zcgl.put("zcglsplc","-1");
				zcxhList.add("-1");
				isCw = true;
			}else{
				//人大信访
				String username = userService.getCurrentUser().getUsername();
				rdxf.put("swlx", "2");//收文类型1正常收文2特殊收文
				rdxf.put("sjr", username);
				List<Swng> query = tsswService.query(SwngDto.class, rdxf);//该用户接收到的所有信息
				for(Swng tssw : query){
					//特殊收文流程记录
					List<SwngFlow> sfs = swngFlowDao.queryBySwngId(tssw.getId().toString());
					int hf = 0;
					for(SwngFlow sf : sfs){
						if(username.equals(sf.getPuser())){
							hf ++;
						}
					}
					if(hf == 0){//未回复
						total++;
					}
				}
				//工单派送办理
				gdpsbl.put("blr", username);
				List<Gdpsbl> query2 = gdblService.query(GdpsblDto.class, gdpsbl);
				
				for(Gdpsbl g : query2){
					List<GdpsblFlow> gfs = gdpsblFlowDao.queryByGdpsId(g.getId().toString());
					int bl = 0;
					for(GdpsblFlow gf : gfs){
						if(username.equals(gf.getPuser())){
							bl++;
						}
					}
					if(bl == 0){//未办理
						count++;
					}
				}

				fwztList.add("1");
				kqglList.add("1");
				kqgl.put("czName", user.getUsername());
				fwztList.add("3");
				hwpxList.add("1");
				gzsqList.add("-1");
				bgypList.add("-1");
				swztList.add("-1");
				jyshList.add("-1");
				xxbsList.add("-1");
				zcgl.put("zcglsplc","3");
				zcgl.put("sqbm", bmdw);
				zcxhList.add("-1");
				isCz = true;
			}
		}else if(roleCode.contains("FGLD")){
			if(roleCode.contains("JZ")){
				kqglList.add("3");
				kqgl.put("jzName", user.getUsername());
				fwztList.add("4");
				gzsqList.add("-1");
				bgypList.add("-1");
				hwpxList.add("4");
				swztList.add("-1");
				jyshList.add("-1");
				xxbsList.add("-1");
				zcglList.add("6");
				zcgl.put("zcglsplcJz",zcglList);
				zcxhList.add("-1");
				//人大信访
				String username = userService.getCurrentUser().getUsername();
				rdxf.put("swlx", "2");//收文类型1正常收文2特殊收文
				rdxf.put("sjr", username);
				List<Swng> query = tsswService.query(SwngDto.class, rdxf);//该用户接收到的所有信息
				for(Swng tssw : query){
					//特殊收文流程记录
					List<SwngFlow> sfs = swngFlowDao.queryBySwngId(tssw.getId().toString());
					int hf = 0;
					for(SwngFlow sf : sfs){
						if(username.equals(sf.getPuser())){
							hf ++;
						}
					}
					if(hf == 0){//未回复
						total++;
					}
				}
				
			}else{
				fwztList.add("4");
				xxbsList.add("-1");
				zcxhList.add("-1");
				swztList.add("2");
				hwpxList.add("4");
				kqglList.add("2");
				kqgl.put("fgname", user.getUsername());
				Txl t = yhgl_service.getDao().getTxlByUserId(userService.getCurrentUser().getId());
				String fgbms = StringSimple.nullToEmpty(t.getFgbms());
				if(!"".equals(fgbms)){
					hwpx.put("sqrbmcodein", Utils.stringToStringList(fgbms, ","));
					zcgl.put("fqbmsList", Utils.stringToStringList(fgbms, ","));
				}
				gzsqList.add("-1");
				bgypList.add("-1");
				jyshList.add("-1");
				//收文拟稿
				List<SwngFlow> sfs = swngFlowDao.queryBySwngId(t.getId().toString());
				String username = userService.getCurrentUser().getUsername();
				int py = 0;
				for(SwngFlow sf : sfs){
					if(username.equals(sf.getPuser())){
						py ++;
					}
				}
				if(py == 0){
					dzwj.put("isPy", "0");
					dzwj.put("swlx", "1");
					dzwj.put("fgld", user.getUsername());
				}
				
				
				//人大信访
//				String username = userService.getCurrentUser().getUsername();
				rdxf.put("swlx", "2");//收文类型1正常收文2特殊收文
				rdxf.put("sjr", username);
				List<Swng> query = tsswService.query(SwngDto.class, rdxf);//该用户接收到的所有信息
				for(Swng tssw : query){
					//特殊收文流程记录
					List<SwngFlow> sfss = swngFlowDao.queryBySwngId(tssw.getId().toString());
					int hf = 0;
					for(SwngFlow sf : sfss){
						if(username.equals(sf.getPuser())){
							hf ++;
						}
					}
					if(hf == 0){//未回复
						total++;
					}
				}
				
				zcglList.add("4");
				zcgl.put("zcglsplcJz", zcglList);
				
			}	
			
		}else if(roleCode.contains("JYWYSZR")){
			fwztList.add("2");
			fwztList.add("5");
			fwztList.add("8");
			swztList.add("3");
			swztList.add("9");
			gzsqList.add("1");
			bgypList.add("1");
			hwpxList.add("-1");
			jyshList.add("1");
			kqglList.add("-1");
			xxbsList.add("-1");
			zcgl.put("zcglsplc","-1");
			zcxhList.add("-1");
		}else if(roleCode.contains("BGSZR")){
			kqglList.add("0");
			kqgl.put("czName", user.getUsername());
			fwztList.add("-1");
			gzsqList.add("-1");
			bgypList.add("-1");
			hwpxList.add("-1");
			swztList.add("-1");
			jyshList.add("-1");
			xxbsList.add("-1");
			zcgl.put("zcglsplc","5");
			//资产销毁
			zcxh.put("sqlx", "2");
			zcxh.put("sqlx", "5");
			xhList = zcglSpService.query(ZcglSqjlDto.class, zcxh);
		}else if(roleCode.contains("XXGLY")){
			xxbsList.add("1");
			fwztList.add("-1");
			gzsqList.add("-1");
			bgypList.add("-1");
			hwpxList.add("-1");
			swztList.add("-1");
			jyshList.add("-1");
			kqglList.add("-1");
			zcgl.put("zcglsplc","-1");
			zcxhList.add("-1");
		}else{
			fwztList.add("-1");
			gzsqList.add("-1");
			bgypList.add("-1");
			hwpxList.add("-1");
			swztList.add("-1");
			jyshList.add("-1");
			kqglList.add("-1");
			xxbsList.add("-1");
			zcgl.put("zcglsplc","-1");
			zcxhList.add("-1");
		}
		
		
		
		//党政文件
		dzwj.put("fwzt", fwztList);
		dzwj.put("username", user.getUsername());
		dzwj.put("swzts", swztList);
		
		dzwj.put("lczts", jyshList);
		List<Fwng> fwngList=fwngService.query(FwngDto.class, dzwj);
		List<Swng> swnglist = swngService.query(SwngDto.class, dzwj);
		List<WdjySqjl> wdjyList = wdjySqjl_service.query(WdjySqjlDto.class, dzwj);
		//会议通知
		hytz.put("username",user.getUsername());
		hytz.put("state", "2");
		hytz.put("sjly", "3");
		List<String> sjlyList=new ArrayList<String>();
		sjlyList.add("1");
		sjlyList.add("2");
		dzwj.put("sjly",sjlyList);
		dzwj.put("state", "2");
		gzap.put("username",user.getUsername());
		gzap.put("state", "2");
		gzap.put("sjly", "4");
		List<Wddy> hytzList=wddyService.query(WddyDto.class, hytz);
		List<Wddy> fwdyList=wddyService.query(WddyDto.class, dzwj);
		List<Wddy> gzapList=wddyService.query(WddyDto.class, gzap);
		//公章申请
		gzsq.put("lczts", gzsqList);
		List<Gzsq> gzsqlist=gzsqService.query(GzsqDto.class, gzsq);
		//办公用品
		bgyp.put("lczts",bgypList);
		bgyp.put("sbwxLczts", bgypList);
		List<BgypSqjl> bgList = bgypSqjlService.query(BgypSqjlDto.class, bgyp);
		List<LwypSqjl> lwList = lwypSqjlService.query(LwypSqjlDto.class, bgyp);
		List<SbwxSqjl> sbwxList = sbwxSqjlService.query(SbwxSqjlDto.class, bgyp);
		//会务培训
		hwpx.put("lczts",hwpxList);
		if(isCz && ! isCw){
			Txl t = yhgl_service.getDao().getTxlByUserId(userService.getCurrentUser().getId());
			hwpx.put("sqrbmcode", null == t ? "-1" : (null == t.getBmdw() ? "-1" : t.getBmdw().getCode()));
		}
		List<Hypxsq> hyList = hypxsqService.query(HypxsqDto.class, hwpx);
		List<Gwjdsq> GwList = gwjdsqService.query(GwjdsqDto.class, hwpx);
		//考勤管理
		kqgl.put("lczts", kqglList);
		kqgl.put("sfcc", "2");
		Map<String,Object> sfcc=new HashMap<String, Object>();
		sfcc.put("lczts", kqglList);
		sfcc.put("sfcc", "1");
		List<Kqgl> ccList = ccsp_service.query(KqglDto.class, sfcc);
		List<Kqgl> qjList = qjsp_service.query(KqglDto.class, kqgl);
		//信息报送
		xxbs.put("lczts", xxbsList);
		List<Xxbs> xxList = xxbxLyService.query(XxbsDto.class, xxbs);
		//资产管理
		zcgl.put("sqlx", "1");
		List<ZcglSqjl> zcList = zcglSpService.query(ZcglSqjlDto.class, zcgl);
		
		/***********************/
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("dzwjNum", fwngList.size()+swnglist.size()+wdjyList.size()+fwdyList.size());
		map.put("gzsqNum", gzsqlist.size());
		map.put("hytzNum",hytzList.size());
		map.put("bgypNum",bgList.size()+lwList.size()+sbwxList.size());
		map.put("hwpxNum",hyList.size()+GwList.size());
		map.put("kqglNum", qjList.size()+ccList.size());
		map.put("xxbsNum", xxList.size());
		map.put("xfrdNum", total);
		map.put("gdpsNum", count);
		map.put("zcglNum", zcList.size()+xhList.size());
		map.put("gzapNum", gzapList.size());
		return map;
	}
	
}
