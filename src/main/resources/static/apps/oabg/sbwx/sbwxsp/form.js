$(function() {
	var URL = {
			spFormWin : ctx + "/oabg/sbwx/sbwxsp/spForm",
			doYwx : ctx + "/oabg/sbwx/sbwxsp/doYwx"
	};

	/** 模块名称* */
	var modelName = "sbwxsp_form";
	/** 绑定按钮事件* */
	bindButtonAction();
	
	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=SBWXSP_FORM_SPBTN]").bind("click", function() {
			var id = $("#sbwxsp_form_id").val();
			openSpForm(id);
		});
		
		
		$("a[data-action=SBWXSP_FORM_DOYWX]").bind("click", function() {
			var id = $("#sbwxsp_form_id").val();
			doYwx(id);
		});
	}

	/**
	 * 打开审批页面
	 */
	function openSpForm(id){
		var win = $("<div id='" + modelName + "_spFormWin'></div>").window({
			title : "申请审批",
			width : 500,
			height : 177,
			href :URL.spFormWin+"?id="+id,
			onLoad : function(){				
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	/**
	 * 进行已维修状态更换
	 */
	function doYwx(id){
		$.messager.confirm("系统提示","是否进行状态变更操作?",function(r){
			if(r){
				$.messager.progress({text:'正在进行，请稍后......'});
				$.ajax({
					url :URL.doYwx,
					data:{id:id},
					type:"get",
					success :function(data){
						$.messager.progress("close");
						if(data.success){
							showMsg(data.returnMsg);
							$("#sbwxsp_formWin").window("close");
							$("#sbwxsp_grid").datagrid("reload");
						}else{
							showMsg("出现错误");
						}
					}
				});
			}
		});
	}
	
});
