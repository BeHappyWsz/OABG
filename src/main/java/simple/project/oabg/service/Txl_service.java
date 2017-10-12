package simple.project.oabg.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import simple.base.utils.StringSimple;
import simple.project.communal.convert.Cci;
import simple.project.communal.convert.CommonConvert;
import simple.project.oabg.dao.DeOrgDao;
import simple.project.oabg.dao.DeRoleDao;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.dic.model.Bmdw;
import simple.project.oabg.dic.model.Bmzw;
import simple.project.oabg.dic.model.Xb;
import simple.project.oabg.dto.YhglDto;
import simple.project.oabg.entities.Txl;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;
import simple.system.simpleweb.platform.util.StringKit;
/**
 * 通讯录管理{全部通讯录管理,新增时不添加系统用户}
 * @author wsz
 * @date 2017年8月30日
 */
@Service
public class Txl_service extends AbstractService<YhglDao, Txl, Long>{

	@Autowired
	private DeOrgDao deOrgDao;
	@Autowired
	private DeRoleDao roleDao;
	@Autowired
	private UserService userService;
	@Autowired
	public Txl_service(YhglDao dao) {
		super(dao);
	}
	
	
	/**
	 * 通讯录列表
	 * @param map
	 * @return
	 * @author wsz
	 * @created 2017年8月30日
	 */
	public DataGrid grid(Map<String, Object> map) {
//		//部门单位查询,先判断是否为常州市民政局查询
//		String bmdw = StringSimple.nullToEmpty(map.get("bmdw"));
//		if(StringKit.isEmpty(bmdw)){ //部门单位查询为空时,默认查询民政局的数据信息
//			map.put("bmdw", "a");
//		}
		Page<Txl> page = queryForPage(YhglDto.class, map);
		return new CommonConvert<Txl>(page, new Cci<Txl>(){
			@Override
			public Map<String, Object> convertObj(Txl t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id",t.getId());
				if(!"".equals(StringSimple.nullToEmpty(t.getUser()))){//包含账户信息
					map.put("isUser", "yes");
				}else{//不包含用户信息
					map.put("isUser", "no");
				}
				map.put("realname", t.getRealname());
				Xb xb = t.getXb();
				if(!StringKit.isEmpty(xb)){
					map.put("xbName", xb.getName());
					map.put("xbCode", xb.getCode());
				}
				if(!StringKit.isEmpty(t.getBmdw())){
					map.put("bmdwCode", t.getBmdw().getCode());
					map.put("bmdwName", t.getBmdw().getName());
				}
				if(!StringKit.isEmpty(t.getBmzw())){
					map.put("bmzwCode", t.getBmzw().getCode());
					map.put("bmzwName", t.getBmzw().getName());
				}
				map.put("mobile", t.getMobile());
				map.put("bgdh", t.getBgdh());
				map.put("telphone", t.getTelphone());
				map.put("dz", t.getDz() == null ? "" : t.getDz());
				map.put("bz", t.getBz() == null ? "" : t.getBz());
				return map;
			}
		}).getDataGrid();
	}

