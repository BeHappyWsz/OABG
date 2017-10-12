$(function() {
	var URL = {
		/** 保存表单**/
		doSwcl : ctx + "/oabg/swng/doSwcl"
	};

	/** 模块名称* */
	var modelName = "swng";

	/** 表单* */
	var form = $("#" + modelName + "_swclForm");

	/** 绑定按钮事件* */
	bindButtonAction();
	closeLoadingDiv();
	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=swng_doswcl]").bind("click", function() {
			if (form.form("validate")) {
				var formData = form.serializeObject({transcript:"overlay"});
				var cl = $("#swng_swclForm_swcl").combo('getValue');
				var clText = $("#swng_swclForm_swcl").combo('getText');
				if(3 == cl || "3" == cl){//1传阅2分办3销毁
					$.messager.confirm('系统提示','即将销毁该条信息,是否确定?',function(r){
						if(r){
							doSwcl(formData);
						}
					});
				}else{
					$.messager.confirm('系统提示','即将'+clText+'该条信息,是否确定?',function(r){
						if(r){
							doSwcl(formData);
						}
					});
				}
			}
		});
	}
	
	/**
	 * 保存
	 */
	function doSwcl(formData) {
		$.ajax({
			type : "post",
			url : URL.doSwcl,
			data :formData,
			success : function(data) {
				$.messager.progress("close");
				if (data.success) {
					showMsg(Msg.saveSuc);
					$("#" + modelName + "_swclFormWin1").window("close");
					$("#" + modelName + "_swclFormWin2").window("close");
					$("#" + modelName + "_formWin").window("close");
					$("#" + modelName + "_grid").datagrid("reload");
				}
			}
		});
	}
	
	
	$("#" + modelName + "_swclForm_swcl").combobox({
		onChange : function(n,o){
			if(3 == n || "3" == n){//1传阅2分办3销毁
				$("#" + modelName + "_swclForm_td").html("收件人");
				$("#" + modelName + "_swclForm_username").textbox({
					disabled : true,
					required : false,
					value : ""
				});
				$("#swng_swclForm_sjr").val("");
				$("#" + modelName + "_swclForm_jcsBtn").hide();
				$("#" + modelName + "_swclForm_zsdwBtn").hide();
				$("#" + modelName + "_swclForm_xsqjBtn").hide();
				$("#" + modelName + "_swclForm_sjrBtn").hide();
				$("#" + modelName + "_swclForm_bmBtn").hide();
			}else{
				$("#" + modelName + "swclForm_td").html("收件人<span style='color:red'>*</span>");
				$("#" + modelName + "_swclForm_username").textbox({
					required:true,
					disabled:false
				});
				$("#" + modelName + "_swclForm_jcsBtn").show();
				$("#" + modelName + "_swclForm_zsdwBtn").show();
				$("#" + modelName + "_swclForm_xsqjBtn").show();
				$("#" + modelName + "_swclForm_sjrBtn").show();
				$("#" + modelName + "_swclForm_bmBtn").show();
			}
		}
	});
});
