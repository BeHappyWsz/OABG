/*$(function() {
	
	var URL = {
		*//** 保存表单**//*
		doGh : ctx + "/oabg/gzgl/gzdj/dogh"
	};
	*//** 模块名称* *//*
	var modelName = "query";
	*//** 表单* *//*
	var form = $("#"+modelName+"_form");
	*//** 绑定按钮事件* *//*
	bindButtonAction();

	function bindButtonAction() {
		*//** 绑定保存事件* *//*
		$("a[data-action=gzdj-gh]").bind("click", function() {
			alert(111);
			doGh()
		});
	}
	
	*//**
	 * 保存
	 *//*
	*//**跳转到新增/修改页面**//*
	function doGh(){
		if (form.form("validate")) {
			*//** 序列化form* *//*
			var formData = form.serializeObject({transcript:"overlay"});
			$.messager.confirm("系统提示","确认归还？",function(r){
				if(r){
					$.ajax({
						type : "post",
						url : URL.doGh,
						data :formData,
						success : function(data) {
							if (data.success) {
								$.messager.show({
									title:'提示',
									msg:'归还成功',
									timeout:5000,
									showType:'slide'
								});
								$("#" + modelName + "_QueryFormWin").window("close");
								$("#" + modelName + "__form").window("close");
								$("#" + modelName + "_grid").datagrid("reload");
							}else{
								$.messager.show({
									title:'提示',
									msg:'归还失败',
									timeout:5000,
									showType:'slide'
								});
								$("#" + modelName + "_QueryFormWin").window("close");
								$("#" + modelName + "__form").window("close");
								$("#" + modelName + "_grid").datagrid("reload");
							}
						}
					});
				}
			});
		}
	}
});
*/