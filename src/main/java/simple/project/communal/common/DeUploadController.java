package simple.project.communal.common;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import simple.WebApplicationStarter;
import simple.system.simpleweb.module.system.file.model.FileInfo;
import simple.system.simpleweb.module.system.file.service.FileService;
import simple.system.simpleweb.module.system.file.service.store.FileStore;
import simple.system.simpleweb.platform.model.web.Result;
import simple.system.simpleweb.platform.util.FileKit;
import simple.system.simpleweb.platform.util.JsonKit;

import com.alibaba.fastjson.JSONObject;


/**
 * 文件上传Controller
 * 2017年9月5日
 * @author yc
 */
@Controller
@RequestMapping(value="/html/website/file")
public class DeUploadController{
	
	protected static Logger logger=LoggerFactory.getLogger(WebApplicationStarter.class);
	private static String fileDir = "kindeditor" ;
	private long maxSize = 2048000000;
	
	@Autowired
	FileService fileService;
	
	/**
	 * 文件上传
	 * 2017年9月5日
	 * yc
	 * @param dir
	 * @param file
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="/upload")
    public String addUser(String dir,@RequestParam("file") MultipartFile file,HttpServletRequest request){  
		FileStore fileStore = fileService.getFileStore();		
		// 判断文件是否为空  
        if (!file.isEmpty()) {
        	String dirName = dir;
        	String fileName = file.getOriginalFilename().substring( file.getOriginalFilename().lastIndexOf('\\')+1);
        	String savePath = FileKit.addFileSpilter(fileDir);
    		if (dirName == null) {
    			dirName = "image";
    		}
    		//创建文件夹
    		savePath += FileKit.addFileSpilter(dirName);
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    		String ymd = sdf.format(new Date());
    		savePath += FileKit.addFileSpilter(ymd);
        	//检查文件大小
			if(file.getSize() > maxSize){
				return JsonKit.toJsonString(getError("上传文件大小超过限制。"));
			}
            try {
            	FileInfo fileInfo = fileStore.saveFileByInputStream(file.getInputStream(), savePath + fileName);
            	fileInfo = fileService.save(fileInfo);
                JSONObject obj = new JSONObject();
                obj.put("url", fileInfo.getUrl());
                obj.put("id", fileInfo.getId());
        		return obj.toJSONString();
            } catch (Exception e) {  
            	logger.error("context", e);
            }  
        }
        return JsonKit.toJsonString(getError("上传文件失败。"));
    }
	
	/**
	 * 处理返回数据格式
	 * 2017年9月5日
	 * yc
	 * @param message
	 * @return
	 */
	private Result getError(String message) {
		Result result = new Result(false,message);
		return result;
	}
}
