$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/kqgl/qjsq/save",
		getinfo : ctx + "/oabg/kqgl/qjsq/info",
		checkoutStrtime :ctx + "/oabg/kqgl/qjsq/checkoutStrtime",
		checkoutendtime :ctx + "/oabg/kqgl/qjsq/checkoutendtime",
	};

	/** 模块名称* */
	var modelName = "qjsq";
	var grid = $("#" + modelName + "_grid");
	/** 表单* */
	var form = $("#" + modelName + "_form");
	/** 绑定按钮事件* */
	bindButtonAction();
	jytime();
	/*info();*/
	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=qjsq-save]").bind("click", function() {
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
			})
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
		/**
		 * 新增回填基本信息
		 * *//*
			function info(){
				var formData = form.serializeObject({transcript:"overlay"});
				console.log(formData);
				if(formData.id==null||formData.id==""){
					$.ajax({
						type : "post",
						url : URL.getinfo,
						success : function(data) {
							form.form('load',{ name:data.name});
							form.form('load',{ lxfs:data.lxfs});
							$("#bm").combobox("setValue",data.bmCode);
							$("#bm").combobox("setText",data.bmName);
							$("#lxfs").textbox("setValue",data.lxfs);
						}
					});
				}
			}
				
	*/
	
});
