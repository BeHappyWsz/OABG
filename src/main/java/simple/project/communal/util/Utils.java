package simple.project.communal.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simple.base.utils.ConvertSimple;
import simple.base.utils.StringSimple;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;

/**
 * 工具类
 * @author wm
 * @date 2016年12月14日
 */
public class Utils {
	
	/**
	 * 把15位身份证号转换成18位身份证号码 出生月份前加"19"(20世纪才使用的15位身份证号码),最后一位加校验码
	 * @param custNo
	 * @return
	 * @author wm
	 * @date 2017年3月20日
	 */
	public static String transformIdFrom15To18(String custNo) {
		String idCardNo = null;
		if (custNo != null && custNo.trim().length() == 15) {
			custNo = custNo.trim();
			StringBuffer newIdCard = new StringBuffer(custNo);
			newIdCard.insert(6, "19");
			newIdCard.append(trasformLastNo(newIdCard.toString()));
			idCardNo = newIdCard.toString();
		}
		;
		if (custNo != null && custNo.trim().length() == 18) {
			idCardNo = custNo;
		}
		;
		return idCardNo;
	}

	/**
	 * 生成身份证最后一位效验码
	 * @param id
	 * @return
	 * @author wm
	 * @date 2017年3月20日
	 */
	private static String trasformLastNo(String id) {
		char pszSrc[] = id.toCharArray();
		int iS = 0;
		int iW[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		char szVerCode[] = new char[] { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
		int i;
		for (i = 0; i < id.length(); i++) {
			iS += (pszSrc[i] - '0') * iW[i];
		}
		int iY = iS % 11;

		return String.valueOf(szVerCode[iY]);
	}

	/**
	 * 15位身份证转18位
	 * @param sfzh_15
	 * @return
	 * @author wm
	 * @date 2015-10-09
	 */
	public static String getSfz_18(String sfzh_15) {
		sfzh_15 = StringSimple.nullToEmpty((sfzh_15));
		if (sfzh_15.length() == 18) {
			return sfzh_15;
		}
		String sfzh_17 = sfzh_15.substring(0, 6) + "19" + sfzh_15.substring(6, 15);
		String sfzh_18 = "";
		try {
			sfzh_18 = getVerifyCode(sfzh_17);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sfzh_18;
	}

	/**
	 * 获取校验码
	 * @param idCardNumber
	 * @return
	 * @throws Exception
	 * @author wm
	 * @date 2017年3月20日
	 */
	private static String getVerifyCode(String idCardNumber) throws Exception {
		char[] Ai = idCardNumber.toCharArray();
		int[] Wi = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		char[] verifyCode = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
		int S = 0;
		int Y;
		for (int i = 0; i < Wi.length; i++) {
			S += (Ai[i] - '0') * Wi[i];
		}
		Y = S % 11;
		return idCardNumber + verifyCode[Y];
	}

	/**
	 * 身份证号18位转15位
	 * @param idCardNo18
	 * @return
	 * @author yc
	 * @date 2016年5月17日
	 */
	public static String from18to15(String idCardNo18) {
		if (!(isIdCardNo(idCardNo18) && idCardNo18.length() == 18))
			throw new IllegalArgumentException("身份证号参数格式不正确！");

		return idCardNo18.substring(0, 6) + idCardNo18.substring(8, 17);
	}

	/**
	 * 判断给定的字符串是不是符合身份证号的要求
	 * @param str
	 * @return
	 * @author wm
	 * @date 2017年3月20日
	 */
	public static boolean isIdCardNo(String str) {
		if (str == null)
			return false;

		int len = str.length();
		if (len != 15 && len != 18)
			return false;

		for (int i = 0; i < len; i++) {
			try {
				str.charAt(i);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 功能：将字符串转化成long类型list
	 * @param str 需要处理的字符串
	 * @param splitStr 分割字符 例如：, |
	 * @return list
	 */
	public static List<Long> stringToLongList(String str, String splitStr) {
		List<Long> list = new ArrayList<Long>();
		if (StringSimple.isNullOrEmpty(str)) {
			return list;
		}
		String[] array = str.split(splitStr);
		for (String id : array) {
			list.add(ConvertSimple.toLong(id));
		}
		return list;
	}
	
	/**
	 * 功能：将字符串转化成String类型list
	 * @param str 需要处理的字符串
	 * @param splitStr 分割字符 例如：, |
	 * @return list
	 */
	public static List<String> stringToStringList(String str, String splitStr) {
		List<String> list = new ArrayList<String>();
		if (StringSimple.isNullOrEmpty(str)) {
			return list;
		}
		String[] array = str.split(splitStr);
		for (String id : array) {
			list.add(id);
		}
		return list;
	}
	
	/**
	 * 根据身份证获取信息(出生日期,年龄,性别)
	 * @param idCard
	 * @return
	 */
	public static Map<String, Object> getInfoByIdCard(String idCard) {
		Map<String, Object> infoMap = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat resultSdf = new SimpleDateFormat("yyyy-MM-dd");
		String birth = "";
		String sexStr = "";
		int sex = 1;
		if (idCard.length() == 18) {
			birth = idCard.substring(6, 14);
			sexStr = idCard.substring(16, 17);
		} else if (idCard.length() == 15) {
			String shortBirth = idCard.substring(6, 12);
			birth = "19" + shortBirth;
			sexStr = idCard.substring(14, 15);
		}
		if (Integer.parseInt(sexStr) % 2 == 0) {
			sexStr = "女";
			sex = 2;
		} else {
			sexStr = "男";
			sex = 1;
		}
		try {
			Date birthDate = sdf.parse(birth);
			String resultDateStr = resultSdf.format(birthDate);
			int age = getAgeByBirthday(birthDate);
			infoMap.put("sex", sex);
			infoMap.put("age", age);
			infoMap.put("birthday", resultDateStr);
			infoMap.put("birthDate", birthDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return infoMap;
	}

	/**
	 * 根据出生日期计算周岁
	 * @param birthDate
	 * @return 周岁数
	 */
	public static int getAgeByBirthday(Date birthDate) {
		if (birthDate == null) {
			return -1;
		}
		int age = 0;
		Date now = new Date();
		SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
		SimpleDateFormat format_M = new SimpleDateFormat("MM");
		SimpleDateFormat format_D = new SimpleDateFormat("dd");
		String birth_year = format_y.format(birthDate);
		String this_year = format_y.format(now);
		String birth_month = format_M.format(birthDate);
		String this_month = format_M.format(now);
		String birth_day = format_D.format(birthDate);
		String this_day = format_D.format(now);
		// 初步，估算
		age = Integer.parseInt(this_year) - Integer.parseInt(birth_year);
		// 如果未到出生月份和日期，则age - 1
		if (!(this_month.compareTo(birth_month) > 0 || (this_month.equals(birth_month) && this_day.compareTo(birth_day) >= 0))) {
			age -= 1;
		}
		if (age < 0)
			age = 0;
		return age;
	}
	
	/**
	 * 执行发送
	 * @param signName 签名
	 * @param code 模板id
	 * @param message 消息参数
	 * @param tel 发送至
	 * @autho yc
	 * @date 2017年09月04日
	 */
	public static boolean dosend(String signName,String code,String tel,JSONObject message){
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23634144", "0f37bff9e6935ed086671382c96c54da");
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");
		req.setSmsFreeSignName(signName);
		req.setSmsParamString(message.toJSONString());
		req.setRecNum(tel);
		req.setSmsTemplateCode(code);
		try {
			client.execute(req);
			return true;
		} catch (ApiException e) {
		}
		return false;
		
	}
}
