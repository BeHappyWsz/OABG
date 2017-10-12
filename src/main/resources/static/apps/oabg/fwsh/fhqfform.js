$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/fwng/fwsh/save",
	};

	/** 模块名称* */
	var modelName = "fwsh";
	
	/** 表单* */
	var form = $("#" + modelName + "_fhqfform");
	
	/** 绑定按钮事件* */
	bindButtonAction();
	

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=fwsh_fhqf_save]").bind("click", function() {
			save();
		});
	}
	
	/**
	 * 保存
	 */
	function save() {
		if (form.form("validate")) {
			var fwngid=$("#fwsh_fhqfform_id").val();
			var fwztCode=$("#fwsh_fhqfform_fwzt").val();
			/** 序列化form* */
			var formData=form.serializeObject({transcript:"overlay"});
			var param = $.extend({},{"fwngid":fwngid,"fwzt":fwztCode},formData);  
			$.messager.confirm("系统提示","确认复核签发",function(r){
				if(r){
					$.messager.progress({text:'正在保存，请稍后......'});
					$.ajax({
						type : "post",
						dataType : "json",
						url : URL.save,
						data :param,
						success : function(data) {
							$.messager.progress("close");
							if (data.success) {
								showMsg(Msg.saveSuc);
								$("#" + modelName + "_fhqfFormWin").window("close");
								$("#" + modelName + "_formWin").window("close");
								$("#" + modelName + "_grid").datagrid("reload");
							}
						}
					});
				}
			});
		}
	}
	
	
});
