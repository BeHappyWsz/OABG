package simple.project.communal.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import simple.base.utils.StringSimple;
import simple.project.oabg.dao.DeOrgDao;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.dto.YhglDto;
import simple.project.oabg.entities.Gzdj;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.service.Gzdj_service;
import simple.project.oabg.service.Yhgl_service;
import simple.system.simpleweb.module.system.dic.model.DicItem;
import simple.system.simpleweb.module.system.dic.service.DicItemService;
import simple.system.simpleweb.module.user.dto.RoleDto;
import simple.system.simpleweb.module.user.dto.UserDto;
import simple.system.simpleweb.module.user.model.Org;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.OrgService;
import simple.system.simpleweb.module.user.service.RoleService;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.util.StringKit;

import com.alibaba.fastjson.JSON;

/**
 * 数据源
 * @author wm
 * @date 2017年03月20日
 */
@Controller
public class DataSourceController {
	
	@Autowired
	DeOrgDao orgDao;
	@Autowired
	private Gzdj_service gzdj_service;
	@Autowired
	DicItemService dicItemService;
	@Autowired
	OrgService orgService;
	@Autowired
	UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private YhglDao yhglDao;
	@Autowired
	private Yhgl_service yhgl_service;
	/**
	 * 数据源查询
	 * @param diccode
	 * @return
	 * @author wm
	 * @date 2017年03月20日
	 */
	@ResponseBody
	@RequestMapping(value = "/ds/{diccode}")
	public List<Map<String, String>> dataSource(@PathVariable("diccode") String diccode) {
		List<DicItem> list = dicItemService.getItemByDicCode(diccode);
		List<Map<String, String>> listdic = new ArrayList<Map<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			DicItem dicItem = list.get(i);
			map.put("value", dicItem.getCode());
			map.put("text", dicItem.getName());
			listdic.add(map);
		}
		return listdic;
	}

	/**
	 * 获取所属地区树
	 * @param id
	 * @param type s：显示到市级 q：显示到区级 j：显示到街道级
	 * @param isAll 是否显示全部
	 * @return
	 * @author wm
	 * @date 2017年03月20日
	 */
	@ResponseBody
	@RequestMapping(value = "/ds/getOrgTree")
	public String getOrgTree(String id, String type, String isAll) {
		List<Map<String, Object>> relist = new ArrayList<Map<String, Object>>();
		List<Org> list;
		if ("".equals(StringSimple.nullToEmpty(id))) {
			String idx;
			if ("1".equals(isAll)) {
				idx = orgDao.queryByCode("3204").getOrgCode();
			} else {
				idx = userService.getCurrentUser().getOrg().getOrgCode();
			}
			list = orgService.getDao().getByCode(idx, true, false);
		} else {
			list = orgDao.queryByPid(orgDao.queryByCode(id).getId());
		}
		for (Org org : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", org.getOrgCode());
			map.put("pid", "");
			map.put("text", org.getOrgName());
			if (org.getChild().isEmpty()) {
				map.put("state", "open");
			} else {
				if (("j".equals(type) && org.getOrgCode().length() == 9) || ("q".equals(type) && org.getOrgCode().length() == 6) || ("s".equals(type) && org.getOrgCode().length() == 4)) {
					map.put("state", "open");
				} else {
					map.put("state", "closed");
				}
			}
			relist.add(map);
		}
		return JSON.toJSONString(relist);
	}
	
	/**
	 * 树形数据源
	 * @param code
	 * @return
	 * @author wm
	 * @date 2017年6月19日
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/ds/dsTree/{diccode}")
	public List<Map<String, Object>> dataSourceTree(@PathVariable("diccode") String code) {
		List<DicItem> list = dicItemService.getItemByDicCode(code);
		Map<Long,Map<String,Object>> tmap = new HashMap<Long,Map<String,Object>>();
		for (DicItem dicItem : list) {
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("id", dicItem.getCode());
			m.put("text", dicItem.getName());
			//0为根 1为子节点
			m.put("type", dicItem.getParentItem() == null?0:1);
			m.put("pid",null == dicItem.getParentItem() ? "" : StringSimple.nullToEmpty(dicItem.getParentItem().getCode()));
			m.put("children", new ArrayList<Map<String,Object>>());
			tmap.put(dicItem.getId(), m);
		}
		List<Map<String, Object>> listdic = new ArrayList<Map<String, Object>>();
		for (DicItem dicItem : list) {
			if(dicItem.getParentItem() == null){
				//根
				listdic.add(tmap.get(dicItem.getId()));
			}else{
				Long pid = dicItem.getParentItem().getId();
				if(pid == null)
					continue;
				List<Map<String,Object>> pChildren = (List<Map<String,Object>>)tmap.get(pid).get("children");
				pChildren.add(tmap.get(dicItem.getId()));
			}
		}
		return listdic;
	}
	
	/**
	 * 获取用户角色
	 * @author wsz
	 * @created 2017年8月21日  
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ds/getRoles")
	public String getRoles(){
		Map<String, Object> map = new HashMap<String,Object>();
//		map.put("roleClass", "用户角色");
		List<Role> roleList = roleService.query(RoleDto.class, map);
		List<Map<String, Object>> roles = new ArrayList<Map<String,Object>>();
		for(Role role : roleList){
			if("admin".equals(role.getRoleCode()))
				continue;
			Map<String, Object> tmap = new HashMap<String,Object>();
			tmap.put("value", role.getRoleCode());
			tmap.put("text",role.getRoleName());
			roles.add(tmap);
		}
		return JSON.toJSONString(roles);
	}
	
	/**
	 * 获取所有分管领导
	 * @author wsz
	 * @created 2017年9月6日  
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ds/getFgld")
	public String getFgld(){
		List<Map<String, Object>> lds = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("rolecode", "FGLD");
		List<Txl> query1 = yhgl_service.query(YhglDto.class, map);
		for(Txl t : query1){
			User user = t.getUser();
			if(StringKit.isEmpty(user)){ 
				continue;
			}
			Map<String, Object> tmap = new HashMap<String,Object>();
			tmap.put("value", user.getUsername());
			tmap.put("text",user.getRealname());
			lds.add(tmap);
		}
		return JSON.toJSONString(lds);
	}
	
	/**
	 * 获取所有分管领导
	 * @author wsz
	 * @created 2017年9月6日  
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ds/getGzsjname")
	public String getGz(){
		List<Map<String, Object>> Gz = new ArrayList<Map<String,Object>>();
		List<Gzdj> gzdjs = gzdj_service.getDao().queryAll();
		for(Gzdj gz : gzdjs){
			Map<String, Object> tmap = new HashMap<String,Object>();
			tmap.put("code", gz.getGzname());
			tmap.put("name",gz.getGzname());
			Gz.add(tmap);
		}
		return JSON.toJSONString(Gz);
	}
	
	/**
	 * 获取所有部门的处长
	 * @author wsz
	 * @created 2017年9月8日  
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ds/getCz")
	public String getCz(){
		List<Map<String, Object>> czs = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("rolecode", "CZ");
		List<User> query = userService.query(UserDto.class, map);
		for(User u : query){
			Txl txl = yhglDao.getTxlByUser(u.getId());
			if(StringKit.isEmpty(txl)){ //工作职位3处长 || !"3".equals(txl.getGzzw().getCode())
				continue;
			}
			Map<String, Object> tmap = new HashMap<String,Object>();
			tmap.put("value", u.getUsername());
			tmap.put("text",u.getRealname());
			czs.add(tmap);
		}
		return JSON.toJSONString(czs);
	}
	
	/**
	 * 获取所有的单位部门用户
	 * @author wsz
	 * @created 2017年9月22日  
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ds/getDwbm")
	public String getDwbm(){
		List<Map<String, Object>> czs = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("rolecode", "DWBM");
		map.put("pitemid", 35);//过滤非局处室人员
		List<Txl> query = yhgl_service.query(YhglDto.class, map);
		for(Txl t : query){
			User user = t.getUser();
			if(StringKit.isEmpty(user) || StringKit.isEmpty(t.getBmdw())){
				continue;
			}
			Map<String, Object> tmap = new HashMap<String,Object>();
			tmap.put("value", user.getUsername());
			tmap.put("text",t.getBmdw().getName());
			czs.add(tmap);
		}
		return JSON.toJSONString(czs);
	}
}
