package simple.project.communal.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import simple.base.utils.StringSimple;
import simple.project.communal.util.Utils;
import simple.system.simpleweb.module.menu.dao.MenuDao;
import simple.system.simpleweb.module.menu.model.Menu;
import simple.system.simpleweb.module.menu.model.Menu.MenuType;
import simple.system.simpleweb.module.menu.service.MenuService;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.service.AbstractService;

/**
 * 自定义菜单service
 * 2017年7月18日
 * @author yc
 *
 */
@Service
public class DeMenuService extends AbstractService<MenuDao,Menu,Long>{

	@Autowired
	private UserService userService;
	@Autowired
	private MenuService menuService;
	@Autowired
	public DeMenuService(MenuDao dao) {
		super(dao);
	}
	
	/**
	 * 获取菜单
	 * 2017年7月18日
	 * yc
	 * @return
	 */
	public List<Map<String,Object>> menutree(String menucodes){
		List<Menu> menus = new ArrayList<Menu>();
		User user = userService.getCurrentUser();
		List<Role> roles = user.getRoles();
		for (Role role : roles) {
			for (Menu m : role.getMenus()) {
				if(m.getDeleted()){
					continue;
				}
				boolean has = false;
				for (Menu m2 : menus) {
					if(m2.getId() == m.getId()){
						has = true;
					}
				}
				if(!has){
					menus.add(m);
				}
			}
		}
		return setChildren(menus,menucodes);
	}
	
	/**
	 * 设置父子节点
	 * 2017年7月18日
	 * yc
	 * @param list
	 * @return
	 */
	public List<Map<String,Object>> setChildren(List<Menu> list,String menucodes){
		
		Collections.sort(list,new Comparator<Menu>(){
			@Override
			public int compare(Menu arg0, Menu arg1) {
				int ordernum1 = arg0.getOrderNum();
				int ordernum2 = arg1.getOrderNum();
				return ordernum1 > ordernum2 ? 1 : (ordernum1 == ordernum2 ? 0 : -1);
			}
		});
		Map<String,Map<String,Object>> objs = new HashMap<String,Map<String,Object>>();
		for (Menu menu : list) {
			if(!menu.isDisplay()){
				continue;
			}
			if(objs.containsKey(StringSimple.nullToEmpty(menu.getCode()))){
				continue;
			}
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("id", menu.getId());
			map.put("text", menu.getName());
			map.put("state", "open");
			map.put("code", menu.getCode());
			map.put("url", menu.getUrl());
			map.put("iconCls", null == menu.getIcon() ? "" : menu.getIcon().getClsName());
			map.put("pid",null ==  menu.getParentMenu() ? "" : menu.getParentMenu().getId());
			map.put("children", new ArrayList<Map<String,Object>>());
			objs.put(StringSimple.nullToEmpty(menu.getCode()), map);
		}
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for (Menu menu : list) {
			if(null == menu.getParentMenu()){
				if(!menu.isDisplay()){
					continue;
				}
				Map<String,Object> t = objs.get(StringSimple.nullToEmpty(menu.getCode()));
				if(null != t){
					result.add(t);
				}
			}else{
				if(!menu.getParentMenu().isDisplay()){
					continue;
				}
				String pcode = StringSimple.nullToEmpty(menu.getParentMenu().getCode());
				Map<String,Object> t = objs.get(pcode);
				if(null != t){
					@SuppressWarnings("unchecked")
					List<Map<String,Object>> childrenList = (List<Map<String, Object>>) t.get("children");
					childrenList.add(objs.get(StringSimple.nullToEmpty(menu.getCode())));
				}
			}
		}
		List<String> menucodesList = Utils.stringToStringList(menucodes, ",");
		if(menucodesList.size() != 0){
			List<Map<String,Object>> result2 = new ArrayList<Map<String,Object>>();
			for (String code : menucodesList) {
				Map<String,Object> t = objs.get(code);
				if(null != t ){
					t.put("pid", "");
					result2.add(t);
				}
			}
			return result2;
		}else
			return result;
	}

