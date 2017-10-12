$(function() {
	
	var URL = {
		/** 保存表单**/
		doSp : ctx + "/oabg/kqgl/qjspOne/doSp"
	};
	/** 模块名称* */
	var modelName = "qjsp";
	/** 表单* */
	var form = $("#"+modelName+"_spform");
	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=qjsp-czsppp]").bind("click", function() {
			doSp()
		});
	}
	
	/**
	 * 保存
	 */
	/**跳转到新增/修改页面**/
	function doSp(){
		if (form.form("validate")) {
			/** 序列化form* */
			var formData = form.serializeObject({transcript:"overlay"});
			var sftg = "1" == formData.sftg ? "<span style='color:green'>通过</span>" : "<span style='color:red'>不通过</span>"; 
			$.messager.confirm("系统提示","确认审批"+sftg+"？",function(r){
				if(r){
					$.ajax({
						type : "post",
						url : URL.doSp,
						data :formData,
						success : function(data) {
							if (data.success) {
								showMsg(Msg.spSuc);
								$("#" + modelName + "_spFormWin").window("close");
								$("#" + modelName + "_formWin").window("close");
								$("#" + modelName + "_grid").datagrid("reload");
							}else{
								showMsg(data.returnMsg);
							}
						}
					});
				}
			});
		}
	}
	
	/*$("#qjsp_spform_sftg").combobox({
		onChange : function(n,o){
			if(1 == n || "1" == n){
				$("#qjsp_spform_spyjtr").html("审批意见");
				$("#qjsp_spform_spyj").textbox({
					required:false
				});
			}else{
				$("#qjsp_spform_spyjtr").html("审批意见<span style='color:red'>*</span>");
				$("#qjsp_spform_spyj").textbox({
					required:true
				});
			}
		}
	});*/
});
