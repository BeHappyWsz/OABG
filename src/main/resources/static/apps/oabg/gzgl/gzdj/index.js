/*$(function(){
	var URL = {
			gridData : ctx + "/oabg/gzgl/gzdj/djgrid",
			formWin : ctx + "/oabg/gzgl/gzdj/djform",
			getInfo : ctx + "/oabg/gzgl/gzdj/djInfo",
			deleteByIds : ctx +"/oabg/gzgl/gzdj/deleteByDjIds",
			QueryWin : ctx + "/oabg/gzgl/gzdj/query"
			
	};
	
	*//** 模块名称* *//*
	var modelName = "gzdj";
	*//** 主页列表* *//*
	var grid = $("#" + modelName + "_grid");
	
	*//** 主页查询表单* *//*
	var queryForm = $("#" + modelName + "_qf");
	*//** 渲染Grid* *//*
	renderGrid();
	*//** 绑定Grid操作按钮方法* *//*
	bindGridToorBar();
	*//** 绑定查询表单按钮方法* *//*
	bindSearchBar()
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			onDblClickRow : function(index,row) {
					openFormWin(row.id);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"gzname",title:"公章名称",align:"left",halign:"center",width:"250"},
			    {field:"glyname",title:"公章管理员",align:"left",halign:"center",width:"200"},
			    {field:"titles",title:"联系方式",align:"center",halign:"center",width:"250"},
			   
			]]
		})
	}
	
	*//** 绑定Grid操作按钮方法* *//*
	function bindGridToorBar() {

		$("a[data-action='gzdj-add']").bind("click",function(){
			openFormWin();
		});
		$("a[data-action='gzdj-update']").bind("click",function(){
			var row = gridSelectedValid(grid);
			console.log(row);
			if(row){
				openFormWin(row.id);
			}
		});
		$("a[data-action='gzdj-delete']").bind("click",function(){
			deleteByIds();
		});
		$("a[data-action='gzdj-query']").bind("click",function(){
			openQueryWin();
		});
	}
	*//** 绑定查询表单按钮方法* *//*
	function bindSearchBar() {
		*//** 查询按钮 * *//*
		$("#" + modelName + "_query").bind("click", function() {
			var formData = queryForm.serializeObject();
			grid.datagrid('reload',formData);
		});
		*//** 清空按钮 * *//*
		$("#" + modelName + "_clear").bind("click", function() {
			queryForm.form("clear");
		});
	}
	*//**跳转到新增/修改页面**//*
	function openFormWin(id){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : id ? "查看/编辑" : "登记",
			width : 350,
			height : 150,
			href :URL.formWin,
			onLoad : function(){
				closeLoadingDiv();
	        	if(id){
					formLoadData(id);
				}
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	*//**
	 * 编辑页面获取信息
	 *//*
	function formLoadData(id){
		$.ajax({
			type : "get",
			url :URL.getInfo,
			data : {id:id},
			success : function(data){
				$("#" + modelName + "_form").form("load", data);
			}
		});
	}
	
	*//**
	 * 删除
	 *//*
	function deleteByIds(){
		var ids=gridCheckedValid(grid);
		if(ids){
			$.messager.confirm('提示','您确认想要删除吗？',function(r){    
			    if(r){
			    	$.ajax({
						type : "post",
						url :URL.deleteByIds,
						data : {ids:ids},
						success : function(data) {
							*//** 显示后台返回的提示信息* *//*
							showMsg(Msg.delSuc);
							*//** 如果删除成功，刷新grid数据，关闭窗口* *//*
							if (data.success) {
								$.messager.progress('close');
								grid.datagrid('reload');
							}else{
								$.messager.show({
									title:'提示',
									msg: data.returnMsg,
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
	*//**
	 * 查询公章使用情况
	 *//*
	function openQueryWin(){
		var win = $("<div id='" + modelName + "_QueryWin'></div>").window({
			title : "公章查询",
			width :590,
			height : 550,
			href :URL.QueryWin,
			onLoad : function(){
				closeLoadingDiv();
			},onClose : function() {
				win.window('destroy');
			}
		});
	}
});*/