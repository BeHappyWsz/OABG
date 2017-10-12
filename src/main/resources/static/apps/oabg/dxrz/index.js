$(function(){
	var URL = {
			gridData : ctx + "/oabg/dxrz/grid",
			formWin : ctx + "/oabg/dxrz/form"
	};
	
	/** 模块名称* */
	var modelName = "dxrz";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	
	/** 主页查询表单* */
	var queryForm = $("#" + modelName + "_qf");
	
	/** 渲染Grid* */
	renderGrid();
	
	/** 绑定Grid操作按钮方法* */
	bindGridToorBar();
	
	/** 绑定Grid操作按钮方法* */
	bindSearchBtns();
	
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			onDblClickRow : function(index,row) {
					openFormWin(row.id);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"jsr",title:"接收人",align:"left",halign:"center",width:150},
			    {field:"tel",title:"手机号",align:"center",halign:"center",width:150},
			    {field:"fssj",title:"发送时间",align:"center",halign:"center",width:150},
			    {field:"fszt",title:"发送状态",align:"center",halign:"center",width:120,formatter:function(value,row,index){
			    	if(1 == value || "1" == value){
			    		return "<span style='color:green'>"+row.fsztName+"</span>";
			    	}else{
			    		return "<span style='color:red'>"+row.fsztName+"</span>";
			    	}
			    }},
			    {field:"fsyy",title:"发送原因",align:"left",halign:"center",width:200}
			    
			]]
		});
	}
	
	/** 绑定Grid操作按钮方法* */
	function bindGridToorBar() {
	}
	
	function bindSearchBtns(){
		$("#"+modelName+"_query").unbind().bind("click",function(){
			var formData = queryForm.serializeObject({transcript:"overlay"});
			grid.datagrid("load",formData);
		});
		$("#"+modelName+"_clear").unbind().bind("click",function(){
			queryForm.form("clear");
		});
	}
	
	/**跳转到新增/修改页面**/
	function openFormWin(id){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : "短信日志[查看]",
			href :URL.formWin+"?id="+id,
			width : 700,
			height : 217,
			onLoad : function(){				
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
});