$(function() {
	
	var URL = {
		/** 保存表单**/
			DAFEN : ctx + "/oabg/xxbsly/dafen",
			XXBSLY :ctx+ "/oabg/xxbsly/xxbsly"
	};
	
	
	/** 模块名称* */
	var modelName = "xxbsly";
	/** 表单* */
	var form = $("#" + modelName + "_form");
	
	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action='XXBSSP_FORMSPBTN']").bind("click", function() {
			var id =$("#xxbsly_form_id").val();
			openWin(id);
		});
		$("a[data-action='XXBSSP_SCORE']").bind("click", function() {
			var id =$("#xxbsly_form_id").val();
			openDfWin(id);
		});
	}
	
	/**
	 * 保存
	 */
	function openWin(id){
		var win = $("<div id='" + modelName + "_lyFormWin'></div>").window({
			title : "审批",
			href :URL.XXBSLY,
			width :350,
			height : 139,
			onLoad : function(){
				$("#xxbsly_spform_id").val(id);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	function openDfWin(id){
		var win = $("<div id='" + modelName + "_dfFormWin'></div>").window({
			title : "打分",
			href :URL.DAFEN,
			width :340,
			height : 106,
			onLoad : function(){
				$("#df_spform_id").val(id);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
});
