$(function(){
	var URL = {
			gridData : ctx + "/oabg/kqgl/ccsq/lxrgrid",
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
	bindButtonAction()
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			idField:"id",
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"realname",title:"姓名",align:"left",halign:"center",width:"24%"},
			    {field:"xbName",title:"性别",align:"center",halign:"center",width:"24%"},
			    {field:"bmdwName",title:"工作部门",align:"left",halign:"center",width:"25%"},
			    {field:"gzzwName",title:"工作职位",align:"center",halign:"center",width:"25%"},
			]],
			onLoadSuccess:function(){
				var data = grid.datagrid("getData");
				var checkids = $("#dataid").val();
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
		$("a[data-action=ccsq-okk]").bind("click", function() {
			data();
			queren();
		});
	}
	
	var names = $("#ccrname").val();
	if(names.length>0){
		names+=","
	}
	
	var ccryId = $("#ccsq_form_sqrid").val();
	var value=$("#ccsq_form_ccry").val();
	if(value.length>0){
		value+=",";
	}
	function data(){
		var data = grid.datagrid("getData");
		for(var i=0;i<data.rows.length;i++){
			if(value.length>0){
				if(value.indexOf(data.rows[i].id)!=-1){
					value=value.replace(data.rows[i].id+",", "")
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
			if(names!=null&&names!=""){
				for(var i=0; i<rows.length; i++){
					if(names.indexOf(rows[i].realname)==-1){
						names+=rows[i].realname+",";
						value+=rows[i].id+",";
					}
				}
				value+=ccryId;
				names=names.substring(0,names.length-1);
			}else{
				for(var i=0; i<rows.length; i++){
					names+=rows[i].realname+","; 
					value+=rows[i].id+",";
				}
				value+=ccryId;
				names=names.substring(0,names.length-1);
			}
			
			$("#ccsq_lxr").window("close");
			$('#ccsq_form').form('load',{ ccrname:names});
			$('#ccsq_form').form('load',{ ccry:value});
			//$("#hytz_form").append("<input type='hidden' name='sjr' value='"+idv+"'");
		}else{
			$.messager.alert("提示","请选择需要添加的联系人！");
		}
	}
	
	
})