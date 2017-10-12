$(function() {
	
	var URL = {
		/** 保存表单**/
		doSp : ctx + "/oabg/xxbsly/doDf"
	};
	/** 模块名称* */
	var modelName = "df";
	/** 表单* */
	var form = $("#"+modelName+"_spform");
	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=XXBSSP_DAFEN]").bind("click", function() {
			doDf()
		});
	}
	/**执行打分**/
	function doDf(){
		if (form.form("validate")) {
			/** 序列化form* */
			var formData = form.serializeObject({transcript:"overlay"});
			$.messager.confirm("系统提示","确认打分？",function(r){
				if(r){
					$.ajax({
						type : "post",
						url : URL.doSp,
						data :formData,
						success : function(data) {
							if (data.success) {
								showMsg("打分成功");
								$("#xxbsly_dfFormWin").window("close");
								$("#xxbsly_formWin").window("close");
								$("#xxbsly_grid").datagrid("reload");
							}else{
								showMsg(data.returnMsg);
							}
						}
					});
				}
			});
		}
	}
	/*setTimeout(function(){
		$("#" + modelName + "_spform_sftg").combobox({
			onChange : function(n,o){
				if(1 == n || "1" == n){
					$("#" + modelName + "_spform_spyjtr").html("审批意见");
					$("#" + modelName + "_spform_spyj").textbox({
						required:false
					});
				}else{
					$("#" + modelName + "_spform_spyjtr").html("审批意见<span style='color:red'>*</span>");
					$("#" + modelName + "_spform_spyj").textbox({
						required:true
					});
				}
			}
		});
	},300)
	*/
});
