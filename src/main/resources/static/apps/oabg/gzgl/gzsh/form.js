$(function() {
	
	var URL = {
		/** 打开审批意见页面**/
		sp : ctx + "/oabg/gzgl/gzsh/sp",
		spFormWin : ctx+"/oabg/gzgl/gzsh/spForm",
	};
	/** 模块名称* */
	var modelName = "gzsh";
	/** 绑定按钮事件* */
	bindButtonAction();
	var grid = $("#" + modelName + "_grid");
	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=gzsp-spp]").bind("click", function() {
			openSpFormWin($("#gzsh_form_id").val());
		});
	}
	
	/**审批**/
	function openSpFormWin(id){
		var win = $("<div id='" + modelName + "_spFormWin'></div>").window({
			title : "公章[审批]",
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
