$(function(){
	var URL = {
			fkWin : ctx +"/oabg/hytz/fkForm",
	};
	/** 绑定Grid操作按钮方法* */
	bindGridToorBar();
	fk();
	/** 绑定Grid操作按钮方法* */
	function bindGridToorBar() {

		$("a[data-action='hytz-fkk']").bind("click",function(){
			var id = $("#hytzfkid").val();
			Hyztxx(id);
		});
	}

	/**反馈页面**/
	function Hyztxx(id){
		var win = $("<div id='hytz_fkWin'></div>").window({
			title : "反馈信息",
			width : 500,
			height : 218,
			href :URL.fkWin+"?id="+id,
			onLoad : function(){				
				formHytz(id);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	/**
	 * 获取会议通知信息
	 */
	function formHytz(id){
		$.ajax({
			type : "get",
			url :URL.getHytz,
			data : {idd:id},
			success : function(data){
				/*$("#huiyitime1").textbox("disable");
				$("#huiyitime1").textbox("setValue",data.huiyitime);
				$("#hytz_address1").textbox("disable");
				$("#hytz_address1").textbox("setValue",data.hytz_address);
				$("#titles1").textbox("disable");
				$("#titles1").textbox("setValue",data.titles);
				$("#fk_form").form("load", data);*/
				
			}
		});
	}
	
	function fk(){
		var hytzfkc = $("#hytzfkc").val();
		if(hytzfkc=="2"||hytzfkc==2){
			$("a[data-action='hytz-fkk']").addClass("hide");	
		}
	}
	
});
