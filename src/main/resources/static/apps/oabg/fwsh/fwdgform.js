$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/fwng/fwsh/save",
	};

	/** 模块名称* */
	var modelName = "fwsh";
	
	/** 表单* */
	var form = $("#" + modelName + "_fwdgform");
	
	/** 绑定按钮事件* */
	bindButtonAction();
	

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=fwsh_fwdg_save]").bind("click", function() {
			save();
		});
	}
	
//	$("#fwdg_form_bhqr").combobox({
//		onSelect:function(){
//			var bhqr=$(this).combobox('getValue');
//			if("1"==bhqr){
//				$('#fwdg_form_djbh').textbox({
//					disabled:false,
//					required:true			
//				})
//			}else if("2"==bhqr){
//				$('#fwdg_form_djbh').textbox({
//					disabled:true,
//					value:""
//				})
//				$('#fwdg_form_djbh').css({'background-color':'#f0f0f0'});
//			}
//		}
//	})
	
	/**
	 * 保存
	 */
	function save() {
		if (form.form("validate")) {
			var fwngid=$("#fwsh_fwdgform_id").val();
			var fwztCode=$("#fwsh_fwdgform_fwzt").val();
			/** 序列化form* */
			var formData=form.serializeObject({transcript:"overlay"});
			var param = $.extend({},{"fwngid":fwngid,"fwzt":fwztCode},formData);  
			$.messager.confirm("系统提示","确认发文定稿",function(r){
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
								$("#" + modelName + "_fwdgFormWin").window("close");
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
