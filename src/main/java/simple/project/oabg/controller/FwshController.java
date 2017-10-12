package simple.project.oabg.controller;

import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jeecgframework.poi.word.WordExportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import simple.base.utils.StringSimple;
import simple.project.oabg.entities.Fwng;
import simple.project.oabg.service.FwngFlowService;
import simple.project.oabg.service.FwngService;
import simple.system.simpleweb.module.system.file.model.FileInfo;
import simple.system.simpleweb.module.system.file.service.FileService;
import simple.system.simpleweb.module.user.model.Role;
import simple.system.simpleweb.module.user.model.User;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.controller.SimplewebBaseController;
import simple.system.simpleweb.platform.model.web.easyui.DataGrid;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;









/**
 * 发文审核controller
 * @author sxm
 * @created 2017年8月28日
 */
@Controller
@RequestMapping("/oabg/fwsh/")
public class FwshController extends SimplewebBaseController{
	
	@Autowired
	private FwngFlowService fwngFlowService;
	@Autowired
	private FileService fileService;
	@Autowired
	private UserService userService;
	@Autowired
	private FwngService fwngService;
	/**模块公共路径**/
	private final static String MODELPATH="/oabg/fwsh/";
	
	/**
	 * 返回主页面
	 * @author sxm
	 * @created 2017年8月28日
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(){
		User user=userService.getCurrentUser();
		List<Role> roles=user.getRoles();
		String roleCode="";
		for(Role role:roles){
			roleCode+=role.getRoleCode()+",";
		}
		roleCode.substring(0,roleCode.length()-1);
		ModelAndView model=new ModelAndView(MODELPATH+"index");
		model.addObject("roleCode", roleCode);
		return model;
	}
	
	/**
	 * 查看流程记录
	 * @author sxm
	 * @created 2017年9月5日
	 * @param id
	 * @return
	 */
	@RequestMapping("lcjlForm")
	public ModelAndView lcjlForm(String id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(MODELPATH+"lcjlForm");
		mav.addObject("fwshFlowList", fwngService.queryFlowById(id));
		return mav;
	}
	
	/**
	 * 返回form表单
	 * @author sxm
	 * @created 2017年8月29日
	 * @return
	 */
	@RequestMapping("form")
	public ModelAndView form(String id){
		List<FileInfo> fjs=null;
		Map<String,Object> fwsh_info = new HashMap<String, Object>();
		if(!"".equals(StringSimple.nullToEmpty(id))){
			Fwng fwng=fwngService.getDao().findOne(Long.valueOf(id));
			fjs=fwng.getFileInfo();
			fwsh_info=fwngService.getInfo(id);
		}
		ModelAndView model=new ModelAndView(MODELPATH+"form");
		model.addObject("fjs",fjs);
		model.addObject("fwsh_info", fwsh_info);
		return model;
	}
	
	/**
	 * 返回form页面
	 * @author sxm
	 * @created 2017年9月18日
	 * @param id
	 * @return
	 */
	@RequestMapping("form1")
	public ModelAndView form1(String id){
		List<FileInfo> fjs=null;
		Map<String,Object> fwsh_info = new HashMap<String, Object>();
		if(!"".equals(StringSimple.nullToEmpty(id))){
			Fwng fwng=fwngService.getDao().findOne(Long.valueOf(id));
			fjs=fwng.getFileInfo();
			fwsh_info=fwngService.getInfo(id);
		}
		ModelAndView model=new ModelAndView(MODELPATH+"form1");
		model.addObject("fjs",fjs);
		model.addObject("fwsh_info", fwsh_info);
		return model;
	}
	
	/**
	 * 跳转核稿
	 * @author sxm
	 * @created 2017年8月31日
	 * @return
	 */
	@RequestMapping("hg")
	public ModelAndView hg(){
		ModelAndView model=new ModelAndView(MODELPATH+"hg");
		User user=userService.getCurrentUser();
		model.addObject("puser",user.getRealname());
		return model;
	}
	
	/**
	 * 跳转核稿
	 * @author sxm
	 * @created 2017年8月31日
	 * @return
	 */
	@RequestMapping("cs")
	public ModelAndView cs(){
		ModelAndView model=new ModelAndView(MODELPATH+"cs");
		User user=userService.getCurrentUser();
		model.addObject("puser",user.getRealname());
		return model;
	}
	
	/**
	 * 跳转流转审批
	 * @author sxm
	 * @created 2017年8月31日
	 * @return
	 */
	@RequestMapping("lzsp")
	public ModelAndView lzsp(){
		ModelAndView model=new ModelAndView(MODELPATH+"lzsp");
		User user=userService.getCurrentUser();
		model.addObject("puser",user.getRealname());
		return model;
	}
	
	/**
	 * 跳转复核签发
	 * @author sxm
	 * @created 2017年8月31日
	 * @return
	 */
	@RequestMapping("fhqf")
	public ModelAndView fhqf(){
		ModelAndView model=new ModelAndView(MODELPATH+"fhqf");
		User user=userService.getCurrentUser();
		model.addObject("puser",user.getRealname());
		return model;
	}
	
