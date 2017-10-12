$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/tssw/save",
		checkoutStrtime :ctx + "/oabg//tssw/checkoutStrtime",
		checkoutendtime :ctx + "/oabg/tssw/checkoutendtime"
	};

	/** 模块名称* */
	var modelName = "tssw";

	/** 表单* */
	var form = $("#" + modelName + "_form");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=tssw_save]").bind("click", function() {
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
			var zw=$("#oabg_tssw_zw").kindeditorbox("html");
			formData.zw=zw;
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
	 * 回复起始时间
	 * */
	$("#tssw_form_hfrqs").datetimebox({
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
		    $.ajax({
		    	url : URL.checkoutStrtime,
		    	type : "get",
		    	data : {timeStr:formData.hfrqs,timeEnd:formData.hfrqe},
		    	success : function(data){
		    		if(data.success){
		    			
		    		}else{
		    			$("#tssw_form_hfrqs").datebox('clear');
		    			$.messager.alert("提示",data.returnMsg);
		    		}
		    	}
		    });
		}       
	});
	
	/*
	 * 回复结束时间
	 * */
	$("#tssw_form_hfrqe").datetimebox({
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
	        $.ajax({
	        	url : URL.checkoutendtime,
	        	type : "get",
	        	data : {timeStr:formData.hfrqs,timeEnd:formData.hfrqe},
	        	success : function(data){
	        		if(data.success){
	        			
	        		}else{
	        			$("#tssw_form_hfrqe").datebox('clear');
	        			$.messager.alert("提示",data.returnMsg);
	        		}
	        	}
	        });
		}
	});
});
