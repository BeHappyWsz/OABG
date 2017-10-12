	var MessageURL = {
		/** 保存表单**/
			getNumber : ctx + "/oabg/wddb/getNumber",
		
	};
	
	

//	$(function(){
//		var windowWidth=460;
//		var windowHeight=180;
//		var win = $("<div id='noticeDialogWin' style='text-align:center;font-size:20px;font-weight:bolder;padding-top:22px;'></div>").window({
//			title : "待办事项",
//			width : windowWidth,
//			height : windowHeight,
//			left:document.documentElement.clientWidth-windowWidth,
//			top:document.documentElement.clientHeight-windowHeight,
//			resizable:false,
//			modal:false,
//			draggable:false,
//			content:"<div id='noticeDialog'></div>",
//		    onOpen:function(){
//		    	//待办
//	    		renderdbsx();
//		    },
//			onClose : function() {
//				win.window('destroy');
//			}
//		});
//	});
	
	function renderdbsx(){
		$.ajax({
			type : "get",
			dataType : "json",
			url : MessageURL.getNumber,
			success : function(data) {
				if (data.success) {

					$("#noticeDialog").html('<div style="cursor:pointer" id="dybtn">您有<span style="color:red">'+data.data.dy+'</span>条待阅文件提醒</div>'+"<br/>"+'<div style="cursor:pointer" id="dbbtn">您有<span style="color:red">'+data.data.db+'</span>条待办工作提醒</div>');
					
					var dybtn = $("#dybtn");
					var dbbtn = $("#dbbtn");
					//绑定事件
					dybtn.bind('click',function(){
						$("#_easyui_tree_2").click();
						//$("#wddy_grid").datagrid("load",{state:2});
					});
					
					dbbtn.bind('click',function(){
						$("#_easyui_tree_3").click();
						//$("#wddb_db_grid").datagrid("load",{sfyb:2});
					});
				}
			}
		});
	}