	/**
	 * 跳转发文打印
	 * @author sxm
	 * @created 2017年8月31日
	 * @return
	 */
	@RequestMapping("fwdy")
	public ModelAndView fwdy(){
		ModelAndView model=new ModelAndView(MODELPATH+"fwdy");
		User user=userService.getCurrentUser();
		model.addObject("puser",user.getRealname());
		return model;
	}
	
	/**
	 * 跳转发文校对
	 * @author sxm
	 * @created 2017年8月31日
	 * @return
	 */
	@RequestMapping("fwjd")
	public ModelAndView fwjd(){
		ModelAndView model=new ModelAndView(MODELPATH+"fwjd");
		return model;
	}
	
	/**
	 * 跳转用印分发
	 * @author sxm
	 * @created 2017年8月31日
	 * @return
	 */
	@RequestMapping("yyff")
	public ModelAndView yyff(){
		ModelAndView model=new ModelAndView(MODELPATH+"yyff");
		User user=userService.getCurrentUser();
		model.addObject("puser",user.getRealname());
		return model;
	}
	
	/**
	 * 跳转发文定稿
	 * @author sxm
	 * @created 2017年8月31日
	 * @return
	 */
	@RequestMapping("fwdg")
	public ModelAndView fwdg(){
		ModelAndView model=new ModelAndView(MODELPATH+"fwdg");
		return model;
	}

	
	/**
	 * 发文打印
	 * @author sxm
	 * @created 2017年9月1日
	 * @param id
	 * @param flag
	 * @return
	 */
	@RequestMapping("print")
	public ModelAndView fwdg(String id){
		id=StringSimple.nullToEmpty(id);
		Fwng fwng=fwngService.getDao().findOne(Long.valueOf(id));
		List<FileInfo> fjss=fwng.getFileInfo();
		List<FileInfo> dzqzs=fwng.getDzqz();
		ModelAndView model=new ModelAndView();
		model=new ModelAndView(MODELPATH+"print1");
		model.addObject("fwng", fwng);
		model.addObject("fjs",fjss);
		model.addObject("dzqz", dzqzs);
		return model;
	}
	
	/**
	 * 返回grid列表
	 * @author sxm
	 * @created 2017年8月29日
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "grid")
	public DataGrid grid(@RequestParam Map<String,Object> map){
		DataGrid dg = fwngService.grid1(map);
		return dg;
	}
	
	/**
	 * 导出pdf文档
	 * @author sxm
	 * @created 2017年9月25日
	 * @param id
	 * @param response
	 */
	@RequestMapping("exportPdf")
	public void exportPdf(String id,HttpServletResponse response){
		Fwng fwng=fwngService.getDao().findOne(Long.valueOf(id));
		// 1.新建document对象
		Document document = new Document();
         // 2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
         // 创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
        try {
			PdfWriter.getInstance(document,response.getOutputStream());
			String filename=fwng.getBt()+getTime();
			response.setHeader("Content-disposition", "attachment; filename="+new String(filename.getBytes("gb2312"),"ISO8859-1")+".pdf"); 
			
			// 3.打开文档
			document.open();
			
			//中文字体,解决中文不能显示问题
			BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
			
			Font blackFont = new Font(bfChinese);
			
			blackFont.setColor(BaseColor.BLACK);
			//创建章节
	         Paragraph chapterTitle = new Paragraph("标题："+" "+fwng.getBt(),blackFont);
	         Chapter chapter1 = new Chapter(chapterTitle, 1);
	         chapter1.setNumberDepth(0);
	        
	         Paragraph sectionTitle = new Paragraph("正文",blackFont);
	         Section section1 = chapter1.addSection(sectionTitle);
	         
	         String zw=fwng.getZw();
//	         zw=zw.replace("<br />", "");
	         zw=zw.replace("&nbsp;", " ");
	         zw=zw.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");      
	         zw = zw.replaceAll("[(/>)<]", ""); 
	         Paragraph sectionContent = new Paragraph(zw,blackFont);
	         section1.add(sectionContent);
	         
	         //将章节添加到文章中
	         document.add(chapter1);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			 // 5.关闭文档
		       document.close();
		}
       
        
	}
	
	/**
	 * 导出word
	 * @author sxm
	 * @created 2017年9月26日
	 * @param id
	 * @param response
	 */
	@RequestMapping("exportWord")
	public void exportWord(String id,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		Fwng fwng=fwngService.getDao().findOne(Long.valueOf(id));
		map.put("bt", fwng.getBt());
		map.put("djbh", fwng.getDjbh());
		String zw=fwng.getZw();
		zw=zw.replace("<br />", "\r\n");
		zw=zw.replace("&nbsp;", " ");
		zw=zw.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");      
        zw = zw.replaceAll("[(/>)<]", ""); 
		map.put("zw", zw);
        try {
        	//获取根路径
        	URL url = FwshController.class.getClassLoader().getResource("model/fwng/guidang.docx");
			String name = URLDecoder.decode(url.getFile(), "UTF-8");
			XWPFDocument doc = WordExportUtil.exportWord07(name, map);
            OutputStream out = response.getOutputStream();  
            String filename=fwng.getBt()+getTime();
			response.setHeader("Content-disposition", "attachment; filename="+new String(filename.getBytes("gb2312"),"ISO8859-1")+".docx");
            doc.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * 获取当前时间
	 * @author sxm
	 * @created 2017年9月26日
	 * @return
	 */
	public String getTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}
}
