$(function() {
	var URL = {
		shFormWin : ctx + "/oabg/jysh/shForm",
	};

	/** 模块名称* */
	var modelName = "jysh";

	/** 表单* */
	var form = $("#jysh_form");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=jysh_shh]").bind("click", function() {
			var id = $("#jysh_form_id").val();
			openShFormWin(id);
		});
	}
	
	/**
	 * 审批页面
	 */
	function openShFormWin(id){
		var win = $("<div id='" + modelName + "_shformWin'></div>").window({
			title :"审核",
			href :URL.shFormWin+"?id="+id,
			width : 500,
			height : 176,
			onLoad : function(){				
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
});
