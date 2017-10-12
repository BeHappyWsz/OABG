$(function() {
	var URL = {
		/** 拟办**/
		doNb : ctx + "/oabg/swsh/doNb",
		/** 批阅**/
		doPy : ctx + "/oabg/swsh/doPy"
	};

	/** 模块名称* */
	var modelName = "swsh";

	/** 表单* */
	var form = $("#" + modelName + "_spform");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		closeLoadingDiv();
		$("a[data-action='swsh_doPy']").bind("click", function() {
			doPy();
		});
	}
	/**批阅**/
	function doPy(){
		if (form.form("validate")) {
			/** 序列化form* */
			var index = $("#" + modelName + "_spform_index").val();
			var formData = form.serializeObject({transcript:"overlay"});
			var spyj = $("#"+modelName+"_spform_spyj").combobox('getValue');
			if(spyj == "2"){//其他
				var sug = $("#swsh_spform_suggestion").val();
				if(sug == "" || sug == undefined){//备注为空
					formData.suggestion = $("#" + modelName + "_spform_spyj").combobox('getText');
				}else{//备注不为空
					formData.suggestion = sug;
				}
			}else{//已阅
				formData.suggestion = $("#" + modelName + "_spform_spyj").combobox('getText');
			}
			$.messager.confirm("系统提示","请确认批阅?",function(r){
				if(r){
					$.ajax({
						type : "post",
						url : URL.doPy,
						data :formData,
						success : function(data) {
							if (data.success) {
								showMsg(Msg.spSuc);
							}else{
								showMsg(Msg.spFal);
							}
							$("#" + modelName + "_spFormWin").window("destroy");
							$("#" + modelName + "_grid").datagrid('reload');
							//1快捷键操作 2form页面操作 
							if("2" == index || 2 == index){
								$("#swsh_formWin").window("destroy");
							}
						}
					});
				}
			});
		}
	}
	
	$("#" + modelName + "_spform_spyj").combobox({
		onChange : function(n,o){
			if(n == "2"){//其他
				$("#" + modelName + "_spform_spyjtr").html("备注");
				$("#swsh_spform_suggestion").textbox('setText',"");
				$("#swsh_spform_suggestion").textbox({
					disabled : false,
					setText  : ""
				});
			}else{//已阅
				$("#" + modelName + "_spform_spyjtr").html("");
				$("#swsh_spform_suggestion").textbox('setText',"");
				$("#swsh_spform_suggestion").textbox({
					disabled : true,
					setText : ""
				});
			}
		}
	});
	
});
