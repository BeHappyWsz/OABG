/*$(function() {
	var URL = {
		*//** 保存表单**//*
		save : ctx + "/oabg/gzgl/gzdj/djsave",
	};

	*//** 模块名称* *//*
	var modelName = "gzdj";

	*//** 表单* *//*
	var form = $("#" + modelName + "_form");

	*//** 绑定按钮事件* *//*
	bindButtonAction();
	

	function bindButtonAction() {
		*//** 绑定保存事件* *//*
		$("a[data-action=gzdj-save]").bind("click", function() {
			save();
		});
		$("a[data-action=gzdj-cross]").bind("click", function() {
			$("#" + modelName + "_formWin").window("close");
		});
	}
	
	*//**
	 * 保存
	 *//*
	function save() {
		if (form.form("validate")) {
			*//** 序列化form* *//*
			var formData = form.serializeObject({transcript:"overlay"});
			$.ajax({
				type : "post",
				dataType : "json",
				url : URL.save,
				data :formData,
				success : function(data) {
					$.messager.progress("close");
					if (data.success) {
						showMsg(Msg.saveSuc);
						$("#" + modelName + "_formWin").window("close");
						$("#" + modelName + "_grid").datagrid("reload");
					}else{
						
						$.messager.show({
							title:'提示',
							msg: data.returnMsg,
							timeout:1000,
							showType:'slide'
						});
					}
				}
			});
		}
	}
	
	
});
*/