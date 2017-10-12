$(function(){
	var URL = {
			gridData : ctx + "/oabg/yhgl/grid",
			formWin : ctx + "/oabg/yhgl/form",
			getInfo : ctx + "/oabg/yhgl/getInfo",
			deleteByIds : ctx +"/oabg/yhgl/deleteByIds",
	};
	
	/** 模块名称* */
	var modelName = "yhgl";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	/** 主页查询表单* */
	var queryForm = $("#" + modelName + "_qf");
	
	/** 渲染Grid* */
	renderGrid();
	/** 绑定Grid查询按钮方法* */
	bindSearchBar();
	/** 绑定Grid操作按钮方法* */
	bindGridToorBar();
	
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			onDblClickRow : function(index,row) {
				if(row.isUser == "yes"){
					openFormWin(row.id,'查看/编辑');
				}else{
					showMsg("无法查看非账户信息");
				}
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"username",title:"用户名",align:"left",halign:"center",width:"100px"},
			    {field:"realname",title:"真实姓名",align:"left",halign:"center",width:"150px"},
			    {field:"rolename",title:"角色",align:"left",halign:"center",width:"150px"},
//			    {field:"xbName",title:"性别",align:"center",halign:"center",width:"100px"},
			    {field:"bmdwName",title:"部门单位",align:"left",halign:"center",width:"150px"},
			    {field:"gzzwName",title:"工作职位",align:"left",halign:"center",width:"100px"},
			]]
		})
	}
	
	/** 绑定Grid操作按钮方法* */
	function bindGridToorBar() {

		$("a[data-action='YHGL_ADD']").bind("click",function(){
			openFormWin();
		});
		
		$("a[data-action='YHGL_UPDATA']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				openFormWin(row.id);
			}
		});
		
		$("a[data-action='YHGL_DELETE']").bind("click",function(){
			deleteByIds();
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
	function openFormWin(id,title){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : id ? "修改/查看" : "新增",
			width : 1060,
			height : 190,
			href :URL.formWin,
			onLoad : function(){				
	        	if(id){//查看修改
					formLoadData(id);
					$("#yhgl_username").textbox({
						"disabled":true
					})
				}else{//新增
					$("#yhgl_fgbm").combo({
						disabled : true
					});
					$("#yhgl_pwd").textbox({
						required : true
					});
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
			url :URL.getInfo,
			data : {id:id},
			async:true,
			success : function(data){
				$("#" + modelName + "_form").form("load", data);
				setTimeout(function(){
					if(data.roleCode != "" || data.roleCode != undefined){
						$("#yhgl_role").combobox("setValues",data.roleCode);
					}
					$("#yhgl_fgbm").combotree("setValues",data.fgbm);
				},300);
				
				//局长+分管领导可以进行分管其他部门
				if(data.roleCode.indexOf("FGLD") != -1 || data.roleCode.indexOf("JZ") != -1){
					$("#yhgl_fgbm").combo({
						disabled : false,
					});
				}else{
					$("#yhgl_fgbm").combo({
						disabled : true
					});
				}
			}
		});
	}
	
	/**
	 * 删除
	 */
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
							/** 显示后台返回的提示信息* */
							showMsg(Msg.delSuc);
							/** 如果删除成功，刷新grid数据，关闭窗口* */
							if (data.success) {
								$.messager.progress('close');
								grid.datagrid('reload');
							}
						}
					});
			    }
			});
		}
	}
});