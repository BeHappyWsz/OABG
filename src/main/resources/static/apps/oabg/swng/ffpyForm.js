$(function() {
	var URL = {
		doFfpy : ctx + "/oabg/swng/doFfpy"
	};

	/** 模块名称* */
	var modelName = "swng";

	/** 表单* */
	var form = $("#" + modelName + "_ffpyForm");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		$("a[data-action='swng_doffpy']").bind("click", function() {
			doFfpy();
		});
	}

	/**
	 * 执行分发批阅
	 */
	function doFfpy() {
		if (form.form("validate")) {
			var formData = form.serializeObject({transcript:"overlay"});
			$.messager.progress({text:'正在保存，请稍后......'}); 
			$.ajax({
				type : "post",
				url : URL.doFfpy,
				data :formData,
				success : function(data) {
					$.messager.progress("close");
					var index = $("#" + modelName + "_swclForm_index").val();
					if (data.success) {
						showMsg(Msg.saveSuc);
					}else{
						showMsg("出现错误");
					}
					$("#" + modelName + "_formWin").window("close");
					$("#" + modelName + "_ffpyFormWin").window("close");
					$("#" + modelName + "_grid").datagrid("reload");
				}
			});
		}
	}
});
