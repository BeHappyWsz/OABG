$(function() {
	var URL = {
		/** 拟办**/
		doNb : ctx + "/oabg/swsh/doNb"
	};

	/** 模块名称* */
	var modelName = "swsh";

	/** 表单* */
	var form = $("#" + modelName + "_nbform");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		closeLoadingDiv();
		$("a[data-action='swsh_doNb']").bind("click", function() {
			doNb();
		});
	}
	/**拟办**/
	function doNb(){
		if (form.form("validate")) {
			var formData = form.serializeObject({transcript:"overlay"});
			var cbyj = $("#" + modelName + "_nbform_cbyj").combobox('getValue');
			if(cbyj == "7"){//其他
				var sug = $("#swsh_nbform_nbyj").val();
				if(sug == "" || sug == undefined){//备注为空
					formData.suggestion = $("#" + modelName + "_nbform_cbyj").combobox('getText');
				}else{//备注不为空
					formData.suggestion = sug;
				}
			}else{
				formData.suggestion = $("#" + modelName + "_nbform_cbyj").combobox('getText');
			}
			$.messager.confirm("系统提示","确认拟办?",function(r){
				if(r){
					$.messager.progress({text:'正在保存，请稍后......'});
					$.ajax({
						type : "post",
						url : URL.doNb,
						data :formData,
						success : function(data) {
							$.messager.progress("close");
							if (data.success) {
								showMsg(Msg.nbSuc);
							}else{
								showMsg(Msg.nbFal);
							}
							var index = $("#" + modelName + "_nbform_index").val();
//							//1快捷键操作 2form页面操作 swsh_nbform_index
							if("2" == index || 2 == index){
								$("#" + modelName + "_formWin").window("destroy");
							}
							$("#" + modelName + "_nbFormWin").window("destroy");
							$("#" + modelName + "_grid").datagrid('reload');
						}
					});
				}
			});
		}
	}
	
	
	$("#" + modelName + "_nbform_cbyj").combobox({
		onChange : function(n,o){
			if(n == "7"){
				$("#" + modelName + "_nbform_nbyjtr").html("备注");
				$("#swsh_nbform_nbyj").textbox({
					disabled : false,
					setText  : ""
				});
				$("#swsh_nbform_nbyj").textbox('setText',"");
			}else{
				$("#" + modelName + "_nbform_nbyjtr").html("");
				$("#swsh_nbform_nbyj").textbox('setText',"");
				$("#swsh_nbform_nbyj").textbox({
					disabled : true,
					setText : ""
				});
			}
		}
	});
});