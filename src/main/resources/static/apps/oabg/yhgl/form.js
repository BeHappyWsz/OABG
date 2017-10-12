$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/yhgl/save",
		check: ctx + "/oabg/yhgl/check"
	};

	closeLoadingDiv();
	
	/** 模块名称* */
	var modelName = "yhgl";

	/** 表单* */
	var form = $("#" + modelName + "_form");

	/** 绑定按钮事件* */
	bindButtonAction();
	/**check username isExist**/
	$("#yhgl_username").textbox({
		onChange:function(n,o){
			var id = $("#yhgl_form_id").val();
			if(id != "")
				return false;
			if($(this).textbox("isValid")){
				check(n);
			}
		}
	});
	
	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=YHGL_SAVE]").bind("click", function() {
			save();
		});
	}
	
	/**
	 * 保存
	 */
	function save() {
		if (form.form("validate")) {
			/** 序列化form* */
			var formData = form.serializeObject({transcript:"overlay"});
			if(formData.fgbm == undefined || formData.fgbm == "" || formData.fgbm == null){
				formData.fgbm = "";
			}
			$.messager.progress({text:'正在保存，请稍后......'});
			$.ajax({
				type : "post",
				dataType : "json",
				url : URL.save,
				data :formData,
				success : function(data) {
					$.messager.progress("close");
					if (data.success) {
						showMsg(Msg.saveSuc);
						$("#" + modelName + "_formWin").window("close");
						$("#" + modelName + "_grid").datagrid("reload");
					}else{
						showMsg(data.returnMsg);
					}
				}
			});
		}
	}
	
	/**
	 * 查重
	 */
	function check(username){
		/** 序列化form* */
		var formData = {"username":username};
		$.ajax({
			type : "post",
			url : URL.check,
			data :formData,
			success : function(data) {
				if (data.success) {		
				}else{
					showMsg(data.returnMsg);
					setTimeout(function(){
						$("#yhgl_username").textbox('clear');
					},500)
					
				}
			}
		});
	} 
	
});
/**
 * 用户角色变更:包含局长/分管领导时,可以进行分管部门设置
 */
function roleChange(newValue, oldValue){
	var role = $("#yhgl_role").combo('getValues');
	var rs = (newValue+"").split(",");
	var len = rs.length;
	
	if(len == 2){
		if(role.indexOf("CZ") != -1 && role.indexOf("DWBM") != -1 ){
			//处长+单位部门
		}else if(role.indexOf("CZ") != -1 && role.indexOf("GHCWC") != -1){
			//处长+规划财务处
		}else if(role.indexOf("GWY") != -1 && role.indexOf("XXGLY") != -1){
			//公务员+信息管理员
		}else if(role.indexOf("JZ") != -1 && role.indexOf("FGLD") != -1){
			//局长+分管领导
		}else{
			showMsg("角色分配错误");
			$("#yhgl_role").combo("clear");
			return ;
		}
	}else if(len == 3){
		if(role.indexOf("CZ") != -1 && role.indexOf("DWBM") != -1 && role.indexOf("BGSFZR") != -1){
			//处长+单位部门+办公室负责人
		}else if(role.indexOf("CZ") != -1 && role.indexOf("DWBM") != -1 && role.indexOf("GHCWC") != -1){
			//处长+单位部门+规划财务处
		}else{
			showMsg("角色分配错误");
			$("#yhgl_role").combo("clear");
			return ;
		}
	
	}else if(len > 3){
		showMsg("所选角色过多");
		$("#yhgl_role").combo("clear");
		return ;
	}
	
	if(role.indexOf("FGLD") != -1 || role.indexOf("JZ") != -1){//分管领导下拉框
		$("#yhgl_fgbm").combo({
			disabled : false
		});

	}else if(role.indexOf("DWBM") != -1){ //单位部门,隐去部分输入框
		$("#yhgl_fgbm").combo({
			disabled : true,
			setValue : ""
		});
	}else{
		$("#yhgl_fgbm").combo({
			disabled : true,
			setValue : ""
		});
	}
	
}
/**
 * 局长/分管领导配置分管部门时,判断某部门是否已经被分管
 */
function fgbmCheck(node,checked){
	//系统中已被分管的部门
	var ok = true;
	var newValue = node.id;
	$.ajax({
		url : ctx + "/oabg/yhgl/getFgbms",
		type : "post",
		async:false,
		success : function(data){
			if(data.indexOf(newValue) != -1 ){
				showMsg("该部门已被分管");
				ok = false;
			}
		}
	});
	return ok;
}

function bmdwChange(newValue, oldValue){
//	var code = parseInt(newValue);
//	var role = $("#yhgl_role").combo('getValues');
//	if(100 < code && code < 200 && role != "DWBM"){
//		$("#yhgl_gzzw").combo({
//			required :true,
//			disabled : false
//		});
//	}else{
//		$("#yhgl_gzzw").combo({
//			required :false,
//			disabled : true
//		});
//	}
}
