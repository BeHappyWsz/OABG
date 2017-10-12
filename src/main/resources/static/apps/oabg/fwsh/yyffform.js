$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/fwng/fwsh/save",
		save2 :ctx + "/oabg/wddb/dy/save?sjly=1",
		print: ctx + "/oabg/fwsh/print"
	};

	/** 模块名称* */
	var modelName = "fwsh";
	
	/** 表单* */
	var form = $("#" + modelName + "_yyffform");
	
	/** 绑定按钮事件* */
	bindButtonAction();
	

	function bindButtonAction() {
		/**分发* */
		$("a[data-action=fwsh_yyff_ff]").bind("click", function() {
			yyff_ff();
		});
		
		/**打印**/
		$("a[data-action=fwsh_fhyz]").bind("click", function() {
			print_yyff();
		});
		
		
	}
	
	/**分发**/
	function yyff_ff(){
		if (form.form("validate")) {
			var fwngid=$("#fwsh_yyffform_id").val();
			var fwztCode=$("#fwsh_yyffform_fwzt").val();
			/** 序列化form* */
			/** 序列化form* */
			var formData=form.serializeObject({transcript:"overlay"});
			var param = $.extend({},{"fwngid":fwngid,"fwzt":fwztCode},formData);
			if("10"==fwztCode){
				$.ajax({
					type : "post",
					dataType : "json",
					url : URL.save2,
					data :{"glid":fwngid},
					success : function(data) {
						$.messager.progress("close");
						if (data.success) {
							showMsg(Msg.saveSuc);
							$("#" + modelName + "_yyffFormWin").window("close");
							$("#fwsh_formWin1").window("close");
							$("#fwsh_grid").datagrid("reload");
						}
					}
				});
			}else{
				$.ajax({
					type : "post",
					dataType : "json",
					url : URL.save,
					data :param,
					success : function(data) {
						$.messager.progress("close");
						if (data.success) {
							$.ajax({
								type : "post",
								dataType : "json",
								url : URL.save2,
								data :{"glid":fwngid},
								success : function(data) {
									$.messager.progress("close");
									if (data.success) {
										showMsg(Msg.saveSuc);
										$("#" + modelName + "_yyffFormWin").window("close");
										$("#fwsh_formWin1").window("close");
										$("#fwsh_grid").datagrid("reload");
									}
								}
							});
						}
					}
				});
			}
		}
	}	
	
	//打印
	function print_yyff(){
		var fwngid=$("#fwsh_yyffform_id").val();
		var fwztCode=$("#fwsh_yyffform_fwzt").val();
		if (form.form("validate")){
			var confirmmsg = '是否确认打印？';
			$.messager.confirm('系统提示', confirmmsg, function(r){
				if (r){
					if("10"==fwztCode){
						window.open(URL.print+"?id="+fwngid);
					}else{
						/** 序列化form* */
						var formData=form.serializeObject({transcript:"overlay"});
						var param = $.extend({},{"fwngid":fwngid,"fwzt":fwztCode},formData);
						$.ajax({
							type : "post",
							dataType : "json",
							url : URL.save,
							data :param,
							success : function(data) {
								$.messager.progress("close");
								if (data.success) {
									showMsg(Msg.saveSuc);
									$("#" + modelName + "_yyffFormWin").window("close");
									$("#" + modelName + "_formWin1").window("close");
									$("#" + modelName + "_grid").datagrid("reload");
									window.open(URL.print+"?id="+fwngid);
								}
							}
						});
					}
					
				}
			});
		}
	}
	
//	function save() {
//		if (form.form("validate")) {
//			var fwngid=$("#fwsh_yyffform_id").val();
//			var fwztCode=$("#fwsh_yyffform_fwzt").val();
//			/** 序列化form* */
//			var formData=form.serializeObject({transcript:"overlay"});
//			var param = $.extend({},{"fwngid":fwngid,"fwzt":fwztCode},formData);  
//			$.messager.confirm("系统提示","确认提交复核意见",function(r){
//				if(r){
//					$.messager.progress({text:'正在保存，请稍后......'});
//					$.ajax({
//						type : "post",
//						dataType : "json",
//						url : URL.save,
//						data :param,
//						success : function(data) {
//							$.messager.progress("close");
//							if (data.success) {
//								showMsg(Msg.saveSuc);
//								$("#" + modelName + "_yyffFormWin").window("close");
//								$("#" + modelName + "_formWin1").window("close");
//								$("#" + modelName + "_grid").datagrid("reload");
//							}
//						}
//					});
//				}
//			});
//		}
//	}

});
