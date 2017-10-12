$(function() {
	var URL = {
			/** 办理**/
			blFormWin : ctx + "/oabg/gdpsbl/gdbl/blForm"
	};

	/** 模块名称* */
	var modelName = "gdbl";

	/** 表单* */
	var form = $("#" + modelName + "_form");

	/** 绑定按钮事件* */
	bindButtonAction();
	closeLoadingDiv();
	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=gdbl_blform]").bind("click", function() {
			var gdpsid = $("#gdbl_form_id").val();
			blFormWin(gdpsid);
		});
	}
	
	/**
	 * 办理页面
	 */
	function blFormWin(id) {
		var win = $("<div id='" + modelName + "_blFormWin'></div>").window({
			title : "工单办理",
			href :URL.blFormWin,
			width : 500,
			height : 146,
			onLoad : function(){
				$("#" + modelName + "_blForm_id").val(id);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
});
