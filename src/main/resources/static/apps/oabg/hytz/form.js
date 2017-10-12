$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/hytz/save",
		lxr: ctx + "/oabg/hytz/lxr",
		bm: ctx + "/oabg/hytz/bm",
		uploadFileUrl: ctx + "/oabg/hytz/uploadFile",
		jcs:ctx+"/oabg/hytz/jcs",
		zsdw:ctx+"/oabg/hytz/zsdw",
		xsqj:ctx+"/oabg/hytz/xsqj",
		huiyitime:ctx+"/oabg/hytz/huiyitime",
		checkouttime :ctx + "/oabg/hytz/checkouttime",
	};

	/** 模块名称* */
	var modelName = "hytz";

	/** 表单* */
	var form = $("#" + modelName + "_form");

	/** 绑定按钮事件* */
	bindButtonAction();
	/** 绑定快捷按钮* */
	bindSearchBar()
	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=hytz-save]").bind("click", function() {
			save();
		});
		$("a[data-action=hytz-cross]").bind("click", function() {
			 $("#test-easyui-kindeditorbox").kindeditorbox("html",null);
			form.form("clear");
			$("#" + modelName + "_jcs").show();
			$("#" + modelName + "_zsdw").show();
			$("#" + modelName + "_xsqj").show();
		});
		$("#" + modelName + "_sjr").bind("click", function() {
			var sjrid = $("#sjr").val();
			var fjrid = $("#fjrid").val();
			var mycars=new Array()
			var newcars = "";
			if(sjrid.length!=0){
				mycars = sjrid.split(",");
				for(var i =0;i<mycars.length;i++){
					if(mycars[i]!=fjrid){
						newcars+=mycars[i]+",";
					}
				}
			/*	var [] sz = sjrid.split(",");*/
			}
			openLxr(newcars);
		});
		$("#" + modelName + "_bm").bind("click", function() {
			var sjrid = $("#sjr").val();
			var fjrid = $("#fjrid").val();
			var mycars=new Array()
			var newcars = "";
			if(sjrid.length!=0){
				mycars = sjrid.split(",");
				for(var i =0;i<mycars.length;i++){
					if(mycars[i]!=fjrid){
						newcars+=mycars[i]+",";
					}
				}
			/*	var [] sz = sjrid.split(",");*/
			}
			openBm(newcars);
		});
	}
	
	/** 绑定快捷按钮方法* */
	function bindSearchBar() {
		/** 局处室 * */
		var fjrid=$("#fjrid").val();
		$("#" + modelName + "_jcs").bind("click", function() {
			var sjrid = $("#sjr").val();
			var value = $("#hytz_username").val();
			if(value.length>0){
				value+=","
			}
			if(sjrid.length>0){
				sjrid+=","
			}
			value+="局处室"
			$('#hytz_form').form('load',{ name:value});
			$("#" + modelName + "_jcs").hide();
			$.ajax({
				type : "post",
				url : URL.jcs,
				data:"",
				success : function(data) {
					for(i=0;i<data.length;i++){
						if(sjrid.indexOf(data[i])==-1){
							sjrid+=data[i]+",";
						}
					}
					var id= fjrid+","+sjrid;
					id =id.substring(0,id.length-1);
					$("#sjr").val(id);
				}
			});
			
		});
		/** 直属单位 * */
		$("#" + modelName + "_zsdw").bind("click", function() {
			var sjrid = $("#sjr").val();
			var value = $("#hytz_username").val();
			if(sjrid.length>0){
				sjrid+=","
			}
			if(value.length>0){
				value+=","
			}
			value+="直属单位"
				$('#hytz_form').form('load',{ name:value})
			$("#" + modelName + "_zsdw").hide();
			$.ajax({
				type : "post",
				url : URL.zsdw,
				data:"",
				success : function(data) {
					for(i=0;i<data.length;i++){
						if(sjrid.indexOf(data[i])==-1){
							sjrid+=data[i]+",";
						}
					}	
					var id= fjrid+","+sjrid;
					id =id.substring(0,id.length-1);
					$("#sjr").val(id);
				}
			});
		});
		/** 辖市区局 * */
		$("#" + modelName + "_xsqj").bind("click", function() {
			var sjrid = $("#sjr").val();
			var value = $("#hytz_username").val();
			if(value.length>0){
				value+=","
			}
			if(sjrid.length>0){
				sjrid+=","
			}
			value+="辖市区局"
			$('#hytz_form').form('load',{ name:value});
			$("#" + modelName + "_xsqj").hide();
			$.ajax({
				type : "post",
				url : URL.xsqj,
				data:"",
				success : function(data) {
					for(i=0;i<data.length;i++){
						if(sjrid.indexOf(data[i])==-1){
							sjrid+=data[i]+",";
						}
					}
						var id= fjrid+","+sjrid;
						id =id.substring(0,id.length-1);
						$("#sjr").val(id);
				}
			});
		});
	}
	/**
	 * 保存
	 */
	function save() {
		if (form.form("validate")) {
			/** 序列化form* */
			var formData = form.serializeObject({transcript:"overlay"});
			$.messager.confirm("系统提示","是否确认保存？",function(r){
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
							$.messager.show({
								title:'提示',
								msg: "会议时间填写有误",
								timeout:1000,
								showType:'slide'
							});
						}
					}
				});
				}
			});	
		}
	}
	/**局领导联系人*/
	function openLxr(id){
			var win = $("<div id='" + modelName + "_lxr'></div>").window({
				title : "联系人",
				width : 650,
				height :400,
				href :URL.lxr,
				onLoad : function(){	
					$("#lxrID").val(id);
					closeLoadingDiv();
				},
				onClose : function() {
					win.window('destroy');
				}
			});
	}
	/**部门联系人*/
	function openBm(id){
		var win = $("<div id='" + modelName + "_bmlxr'></div>").window({
			title : "部门联系人",
			width : 650,
			height :400,
			href :URL.bm,
			onLoad : function(){	
				$("#bm_qf_id").val(id);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
}
	/**
	 * 根据下拉框显示隐藏表格
	 * */
	$("#sfnote").combobox({
		onSelect:function(data){
			if(data.value== 1){
				$("#note").show();
				$("#notetext").textbox('enable');
			}
			if(data.value== 2){
				$("#notetext").textbox('disable');
				$("#note").hide();
				
			}
		}
	})
	
		/*var value = $("#sfnote").combobox("getValue");
		if(value== 1){
			$("#note").show();
			$("#notetext").textbox('enable');
		}
		if(value== 2){
			$("#notetext").textbox('disable');
			$("#note").hide();
			
		}
	}*/
	
	
	/*
	 * 校验会议时间
	 * */
	$("#huiyitime").datetimebox({
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
				data : {huiyitime:formData.huiyitime},
				success : function(data){
					if(data.success){
						
					}else{
						$("#huiyitime").datebox('clear');
						$.messager.alert("提示",data.returnMsg);
					}
				}
			});
		}
	});
	$("#hytz_username").textbox({
		icons: [{
		iconCls:'icon-combo-clear',
			handler: function(e){
				$(e.data.target).textbox('setValue', '');
				$("#sjr").val("");
				$("#hytz_username").val("");
				$("#" + modelName + "_jcs").show();
				$("#" + modelName + "_zsdw").show();
				$("#" + modelName + "_xsqj").show();
			}
		}]
	})
});
