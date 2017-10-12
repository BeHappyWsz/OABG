$(function(){
	var URL = {
			gridData : ctx + "/oabg/hytz/grid",
			formWin : ctx + "/oabg/hytz/form",
			getInfo : ctx + "/oabg/hytz/getInfo",
			deleteByIds : ctx +"/oabg/hytz/deleteByIds",
			fkWin : ctx +"/oabg/hytz/fkForm",
			Win : ctx +"/oabg/hytz/Win",
			getHytz : ctx + "/oabg/hytz/hytzxx",
			fkqkWin : ctx +"/oabg/hytz/fkqkForm",
			getfkqk : ctx + "/oabg/hytz/fkzxx",
	};
	/** 模块名称* */
	var modelName = "hytz";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	/** 主页查询表单* */
	var queryForm = $("#" + modelName + "_qf");
	/** 当前登录用户名* */
	var name=$("#" + modelName + "_name").val();
	/** 渲染Grid* */
	renderGrid();
	/** 绑定Grid查询按钮方法* */
	bindSearchBar();
	/** 绑定Grid操作按钮方法* */
	bindGridToorBar();
	var ishide = false;
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			onDblClickRow : function(index,row) {
					openWin(row.id,row.hyfk,row.fjr);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"titles",title:"标题",align:"left",halign:"center",width:"200"},
			    {field:"huiyiaddress",title:"会议地址",align:"left",halign:"center",width:"200"},
			    {field:"huiyitime",title:"会议时间",align:"center",halign:"center",width:"150"},
			    {field:"fjr",title:"发件人",align:"center",halign:"center",width:"100"},
			    {field:"hyfk",title:"是否参加",align:"center",halign:"center",width:"100"},
			    {field:"cz",title:"操作",align:"center",halign:"center",width:"120",formatter : renderButton}
			]],
			 onLoadSuccess : function() {
          	 	$(".fk").linkbutton({
          	 		iconCls : "icon-standard-database-refresh"
          	 	});
          	 	$(".cj").linkbutton({
          	 		iconCls : "icon-standard-database-refresh"
          	 	});
          	 	$(".fk").bind("click", function() {
          	 		var id = $(this).data("rowid");
          	 		Hyztxx(id);
          	 	});
          	 	$(".cj").bind("click", function() {
          	 		var id = $(this).data("rowid");
          	 		fkqk(id);
          	 	});
           }
		})
	}
	
	/** 绑定Grid操作按钮方法* */
	function bindGridToorBar() {

		$("a[data-action='hytz-add']").bind("click",function(){
			openFormWin();
		});
		
		$("a[data-action='hytz-update']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				openWin(row.id);
			}
		});
	}
	
	/** 绑定查询表单按钮方法* */
	function bindSearchBar() {
		/** 查询按钮 * */
		$("#" + modelName + "_query").bind("click", function() {
			var formData = queryForm.serializeObject();
			grid.datagrid('reload',formData);
		});
		/** 清空按钮 * */
		$("#" + modelName + "_clear").bind("click", function() {
			queryForm.form("clear");
		});
	}
	
	/**跳转到新增/修改页面**/
	function openFormWin(){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title :  "新增",
			width : 900,
			height : 642,
			href :URL.formWin,
			onLoad : function(){				
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	/**跳转到新增/修改页面**/
	function openWin(id,hyfz,fjr){
		var win = $("<div id='" + modelName + "_mWin'></div>").window({
			title : "查看",
			width : 750,
			height : 600,
			href :URL.Win+"?id="+id,
			onLoad : function(){
				if(hyfz!=null&&hyfz!=""){
					$("a[data-action='hytz-fkk']").addClass("hide");	
				}else if(fjr==name){
					$("a[data-action='hytz-fkk']").addClass("hide");	
				}
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	/**
	 * 编辑页面获取信息
	 */
	function formLoadData(id){
		$.ajax({
			type : "get",
			dataType : "json",
			url :URL.getInfo,
			data : {id:id},
			success : function(data){
				$("#" + modelName + "_form").form("load", data);
				KindEditor.html("#test-easyui-kindeditorbox",data.huiyitext);
			}
		});
	}
	
	/**
	 * 删除
	 *//*
	function deleteByIds(){
		var ids=gridCheckedValid(grid);
		if(ids){
			$.messager.confirm('提示','您确认想要删除吗？',function(r){    
			    if(r){
			    	$.ajax({
						type : "post",
						dataType : "json",
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
								showMsg(data.returnMsg);
							}
						}
					});
			    }
			});
		}
	}*/
	function renderButton(value,row,index){
		var btn = "";
		if(row.fjr != name && row.hyfk == null){//反馈
			btn += "<a class='fk' data-rowid='"+row.id+"'>反馈</a>";
		}else if(row.fjr == name && row.hyfk == null){
			btn += "<a class='cj' data-rowid='"+row.id+"'>反馈情况</a>";
		}else if(row.fjr != name && row.hyfk != null){
			btn = "";
		}
		return btn;
	}
	
	/**反馈页面**/
	function Hyztxx(id){
		var win = $("<div id='" + modelName + "_fkWin'></div>").window({
			title : "反馈信息",
			width : 500,
			height : 218,
			href :URL.fkWin+"?id="+id,
			onLoad : function(){				
				formHytz(id);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	/**
	 * 获取会议通知信息
	 */
	function formHytz(id){
		$.ajax({
			type : "get",
			dataType : "json",
			url :URL.getHytz,
			data : {idd:id},
			success : function(data){
				/*$("#huiyitime1").textbox("disable");
				$("#huiyitime1").textbox("setValue",data.huiyitime);
				$("#hytz_address1").textbox("disable");
				$("#hytz_address1").textbox("setValue",data.hytz_address);
				$("#titles1").textbox("disable");
				$("#titles1").textbox("setValue",data.titles);
				$("#fk_form").form("load", data);*/
				
			}
		});
	}
	
	/**反馈情况页面**/
	function fkqk(id){
		var win = $("<div id=hytz_fkqkWin'></div>").window({
			title : "反馈情况",
			width :650,
			height : 300,
			href :URL.fkqkWin+"?id="+id,
			onLoad : function(){				
				getFkqk(id);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	/**
	 * 反馈情况信息
	 */
	function getFkqk(id){
		$.ajax({
			type : "get",
			url :URL.getfkqk,
			data : {idd:id},
			success : function(data){
				/*$("#fkqk_huiyitime").textbox("disable");
				$("#fkqk_huiyitime").textbox("setValue",data.huiyitime);
				$("#hytz_address").textbox("disable");
				$("#hytz_address").textbox("setValue",data.hytz_address);
				$("#titles").textbox("disable");
				$("#titles").textbox("setValue",data.titles);*/
				$("#fkqk_form").form("load", data);
				
			}
		});
	}
	
	
});