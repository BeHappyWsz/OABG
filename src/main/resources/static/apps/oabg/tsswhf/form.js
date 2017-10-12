$(function() {
	var URL = {
			/** 回复**/
			hfFormWin : ctx + "/oabg/tsswhf/hfForm"
	};

	/** 模块名称* */
	var modelName = "tsswHf";

	/** 表单* */
	var form = $("#" + modelName + "_form");

	/** 绑定按钮事件* */
	bindButtonAction();
	closeLoadingDiv();
	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=tsswhf_saveForm]").bind("click", function() {
			var tsswid = $("#tsswHf_tsswid").val();
			hfFormWin(tsswid);
		});
	}
	
	/**
	 * 回复页面
	 */
	function hfFormWin(id) {
		var win = $("<div id='" + modelName + "_hfFormWin'></div>").window({
			title : "收文回复",
			href :URL.hfFormWin,
			width : 500,
			height : 146,
			onLoad : function(){
				$("#" + modelName + "_hfForm_id").val(id);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
});
