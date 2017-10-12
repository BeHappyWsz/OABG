$(function() {
	var URL = {
		/** 拟办**/
		nbFormWin : ctx + "/oabg/swsh/nbForm",
		/** 批阅**/
		pyFormWin : ctx + "/oabg/swsh/spForm"
	};

	/** 模块名称* */
	var modelName = "swsh";

	/** 表单* */
	var form = $("#" + modelName + "_form");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action='swsh_nbForm']").bind("click", function() {
			var swshid = $("#swsh_form_id").val();
			nbFormWin(swshid);
		});
		
		/** 绑定保存事件* */
		$("a[data-action='swsh_pyForm']").bind("click", function() {
			var swshid = $("#swsh_form_id").val();
			pyFormWin(swshid);
		});
	}
	
	/**
	 * 办公室负责人拟办页面
	 */
	function nbFormWin(id) {
		var win = $("<div id='" + modelName + "_nbFormWin'></div>").window({
			title : "收文管理[拟办]",
			href :URL.nbFormWin,
			width : 500,
			height : 212,
			onLoad : function(){
				$("#" + modelName + "_nbform_ids").val(id);
				$("#" + modelName + "_nbform_index").val("2");
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	/**
	 * 分管领导批阅页面
	 */
	function pyFormWin(id) {
		var win = $("<div id='" + modelName + "_spFormWin'></div>").window({
			title : "收文管理[批阅]",
			href :URL.pyFormWin,
			width : 500,
			height : 177,
			onLoad : function(){
				$("#" + modelName + "_spform_ids").val(id);
				$("#" + modelName + "_spform_index").val("2");
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
});
