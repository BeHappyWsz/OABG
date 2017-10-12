$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/fwng/fwsh/save",
		print: ctx + "/oabg/fwsh/print",
		fwjdformWin : ctx + "/oabg/fwsh/fwjd",
		yyffformWin : ctx + "/oabg/fwsh/yyff",
		getInfo : ctx + "/oabg/fwng/getInfo",
		exportPdfUrl:ctx+"/oabg/fwsh/exportPdf",
		exportWordUrl:ctx+"/oabg/fwsh/exportWord"
	};

	/** 模块名称* */
	var modelName = "fwsh";

	/** 表单* */
	var form = $("#" + modelName + "_form1");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		$("a[data-action='fwsh_fwjd_']").bind("click", function() {
			var id=$("#" + modelName + "_form1_id").val();
			var fwzt=$("#" + modelName + "_form1_fwzt").val();
			fwjdFormWin(id,fwzt);
		});
		
		$("a[data-action='fwsh_yyff_']").bind("click", function() {
			var id=$("#" + modelName + "_form1_id").val();
			var fwzt=$("#" + modelName + "_form1_fwzt").val();
			yyffFormWin(id,fwzt);
		});
		
		$("a[data-action='fwsh_fwdy_']").bind("click",function(){
			var id=$("#" + modelName + "_form1_id").val();
			var fwzt=$("#" + modelName + "_form1_fwzt").val();	
			if(6 == fwzt || "6" == fwzt || 7 == fwzt || "7" == fwzt || 8 == fwzt || "8" == fwzt || 11 == fwzt || "11" == fwzt ){
				var confirmmsg = '是否确认打印？';
				$.messager.confirm('系统提示', confirmmsg, function(r){
					if (r){
						//往后台保存相关信息
						if(6 == fwzt || "6" == fwzt){
							$.ajax({
								type : "post",
								dataType : "json",
								url : URL.save+"?fwngid="+id+"&fwzt="+fwzt,
								success : function(data) {
									$.messager.progress("close");
									if (data.success) {
										$("#" + modelName + "_grid").datagrid("reload");
									}
								}
							});
						}
						//打印
						window.open(URL.print+"?id="+id);
						
					}
				});
			}
		});
		
		//导出pdf
		$("a[data-action='fwsh_exportpdf']").bind("click",function(){
			var id=$("#" + modelName + "_form1_id").val();
			exportPdf(id);
		})
		
		//导出word
		$("a[data-action='fwsh_exportword']").bind("click",function(){
			var id=$("#" + modelName + "_form1_id").val();
			exportWord(id);
		})
		
	}
	
	function fwjdFormWin(id,fwzt){
		var win = $("<div id='" + modelName + "_fwjdFormWin'></div>").window({
			title : "发文拟稿[发文校对]",
			href :URL.fwjdformWin,
			width : 500,
			height : 142,
			onLoad : function(){
				$("#" + modelName + "_fwjdform_id").val(id);
				$("#" + modelName + "_fwjdform_fwzt").val(fwzt);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	function yyffFormWin(id,fwzt){
		var win = $("<div id='" + modelName + "_yyffFormWin'></div>").window({
			title : "发文拟稿[打印分发]",
			href :URL.yyffformWin,
			width : 500,
			height : 250,
			onLoad : function(){
				formLoadData(id);
				$("#" + modelName + "_yyffform_id").val(id);
				$("#" + modelName + "_yyffform_fwzt").val(fwzt);
				if("11"==fwzt || 11==fwzt){
					$("a[data-action='fwsh_yyff_ff']").addClass("hide");
				}
				
				closeLoadingDiv();
				
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	function formLoadData(id){
		$.ajax({
			type : "get",
			dataType : "json",
			url :URL.getInfo,
			data : {id:id},
			success : function(data){
				$("#" + modelName + "_form").form("load", data);
				$("#" + modelName + "_yyffFormWin").form("load", data);
				$("#" + modelName + "_form1").form("load", data);
			}
		});
	}
	
	function exportPdf(id){
		$.messager.confirm('确认导出','您确定要导出吗?', function(r){
			 if (r){
				 window.open(URL.exportPdfUrl+"?id="+id);
			 }
		});
	}
	
	function exportWord(id){
		$.messager.confirm('确认导出','您确定要导出吗?', function(r){
			 if (r){
				 window.open(URL.exportWordUrl+"?id="+id);
			 }
		});
	}
});
