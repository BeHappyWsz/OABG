$(function(){
	var URL = {
			lxr: ctx + "/oabg/fwng/lxr?flag=",
			
			bm: ctx + "/oabg/fwng/bm?flag=",
			//局处室
			jcs:ctx+"/oabg/hytz/jcs",
			//直属单位
			zsdw:ctx+"/oabg/hytz/zsdw",
			//辖市区局
			xsqj:ctx+"/oabg/hytz/xsqj"	
	};
	
	var modelName = "fwng";
	
	var form = $("#"+modelName+"_form");
	
	//收件人	快捷标签jcs局处室 zsdw直属单位 xsqj辖市区局 按钮
	var sjrBtn  = $("#"+modelName+"_sjrBtn");
	
	var bmBtn= $("#"+modelName+"_bmBtn");
	
	var jcsBtn  = $("#"+modelName+"_jcsBtn");
	
	var zsdwBtn = $("#"+modelName+"_zsdwBtn");
	
	var xsqjBtn = $("#"+modelName+"_xsqjBtn");
	
	var csBtn=$("#"+modelName+"_csdwBtn");
	
	var csdwBtn=$("#"+modelName+"_csBtn");
	//发件人id+收件人输入框+收件人信息保存
	var fjr= $("#"+modelName+"_uid");
	//sjr的name值
	var username = $("#"+modelName+"_username");
	//sjr的value值
	var sjr = $("#"+modelName+"_sjr");
	
	var names=username.val();
	
	if(names.length>0){
		names+=",";
	}
	
	var sjrid = sjr.val();
	
	bindButton();
	
	//textbox点击事件
	username.textbox({
		icons: [{
		iconCls:'icon-combo-clear',
			handler: function(e){
				$(e.data.target).textbox('setValue', '');
				names="";
				sjr.val("");
				sjrid="";
				jcsBtn.show();
				zsdwBtn.show(); 
				xsqjBtn.show();
			}
		}]
	})
	
	$("#"+modelName+"_cs").textbox({
		icons: [{
		iconCls:'icon-combo-clear',
			handler: function(e){
				$(e.data.target).textbox('setValue', '');
				var csdwid=$("#"+modelName+"_csdw").val("");
				var csname=$("#"+modelName+"cs").val("");
			}
		}]
	});
	
	function bindButton(){
	
		sjrBtn.bind("click",function(){
			openLxr("zs");
		});
		
		csBtn.bind("click",function(){
			openLxr("cs");
		});
		
		bmBtn.bind("click",function(){
			openBm("zs");
		});
		
		csdwBtn.bind("click",function(){
			openBm("cs");
		});
		

		jcsBtn.bind("click",function(){
			var names=username.val();
			var sjrid = sjr.val();
			if(names.length>0){
				names+=",";
			}
			if(sjrid.length>0){
				sjrid+=","
			}
			names+="局处室"+",";
			form.form('load',{zs:names.substring(0,names.length-1)});
			jcsBtn.hide();
			$.ajax({
				type : "post",
				url : URL.jcs,
				success : function(data) {
					for(i=0;i<data.length;i++){
						if(sjrid.indexOf(data[i])==-1){
							sjrid+=data[i]+",";
						}
					}
					sjr.val(sjrid.substring(0,sjrid.length-1));
				}
			});
			
		});
		
		zsdwBtn.bind("click",function(){
			var names=username.val();
			var sjrid = sjr.val();
			if(names.length>0){
				names+=",";
			}
			if(sjrid.length>0){
				sjrid+=","
			}
			names+="直属单位"+",";
			form.form('load',{zs:names.substring(0,names.length-1)});
			zsdwBtn.hide();
			$.ajax({
				type : "post",
				url : URL.zsdw,
				success : function(data) {
					for(i=0;i<data.length;i++){
						if(sjrid.indexOf(data[i])==-1){
							sjrid+=data[i]+",";
						}
					}
					sjr.val(sjrid.substring(0,sjrid.length-1));
				}
			});
			
		});
		
		xsqjBtn.bind("click",function(){
			var names=username.val();
			var sjrid = sjr.val();
			if(names.length>0){
				names+=",";
			}
			if(sjrid.length>0){
				sjrid+=","
			}
			names+="辖市区局"+",";
			form.form('load',{zs:names.substring(0,names.length-1)});
			xsqjBtn.hide();
			$.ajax({
				type : "post",
				url : URL.xsqj,
				success : function(data) {
					for(i=0;i<data.length;i++){
						if(sjrid.indexOf(data[i])==-1){
							sjrid+=data[i]+",";
						}
					}
					sjr.val(sjrid.substring(0,sjrid.length-1));
				}
			});
			
		});
	}
	//收件人弹窗
	function openLxr(flag){
		var win = $("<div id='" + modelName + "_lxr'></div>").window({
			title : "联系人",
			width : 700,
			height :500,
			href :URL.lxr+flag,
			onLoad : function(){
				$("#fwng_lxr_flag").val(flag);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	/**部门联系人*/
	function openBm(flag){
		var win = $("<div id='" + modelName + "_bmlxr'></div>").window({
			title : "部门联系人",
			width : 670,
			height :400,
			href :URL.bm+flag,
			onLoad : function(){	
				$("#fwng_bm_flag").val(flag);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
});
