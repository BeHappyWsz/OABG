$(function() {
	
	var URL = {
		/** 打开审批意见页面**/
			spFormWin : ctx + "/oabg/kqgl/ccspOne/ccspF",
	};
	/** 模块名称* */
	var modelName = "ccsp";
	/** 绑定按钮事件* */
	bindButtonAction();
	var grid = $("#" + modelName + "_grid");
	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=ccsp-spp]").bind("click", function() {
			openSpFormWin($("#" + modelName + "_id").val());
		});
	}
	
	/**跳转到新增/修改页面**/
	function openSpFormWin(id){
		var win = $("<div id='" + modelName + "_spFormWin'></div>").window({
			title : "出差[审批]",
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
