$(function(){
	var URL = {
			gridData : ctx + "/oabg/wdjy/wdGrid",
	};
	
	/** 模块名称* */
	var modelName = "wdjy_wdform";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	
	/** 主页查询表单* */
	var queryForm = $("#" + modelName + "_qf");
	
	/** 渲染Grid* */
	renderGrid();
	/** 绑定Grid查询按钮方法* */
	bindSearchBar();
	/** 绑定确认事件* */
	bindButtonAction();
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"bt",title:"文档标题",align:"left",halign:"center",width:"20%"},
			    {field:"dabh",title:"文档编号",align:"left",halign:"center",width:"20%"},
			    {field:"gdlx",title:"文档类型",align:"left",halign:"center",width:"20%"},
			]]
		})
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
	
	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=wdjy_wdform_xz]").bind("click", function() {
			queren();
		});
	}
	var modelName1 = "wdjy";
	var form = $("#"+modelName1+"_form");
	
	function queren(){
		var rows = grid.datagrid("getChecked");
		if(rows.length == 1){
			form.form('load',{wdbt:rows[0].bt,wdId:rows[0].id,wdbh:rows[0].dabh});
			$("#wdjy_wd").window("close");
		}else{
			showMsg("请选择一条数据");
		}
	}
});