	/**
	 * 通讯录新增信息
	 * @param map
	 * @return
	 * @author wsz
	 * @created 2017年8月30日
	 */
	public Result saveTxl(Map<String, Object> map) {
		String id = StringSimple.nullToEmpty(map.get("id"));
		String realname = StringSimple.nullToEmpty(map.get("realname"));
		String xbCode = StringSimple.nullToEmpty(map.get("xb"));
		String mobile = StringSimple.nullToEmpty(map.get("mobile"));
		String bgdh = StringSimple.nullToEmpty(map.get("bgdh"));
		String telphone = StringSimple.nullToEmpty(map.get("telphone"));
		String bmdwCode = StringSimple.nullToEmpty(map.get("bmdw"));
		String bmzwCode = StringSimple.nullToEmpty(map.get("bmzw"));
		String dz = StringSimple.nullToEmpty(map.get("dz"));
		String bz = StringSimple.nullToEmpty(map.get("bz"));
		if(!StringKit.isEmpty(id)){//更新
			Txl txl = getDao().findOne(Long.parseLong(id));
			txl.setRealname(realname);
			if(!"".equals(StringSimple.nullToEmpty(txl.getUser()))){
				User u =txl.getUser();
				u.setRealname(realname);
				userService.save(u);
				
				txl.setUser(u);
			}
			if(!StringKit.isEmpty(xbCode)){//性别可空
				Xb xb = new Xb();
				xb.setCode(xbCode);
				txl.setXb(xb);
			}
			txl.setMobile(mobile);
			txl.setBgdh(bgdh);
			txl.setTelphone(telphone);
			txl.setDz(dz);
			txl.setBz(bz);
			//部门职位设置
			if(!StringKit.isEmpty(bmdwCode)){
				Bmdw bmdw = new Bmdw();
				bmdw.setCode(bmdwCode);
				txl.setBmdw(bmdw);
			}
			//部门职位，可以修改为空
			if(!StringKit.isEmpty(bmzwCode)){
				Bmzw bmzw = new Bmzw();
				bmzw.setCode(bmzwCode);
				txl.setBmzw(bmzw);
			}else{
				txl.setBmzw(null);
			}
			this.save(txl);
			return new Result(true);
		}
		//新增
		if(StringKit.isEmpty(realname)){
			return new Result(false,"姓名不能为空");
		}
		Txl txl  = new Txl();
		txl.setRealname(realname);
		if(!StringKit.isEmpty(xbCode)){//性别可空
			Xb xb = new Xb();
			xb.setCode(xbCode);
			txl.setXb(xb);
		}
		txl.setMobile(mobile);
		txl.setBgdh(bgdh);
		txl.setTelphone(telphone);
		txl.setDz(dz);
		txl.setBz(bz);
		//部门职位设置
		if(!StringKit.isEmpty(bmdwCode)){
			Bmdw bmdw = new Bmdw();
			bmdw.setCode(bmdwCode);
			txl.setBmdw(bmdw);
		}
		if(!StringKit.isEmpty(bmzwCode)){
			Bmzw bmzw = new Bmzw();
			bmzw.setCode(bmzwCode);
			txl.setBmzw(bmzw);
		}
		this.save(txl);
		return new Result(true);
	}

	/**
	 * 双击通讯录，查询数据
	 * @param id
	 * @return
	 * @author wsz
	 * @created 2017年8月30日
	 */
	public Map<String, Object> getInfo(Long id) {
		Map<String,Object> map = new HashMap<String,Object>();
		Txl txl = getDao().findOne(id);
		map.put("id", txl.getId());
		map.put("realname", txl.getRealname());
		map.put("xb", txl.getXb() == null ? "" :txl.getXb().getCode());
		map.put("mobile", txl.getMobile() == null ? "" : txl.getMobile());
		map.put("bgdh", txl.getBgdh() == null ? "" :txl.getBgdh());
		map.put("telphone", txl.getTelphone() == null ? "" : txl.getTelphone() );
		map.put("bmzw", txl.getBmzw() == null ? "" : txl.getBmzw().getCode());
		map.put("bmdw", txl.getBmdw() == null ? "" : txl.getBmdw().getCode());
		map.put("dz", txl.getDz() == null ? "" : txl.getDz());
		map.put("bz", txl.getBz() == null ? "" : txl.getBz());
		return map;
	}


	
	/**
	 * 删除用户
	 * @param id
	 * @author wsz
	 * @created 2017年8月31日
	 */
	public void deleteTxl(long id) {
		Txl ui = getDao().findOne(id);
		if(!"".equals(StringSimple.nullToEmpty(ui.getUser()))){
			userService.logicDeleteModel(ui.getUser());
		}
		this.logicDeleteModel(ui);
	}
	
}
