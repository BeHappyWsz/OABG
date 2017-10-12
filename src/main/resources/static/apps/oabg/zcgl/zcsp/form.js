$(function() {
	var URL = {
		spFormWin : ctx + "/oabg/zcsp/spForm",
	};

	/** 模块名称* */
	var modelName = "zcsp";

	/** 表单* */
	var form = $("#zcsp_form");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=zcsp_czform]").unbind().bind("click", function() {
			var id = $("#zcsp_form_id").val();
			var zcglsplc = $("#zcsp_form_zcglsplc").val();
			openSpFormWin(id,zcglsplc);
		});
		$("a[data-action=zcsp_fgldform]").unbind().bind("click", function() {
			var id = $("#zcsp_form_id").val();
			var zcglsplc = $("#zcsp_form_zcglsplc").val();
			openSpFormWin(id,zcglsplc);
		});
		$("a[data-action=zcsp_glyform]").unbind().bind("click", function() {
			var id = $("#zcsp_form_id").val();
			var zcglsplc = $("#zcsp_form_zcglsplc").val();
			openSpFormWin(id,zcglsplc);
		});
		$("a[data-action=zcsp_jzform]").unbind().bind("click", function() {
			var id = $("#zcsp_form_id").val();
			var zcglsplc = $("#zcsp_form_zcglsplc").val();
			openSpFormWin(id,zcglsplc);
		});
		$("a[data-action=zcsp_ffform]").unbind().bind("click", function() {
			var id = $("#zcsp_form_id").val();
			var zcglsplc = $("#zcsp_form_zcglsplc").val();
			openSpFormWin(id,zcglsplc);
		});
	}
	
	/**
	 * 审批页面
	 */
	function openSpFormWin(id,zcglsplc){
		var win = $("<div id='" + modelName + "_spformWin'></div>").window({
			title :"审批",
			href :URL.spFormWin+"?id="+id,
			width : 500,
			height : 180,
			onLoad : function(){				
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
});
