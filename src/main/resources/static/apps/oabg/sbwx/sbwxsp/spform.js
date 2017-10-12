$(function() {
	var URL = {
		doSp : ctx + "/oabg/sbwx/sbwxsp/doSp"
	};

	/** 模块名称* */
	var modelName = "sbwxsp";

	/** 表单* */
	var form = $("#" + modelName + "_spform");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action='SBWXSP_DOSPBTN']").bind("click", function() {
			doSp();
		});
	}
	
	/**审批**/
	function doSp(){
		if (form.form("validate")) {
			/** 序列化form* */
			var formData = form.serializeObject({transcript:"overlay"});
			var sftg = "1" == formData.sftg ? "<span style='color:green'>通过</span>" : "<span style='color:red'>不通过</span>"; 
			$.messager.confirm("系统提示","确认审批"+sftg+"？",function(r){
				if(r){
					$.messager.progress({text:'正在保存，请稍后......'});
					$.ajax({
						type : "post",
						url : URL.doSp,
						data :formData,
						success : function(data) {
							$.messager.progress("close");
							if (data.success) {
								showMsg(Msg.spSuc);
								$("#" + modelName + "_form_spFormWin").window("close");
								$("#" + modelName + "_formWin").window("close");
								//index页面操作时关闭的页面
								$("#" + modelName + "_spFormWin").window("close");
								$("#" + modelName + "_grid").datagrid("reload");
							}else{
								showMsg("出现错误");
							}
						}
					});
				}
			});
		}
	}
	
});
