$(function() {
	
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/gzgl/gzsq/sqsave",
		info : ctx + "/oabg/gzgl/gzsq/info",
		checkouttime :ctx + "/oabg/gzgl/gzsq/checkouttime",
	};
	
	$('#gongzname').combobox({    
	    url: ctx+'/ds/getGzsjname',    
	    valueField:'code',    
	    textField:'name'   
	}); 
	
	/** 模块名称* */
	var modelName = "gzsq";
	/** 表单* */
	var form = $("#" + modelName + "_form");
	$('#bmdw').combotree({ disabled: true }); 
	$('#sqname').combobox({ disabled: true }); 
	
	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=gzsq-save]").bind("click", function() {
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
			$.messager.confirm("系统提示","是否确认提交？",function(r){
				if(r){
					$.ajax({
						type : "post",
						dataType : "json",
						url : URL.save,
						data :formData,
						success : function(data) {
							$.messager.progress("close");
							if (data.success) {
								$.messager.show({
									title:'提示',
									msg: "保存成功！",
									timeout:1000,
									showType:'slide'
								});
								$("#gzsq_formWin").window("close");
								$("#gzsq_grid").datagrid("reload");
							}else{
								showMsg(data.returnMsg);
							}
						}
					});
				}
			});	
		}
	}
	
	/*
	 * 获取当前登录用户的部门和名字
	 * */
		
	setTimeout(function(){
			var formData = form.serializeObject({transcript:"overlay"});
			if(formData.id==null || formData.id==""){
			$.ajax({
				url : URL.info,
				success : function(data) {
					$('#bmdw').combotree("setValue",data.bmdwCode);
					$('#bmdw').combotree("setText",data.bmdwName);
					$('#sqname').textbox("setValue",data.sqname); 
				}
			});
		};
	},300);
	/*
	 * 校验盖章时间
	 * */
	$("#gztime").datetimebox({
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
				url : URL.checkouttime,
				type : "get",
				data : {gztime:formData.gztime},
				success : function(data){
					if(data.success){
						
					}else{
						$("#gztime").datebox('clear');
						$.messager.alert("提示",data.returnMsg);
					}
				}
			});
		}
	});
		
	
});
