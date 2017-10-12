$(function(){
	var URL = {
			gridData : ctx + "/oabg/wddb/dy/grid",
			
			formWin : ctx + "/oabg/wddb/dy/form",
			
			getInfo : ctx + "/oabg/wddb/dy/getInfo",
			
			/** 保存表单**/
			close : ctx + "/oabg/wddb/dy/close"
	};
	
	/** 模块名称* */
	var modelName = "wddy";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	
	/** 主页查询表单* */
	var queryForm = $("#" + modelName + "_qf");
	
	/** 渲染Grid* */
	renderGrid();
	
	bindSearchBtns();
	
	
	
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			onDblClickRow : function(index,row) {
					openFormWin(row.id,row.sjly,row.glid);
			},
			columns:[[
			    {field:"state",title : "是否阅读",width:100,align : "center",hidden:true},
			    {field:"id",checkbox:true}, 
			    {field:"fwrName",title:"发文人",align:"left",halign:"center",width:"200px",
			    	styler: function(value,row,index){
			    		if(row.state=="2"){
			    			return 'font-weight:bolder';
			    		}else if(row.state=="1"){
			    			return 'font-weight:normal';
			    		}
			    	}
			    },
			    
			    {field:"bt",title:"标题",align:"left",halign:"center",width:"400px",
			    	styler: function(value,row,index){
			    		if(row.state=="2"){
			    			return 'font-weight:bolder';
			    		}else if(row.state=="1"){
			    			return 'font-weight:normal';
			    		}
			    	}
			    },
			    {field:"fwsj",title:"发文时间",align:"center",halign:"center",width:"120px"},
			    {field:"ydsj",title:"阅读时间",align:"center",halign:"center",width:"250px"},
			    {field:"sjly",title:"数据来源",align:"center",halign:"center",width:"250px",hidden:true},
			    {field:"glid",title:"关联id",align:"center",halign:"center",width:"250px",hidden:true}
			]]
		})
	}
	
	
	
	function bindSearchBtns(){
		$("#" + modelName + "_query").unbind().bind("click",function(){
			var formData = queryForm.serializeObject({transcript:"overlay"});
			grid.datagrid("load",formData);
		});
		$("#" + modelName + "_clear").unbind().bind("click",function(){
			queryForm.form("clear");
		});
	}
	
	/**定义全局变量**/
	var wh = {
			1 : {
				width : 710,
				height : 670,
				formWinId : "fwng_formWin",
				form:"wddy_form"
			},
			2 : {
				width : 710,
				height : 410,
				formWinId : "swng_lookFormWin",
				form:"swng_lookForm"
			},
			3 : {
				width : 900,
				height : 600,
				formWinId : "hytz_formWin",
				form:"hytz_form"
			}
	};
	
	/**跳转到新增/修改页面**/
	function openFormWin(id,sjly,glid){
		var win = $("<div id='" + wh[sjly].formWinId+"'></div>").window({
			title : id ? "修改/查看" : "新增",
			width :  wh[sjly].width,
			height : wh[sjly].height,
			href :URL.formWin+"?id="+id+"&sjly="+sjly+"&glid="+glid,
			onLoad : function(){
				if('2'==sjly){
					window.editor1=KindEditor.create('##oabg_swng_zw',{});
					$("a[data-action=swng_save]").addClass("hide");
				}else if('3'==sjly){
					$("a[data-action=hytz-save]").addClass("hide");
					$("a[data-action=hytz-cross]").addClass("hide");
				}
	        	if(id){
					formLoadData(id,sjly,glid);
					close(id,sjly);
				}
	        	setTimeout(function(){
	        		renderdbsx();
	        	},800);
	        	
			},
			onBeforeClose: function () {
				if('2'==sjly){
					KindEditor.remove('#oabg_swng_zw');
				}
                
            },
			onClose : function() {
				if('2'==sjly){
					window.editor1.remove();
				}
				win.window('destroy');
			}
		});
	} 
	
	
	/**
	 * 编辑页面获取信息
	 */
	function formLoadData(id,sjly,glid){
		$.ajax({
			type : "get",
			dataType : "json",
			url :URL.getInfo,
			data : {id:id,sjly:sjly,glid:glid},
			success : function(data){
				$("#"+wh[sjly].form).form("load", data);
				if('1'==sjly){
					$("#oabg_wddy_zw").html(data.zw);
				}else if('2'==sjly){
					KindEditor.html("#oabg_swsh_zw",data.zw);
				}else if('3'==sjly){
					KindEditor.html("#test-easyui-kindeditorbox",data.huiyitext);
				}
				closeLoadingDiv();
				
			}
		});
	}

	function close(id,sjly) {
		$.ajax({
			type : "get",
			dataType : "json",
			url : URL.close,
			data :{id:id},
			success : function(data) {
				$.messager.progress("close");
				if (data.success) {
					$("#" + modelName + "_grid").datagrid("reload");
				}
			}
		});
	}
	
	

});