/**一些复用的方法**/
/** 传入id(string或数组),赋值为yyyy-mm,报表my97date使用* */
function setCurrDate(_id) {
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	month <= 10 && (month = "0" + month);
	var _val = year + "-" + month;
	if (typeof _id == "string") {
		$("#" + _id).val(_val);
	} else if (!!_id.push) {
		for (var i = 0; i < _id.length; i++) {
			$("#" + _id[i]).val(_val);
		}
	}
}
/** 将2016-8或者2016-08这种类型的字符串转为yyy年mm月* */
function date2str(date) {
	var _arr = date.split("-");
	var month = _arr[1];
	if (month.length == 1) {
		month = "0" + month;
	}
	return _arr[0] + "年" + month + "月";
}
/** 渲染起始截止日期的查询条件* */
function renderQueryDate(ffnys, ffnye) {
	if (!ffnys && !ffnye) {
		var _curr = new Date();
		var _currStr = _curr.getFullYear() + "-" + (_curr.getMonth() + 1);
		return "从" + date2str(_currStr) + "起到" + date2str(_currStr) + "止";
	} else {
		var _ny = "";
		if (ffnys) {
			_ny += "从" + date2str(ffnys) + "起";
		}
		if (ffnye) {
			_ny += "到" + date2str(ffnye) + "止";
		}
		return _ny;
	}
}

/** 选择所属地区树节点，更新多选的树,显示该节点下的子节点 * */
function selectTreeSeriDxTree(node, sontree) {
	sontree.combotree("clear");
	sontree.combotree("reload", ctx + "/ds/getOrgTree?id=" + node.id);
}

/** 遍历对象的属性和值并将后者有前者没有的值附上 **/
function changeProp(obj1,obj2) {
	// 开始遍历
	for ( var p in obj2) { 
		// 方法
		if (typeof (obj2[p]) == " function ") {
			obj2[p]();
		} else { 
			// p 为属性名称，obj[p]为对应属性的值
			if(obj1[p] == null){
				obj1[p] = obj2[p];
			}
		}
	}
}

/** 变更是否有网站，自动切换网站输入框状态 **/
function checkSfywz(newValue,modelName){
	if(newValue == 1){
		$("#"+modelName+"_form_wz_span").html("*&nbsp;");
		$("#"+modelName+"_form_wz").textbox("enable");
		$("#"+modelName+"_form_wz").textbox({"required":true});
	}else{
		$("#"+modelName+"_form_wz_span").html("&nbsp;");
		$("#"+modelName+"_form_wz").textbox({"required":false});
		$("#"+modelName+"_form_wz").textbox("disable");
		$("#"+modelName+"_form_wz").textbox('setValue',"");
	}
}

/** 显示后台返回的提示信息 **/
function showMsg(msg){
	$.messager.show({
		msg : msg,
		title : "系统提示"
	});
}