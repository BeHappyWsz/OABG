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
import simple.project.oabg.dao.WddyDao;
import simple.project.oabg.dic.model.Fwzt;
import simple.project.oabg.dic.model.SF;
import simple.project.oabg.dic.model.Wddysjly;
import simple.project.oabg.dic.model.Zt;
import simple.project.oabg.dto.WddyDto;
import simple.project.oabg.entities.Fwng;
import simple.project.oabg.entities.Hytz;
import simple.project.oabg.entities.Swng;
import simple.project.oabg.entities.Txl;
import simple.project.oabg.entities.Wddy;
import simple.system.simpleweb.module.system.file.model.FileInfo;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;
import simple.system.simpleweb.platform.service.AbstractService;

/**
 * 我的待阅service
 * @author sxm
 * @created 2017年9月4日
 */
@Service
public class WddyService extends AbstractService<WddyDao, Wddy, Long>{
	
	@Autowired
	private UserService userService;
	@Autowired
	private FwngService fwngService;
	@Autowired
	private Swgl_swng_service swng_service;
	@Autowired
	private Hytz_service hytz_service;
	@Autowired
	public WddyService(WddyDao dao) {
		super(dao);
	}
	
	
	public DataGrid grid(Map<String,Object> map){
		String userName=userService.getCurrentUser().getUsername();
		//过滤当前用户的信息
		map.put("username", userName);
		map.put("sjly","1");
		try {
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
		Page<Wddy> page = queryForPage(WddyDto.class, map);
		return new CommonConvert<Wddy>(page, new Cci<Wddy>(){
			@Override
			public Map<String, Object> convertObj(Wddy t) {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("id", t.getId());
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm");
				map.put("glid", t.getGlId());
				map.put("ydsj", t.getYdsj()==null?"":sdf.format(t.getYdsj()));
				map.put("state", t.getState().getCode());
//				map.put("fwr", t.getFwr());
				User fwr=userService.getUserByUsername(t.getFwr());
				map.put("fwrName", fwr==null?t.getFwr():fwr.getRealname());
				map.put("bt", t.getBt());
				map.put("fj", "");
				map.put("fwsj", t.getFqsj()==null?"":sdf.format(t.getFqsj()));
				map.put("sjly", t.getSjly().getCode());
				return map;
			}
		}).getDataGrid();
	}
	
	/**
	 * 获得详情
	 * @author sxm
	 * @created 2017年9月4日
	 * @param id
	 * @return
	 */
	public Map<String,Object> getInfo(String id){
		Wddy dy=getDao().findOne(Long.valueOf(id));
		String glid=dy.getGlId();
		Fwng fwng=fwngService.getDao().findOne(Long.valueOf(glid));
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id",dy.getId() );
		map.put("bt", fwng.getBt());
		map.put("zsdws", fwng.getZs());
		map.put("sfgk", fwng.getSfgk()==null?"":fwng.getSfgk().getCode());
		map.put("ngr", fwng.getNgr());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm");
		map.put("ngrq", sdf.format(fwng.getNgrq()));
		map.put("mj", fwng.getMj()==null?"":fwng.getMj());
		map.put("fs", fwng.getFs());
		map.put("zw", fwng.getZw());
		return map;
	}
	
	/**
	 * 获取附件
	 * @author sxm
	 * @created 2017年9月4日
	 * @param map
	 * @return
	 */
	public List<FileInfo> getFjs(String glid){
		Fwng fwng=fwngService.getDao().findOne(Long.valueOf(glid));
		return fwng.getFileInfo();
	} 
	
	public List<FileInfo> getDzqzs(String glid){
		Fwng fwng=fwngService.getDao().findOne(Long.valueOf(glid));
		return fwng.getDzqz();
	} 
	
	/**
	 * 更改阅读状态
	 * @author sxm
	 * @created 2017年9月4日
	 * @param id
	 */
	public void close(String id){
		Wddy dy=getDao().findOne(Long.valueOf(id));
		//状态为已阅读
		SF sfyd= new SF();
		sfyd.setCode("1");
		dy.setState(sfyd);
		Date date=new Date();
		dy.setYdsj(date);
		save(dy);
	}
	
	/**
	 * 保存我的待阅
	 * @author sxm
	 * @created 2017年9月8日
	 * @param sjly
	 * @param glid
	 */
	public void save(String sjly,String glid,String sjr){
		sjly=StringSimple.nullToEmpty(sjly);
		if(!"".equals(sjly)){
			//1.发文  2.传阅 3.会议通知
			if("1".equals(sjly)){
				Fwng fwng=fwngService.getDao().findOne(Long.valueOf(glid));
				List<Txl> zsList=fwng.getZsdw();
				List<Txl> csList=fwng.getCsdw();
				//主送对象
				if(!zsList.isEmpty()){
					for(Txl zs:zsList){
						Wddy wddy=new Wddy();
						wddy.setGlId(glid);
						SF sfyd= new SF();
						//默认为未阅状态
						sfyd.setCode("2");
						//数据来源
						wddy.setState(sfyd);
						Wddysjly ly=new Wddysjly();
						ly.setCode(sjly);
						wddy.setSjly(ly);
						//标题
						wddy.setBt(fwng.getBt());
						//发起时间
						wddy.setFqsj(fwng.getNgrq());
						wddy.setUserName(zs.getName());
						wddy.setFwr(fwng.getNgr());
						save(wddy);
					}
				}
				//抄送对象
				if(!csList.isEmpty()){
					for(Txl cs:csList){
						Wddy wddy=new Wddy();
						wddy.setGlId(glid);
						SF sfyd= new SF();
						//默认为未阅状态
						sfyd.setCode("2");
						//数据来源
						wddy.setState(sfyd);
						Wddysjly ly=new Wddysjly();
						ly.setCode(sjly);
						wddy.setSjly(ly);
						//标题
						wddy.setBt(fwng.getBt());
						//发起时间
						wddy.setFqsj(fwng.getNgrq());
						wddy.setUserName(cs.getName());
						wddy.setFwr(fwng.getNgr());
						save(wddy);
					}
				}
				
				Fwzt fwzt=new Fwzt();
				//设置发文状态为已分发
				fwzt.setCode("11");
				fwng.setFwzt(fwzt);
				
				Zt zt=new Zt();
				zt.setCode("1");
				fwng.setZt(zt);
				fwngService.save(fwng);

			}else if("2".equals(sjly)){
				sjr=StringSimple.nullToEmpty(sjr);
				if(!"".equals(sjr)){
					String[] sjrs =sjr.split(",");
					for(String sjrname:sjrs){
						Wddy wddy=new Wddy();
						wddy.setGlId(glid);
						wddy.setUserName(sjrname);
						SF sfyd= new SF();
						//默认为未阅状态
						sfyd.setCode("2");
						//数据来源
						wddy.setState(sfyd);
						Wddysjly ly=new Wddysjly();
						ly.setCode(sjly);
						wddy.setSjly(ly);
						
						//标题
						Swng swng = swng_service.getDao().findOne(Long.valueOf(glid));
						wddy.setBt(swng.getBt());
						wddy.setFwr(swng.getSwr());
						//发起时间
						wddy.setFqsj(swng.getCreateTime());
						save(wddy);
					}
				}
			}else if("3".equals(sjly)){
				sjr=StringSimple.nullToEmpty(sjr);
				if(!"".equals(sjr)){
					String[] sjrs =sjr.split(",");
					for(String sjrname:sjrs){
						Wddy wddy=new Wddy();
						wddy.setGlId(glid);
						wddy.setUserName(sjrname);
						SF sfyd= new SF();
						//默认为未阅状态
						sfyd.setCode("2");
						//数据来源
						wddy.setState(sfyd);
						Wddysjly ly=new Wddysjly();
						ly.setCode(sjly);
						wddy.setSjly(ly);
						//标题
						Hytz hyzt=hytz_service.getDao().findOne(Long.valueOf(glid));
						wddy.setBt(hyzt.getTitles());
						wddy.setFwr(hyzt.getFjrName());
						//发起时间
						wddy.setFqsj(hyzt.getCreateTime());
						save(wddy);
					}
				}
			}else if("4".equals(sjly)){
				sjr=StringSimple.nullToEmpty(sjr);
				if(!"".equals(sjr)){
					String[] sjrs =sjr.split(",");
					for(String sjrname:sjrs){
						Wddy wddy=new Wddy();
						wddy.setGlId(glid);
						wddy.setUserName(sjrname);
						SF sfyd= new SF();
						//默认为未阅状态
						sfyd.setCode("2");
						//数据来源
						wddy.setState(sfyd);
						Wddysjly ly=new Wddysjly();
						ly.setCode(sjly);
						wddy.setSjly(ly);
						//标题
//						Hytz hyzt=hytz_service.getDao().findOne(Long.valueOf(glid));
//						wddy.setBt(hyzt.getTitles());
//						wddy.setFwr(hyzt.getFjrName());
//						//发起时间
//						wddy.setFqsj(hyzt.getCreateTime());
						save(wddy);
					}
				}
			}
		}
		
	}

}
