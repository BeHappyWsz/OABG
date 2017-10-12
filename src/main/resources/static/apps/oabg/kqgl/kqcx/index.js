$(function(){
	var URL = {
			gridData : ctx + "/oabg/kqgl/kqcx/grid",
	};
	
	/** 模块名称* */
	var modelName = "kqcx";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	
	/** 主页查询表单* */
	var queryForm = $("#" + modelName + "_qf");
	/** 渲染Grid* */
	renderGrid();
	/** 绑定Grid查询按钮方法* */
	bindSearchBar();
	
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			rownumbers :true,
			columns:[[
				/*{field:"id",}, */
			    {field:"name",title:"姓名",align:"left",halign:"center",width:"100"},
			    {field:"bm",title:"部门",align:"left",halign:"center",width:"100"},
			    {field:"qjlx",title:"请假类型",align:"center",halign:"center",width:"150"},
			    {field:"ccaddress",title:"出差地点",align:"center",halign:"center",width:"100"}
			]],
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
	
});