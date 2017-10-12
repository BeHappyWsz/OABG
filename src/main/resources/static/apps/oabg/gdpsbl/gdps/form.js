$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/gdpsbl/gdps/save",
		checkoutStrtime :ctx + "/oabg//gdpsbl/gdps/checkoutStrtime",
		checkoutendtime :ctx + "/oabg/gdpsbl/gdps/checkoutendtime"
	};

	/** 模块名称* */
	var modelName = "gdps";

	/** 表单* */
	var form = $("#" + modelName + "_form");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=gdps_save]").bind("click", function() {
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
			var gdnr=$("#oabg_gdps_gdnr").kindeditorbox("html");
			formData.gdnr=gdnr;
			$.messager.progress({text:'正在保存，请稍后......'});
			$.ajax({
				type : "post",
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
	 * 办理期限起始时间
	 * */
	$("#gdps_form_blqxs").datetimebox({
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
		    	data : {timeStr:formData.blqxs,timeEnd:formData.blqxe},
		    	success : function(data){
		    		if(data.success){
		    			
		    		}else{
		    			$("#gdps_form_blqxs").datebox('clear');
		    			$.messager.alert("提示",data.returnMsg);
		    		}
		    	}
		    });
		}       
	});
	
	/*
	 * 办理期限结束时间
	 * */
	$("#gdps_form_blqxe").datetimebox({
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
	        	data : {timeStr:formData.blqxs,timeEnd:formData.blqxe},
	        	success : function(data){
	        		if(data.success){
	        			
	        		}else{
	        			$("#gdps_form_blqxe").datebox('clear');
	        			$.messager.alert("提示",data.returnMsg);
	        		}
	        	}
	        });
		}
	});
});
