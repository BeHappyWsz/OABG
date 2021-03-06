$(function(){
	var URL = {
			gridData : ctx + "/oabg/swng/bmgrid",
	};
	
	/** 模块名称* */
	var modelName = "swng_bm";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	
	/** 主页查询表单* */
	var queryForm = $("#" + modelName + "_qf");
	
	/** 渲染Grid* */
	renderGrid();
	/** 绑定Grid查询按钮方法* */
	closeLoadingDiv();
	bindSearchBar();
	/** 绑定确认事件* */
	bindButtonAction()
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			idField:"id",
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"bmdwName",title:"工作部门",align:"left",halign:"center",width:"30%"},
			    {field:"realname",title:"姓名",align:"left",halign:"center",width:"30%"},
			]],
			onLoadSuccess:function(){
				var data = grid.datagrid("getData");
				var checkids = $("#swng_swclForm_sjr").val();
				if(checkids.length!=0){
					for(var i= 0;i<data.rows.length;i++){
						var id=data.rows[i].username;
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
		$("a[data-action=swng_swcl_bmxz]").bind("click", function() {
			data();
			queren();
		});
	}
	
	var modelName1 = "swng_swclForm";
	
	var sjr = $("#"+modelName1+"_sjr");
	
	var names=$("#"+modelName1+"_username").val();
	var sjrid=sjr.val();
	
	if(names.length>0){
		names+=",";
	}
	if(sjrid.length > 0){
		sjrid +=",";
	}
	var form = $("#"+modelName1);
	
	function data(){
		var data = grid.datagrid("getData");
		for(var i=0;i<data.rows.length;i++){
			if(sjrid.length>0){
				if(sjrid.indexOf(data.rows[i].username)!=-1){
					sjrid=sjrid.replace(data.rows[i].username+",", "")
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
		if(rows.length > 0){
			if(names!= null && names != ""){
				for(var i=0; i<rows.length; i++){
					if(names.indexOf(rows[i].realname ) == -1){
						names += rows[i].realname+",";
						if(sjrid.indexOf(rows[i].username) == -1){
							sjr.val(rows[i].username);
							sjrid+=sjr.val()+",";
						}
					}
				}
				names=names.substring(0,names.length-1);
				sjrid=sjrid.substring(0,sjrid.length-1);
			}else{
				for(var i=0; i<rows.length; i++){
					names+=rows[i].realname+","; 
					if(sjrid.indexOf(rows[i].username)==-1){
						sjr.val(rows[i].username);
						sjrid+=sjr.val()+",";
					}
				}
				names=names.substring(0,names.length-1);
				sjrid=sjrid.substring(0,sjrid.length-1);
			}
			$("#swng_swclForm_bmlxr").window("close");
			form.form('load',{name:names,sjr:sjrid});
		}else{
			showMsg("请选择");
		}
	}
})