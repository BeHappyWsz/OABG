$(function(){
	var URL = {
			gridData : ctx + "/oabg/hytz/lxrgrid",
	};
	
	/** 模块名称* */
	var modelName = "lxr";
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
			idField:"id",
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"realname",title:"姓名",align:"left",halign:"center",width:"200"},
			    {field:"bmdwName",title:"工作部门",align:"left",halign:"center",width:"200"}
			]],
			/*loadFilter :function(data){
				var checkids = $("#lxrID").val();
				if(checkids.length!=0){
					for(var i= 0;i<data.rows.length;i++){
						var id=data.rows[i].id;
						if(checkids.indexOf(id)!=-1){
							data.rows[i].checked = true;
						}
					}
				}
				return data;
			}*/
			onLoadSuccess:function(){
				var data = grid.datagrid("getData");
				var checkids = $("#lxrID").val();
				if(checkids.length!=0){
					for(var i= 0;i<data.rows.length;i++){
						var id=data.rows[i].id;
						if(checkids.indexOf(id)!=-1){
							grid.datagrid('checkRow', i);
						}
					}
				}
			}
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
		$("a[data-action=hytz-ok]").bind("click", function() {
			data();
			queren();
			
		});
	}
	var names=$("#hytz_username").val();
	var sjrid = $("#sjr").val();
	if(names.length>0){
		names+=",";
	}
	if(sjrid.length>0){
		sjrid+=",";
	}
	function data(){
		var data = grid.datagrid("getData");
		for(var i=0;i<data.rows.length;i++){
			if(sjrid.length>0){
				if(sjrid.indexOf(data.rows[i].id)!=-1){
					sjrid=sjrid.replace(data.rows[i].id+",", "")
				}
			}
			if(names.length>0){
				if(names.indexOf(data.rows[i].realname)!=-1){
					names=names.replace(data.rows[i].realname+",", "")
				}
			}
		}	
	}
	
	
	function queren(){
		var rows = grid.datagrid("getChecked");
		var value=sjrid+$("#fjrid").val()+",";
		if(rows.length > 0){
			if(names!=null&&names!=""){
				for(var i=0; i<rows.length; i++){
					if(names.indexOf(rows[i].realname)==-1){
						names+=rows[i].realname+",";
						value+=rows[i].id+",";
					}
					
				}
				names=names.substring(0,names.length-1);
				value=value.substring(0,value.length-1);
			}else{
				for(var i=0; i<rows.length; i++){
					names+=rows[i].realname+","; 
					value+=rows[i].id+",";
				}
				names=names.substring(0,names.length-1);
				value=value.substring(0,value.length-1);
			}
			$("#hytz_lxr").window("close");
			$('#hytz_form').form('load',{ name:names});
			$('#hytz_form').form('load',{ sjr:value});
			
		}else{
			$.messager.alert("提示","请选择需要添加的联系人！");
		}
	}
	
})