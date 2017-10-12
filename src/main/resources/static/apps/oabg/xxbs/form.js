$(function() {
	
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/xxbs/save",
	};
	
	
	/** 模块名称* */
	var modelName = "xxbs";
	/** 表单* */
	var form = $("#" + modelName + "_form");
	var date = $("#xxbs_form_date").val();
	form.form('load',{ bssj:date});
	
	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=XXBSSQ_SAVEBTN]").bind("click", function() {
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
			$.messager.confirm("系统提示","是否确认提交？",function(r){
				if(r){
					$.ajax({
						type : "post",
						dataType : "json",
						url : URL.save,
						data :formData,
						success : function(data) {
							$.messager.progress("close");
							if (data.success) {
								$.messager.show({
									title:'提示',
									msg: "保存成功！",
									timeout:1000,
									showType:'slide'
								});
								$("#"+modelName+"_formWin").window("close");
								$("#"+modelName+"_grid").datagrid("reload");
							}else{
								showMsg(data.returnMsg);
							}
						}
					});
				}
			})
		}
	}
	
});
