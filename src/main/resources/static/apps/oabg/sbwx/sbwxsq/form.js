$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/sbwx/sbwxsq/saveSqjl",
		checkoutStrtime :ctx + "/oabg/kqgl/qjsq/checkoutStrtime",
		checkoutendtime :ctx + "/oabg/kqgl/qjsq/checkoutendtime"
	};

	/** 模块名称* */
	var modelName = "sbwxsq";
	/** 表单* */
	var form = $("#" + modelName + "_form");
	/** 绑定按钮事件* */
	bindButtonAction();
	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=SBWXSQ_SAVEBTN]").bind("click", function() {
			save();
		});
	}
	
	/**
	 * 报修部门不可选
	 */
	$("#sbwxsq_bxbm").combo({
		disabled : true
	});
	
	/**
	 * 保存
	 */
	function save() {
		if (form.form("validate")) {
			$.messager.confirm("提示","确认提交?",function(r){
				if(r){
					/** 序列化form* */
					var formData = form.serializeObject({transcript:"overlay"});
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
	 * 校验请假起始时间
	 * */
	function jytime(){
			$("#timeStr1").datetimebox({
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
						data : {timeStr:formData.timeStr,timeEnd:formData.timeEnd},
						success : function(data){
							if(data.success){
								
							}else{
								$("#timeStr1").datebox('clear');
								$.messager.alert("提示",data.returnMsg);
							}
						}
					});
				}
			});
			$("#timeEnd1").datetimebox({
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
						data : {timeStr:formData.timeStr,timeEnd:formData.timeEnd},
						success : function(data){
							if(data.success){
								
							}else{
								$("#timeEnd1").datebox('clear');
								$.messager.alert("提示",data.returnMsg);
							}
						}
					});
				}
			});
	}
});
