$(function() {
	var URL = {
		/** 保存表单**/
		doSh : ctx + "/oabg/gwjd/doSh"
	};

	/** 模块名称* */
	var modelName = "gwjdsh";

	/** 表单* */
	var form = $("#" + modelName + "_shform");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action='GWJDSH_CZDOSHBTN']").bind("click", function() {
			doSh();
		});
		$("a[data-action='GWJDSH_BGSDOSHBTN']").bind("click", function() {
			doSh();
		});
		$("a[data-action='GWJDSH_CWDOSHBTN']").bind("click", function() {
			doSh();
		});
	}
	
	/**跳转到新增/修改页面**/
	function doSh(){
		if (form.form("validate")) {
			/** 序列化form* */
			var formData = form.serializeObject({transcript:"overlay"});
			var sftg = "1" == formData.sftg ? "<span style='color:green'>通过</span>" : "<span style='color:red'>不通过</span>"; 
			$.messager.confirm("系统提示","确认审核"+sftg+"？",function(r){
				if(r){
					$.ajax({
						type : "post",
						url : URL.doSh,
						data :formData,
						success : function(data) {
							if (data.success) {
								showMsg(Msg.shSuccess);
								$("#" + modelName + "_shFormWin").window("close");
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
