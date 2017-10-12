$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/swng/save",
		
		swclFormWin : ctx + "/oabg/swng/swclForm",
		
		fwrqCheck : ctx + "/oabg/swng/fwrqCheck",
		
		//分发批阅页面
		ffpyFormWin : ctx + "/oabg/swng/ffpyForm"
	};

	/** 模块名称* */
	var modelName = "swng";

	/** 表单* */
	var form = $("#" + modelName + "_form");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		closeLoadingDiv();
		/** 绑定保存事件* */ 
		$("a[data-action=swng_save]").bind("click", function() {
			save();
		});
		
		$("a[data-action=swng_ffpyform]").bind("click", function() {
			var id = $("#swng_form_id").val();
			ffpyFormWin(id);
		});
		
		$("a[data-action=swng_swclform]").bind("click", function() {
			var id = $("#swng_form_id").val();
			swclFormWin(id);
		});
	}
	
	function swclFormWin(id){
		var win = $("<div id='" + modelName + "_swclFormWin2'></div>").window({
			title : "收文处理",
			href :URL.swclFormWin,
			width : 560,
			height : 198,
			onLoad : function(){
				$("#" + modelName + "_swclForm_ids").val(id);
				setTimeout(function(){//默认为传阅
					$("#" + modelName + "_swclForm_swcl").combobox("setValue",1);
				},300);
				$("#" + modelName + "_swclForm_swcl").combobox({
					disabled : true
				});
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	function ffpyFormWin(id){
		var win = $("<div id='" + modelName + "_ffpyFormWin'></div>").window({
			title : "收文管理[分发批阅]",
			href :URL.ffpyFormWin,
			width : 500,
			height : 110,
			onLoad : function(){
				$("#" + modelName + "_ffpyForm_ids").val(id);
				$("#" + modelName + "_ffpyForm_index").val("2");
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	/**
	 * 保存
	 */
	function save() {
		if (form.form("validate")) {
			/** 序列化form* */
			var formData = form.serializeObject({transcript:"overlay"});
			formData.zrbms = $("#swng_form_zrbms").combo('getValues')+"";
			$.messager.progress({text:'正在保存，请稍后......'}); 
			$.ajax({
				type : "post",
				dataType : "json",
				url : URL.save,
				data :formData,
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
	}
	/*
	 * 校验发文日期
	 * */
	$("#swng_form_fwrq").datebox({
		stopFirstChangeEvent: true,
		onChange:function(data){
			var formData = form.serializeObject({transcript : "overlay"});
			if(formData.id!=null&&formData.id!=""){
				var options = $(this).datebox('options');
		        if(options.stopFirstChangeEvent) {
		            options.stopFirstChangeEvent = false;
		            return;
		        }
			} 
			$.ajax({
				url : URL.fwrqCheck,
				type : "get",
				data : {fwrq:formData.fwrq},
				success : function(data){
					if(data.success){
						
					}else{
						$("#swng_form_fwrq").datebox('clear');
						$.messager.alert("提示",data.returnMsg);
					}
				}
			});
		}
	});
});
