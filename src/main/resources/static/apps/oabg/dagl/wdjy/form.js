$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/wdjy/save",
		
		wdFormWin : ctx + "/oabg/wdjy/wdForm",
		
		checkoutStrtime :ctx + "/oabg//wdjy/checkoutStrtime",
		checkoutendtime :ctx + "/oabg/wdjy/checkoutendtime"
	};

	/** 模块名称* */
	var modelName = "wdjy";

	/** 表单* */
	var form = $("#" + modelName + "_form");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=wdjy_save]").bind("click", function() {
			save();
		});
	}
	
	/**
	 * 保存
	 */
	function save() {
		if (form.form("validate")) {
			/** 序列化form* */
			var formData = form.serializeObject({transcript:"overlay"});
			$.messager.confirm("系统提示","确认提交？",function(r){
				if(r){
					$.ajax({
						type : "post",
						url : URL.save,
						data :formData,
						success : function(data) {
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
	}
	
	$("#"+modelName+"_wdBtn").bind("click",function(){
		var win = $("<div id='" + modelName + "_wd'></div>").window({
			title : "文档信息",
			width : 1070,
			height :530,
			href :URL.wdFormWin,
			onLoad : function(){	
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	});
	
	
	/*
	 * 借阅起始时间
	 * */
	$("#wdjy_form_jysjs").datetimebox({
		stopFirstChangeEvent: true,
		onChange:function(data){
	        var formData = form.serializeObject({transcript : "overlay"});
			if(formData.id!=null&&formData.id!=""){
				var options = $(this).datetimebox('options');
		        if(options.stopFirstChangeEvent) {
		            options.stopFirstChangeEvent = false;
		            return;
		        }
			}
			if(null == formData.lcztcode || "" == formData.lcztcode || "9" == formData.lcztcode){
				$.ajax({
		        	url : URL.checkoutStrtime,
		        	type : "get",
		        	data : {timeStr:formData.jysjs,timeEnd:formData.jysje},
		        	success : function(data){
		        		if(data.success){
		        			
		        		}else{
		        			$("#wdjy_form_jysjs").datebox('clear');
		        			$.messager.alert("提示",data.returnMsg);
		        		}
		        	}
		        });
			}
		}
	});
	
	/*
	 * 借阅结束时间
	 * */
	$("#wdjy_form_jysje").datetimebox({
		stopFirstChangeEvent: true,
		onChange:function(data){
			var formData = form.serializeObject({transcript : "overlay"});
			if(formData.id!=null&&formData.id!=""){
				var options = $(this).datetimebox('options');
		        if(options.stopFirstChangeEvent) {
		            options.stopFirstChangeEvent = false;
		            return;
		        }
			}
			if(null == formData.lcztcode || "" == formData.lcztcode || "9" == formData.lcztcode){
		    	$.ajax({
		    		url : URL.checkoutendtime,
		    		type : "get",
		    		data : {timeStr:formData.jysjs,timeEnd:formData.jysje},
		    		success : function(data){
		    			if(data.success){
		    				
		    			}else{
		    				$("#wdjy_form_jysje").datebox('clear');
		    				$.messager.alert("提示",data.returnMsg);
		    			}
		    		}
			    });
			}
		}
	});
});
