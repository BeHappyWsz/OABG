package simple.project.communal.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;

import simple.project.oabg.controller.Xxbscontroller;

/**
 * 模版导出
 * modelName:模版路径(主项目model下mode/)
 * fileName:生成文件名
 * data:导入数据
 * @author yefei       
 * @created 2016年10月28日 上午11:30:46
 */
public class ModelExport {
	public static void exportWithModel(String modelName,String fileName,Map<String,Object> data,HttpServletResponse response){
		SimpleDateFormat sfm=new SimpleDateFormat("yyyyMMddhhmmss");
		
		URL url = Xxbscontroller.class.getClassLoader().getResource(modelName);
		try {
			String name = URLDecoder.decode(url.getFile(), "UTF-8");
			TemplateExportParams tep=new TemplateExportParams(name);
			Workbook workbook = ExcelExportUtil.exportExcel(tep,data);
			// 取得输出流
			OutputStream os=null;
			try {
				os = response.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 清空输出流
			response.reset();
			// 设定输出文件头
			String filename=fileName+sfm.format(new Date());
			response.setHeader("Content-disposition", "attachment; filename="+new String(filename.getBytes("gb2312"),"ISO8859-1")+".xls");
			// 定义输出类型
			response.setContentType("application/msexcel");
			try {
				workbook.write(os);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 模板导出合并单元格
	 * 2017年9月23日
	 * yc
	 * @param modelName
	 * @param fileName
	 * @param data
	 * @param response
	 */
	public static void exportWithModelAndMergeCells(String modelName,String fileName,Map<String,Object> data,HttpServletResponse response,int sheetindex, int startRow, Integer... columns){
		SimpleDateFormat sfm=new SimpleDateFormat("yyyyMMddhhmmss");
		
		URL url = Xxbscontroller.class.getClassLoader().getResource(modelName);
		try {
			String name = URLDecoder.decode(url.getFile(), "UTF-8");
			TemplateExportParams tep=new TemplateExportParams(name);
			Workbook workbook = ExcelExportUtil.exportExcel(tep,data);
			PoiMergeCellUtil.mergeCells(workbook.getSheetAt(sheetindex), startRow,columns);
			// 取得输出流
			OutputStream os=null;
			try {
				os = response.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 清空输出流
			response.reset();
			// 设定输出文件头
			String filename=fileName+sfm.format(new Date());
			response.setHeader("Content-disposition", "attachment; filename="+new String(filename.getBytes("gb2312"),"ISO8859-1")+".xls");
			// 定义输出类型
			response.setContentType("application/msexcel");
			try {
				workbook.write(os);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	}
}
