package simple.project.oabg.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import simple.project.oabg.dao.GdpsblDao;
import simple.project.oabg.dao.GdpsblFlowDao;
import simple.project.oabg.dao.YhglDao;
import simple.project.oabg.dic.dao.BmdwDao;
import simple.project.oabg.dic.model.Gdpsblzt;
import simple.project.oabg.dic.model.Tgzt;
import simple.project.oabg.dto.GdpsblDto;
import simple.project.oabg.entities.Gdpsbl;
import simple.project.oabg.entities.GdpsblFlow;
import simple.project.oabg.entities.Txl;
import simple.system.simpleweb.module.system.file.dao.FileInfoDao;
import simple.system.simpleweb.module.system.file.model.FileInfo;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;
import simple.system.simpleweb.platform.util.StringKit;

/**
 * 工单派送办理之工单办理Service
 * @author wsz
 * @date 2017年9月27日
 */
@Service
public class GdblService extends AbstractService<GdpsblDao, Gdpsbl, Long>{

	@Autowired
	public GdblService(GdpsblDao arg0) {
		super(arg0);
	}

	@Autowired
	private UserService userService;
	@Autowired
	private GdpsblFlowDao gdpsblFlowDao;
	@Autowired
	private BmdwDao bmdwDao;
	@Autowired
	private FileInfoDao fileInfoDao;
	@Autowired
	private YhglDao yhglDao;
	@Autowired
	private Yhgl_service yhgl_service;
	@Autowired
	private GdpsblFlowService gdpsblFlowService;
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	/**
	 * 返回grid列表
	 * @author wsz
	 * @created 2017年9月27日
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
		map.put("blr", userService.getCurrentUser().getUsername());
		Page<Gdpsbl> page = queryForPage(GdpsblDto.class, map);
		return new CommonConvert<Gdpsbl>(page, new Cci<Gdpsbl>(){
			@Override
			public Map<String, Object> convertObj(Gdpsbl t) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", t.getId());
				map.put("gdpslx", t.getGdpslx() == null ? "" : t.getGdpslx().getCode());
				map.put("gdpslxName", t.getGdpslx() == null ? "" : t.getGdpslx().getName());
				map.put("sldw", t.getBlbmNames());
				map.put("pssj", t.getPssj() == null ? "" : sdf1.format(t.getPssj()));
				map.put("bt", t.getBt());
				map.put("files", "");
				map.put("gdpszt", t.getGdpszt() == null ? "" : t.getGdpszt().getCode());
				map.put("gdpsztName", t.getGdpszt() == null ? "" : t.getGdpszt().getName());
				map.put("blqxs", t.getBlqxs() == null ? "" : sdf1.format(t.getBlqxs()));
				map.put("blqxe", t.getBlqxe() == null ? "" : sdf1.format(t.getBlqxe()));
				List<FileInfo> list = t.getFileInfo();
				String files = "";
				for(int i=0; i < list.size(); i++){
					if(i == list.size()-1){
						files += list.get(i).getUrl()+"#"+list.get(i).getFileName();
					}else{
						files += list.get(i).getUrl()+"#"+list.get(i).getFileName()+"&";
					}
				}
				map.put("files", files);
				
				List<GdpsblFlow> gfs = gdpsblFlowDao.queryByGdpsId(t.getId().toString());
				String username = userService.getCurrentUser().getUsername();
				map.put("isBl", "0");
				for(GdpsblFlow gf : gfs){
					if(username.equals(gf.getPuser())){
						map.put("isBl", "1");//已办理
					}
				}
				return map;
			}
		}).getDataGrid();
	}

	/**
	 * 办理
	 * @author wsz
	 * @created 2017年9月27日
	 * @param id 工单派送id
	 * @param suggestion 办理意见
	 * @return
	 */
	public Result doBl(Long id, String suggestion) {
		Gdpsbl gdpsbl = getDao().findOne(id);
		int size = gdpsbl.getBlr().size();
		int blcs = gdpsbl.getBlcs();
		++blcs;
		User user = userService.getCurrentUser();
		Txl txl = yhgl_service.getDao().getTxlByUserId(user.getId());
		
		GdpsblFlow gf = new GdpsblFlow();
		gf.setGdps_id(gdpsbl.getId().toString());
		Tgzt tgzt = new Tgzt();//1通过2不通过
		tgzt.setCode("1");
		gf.setTgzt(tgzt);
		gf.setCnode("办理");
		gf.setPuser(user.getUsername());
		gf.setPuserName(txl.getBmdw().getName()+":"+user.getRealname());
		gf.setNuser("");
		gf.setSuggestion(suggestion);
		gf.setCzsj(new Date());
		
		gdpsbl.setBlcs(blcs);
		if(blcs >= size){
			Gdpsblzt zt = new Gdpsblzt();
			zt.setCode("2");//工单派送办理状态1待办理2已办理
			gdpsbl.setGdpszt(zt);
		}
		save(gdpsbl);
		gdpsblFlowService.save(gf);
		return new Result(true);
	}

	/**
	 * 判断当前办理日期是否超过办理截止日期
	 * 2017年9月27日
	 * wsz
	 * @param id 工单派送id
	 * @return
	 */
	public Map<String, Object> checkHfrq(Long id) {
		Gdpsbl gdpsbl = getDao().findOne(id);
		Map<String,Object> map = new HashMap<String,Object>();
		if(StringKit.isEmpty(gdpsbl)){
			map.put("isIn", "false");
			return map;
		}
		Date blqxe = gdpsbl.getBlqxe();
		if(blqxe.getTime() > new Date().getTime()){
			map.put("isIn", "1");
			return map;
		}
		map.put("isIn", "2");
		return map;
	}
	
}
