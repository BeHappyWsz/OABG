$(function() {
	var URL = {
		/** 保存表单**/
		doSp : ctx + "/oabg/hypx/doSp"
	};

	/** 模块名称* */
	var modelName = "hypxsp";

	/** 表单* */
	var form = $("#" + modelName + "_spform");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action='HYPXSP_DOSPBTN']").bind("click", function() {
			doSp();
		});
	}
	
	/**跳转到新增/修改页面**/
	function doSp(){
		if (form.form("validate")) {
			/** 序列化form* */
			var formData = form.serializeObject({transcript:"overlay"});
			var sftg = "1" == formData.sftg ? "<span style='color:green'>通过</span>" : "<span style='color:red'>不通过</span>"; 
			$.messager.confirm("系统提示","确认审核"+sftg+"？",function(r){
				if(r){
					$.ajax({
						type : "post",
						url : URL.doSp,
						data :formData,
						success : function(data) {
							if (data.success) {
								showMsg(Msg.spSuccess);
								$("#" + modelName + "_spFormWin").window("close");
								if($("#" + modelName + "_formWin").size() != 0){
									$("#" + modelName + "_formWin").window("close");
								}
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
	
});
