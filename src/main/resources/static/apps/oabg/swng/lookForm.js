$(function(){
	var URL = {
			//分发批阅页面
			ffpyFormWin : ctx + "/oabg/swng/ffpyForm",
			//收文处理页面
			swclFormWin : ctx + "/oabg/swng/swclForm"
	};
	
	/** 模块名称* */
	var modelName = "swng";
	
	bindButton();
	/** 绑定按钮方法* */
	function bindButton() {
		//收文处理
		$("a[data-action='swng_swcl']").bind("click",function(){
			var id=$("#"+modelName+"_lookForm_id").val();
			swclFormWin(id);
		});
		//分发批阅
		$("a[data-action='swng_lookForm_ffpy']").bind("click",function(){
			var id=$("#"+modelName+"_lookForm_id").val();
			ffpyFormWin(id);
		});
	}

	/**
	 * 分发批阅页面
	 */
	function ffpyFormWin(id){
		var win = $("<div id='" + modelName + "_ffpyFormWin'></div>").window({
			title : "收文管理[分发批阅]",
			href :URL.ffpyFormWin,
			width : 500,
			height : 110,
			onLoad : function(){
				$("#" + modelName + "_ffpyForm_ids").val(id);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	
	/**
	 * 收文处理页面
	 */
	function swclFormWin(id){
		var win = $("<div id='" + modelName + "_swclFormWin'></div>").window({
			title : "收文处理",
			href :URL.swclFormWin,
			width : 560,
			height : 198,
			onLoad : function(){
				$("#" + modelName + "_swclForm_id").val(id);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
});