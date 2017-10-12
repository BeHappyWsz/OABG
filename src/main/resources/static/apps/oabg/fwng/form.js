$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/fwng/save",
		
		check: ctx + "/oabg/fwng/check"
	};

	/** 模块名称* */
	var modelName = "fwng";

	/** 表单* */
	var form = $("#" + modelName + "_form");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=fwng_save]").bind("click", function() {
			save();
		});
		
//		$("a[data-action=fwng_close]").bind("click", function() {
//			$("#" + modelName + "_formWin").window("close");
//		});
	}
	
	/**
	 * 保存
	 */
	function save() {
		if (form.form("validate")) {
			/** 序列化form* */
			var formData = form.serializeObject({transcript:"overlay"});
			var zw=$("#oabg_fwng_zw").kindeditorbox("html");
			formData.zw=zw;
			$.messager.progress({text:'正在提交，请稍后......'}); 
			$.ajax({
				type : "post",
				url : URL.save,
				data :formData,
				success : function(data) {
					$.messager.progress("close");
					if (data.success) {
						showMsg(Msg.saveSuc);
						$("#" + modelName + "_formWin").window("close");
						$("#" + modelName + "_grid").datagrid("reload");
					}
				}
			});
		}
	}

});
