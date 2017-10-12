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
import simple.project.oabg.dao.XxbsDao;
import simple.project.oabg.dao.XxbsSqFlowDao;
import simple.project.oabg.dic.model.SF;
import simple.project.oabg.dic.model.Tgzt;
import simple.project.oabg.dic.model.Xxbsly;
import simple.project.oabg.dto.XxbsDto;
import simple.project.oabg.entities.Xxbs;
import simple.project.oabg.entities.XxbsSqFlow;
import simple.system.simpleweb.module.system.dic.model.DicItem;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;
import simple.system.simpleweb.platform.util.StringKit;


	
/** 
 * @author  YZW: 
 * @date 创建时间：2017年9月19日 下午1:34:07 
 * @parameter  
 */
@Service
public class XxbsLyService extends AbstractService<XxbsDao, Xxbs, Long> {
	@Autowired
	private UserService userService;
	@Autowired
	private DeDicItemDao deDicItemDao;
	@Autowired
	private XxbsSqFlowDao xxbsSqFlowDao;
	@Autowired
	private XxbsService xxbsService;
	@Autowired
	public XxbsLyService(XxbsDao dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public DataGrid grid(Map<String , Object> map){
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
		try {
			if(!StringKit.isEmpty(map.get("bssjstart"))){
				String timeStr = map.get("bssjstart")+" 00:00:00";
				Date str = sdf.parse(timeStr); 
				map.put("bssjstart", str);
			}
			if(!StringKit.isEmpty(map.get("bssjend"))){
				String timeEnd = map.get("bssjend")+" 23:59:59";
				Date str = sdf.parse(timeEnd); 
				map.put("bssjend", str);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<String> lczts = new ArrayList<String>();
		lczts.add("1");
		lczts.add("2");
		map.put("lczts", lczts);
		Page<Xxbs> page = queryForPage(XxbsDto.class, map);
		return new CommonConvert<Xxbs>(page, new Cci<Xxbs>() {
			@Override
			public Map<String, Object> convertObj(Xxbs x) {
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("id", x.getId());
				map.put("bsbm", x.getBsbm()==null?"":x.getBsbm().getName());
				map.put("bssj",sdf.format(x.getBssj()));
				map.put("bsbt", x.getBsbt());
				map.put("score", x.getScore());
				map.put("lczt", null == x.getLczt() ? "" : x.getLczt().getCode());
				map.put("lcztName", null == x.getLczt() ? "" : x.getLczt().getName());
				return map;
			}
		}).getDataGrid();
	}
	/**
	 * 查找单个信息
	 * */
	public Map< String, Object> getInfo(Long id){
		Map<String, Object> map = new HashMap<String, Object>();
		Xxbs xxbs = getDao().findOne(id);
		map.put("id", xxbs.getId());
		map.put("bsbm", xxbs.getBsbm()==null?"":xxbs.getBsbm().getCode());
		map.put("bsbmName", xxbs.getBsbm()==null?"":xxbs.getBsbm().getName());
		map.put("bssj",sdf.format(new Date()));
		map.put("bsbt", xxbs.getBsbt());
		map.put("bsnr", xxbs.getBsnr());
		return map;
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
		List<XxbsSqFlow> list = xxbsSqFlowDao.queryByGlid(id);
		for (XxbsSqFlow t : list) {
			Map<String,Object> m = new HashMap<String, Object>();
			User user = userService.getUserByUsername(t.getPuser());
			m.put("pUserName",  null == user ? "" : user.getRealname());
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
	public Result doSp(Long id,String sftg,String suggestion){
		Xxbs t = getDao().findOne(id);
		Xxbsly lczt = new Xxbsly();
		lczt.setCode("1".equals(StringSimple.nullToEmpty(sftg)) ? "2" : "3");
		SF sf = new SF();
		sf.setCode(sftg);
		t.setSfly(sf);
		t.setLczt(lczt);
		if(!"".equals(StringSimple.nullToEmpty(t.getScore()))){
			if(Double.parseDouble(suggestion)+t.getScore()>100){
				return new Result(false,"累计分数不能超过100分");
			}
			t.setScore(Double.parseDouble(suggestion)+t.getScore());
		}else{
			t.setScore(Double.parseDouble(suggestion));
		}
		xxbsService.save(t);
		//保存流程记录
		XxbsSqFlow lcjl = new XxbsSqFlow();
		lcjl.setGlid(StringSimple.nullToEmpty(t.getId()));
		lcjl.setCnode("审批");
		Tgzt tgzt = new Tgzt();
		tgzt.setCode("1".equals(StringSimple.nullToEmpty(sftg)) ? "1" : "2");
		lcjl.setTgzt(tgzt);
		lcjl.setPuser(userService.getCurrentUser().getUsername());
		
		lcjl.setSuggestion(suggestion);
		lcjl.setClsj(new Date());
		xxbsSqFlowDao.save(lcjl);
		return new Result(true);
	}
	/**
	 * 执行打分
	 * 2017年9月4日
	 * yc
	 * @param id
	 * @param sftg
	 * @param suggestion
	 * @return
	 */
	public Result doDf(Long id,String score){
		Xxbs t = getDao().findOne(id);
		Double sco = 0.0;
		if(t.getScore()!=null){
			sco = t.getScore();
		}
		if(sco+Double.parseDouble(score)>100){
			return new Result(false,"累计分数不能超过100分");
		}
		t.setScore(sco+Double.parseDouble(score));
		xxbsService.save(t);
		//保存流程记录
		XxbsSqFlow lcjl = new XxbsSqFlow();
		lcjl.setGlid(StringSimple.nullToEmpty(t.getId()));
		lcjl.setCnode("打分");
		lcjl.setPuser(userService.getCurrentUser().getUsername());
		lcjl.setSuggestion(score);
		lcjl.setClsj(new Date());
		xxbsSqFlowDao.save(lcjl);
		return new Result(true);
	}
}
