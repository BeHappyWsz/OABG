$(function() {
	var URL = {
		/** 保存表单**/
		saveRk : ctx + "/oabg/zcdj/saveRk"
	};

	/** 模块名称* */
	var modelName = "zcdj";

	/** 表单* */
	var form = $("#" + modelName + "_rKform");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=ZCDJ_RK_SAVE]").bind("click", function() {
			saveRk();
		});
	}
	
	/**
	 * 保存
	 */
	function saveRk() {
		if (form.form("validate")) {
			/** 序列化form* */
			var formData = form.serializeObject({transcript:"overlay"});
			$.ajax({
				type : "post",
				url : URL.saveRk,
				data :formData,
				success : function(data) {
					if (data.success) {
						showMsg(Msg.saveSuc);
						$("#" + modelName + "_rKformWin").window("close");
						$("#" + modelName + "_grid").datagrid("reload");
					}
				}
			});
		}
	}
	
});
