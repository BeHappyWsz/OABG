$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/kqgl/ccsq/save",
		getinfo : ctx + "/oabg/kqgl/ccsq/info",
		checkoutStrtime :ctx + "/oabg/kqgl/ccsq/checkoutStrtime",
		checkoutendtime :ctx + "/oabg/kqgl/ccsq/checkoutendtime",
		lxr: ctx + "/oabg/kqgl/ccsq/lxr"
	};
	var ccryId = $("#ccsq_form_sqrid").val();
	$("#ccsq_form_ccry").val(ccryId);
	/** 模块名称* */
	var modelName = "ccsq";
	var grid = $("#" + modelName + "_grid");
	/** 表单* */
	var form = $("#" + modelName + "_form");
	/*info();*/
	open();
	/** 绑定按钮事件* */
	bindButtonAction();
	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=ccsq-save]").bind("click", function() {
			save();
		});
		
		$("#"+modelName+"_form_ccrname").bind("click", function() {
		var ccry = $("#ccsq_form_ccry").val();
		var sqrid = $("#ccsq_form_sqrid").val();
		var ccrysz = new Array();
		var dataid = "";
		if(ccry.length>0){
			ccrysz = ccry.split(",");
			for(var i=0;i<ccrysz.length;i++){
				if(ccrysz[i]!=sqrid){
					dataid+=ccrysz[i]+",";
				}
				
			}
		}
			openLxr(dataid);
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
			});
		}
	}
		/*function info(){
			var formData = form.serializeObject({transcript:"overlay"});
			console.log(formData);
			if(formData.id==null || formData.id==""){
				$.ajax({
					type : "post",
					url : URL.getinfo,
					success : function(value) {
						console.log(value);
						form.form('load',{ name:value.name});
						form.form('load',{ lxfs:value.lxfs});
						$("#bm").combobox("setValue",value.bmCode);
						$("#bm").combobox("setText",value.bmName);
						$("#lxfs").textbox("setValue",value.lxfs);
					}
				});
			}
		}*/
	/*
	 * 校验请假起始时间
	 * */
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
	
	/*
	 * 校验请假起始时间
	 * */
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
	/**设置多人出差*/
	function open(){
		$('#checkbox').click(function(){
	        if($('#checkbox').prop("checked"))
	        {	
	            $("#trccname").show();
	            $.parser.parse('#trccname'); 
	            $("#ccrname").textbox({
					required:true
				})
	        }
	        else{
	        	$("#trccname").hide();
	        	$("#ccrname").textbox("setValue","");
	        	$("#ccrname").textbox({
					required:false
				});}
	    });
	}
	
	function openLxr(id){
		var win = $("<div id='" + modelName + "_lxr'></div>").window({
			title : "联系人",
			width : 600,
			height :400,
			href :URL.lxr,
			onLoad : function(){
				$("#dataid").val(id);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	$("#ccrname").textbox({
		icons: [{
		iconCls:'icon-combo-clear',
			handler: function(e){
				$(e.data.target).textbox('setValue', '');
				$("#ccsq_form_ccry").val("");
				$("#ccrname").val("");
			}
		}]
	});
	
	
	
	
});
