package simple.project.oabg.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import simple.project.communal.util.Utils;
import simple.project.oabg.dic.model.Dxfszt;
import simple.project.oabg.entities.Dxrz;
import simple.system.simpleweb.module.user.service.UserService;
import simple.system.simpleweb.platform.model.web.Result;

import com.alibaba.fastjson.JSONObject;

/**
 * 短信发送service
 * 2017年9月4日
 * @author yc
 */
@Service
public class DxfsService{
	@Autowired
	private DxrzService dxrzService;
	@Autowired
	private UserService userService;
	
	/**
	 * 执行发送
	 * 2017年9月4日
	 * yc
	 * @param signName 签名
	 * @param code code
	 * @param jsr 接收人名称
	 * @param tel 接收人电话
	 * @param fsyy 发送原因
	 * @param message 消息参数
	 * @return
	 */
	public Result doFs(String signName,String code,String jsr,String tel,String fsyy,JSONObject message){
		//执行发放
		boolean success = Utils.dosend(signName, code, tel, message);
		//保存日志
		Dxrz rz = new Dxrz();
		rz.setFssj(new Date());
		rz.setJsr(jsr);
		rz.setTel(tel);
		rz.setNr(message.toJSONString());
		Dxfszt fszt = new Dxfszt();
		fszt.setCode(success ? "1" : "2");
		rz.setFszt(fszt);
		rz.setFsyy(fsyy);
		dxrzService.save(rz);
		return new Result(true);
	}
	
	public static void main(String[] args){
		JSONObject message = new JSONObject();
		message.put("meeting", "姚澄");
		Utils.dosend("新博科技", "SMS_77030033","15240533706", message);
	}
}
