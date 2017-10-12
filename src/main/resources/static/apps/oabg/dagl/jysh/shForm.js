$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/jysh/save",
	};

	/** 模块名称* */
	var modelName = "jysh";

	/** 表单* */
	var form = $("#jysh_sh_form");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=jysh_save]").bind("click", function() {
			save();
		});
	}
	
	/**
	 * 保存
	 */
	function save() {
		if (form.form("validate")) {
			/** 序列化form* */
			var formData = form.serializeObject({transcript:"overlay"});
			$.messager.confirm("系统提示","确认提交？",function(r){
				if(r){
					$.ajax({
							type : "post",
							url : URL.save,
							data :formData,
							success : function(data) {
								if (data.success) {
									showMsg(Msg.saveSuc);
									$("#jysh_shformWin").window("close");
									$("#jysh_formWin").window("close");
									$("#" + modelName + "_grid").datagrid("reload");
								}
							}
						});
					}
			});
		}
	}
	
	
	$("#jysh_sp_form_tgzt").combobox({
		onChange : function(n,o){
			if(1 == n || "1" == n){
				$("#" + modelName + "_spform_spyjtr").html("审批意见");
				$("#" + modelName + "_spform_spyj").textbox({
					required:false
				});
			}else{
				$("#jysh_spform_spyjtr").html("审批意见<span style='color:red'>*</span>");
				$("#jysh_spform_spyj").textbox({
					required:true
				});
			}
		}
	});
});
