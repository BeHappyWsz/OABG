package simple.project.oabg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import simple.base.utils.StringSimple;
import simple.project.communal.convert.Cci;
import simple.project.communal.convert.CommonConvert;
import simple.project.oabg.dao.DeOrgDao;
import simple.project.oabg.dao.DeRoleDao;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.dic.model.Bmdw;
import simple.project.oabg.dic.model.Gzzw;
import simple.project.oabg.dic.model.Xb;
import simple.project.oabg.dto.YhglDto;
import simple.project.oabg.entities.Txl;
import simple.system.simpleweb.module.user.dto.UserDto;
import simple.system.simpleweb.module.user.model.Org;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;
import simple.system.simpleweb.platform.util.StringKit;
/**
 * 用户管理{用户管理;新增时添加系统用户}
 * @author wsz
 * @date 2017年8月28日
 */
@Service
public class Yhgl_service extends AbstractService<YhglDao, Txl, Long>{

	@Autowired
	private YhglDao yhglDao;
	@Autowired
	private DeOrgDao deOrgDao;
	@Autowired
	private DeRoleDao roleDao;
	@Autowired
	private UserService userService;
	@Autowired
	public Yhgl_service(YhglDao dao) {
		super(dao);
	}

	/**
	 * 用户列表
	 * @param map
	 * @return
	 * @author wsz
	 * @created 2017年8月21日
	 */
	public DataGrid grid(Map<String,Object> map){
		//只能显示该列不为空的行信息
		map.put("username_", " ");
		Page<Txl> page = queryForPage(YhglDto.class, map);
		return new CommonConvert<Txl>(page, new Cci<Txl>() {
			@Override
			public Map<String, Object> convertObj(Txl t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id",t.getId());
				if("".equals(StringSimple.nullToEmpty(t.getUser()))){//用户为空,不存在可登陆账户
					map.put("isUser", "no");
					map.put("realname", t.getRealname());
				}else{//存在可登录账户
					map.put("isUser", "yes");
					map.put("username", t.getUser().getUsername());
					map.put("realname", t.getUser().getRealname());
					//多角色
					String rolename = "";
					List<Role> roles=t.getUser().getRoles();
					for(int i = 0; i < roles.size(); i++){
						Role role = roles.get(i);
						if(i == roles.size()-1){
							rolename += role.getRoleName();
						}else{
							rolename += role.getRoleName() + ",";
						}
					}
					map.put("rolename", rolename);
				}
				Xb xb = t.getXb();
				if(!StringKit.isEmpty(xb)){
					map.put("xbName", xb.getName());
					map.put("xbCode", xb.getCode());
				}
				Bmdw bmdw = t.getBmdw();
				if(!StringKit.isEmpty(bmdw)){
					map.put("bmdwCode", bmdw.getCode());
					map.put("bmdwName", bmdw.getName());
				}
				Gzzw gzzw = t.getGzzw();
				if(!StringKit.isEmpty(gzzw)){
					map.put("gzzwCode",gzzw.getCode());
					map.put("gzzwName",gzzw.getName());
				}
				return map;
			}
		}).getDataGrid();
	}

	
	/**
	 * 新增用户
	 * @return
	 * @author wsz
	 * @created 2017年8月22日
	 */
	public Result createUser(Map<String, Object> map) {
		String rolecode=StringSimple.nullToEmpty(map.get("role"));
		String username = StringSimple.nullToEmpty(map.get("username"));
		String realname = StringSimple.nullToEmpty(map.get("realname"));
		String password = StringSimple.nullToEmpty(map.get("password"));
		String  bmdw = StringSimple.nullToEmpty(map.get("bmdw"));
		String zw = StringSimple.nullToEmpty(map.get("gzzw"));
		String fgbm = StringSimple.nullToEmpty(map.get("fgbm"));
		
		//先判断新增用户角色是否已存在
		if(rolecode.contains("JZ")){
			Map<String,Object> tmap = new HashMap<String,Object>();
			tmap.put("rolecode", "JZ");
			List<User> list = userService.query(UserDto.class, tmap);
			if(!list.isEmpty()){
				return new Result(false,"已创建局长角色账号");
			}
		}else if(rolecode.contains("FGLD")){
			if(!StringKit.isEmpty(fgbm)){
				String[] bm = fgbm.split(",");
				boolean isBfg = false;
				for(int i =0;i <bm.length;i++){
					User user = getFgldByCode(bm[i]);
					if(!StringKit.isEmpty(user.getId())){//该部门已被分管
						isBfg = true;
						break;
					}
				}
				if(isBfg)
					return new Result(false,"所选单位已被分管");
			}
		}else if(rolecode.contains("BGSFZR")){
			Map<String,Object> tmap = new HashMap<String,Object>();
			tmap.put("rolecode", "BGSFZR");
			List<User> list = userService.query(UserDto.class, tmap);
			if(!list.isEmpty()){
				return new Result(false,"已创建办公室负责人角色账号");
			}
		}else if(rolecode.contains("BGSZR")){
			Map<String,Object> tmap = new HashMap<String,Object>();
			tmap.put("rolecode", "BGSZR");
			List<User> list = userService.query(UserDto.class, tmap);
			if(!list.isEmpty()){
				return new Result(false,"已创建办公室主任角色账号");
			}
		}else if(rolecode.contains("JYWYSZR")){
			Map<String,Object> tmap = new HashMap<String,Object>();
			tmap.put("rolecode", "JYWYSZR");
			List<User> list = userService.query(UserDto.class, tmap);
			if(!list.isEmpty()){
				return new Result(false,"已创建机要文印室主任角色账号");
			}
		}else if(rolecode.contains("CZ")){
			Map<String, Object> tmap=new HashMap<String,Object>();
			tmap.put("rolecode", "CZ");
			tmap.put("bmdw", bmdw);
			List<Txl> list = query(YhglDto.class, tmap);
			if(!list.isEmpty()){
				return new Result(false,"该部门已存在处长角色");
			}
		}else if(rolecode.contains("XXGLY")){
			Map<String,Object> tmap = new HashMap<String,Object>();
			tmap.put("rolecode", "XXGLY");
			List<User> list = userService.query(UserDto.class, tmap);
			if(!list.isEmpty()){
				return new Result(false,"已创建信息管理员角色账号");
			}
		}else if(rolecode.contains("GHCWC")){
			Map<String,Object> tmap = new HashMap<String,Object>();
			tmap.put("rolecode", "GHCWC");
			List<User> list = userService.query(UserDto.class, tmap);
			if(!list.isEmpty()){
				return new Result(false,"已创建规划财务处角色账号");
			}
		}
		
		//用户对象
		User user = new User();
		if(!StringKit.isEmpty(username)){
			user.setUsername(username);
		}
		if(!StringKit.isEmpty(realname)){
			user.setRealname(realname);
		}
		if(!StringKit.isEmpty(password)){
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); 
			user.setPassword(passwordEncoder.encode(password));
		}
		