	/**
	 * 获取主页角色拥有的全部菜单
	 * 2017年9月25日
	 * yc
	 * @return
	 */
	public List<Map<String,Object>> getMainRoleMenus(){
		List<Menu> menus1 = new ArrayList<Menu>();
		List<Menu> menuList = menuService.getDao().findAll();
		for (Menu menu : menuList) {
			if(!menu.getDeleted() && menu.isDisplay() && MenuType.M2.equals(menu.getMenuType())){
				menus1.add(menu);
			}
		}
		
		Map<String,Map<String,Object>> resources1 = new HashMap<String,Map<String,Object>>();
		for (Menu menu : menus1) {
			if(!menu.getDeleted() && menu.isDisplay() && MenuType.M2.equals(menu.getMenuType())){
				if("OABG_GWGL".equals(menu.getCode())){
					if(resources1.containsKey("dzwj")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "dzwj");
					m.put("name", "党政文件");
					m.put("mycodes", "OABG_GWGL");
					m.put("ordernum", menu.getOrderNum());
					m.put("hasres", "0");
					resources1.put("dzwj", m);
				}else if("OABG_HYTZ".equals(menu.getCode())){
					if(resources1.containsKey("hytz")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "hytz");
					m.put("name", "会议通知");
					m.put("mycodes", "OABG_HYTZ");
					m.put("ordernum", menu.getOrderNum());
					m.put("hasres", "0");
					resources1.put("hytz", m);
				}else if("OABG_GZGL".equals(menu.getCode())){
					if(resources1.containsKey("gzgl")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "gzgl");
					m.put("name", "公章管理");
					m.put("mycodes", "OABG_GZGL");
					m.put("ordernum", menu.getOrderNum());
					m.put("hasres", "0");
					resources1.put("gzgl", m);
				}else if("OABG_HQGL".equals(menu.getCode())){
					if(resources1.containsKey("bgyp")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "bgyp");
					m.put("name", "办公用品");
					m.put("mycodes", "OABG_HQGL");
					m.put("ordernum", menu.getOrderNum());
					m.put("hasres", "0");
					resources1.put("bgyp", m);
				}else if("OABG_HQGL_ZCGL".equals(menu.getCode())){
					if(resources1.containsKey("s")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "zcgl");
					m.put("name", "资产管理");
					m.put("mycodes", "OABG_HQGL_ZCGL");
					m.put("ordernum", menu.getOrderNum());
					m.put("hasres", "0");
					resources1.put("zcgl", m);
				}else if("XXBS".equals(menu.getCode())){
					if(resources1.containsKey("xxbs")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "xxbs");
					m.put("name", "信息报送");
					m.put("mycodes", "XXBS");
					m.put("ordernum", menu.getOrderNum());
					m.put("hasres", "0");
					resources1.put("xxbs", m);
				}else if("OABG_KQGL".equals(menu.getCode())){
					if(resources1.containsKey("kqgl")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "kqgl");
					m.put("name", "考勤管理");
					m.put("mycodes", "OABG_KQGL");
					m.put("ordernum", menu.getOrderNum());
					m.put("hasres", "0");
					resources1.put("kqgl", m);
				}else if("OABG_HWPX".equals(menu.getCode())){
					if(resources1.containsKey("hwpx")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "hwpx");
					m.put("name", "会务培训");
					m.put("mycodes", "OABG_HWPX");
					m.put("ordernum", menu.getOrderNum());
					m.put("hasres", "0");
					resources1.put("hwpx", m);
				}else if("OABG_XFRD".equals(menu.getCode())){
					if(resources1.containsKey("xfrdta")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "xfrdta");
					m.put("name", "信访人大提案");
					m.put("mycodes", "OABG_XFRD");
					m.put("ordernum", menu.getOrderNum());
					m.put("hasres", "0");
					resources1.put("xfrdta", m);
				}else if("OABG_GZAP".equals(menu.getCode())){
					if(resources1.containsKey("gzap")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "gzap");
					m.put("name", "工作安排");
					m.put("mycodes", "OABG_GZAP");
					m.put("ordernum", menu.getOrderNum());
					m.put("hasres", "0");
					resources1.put("gzap", m);
				}else if("OABG_ZLZX".equals(menu.getCode())){
					if(resources1.containsKey("zlzx")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "zlzx");
					m.put("name", "资料中心");
					m.put("mycodes", "OABG_ZLZX");
					m.put("ordernum", menu.getOrderNum());
					m.put("hasres", "0");
					resources1.put("zlzx", m);
				}else if("OABG_RCGL".equals(menu.getCode())){
					if(resources1.containsKey("txl")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "txl");
					m.put("name", "通讯录");
					m.put("mycodes", "OABG_RCGL");
					m.put("ordernum", menu.getOrderNum());
					m.put("hasres", "0");
					resources1.put("txl", m);
				}else if("OABG_XTGL".equals(menu.getCode())){
					if(resources1.containsKey("xtgl")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "xtgl");
					m.put("name", "系统管理");
					m.put("mycodes", "OABG_XTGL");
					m.put("ordernum", menu.getOrderNum());
					m.put("hasres", "0");
					resources1.put("xtgl", m);
				}else if("OABG_GDPSBL".equals(menu.getCode())){
					if(resources1.containsKey("gdpsbl")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "gdpsbl");
					m.put("name", "工单派送");
					m.put("mycodes", "OABG_GDPSBL");
					m.put("ordernum", menu.getOrderNum());
					m.put("hasres", "0");
					resources1.put("gdpsbl", m);
				}
			}
		}
		
		List<Menu> menus = new ArrayList<Menu>();
		User user = userService.getCurrentUser();
		List<Role> roles = user.getRoles();
		for (Role role : roles) {
			menus.addAll(role.getMenus());
		}
		Map<String,Map<String,Object>> resources = new HashMap<String,Map<String,Object>>();
		for (Menu menu : menus) {
			if(!menu.getDeleted() && menu.isDisplay() && MenuType.M2.equals(menu.getMenuType())){
				if("OABG_GWGL".equals(menu.getCode())){
					if(resources.containsKey("dzwj")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "dzwj");
					m.put("name", "党政文件");
					m.put("mycodes", "OABG_GWGL");
					m.put("ordernum", menu.getOrderNum());
					resources.put("dzwj", m);
				}else if("OABG_HYTZ".equals(menu.getCode())){
					if(resources.containsKey("hytz")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "hytz");
					m.put("name", "会议通知");
					m.put("mycodes", "OABG_HYTZ");
					m.put("ordernum", menu.getOrderNum());
					resources.put("hytz", m);
				}else if("OABG_GZGL".equals(menu.getCode())){
					if(resources.containsKey("gzgl")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "gzgl");
					m.put("name", "公章管理");
					m.put("mycodes", "OABG_GZGL");
					m.put("ordernum", menu.getOrderNum());
					resources.put("gzgl", m);
				}else if("OABG_HQGL".equals(menu.getCode())){
					if(resources.containsKey("bgyp")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "bgyp");
					m.put("name", "办公用品");
					m.put("mycodes", "OABG_HQGL");
					m.put("ordernum", menu.getOrderNum());
					resources.put("bgyp", m);
				}else if("OABG_HQGL_ZCGL".equals(menu.getCode())){
					if(resources.containsKey("s")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "zcgl");
					m.put("name", "资产管理");
					m.put("mycodes", "OABG_HQGL_ZCGL");
					m.put("ordernum", menu.getOrderNum());
					resources.put("zcgl", m);
				}else if("XXBS".equals(menu.getCode())){
					if(resources.containsKey("xxbs")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "xxbs");
					m.put("name", "信息报送");
					m.put("mycodes", "XXBS");
					m.put("ordernum", menu.getOrderNum());
					resources.put("xxbs", m);
				}else if("OABG_KQGL".equals(menu.getCode())){
					if(resources.containsKey("kqgl")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "kqgl");
					m.put("name", "考勤管理");
					m.put("mycodes", "OABG_KQGL");
					m.put("ordernum", menu.getOrderNum());
					resources.put("kqgl", m);
				}else if("OABG_HWPX".equals(menu.getCode())){
					if(resources.containsKey("hwpx")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "hwpx");
					m.put("name", "会务培训");
					m.put("mycodes", "OABG_HWPX");
					m.put("ordernum", menu.getOrderNum());
					resources.put("hwpx", m);
				}else if("OABG_XFRD".equals(menu.getCode())){
					if(resources.containsKey("xfrdta")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "xfrdta");
					m.put("name", "信访人大提案");
					m.put("mycodes", "OABG_XFRD");
					m.put("ordernum", menu.getOrderNum());
					resources.put("xfrdta", m);
				}else if("OABG_GZAP".equals(menu.getCode())){
					if(resources.containsKey("gzap")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "gzap");
					m.put("name", "工作安排");
					m.put("mycodes", "OABG_GZAP");
					m.put("ordernum", menu.getOrderNum());
					resources.put("gzap", m);
				}else if("OABG_ZLZX".equals(menu.getCode())){
					if(resources.containsKey("zlzx")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "zlzx");
					m.put("name", "资料中心");
					m.put("mycodes", "OABG_ZLZX");
					m.put("ordernum", menu.getOrderNum());
					resources.put("zlzx", m);
				}else if("OABG_RCGL".equals(menu.getCode())){
					if(resources.containsKey("txl")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "txl");
					m.put("name", "通讯录");
					m.put("mycodes", "OABG_RCGL");
					m.put("ordernum", menu.getOrderNum());
					resources.put("txl", m);
				}else if("OABG_XTGL".equals(menu.getCode())){
					if(resources.containsKey("xtgl")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "xtgl");
					m.put("name", "系统管理");
					m.put("mycodes", "OABG_XTGL");
					m.put("ordernum", menu.getOrderNum());
					resources.put("xtgl", m);
				}else if("OABG_GDPSBL".equals(menu.getCode())){
					if(resources.containsKey("gdpsbl")){
						continue;
					}
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("key", "gdpsbl");
					m.put("name", "工单派送");
					m.put("mycodes", "OABG_GDPSBL");
					m.put("ordernum", menu.getOrderNum());
					resources.put("gdpsbl", m);
				}
			}
		}
		for (String key1 : resources1.keySet()) {
			if(resources.containsKey(key1)){
				resources1.get(key1).put("hasres", "1");
			}
		}
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		Set<String> keys = resources1.keySet();
		for (String key : keys) {
			res.add(resources1.get(key));
		}
		Collections.sort(res,new Comparator<Map<String,Object>>(){
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				int ordernum1 = Integer.parseInt(StringSimple.nullToEmpty(o1.get("ordernum")));
				int ordernum2 = Integer.parseInt(StringSimple.nullToEmpty(o2.get("ordernum")));
				return ordernum1 == ordernum2 ? 0 : (ordernum1 > ordernum2 ? 1 : -1);
			}
			
		});
		return res;
	}
}
