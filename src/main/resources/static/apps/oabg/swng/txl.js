$(function(){
	var URL = {
			lxr: ctx + "/oabg/swng/lxr",
			
			bm: ctx + "/oabg/swng/bm",
			//局处室
			jcs:ctx+"/oabg/swng/jcs",
			//直属单位
			zsdw:ctx+"/oabg/swng/zsdw",
			//辖市区局
			xsqj:ctx+"/oabg/swng/xsqj"	
	};
	
	var modelName = "swng_swclForm";
	
	var form = $("#"+modelName);
	
	//收件人	快捷标签jcs局处室 zsdw直属单位 xsqj辖市区局 按钮
	var sjrBtn  = $("#"+modelName+"_sjrBtn");
	
	var bmBtn  = $("#"+modelName+"_bmBtn");
	
	var jcsBtn  = $("#"+modelName+"_jcsBtn");
	
	var zsdwBtn = $("#"+modelName+"_zsdwBtn");
	
	var xsqjBtn = $("#"+modelName+"_xsqjBtn");
	
	//保存信息
	var username = $("#"+modelName+"_username");
	var sjr = $("#"+modelName+"_sjr");
	
	bindButton();
	
	//textbox点击事件
	username.textbox({
		icons: [{
		iconCls:'icon-combo-clear',
			handler: function(e){
				$(e.data.target).textbox('setValue', '');
				names="";
				sjr.val("");
				jcsBtn.show();
				zsdwBtn.show(); 
				xsqjBtn.show();
			}
		}]
	})
	
	function bindButton(){
	
		sjrBtn.bind("click",function(){
			openLxr();
		});
		
		bmBtn.bind("click",function(){
			openBm();
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
			form.form('load',{name:names.substring(0,names.length-1)});
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
			form.form('load',{name:names.substring(0,names.length-1)});
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
			form.form('load',{name:names.substring(0,names.length-1)});
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
	function openLxr(){
		var win = $("<div id='" + modelName + "_lxr'></div>").window({
			title : "联系人",
			width : 700,
			height :530,
			href :URL.lxr,
			onLoad : function(){	
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	/**部门联系人*/
	function openBm(){
		var win = $("<div id='" + modelName + "_bmlxr'></div>").window({
			title : "部门联系人",
			width : 650,
			height :400,
			href :URL.bm,
			onLoad : function(){	
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
});
