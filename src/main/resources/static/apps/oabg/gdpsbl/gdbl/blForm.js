$(function() {
	var URL = {
		/** 办理**/
		doBl : ctx + "/oabg/gdpsbl/gdbl/doBl"
	};

	/** 模块名称* */
	var modelName = "gdbl";

	/** 表单* */
	var form = $("#" + modelName + "_blForm");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		closeLoadingDiv();
		$("a[data-action='gdbl_dobl']").bind("click", function() {
			doBl();
		});
	}
	/**办理**/
	function doBl(){
		if (form.form("validate")) {
			var formData = form.serializeObject({transcript:"overlay"});
			$.messager.confirm("系统提示","是否确认办理？",function(r){
				if(r){
					$.messager.progress({text:'正在保存，请稍后......'});
					$.ajax({
						type : "post",
						url : URL.doBl,
						data :formData,
						success : function(data) {
							$.messager.progress("close");
							if (data.success) {
								showMsg("已回复");
								$("#" + modelName + "_blFormWin").window("close");
								$("#" + modelName + "_formWin").window("close");
								$("#" + modelName + "_grid").datagrid("reload");
							}else{
								showMsg("异常");
								$("#" + modelName + "_blFormWin").window("close");
								$("#" + modelName + "_formWin").window("close");
								$("#" + modelName + "_grid").datagrid("reload");
							}
						}
					});
				}
			});
		}
	}
});
