$(function(){
	var URL = {
			gridData : ctx + "/oabg/swcy/grid",
			
			formWin : ctx + "/oabg/swcy/form"
	};
	
	/** 模块名称* */
	var modelName = "swcy";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	var queryForm = $("#"+modelName+"_qf");
	
	/** 渲染Grid* */
	renderGrid();
	/** 绑定Grid操作按钮方法* */
	bindGridToorBar();
	
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			onDblClickRow : function(index,row) {
					openFormWin(row.id,row.state);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"swrq",title:"收文日期",align:"center",halign:"center",width:"100px",
			    	styler: function(value,row,index){
			    		if(row.state=="2"){
			    			return 'font-weight:bolder';
			    		}else if(row.state=="1"){
			    			return 'font-weight:normal';
			    		}
			    	}
			    },
			    {field:"fwrq",title:"原文日期",align:"center",halign:"center",width:"100px",
			    	styler: function(value,row,index){
			    		if(row.state=="2"){
			    			return 'font-weight:bolder';
			    		}else if(row.state=="1"){
			    			return 'font-weight:normal';
			    		}
			    	}
			    },
			    {field:"fwzh",title:"原文号",align:"left",halign:"center",width:"150px",
			    	styler: function(value,row,index){
			    		if(row.state=="2"){
			    			return 'font-weight:bolder';
			    		}else if(row.state=="1"){
			    			return 'font-weight:normal';
			    		}
			    	}
			    },
			    {field:"fwjg",title:"发文机关",align:"left",halign:"center",width:"100px",
			    	styler: function(value,row,index){
			    		if(row.state=="2"){
			    			return 'font-weight:bolder';
			    		}else if(row.state=="1"){
			    			return 'font-weight:normal';
			    		}
			    	}
			    },
			    {field:"bt",title:"文件标题",align:"left",halign:"center",width:"250px",
			    	styler: function(value,row,index){
			    		if(row.state=="2"){
			    			return 'font-weight:bolder';
			    		}else if(row.state=="1"){
			    			return 'font-weight:normal';
			    		}
			    	}
			    },
			    {field:"files",title:"附件",align:"center",halign:"center",width:"100px",formatter:fjButtons},
			    {field:"wjly",title:"文件来源",align:"left",halign:"center",width:"80px",
			    	styler: function(value,row,index){
			    		if(row.state=="2"){
			    			return 'font-weight:bolder';
			    		}else if(row.state=="1"){
			    			return 'font-weight:normal';
			    		}
			    	}
			    },
			    {field:"ydsj",title:"阅读时间",align:"center",halign:"center",width:"180px"},
			]],
			onLoadSuccess : function() {
				$(".fileBtn").each(function(){
					var fileName = $(this).data("filename");
					var icon = getLxByFileName(fileName);
					$(this).linkbutton({
						iconCls : icon,
						plain : true,
						onClick : function(){
							var url = $(this).data("url");
							doDwonLoad(url);
						}
					});
					$(this).tooltip({    
						position: 'right',    
						content: "<span style='color:#fff'>"+fileName+"</span>",    
						onShow: function(){        
							$(this).tooltip('tip').css({            
								backgroundColor: '#666',            
								borderColor: '#666'        
							});    
						}
					});
				});
			}
		})
	}
	
	//附件下载
	function fjButtons(value,row,index){
		if(row.files.length == 0){
			return;
		}
		var button = "";
		var files = row.files.split("&");
		for (var i=0;i<files.length;i++){
			var file = files[i].split("#");
			button += " <a class='fileBtn' href='#' data-url='"+file[0]+"' data-filename='"+file[1]+"' ></a>";
		}
		return button;
	}
	/**文件下载**/
	function doDwonLoad(url){
		window.open(ctx+url);
	}
	/** 绑定Grid操作按钮方法* */
	function bindGridToorBar() {
		
		$("#"+modelName+"_qf_query").bind("click",function(){
			var formData = queryForm.serializeObject();
			grid.datagrid('reload',formData);
		});
		
		$("#"+modelName+"_qf_clear").bind("click",function(){
			queryForm.form("clear");
		});
		
	}
	
	/**跳转查阅页面**/
	function openFormWin(id,state){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : "查阅",
			width : 710,
			height : 405,
			href :URL.formWin+"?id="+id,
			onLoad : function(){
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
				if(state == 2 || state == "2"){
					grid.datagrid("reload");
				}
			}
		});
	}

});