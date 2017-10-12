$(function() {
	var URL = {
		/** 保存表单**/
		shFormWin : ctx + "/oabg/hypx/hypxsh/shForm"
	};

	/** 模块名称* */
	var modelName = "hypxsh";

	/** 表单* */
	var form = $("#" + modelName + "_form");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action='HYPXSH_FORMCZSHBTN']").bind("click", function() {
			openShFormWin($("#" + modelName + "_form_id").val(),"cz");
		});
		$("a[data-action='HYPXSH_FORMBGSSHBTN']").bind("click", function() {
			openShFormWin($("#" + modelName + "_form_id").val(),"bgs");
		});
		$("a[data-action='HYPXSH_FORMCWSHBTN']").bind("click", function() {
			openShFormWin($("#" + modelName + "_form_id").val(),"cw");
		});
	}
	
	/**跳转到新增/修改页面**/
	function openShFormWin(id,type){
		var win = $("<div id='" + modelName + "_shFormWin'></div>").window({
			title : "公务接待[审核]",
			href :URL.shFormWin,
			width : 500,
			height : 177,
			onLoad : function(){
				$("#" + modelName + "_shform_id").val(id);
				$("#" + modelName + "_shform_stype").val(type);
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
});