		//项目中暂时只为单角色
		List<Role> roles=new ArrayList<Role>();
		String [] rs = rolecode.split(",");
		for(String r : rs){
			roles.add(roleDao.queryByCode(r));
		}
		user.setRoles(roles);
		//地区默认为常州
		Org org=deOrgDao.queryByCode("3204");
		user.setOrg(org);
		userService.save(user);
		//用户信息
		Txl txl = new Txl();
		txl.setRoleCode(rolecode);//单独保存用户角色Code------

		//部门单位
		if(!StringKit.isEmpty(bmdw)){
			Bmdw dw = new Bmdw();
			dw.setCode(bmdw);
			txl.setBmdw(dw);
		}
		//工作职位
		if(!StringKit.isEmpty(zw)){
			Gzzw gzzw = new Gzzw();
			gzzw.setCode(zw);
			txl.setGzzw(gzzw);
		}else{
			txl.setGzzw(null);
		}
		//局长+分管领导的分管部门
		if(!StringKit.isEmpty(fgbm)){
			txl.setFgbms(fgbm);
		}
		
		txl.setName(username);
		txl.setRealname(realname);
		txl.setUser(user);
		this.save(txl);
		return new Result(true,"新增成功");
	}

	/**
	 * 获取单个用户信息
	 * @param id
	 * @return
	 * @author wsz
	 * @created 2017年8月22日
	 */
	public Map<String, Object> getInfo(Long id) {
		Map<String,Object> map = new HashMap<String,Object>();
		Txl txl = getDao().findOne(id);
		User user = txl.getUser();
		if("".equals(StringSimple.nullToEmpty(user))){//不存在登录账户
			return map;
		} 
		map.put("id", txl.getId());
		map.put("username", txl.getName());
		map.put("realname", user.getRealname());
		//多角色
		String rolename = "";
		String roleCode = "";
		List<Role> roles=user.getRoles();
		for(int i = 0; i < roles.size(); i++){
			Role role = roles.get(i);
			if(i == roles.size()-1){
				rolename += role.getRoleName();
				roleCode += role.getRoleCode();
			}else{
				rolename += role.getRoleName() + ",";
				roleCode += role.getRoleCode() + ",";
			}
		}
		map.put("rolename", rolename);
		map.put("roleCode", roleCode);
		
		map.put("xb", txl.getXb() == null ? "" : txl.getXb().getCode());
		map.put("bmdw", txl.getBmdw()== null ? "" : txl.getBmdw().getCode());
		map.put("bmdwName", txl.getBmdw()== null ? "" : txl.getBmdw().getName());
		map.put("gzzw", txl.getGzzw()== null ? "" : txl.getGzzw().getCode());
		map.put("gzzwName", txl.getGzzw()== null ? "" : txl.getGzzw().getName());
		//显示分管部门(工作部门)
		String fgbms = txl.getFgbms();
		if(!StringKit.isEmpty(fgbms)){
			map.put("fgbm", fgbms);
		}
		return map;
	}

	/**
	 * 更新用户
	 * @param map
	 * @author wsz
	 * @created 2017年8月22日
	 */
	public Result updataUser(Map<String, Object> map) {
		String id = StringSimple.nullToEmpty(map.get("id"));
		String realname = StringSimple.nullToEmpty(map.get("realname"));
		String pwd = StringSimple.nullToEmpty(map.get("password"));
		String rolecode = StringSimple.nullToEmpty(map.get("role"));
		String bmdw = StringSimple.nullToEmpty(map.get("bmdw"));
		String gzzw = StringSimple.nullToEmpty(map.get("gzzw"));
		String fgbm = StringSimple.nullToEmpty(map.get("fgbm"));
		Txl findOne = getDao().findOne(Long.parseLong(id));
		String roleCode2 = findOne.getRoleCode();
		//先判断新增用户角色是否已存在
		if(rolecode.contains("JZ") && (!roleCode2.contains("JZ"))){
			Map<String,Object> tmap = new HashMap<String,Object>();
			tmap.put("rolecode", "JZ");
			List<User> list = userService.query(UserDto.class, tmap);
			if(!list.isEmpty()){
				return new Result(false,"已创建局长角色账号");
			}
		}else if(rolecode.contains("FGLD") && (!roleCode2.contains("FGLD"))){
			if(!StringKit.isEmpty(fgbm)){
				String[] bm = fgbm.split(",");
				boolean isBfg = false;
				for(int i =0;i <bm.length;i++){
					User user = getFgldByCode(bm[i]);
					if(!StringKit.isEmpty(user)){//该部门已被分管
						isBfg = true;
						break;
					}
				}
				if(isBfg)
					return new Result(false,"所选单位已被分管");
			}
		}else if(rolecode.contains("BGSFZR") && (!roleCode2.contains("BGSFZR"))){
			Map<String,Object> tmap = new HashMap<String,Object>();
			tmap.put("rolecode", "BGSFZR");
			List<User> list = userService.query(UserDto.class, tmap);
			if(!list.isEmpty()){
				return new Result(false,"已创建办公室负责人角色账号");
			}
		}else if(rolecode.contains("BGSZR") && (!roleCode2.contains("BGSZR"))){
			Map<String,Object> tmap = new HashMap<String,Object>();
			tmap.put("rolecode", "BGSZR");
			List<User> list = userService.query(UserDto.class, tmap);
			if(!list.isEmpty()){
				return new Result(false,"已创建办公室主任角色账号");
			}
		}else if(rolecode.contains("JYWYSZR") && (!roleCode2.contains("JYWYSZR"))){
			Map<String,Object> tmap = new HashMap<String,Object>();
			tmap.put("rolecode", "JYWYSZR");
			List<User> list = userService.query(UserDto.class, tmap);
			if(!list.isEmpty()){
				return new Result(false,"已创建机要文印室主任角色账号");
			}
		}else if(rolecode.contains("CZ") && (!roleCode2.contains("CZ"))){
			Map<String, Object> tmap=new HashMap<String,Object>();
			tmap.put("rolecode", "CZ");
			tmap.put("bmdw", bmdw);
			List<Txl> list = query(YhglDto.class, tmap);
			if(!list.isEmpty()){
				return new Result(false,"该部门已存在处长角色");
			}
		}else if(rolecode.contains("XXGLY") && (!roleCode2.contains("XXGLY"))){
			Map<String,Object> tmap = new HashMap<String,Object>();
			tmap.put("rolecode", "XXGLY");
			List<User> list = userService.query(UserDto.class, tmap);
			if(!list.isEmpty()){
				return new Result(false,"已创建信息管理员角色账号");
			}
		}else if(rolecode.contains("GHCWC") && (!roleCode2.contains("GHCWC"))){
			Map<String,Object> tmap = new HashMap<String,Object>();
			tmap.put("rolecode", "GHCWC");
			List<User> list = userService.query(UserDto.class, tmap);
			if(!list.isEmpty()){
				return new Result(false,"已创建规划财务处角色账号");
			}
		}
		
		
		Txl txl = findOne;
		// 部门单位 工作职位
		Bmdw dw = new Bmdw();
		dw.setCode(bmdw);
		if(!StringKit.isEmpty(gzzw)){
			Gzzw zw = new Gzzw();
			zw.setCode(gzzw);
			txl.setGzzw(zw);
		}else{
			txl.setGzzw(null);
		}
		
		txl.setBmdw(dw);
		txl.setRealname(realname);
		txl.setRoleCode(rolecode);
		User user = txl.getUser();
		user.setRealname(realname);
		if(!"".equals(pwd)){
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); 
			user.setPassword(passwordEncoder.encode(pwd));
		}
		//多角色
		List<Role> roles=new ArrayList<Role>();
		String [] rs = rolecode.split(",");
		for(String r : rs){
			roles.add(roleDao.queryByCode(r));
		}
		//局长+分管领导的分管部门
		if(!"".equals(StringSimple.nullToEmpty(map.get("fgbm")))){
			txl.setFgbms(map.get("fgbm").toString());
		}else{
			txl.setFgbms("");
		}
		user.setRoles(roles);
		
		save(txl);
		userService.save(user);
		
		return new Result(true,"修改成功");
	}

	/**
	 * 删除用户
	 * @param id
	 * @author wsz
	 * @created 2017年8月23日
	 */
	public void deleteTxl(long id) {
		Txl ui = getDao().findOne(id);
		if(!"".equals(StringSimple.nullToEmpty(ui.getUser()))){
			userService.logicDeleteModel(ui.getUser());
		}
		this.logicDeleteModel(ui);
	}
	
	/**
	 * 获取办公室负责人
	 * @author wsz
	 * @created 2017年9月5日
	 */
	public User getBgsfzr(){
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("rolecode", "BGSFZR");
		List<User> list = userService.query(UserDto.class, map);
		if(list.isEmpty()){
			return new User();
		}
		return list.get(0) == null ? new User() : list.get(0);
	}
	
	/**
	 * 获取本部门的分管领导
	 * @author wsz
	 * @created 2017年9月5日
	 */
	public User getFgld(){
		Map<String, Object> map=new HashMap<String,Object>();
		Txl txl = getDao().getTxlByUser(userService.getCurrentUser().getId());
		String code = txl.getBmdw().getCode();
		map.put("fgbm", code);
		List<Txl> list = query(YhglDto.class, map);
		if(list.isEmpty()){
			return new User();
		}
		return list.get(0).getUser();
	}
	
	/**
	 * 获取本部门的分管领导
	 * @author wsz
	 * @created 2017年9月5日
	 */
	public User getFgldByCode(String code){
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("fgbm", code);
		List<Txl> list = query(YhglDto.class, map);
		if(list.isEmpty()){
			return new User();
		}
		return list.get(0).getUser();
	}
	
	/**
	 * 获取本部门的处长
	 * @author wsz
	 * @created 2017年9月6日
	 */
	public User getCz(){
		Map<String, Object> map=new HashMap<String,Object>();
		Txl txl = getDao().getTxlByUser(userService.getCurrentUser().getId());
		String code = txl.getBmdw().getCode();
		if("102".equals(code)){//如果本部门为办公室则查找办公室负责人
			map.put("rolecode", "BGSFZR");
		}else{
			map.put("rolecode", "CZ");
		}
		map.put("bmdw", code);//本部门
		List<Txl> list = query(YhglDto.class, map);
		return list.get(0).getUser() == null ? new User() : list.get(0).getUser();
	}
	
	
	/**
	 * 获取局长
	 * @author wsz
	 * @created 2017年9月6日
	 */
	public User getJz(){
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("rolecode", "JZ");
		List<User> list = userService.query(UserDto.class, map);
		if(list.isEmpty()){
			return new User();
		}
		return list.get(0) == null ? new User() : list.get(0);
	}
	
	/**
	 * 根据roldecode查找角色用户
	 * @author wsz
	 * @created 2017年9月8日
	 */
	public List<User> getUserByRoleCode(String roleCode){
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("rolecode", roleCode);
		List<User> list = userService.query(UserDto.class, map);
		return list;
	}
	
	/**
	 * 获取某一个分管领导
	 * @param id 某一用户id
	 * @author wsz
	 * @created 2017年9月14日
	 */
	public User getOneFgld(Long id){
		Map<String, Object> map=new HashMap<String,Object>();
		Txl txl = getDao().getTxlByUser(id);
		String code = txl.getBmdw().getCode();
		map.put("fgbm", code);
		List<Txl> list = query(YhglDto.class, map);
		if(list.isEmpty()){
			return new User();
		}
		return list.get(0).getUser() == null ? new User() : list.get(0).getUser();
	}
}
