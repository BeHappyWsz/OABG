$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/fwng/fwsh/save",
		hgformWin : ctx + "/oabg/fwsh/hg",
		csformWin : ctx + "/oabg/fwsh/cs",
		lzspformWin : ctx + "/oabg/fwsh/lzsp",
		fhqfformWin : ctx + "/oabg/fwsh/fhqf",
		fwdgformWin : ctx + "/oabg/fwsh/fwdg",
		fwjdformWin : ctx + "/oabg/fwsh/fwjd",
		yyffformWin : ctx + "/oabg/fwsh/yyff",
		print: ctx + "/oabg/fwsh/print",
		
	};

	/** 模块名称* */
	var modelName = "fwsh";
	
	/** 表单* */
	var form = $("#" + modelName + "_form");	

	/** 绑定按钮事件* */
	bindButtonAction();
	
	function bindButtonAction(){
		
		$("a[data-action='fwsh_hg_']").bind("click", function() {
			var id=$("#" + modelName + "_form_id").val();
			var fwzt=$("#" + modelName + "_form_fwzt").val();
			hgFormWin(id,fwzt);
		});
		
		$("a[data-action='fwsh_cs_']").bind("click", function() {
			var id=$("#" + modelName + "_form_id").val();
			var fwzt=$("#" + modelName + "_form_fwzt").val();
			csFormWin(id,fwzt);
		});
		
		$("a[data-action='fwsh_lzsp_']").bind("click", function() {
			var id=$("#" + modelName + "_form_id").val();
			var fwzt=$("#" + modelName + "_form_fwzt").val();
			lzspFormWin(id,fwzt);
		});
		
		$("a[data-action='fwsh_fhqf_agree_']").bind("click", function() {
			var id=$("#" + modelName + "_form_id").val();
			var fwzt=$("#" + modelName + "_form_fwzt").val();
			save(id,fwzt,"1");
		});
		
		$("a[data-action='fwsh_fhqf_cancel_']").bind("click", function() {
			var id=$("#" + modelName + "_form_id").val();
			var fwzt=$("#" + modelName + "_form_fwzt").val();
			save(id,fwzt,"2");
		});
		
		$("a[data-action='fwsh_fwdg_']").bind("click", function() {
			var id=$("#" + modelName + "_form_id").val();
			var fwzt=$("#" + modelName + "_form_fwzt").val();
			fwdgFormWin(id,fwzt);
		});

	}
	
	function hgFormWin(id,fwzt){
		var win = $("<div id='" + modelName + "_hgFormWin'></div>").window({
			title : "发文拟稿[核稿]",
			href :URL.hgformWin,
			width : 500,
			height : 173,
			onLoad : function(){
				$("#" + modelName + "_hgform_id").val(id);
				$("#" + modelName + "_hgform_fwzt").val(fwzt);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	function csFormWin(id,fwzt){
		var win = $("<div id='" + modelName + "_csFormWin'></div>").window({
			title : "发文拟稿[初审]",
			href :URL.csformWin,
			width : 500,
			height : 238,
			onLoad : function(){
				$("#" + modelName + "_csform_id").val(id);
				$("#" + modelName + "_csform_fwzt").val(fwzt);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	function lzspFormWin(id,fwzt){
		var win = $("<div id='" + modelName + "_lzspFormWin'></div>").window({
			title : "发文拟稿[流转审批]",
			href :URL.lzspformWin,
			width : 500,
			height : 174,
			onLoad : function(){
				$("#" + modelName + "_lzspform_id").val(id);
				$("#" + modelName + "_lzspform_fwzt").val(fwzt);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	function fhqfFormWin(id,fwzt){
		var win = $("<div id='" + modelName + "_fhqfFormWin'></div>").window({
			title : "发文拟稿[复核签发]",
			href :URL.fhqfformWin,
			width : 500,
			height : 207,
			onLoad : function(){
				$("#" + modelName + "_fhqfform_id").val(id);
				$("#" + modelName + "_fhqfform_fwzt").val(fwzt);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	function fwdgFormWin(id,fwzt){
		var win = $("<div id='" + modelName + "_fwdgFormWin'></div>").window({
			title : "发文拟稿[发文定稿]",
			href :URL.fwdgformWin,
			width : 500,
			height : 173,
			onLoad : function(){
				$("#" + modelName + "_fwdgform_id").val(id);
				$("#" + modelName + "_fwdgform_fwzt").val(fwzt);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	//保存复核签发的内容
	function save(id,fwzt,tgzt) {
		/** 序列化form* */
		$.messager.confirm("系统提示","确认审批",function(r){
			if(r){
				$.messager.progress({text:'正在保存，请稍后......'});
				$.ajax({
					type : "post",
					dataType : "json",
					url : URL.save,
					data :{"fwngid":id,"fwzt":fwzt,"tgzt":tgzt},
					success : function(data) {
						$.messager.progress("close");
						if (data.success) {
							showMsg(Msg.saveSuc);
							$("#" + modelName + "_formWin").window("close");
							$("#" + modelName + "_grid").datagrid("reload");
						}
					}
				});
			}
		});
	}

});
