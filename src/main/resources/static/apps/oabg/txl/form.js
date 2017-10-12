$(function(){
	var URL = {
		saveUrl : ctx +"/oabg/txl/save",	
	};
	
	var modelName = "txl";
	var form = $("#"+modelName+"_form");
	
	renderButton();
	function renderButton(){
		$("a[data-action=TXL_SAVE]").bind("click", function() {
			save();
		});
	}
	
	function save(){
		if(form.form("validate")){
			var formData = form.serializeObject({transcript:"overlay"});
			$.messager.progress({text:'正在保存，请稍后......'});
			$.ajax({
				type : "post",
				dataType : "json",
				url : URL.saveUrl,
				data :formData,
				success : function(data) {
					$.messager.progress("close");
					if (data.success) {
						showMsg(Msg.saveSuc);
						$("#" + modelName + "_formWin").window("close");
						$("#txl_grid").datagrid('reload');
					}else{
						showMsg(data.returnMsg);
					}
				}
			});
		}
	}
});