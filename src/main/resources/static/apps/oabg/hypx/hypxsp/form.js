$(function() {
	var URL = {
		/** 保存表单**/
		spFormWin : ctx + "/oabg/hypx/hypxsp/spForm"
	};

	/** 模块名称* */
	var modelName = "hypxsp";

	/** 表单* */
	var form = $("#" + modelName + "_form");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action='HYPXSP_FORMSPBTN']").bind("click", function() {
			openSpFormWin($("#" + modelName + "_form_id").val());
		});
	}
	
	/**跳转到新增/修改页面**/
	function openSpFormWin(id){
		var win = $("<div id='" + modelName + "_spFormWin'></div>").window({
			title : "公务接待[审批]",
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
