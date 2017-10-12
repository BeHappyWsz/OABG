$(function() {
	var URL = {
		/** 回复**/
		doSave : ctx + "/oabg/tsswhf/doSave"
	};

	/** 模块名称* */
	var modelName = "tsswHf";

	/** 表单* */
	var form = $("#" + modelName + "_hfForm");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		closeLoadingDiv();
		$("a[data-action='tsswhf_dosave']").bind("click", function() {
			doSave();
		});
	}
	/**回复**/
	function doSave(){
		if (form.form("validate")) {
			var formData = form.serializeObject({transcript:"overlay"});
			$.messager.confirm("系统提示","是否确认回复？",function(r){
				if(r){
					$.messager.progress({text:'正在保存，请稍后......'});
					$.ajax({
						type : "post",
						url : URL.doSave,
						data :formData,
						success : function(data) {
							$.messager.progress("close");
							if (data.success) {
								showMsg("已回复");
								$("#" + modelName + "_hfFormWin").window("close");
								$("#" + modelName + "_formWin").window("close");
								$("#" + modelName + "_grid").datagrid("reload");
							}else{
								showMsg("异常");
								$("#" + modelName + "_hfFormWin").window("close");
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
