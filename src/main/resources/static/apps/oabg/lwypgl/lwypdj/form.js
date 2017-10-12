$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/lwypdj/save"
	};

	/** 模块名称* */
	var modelName = "lwypdj";

	/** 表单* */
	var form = $("#" + modelName + "_form");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action='LWYPDJ_SAVEBTN']").bind("click", function() {
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
			$.ajax({
				type : "post",
				url : URL.save,
				data :formData,
				success : function(data) {
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
