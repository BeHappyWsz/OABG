$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/fwng/fwsh/save",
	};

	/** 模块名称* */
	var modelName = "fwsh";
	
	/** 表单* */
	var form = $("#" + modelName + "_csform");
	
	/** 绑定按钮事件* */
	bindButtonAction();
	

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=fwsh_cs_save]").bind("click", function() {
			save();
		});
	}
	
	/**通过状态联动**/
	$("#cs_form_tgzt").combobox({
		onSelect:function(){
			var tgzt=$(this).combobox('getValue');
			if("1"==tgzt){
				$('#fwsh_sflz').combobox({
					disabled:false,
					required:true
				})
			}else if("2"==tgzt){
				$('#fwsh_sflz').combobox({
					disabled:true,
					setValue:""
				});
				$('#fwsh_qtbm').textbox({
					disabled:true,
					value:""
				});
				$('#fwsh_qtbm').css({'background-color':'#f0f0f0'});
				$('#fwsh_sflz').css({'background-color':'#f0f0f0'});
			}
		}
	})
	
	/**流转到其他部门**/
	$("#fwsh_sflz").combobox({
		onSelect:function(){
			var bhqr=$(this).combobox('getValue');
			if("1"==bhqr){
				$('#fwsh_qtbm').textbox({
					disabled:false,
					required:true
				})
			}else if("2"==bhqr){
				$('#fwsh_qtbm').textbox({
					disabled:true,
					value:""
				})
				$('#fwsh_qtbm').css({'background-color':'#f0f0f0'});
			}
		}
	})
	
	/**
	 * 保存
	 */
	function save() {
		if (form.form("validate")) {
			var fwngid=$("#fwsh_csform_id").val();
			var fwztCode=$("#fwsh_csform_fwzt").val();
			/** 序列化form* */
			var formData=form.serializeObject({transcript:"overlay"});
			var param = $.extend({},{"fwngid":fwngid,"fwzt":fwztCode},formData);  
			$.messager.confirm("系统提示","确认初审",function(r){
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
								$("#" + modelName + "_csFormWin").window("close");
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
