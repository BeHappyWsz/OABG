$(function() {
	var URL = {
		/** 保存表单**/
		spFormWin : ctx + "/oabg/bgypsp/spForm"
	};

	/** 模块名称* */
	var modelName = "bgypsp";

	/** 表单* */
	var form = $("#" + modelName + "_form");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=BGYPSP_DOSPBTN]").bind("click", function() {
			openSpFormWin($("#" + modelName + "_form_id").val());
		});
	}
	
	/**跳转到新增/修改页面**/
	function openSpFormWin(id){
		var win = $("<div id='" + modelName + "_spFormWin'></div>").window({
			title : "办公物品[审批]",
			href :URL.spFormWin,
			width : 500,
			height : 177,
			onLoad : function(){
				$("#" + modelName + "_spform_id").val(id);
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
});
