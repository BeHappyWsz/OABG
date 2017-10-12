$(function(){
	var URL = {
			gridData : ctx + "/oabg/hytz/lxcgryrid",
	};
	
	/** 模块名称* */
	var modelName = "lxcgry";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	
	/** 主页查询表单* */
	var queryForm = $("#" + modelName + "_qf");
	var bmc = $("#bmc").val();
	/** 渲染Grid* */
	renderGrid();
	/** 绑定Grid查询按钮方法* */
	bindSearchBar();
	/** 绑定确认事件* */
	bindButtonAction();
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData+"?bmc="+bmc,
			idField:"id",
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"realname",title:"姓名",align:"left",halign:"center",width:"24%"},
			    {field:"xbName",title:"性别",align:"center",halign:"center",width:"24%"},
			    {field:"bmdwName",title:"工作部门",align:"left",halign:"center",width:"25%"},
			    {field:"gzzwName",title:"工作职位",align:"center",halign:"center",width:"25%"}
			]],
			/*loadFilter :function(data){
				var checkids = $("#lxcgry_names").val();
				if(checkids.length!=0){
					for(var i= 0;i<data.rows.length;i++){
						var id=data.rows[i].realname;
						if(checkids.indexOf(id)!=-1){
							data.rows[i].checked = true;
						}
					}
				}
				return data;
			}*/
			onLoadSuccess:function(){
				var data = grid.datagrid("getData");
				var checkids = $("#lxcgry_names").val();
				if(checkids.length!=0){
					for(var i= 0;i<data.rows.length;i++){
						var id=data.rows[i].realname;
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
		$("a[data-action=hytz-qr]").bind("click", function() {
			data();
			queren();
		});
	}
	var fa_chrname = $("#fa_chrname").val();
	if(fa_chrname.length>0){
		fa_chrname+=",";
	}
	function data(){
		var data = grid.datagrid("getData");
		for(var i=0;i<data.rows.length;i++){
			if(fa_chrname.length>0){
				if(fa_chrname.indexOf(data.rows[i].realname)!=-1){
					fa_chrname=fa_chrname.replace(data.rows[i].realname+",", "")
				}
			}
		}	
	}
	function queren(){
		var rows = grid.datagrid("getChecked");
		if(rows.length > 0){
			for(var i=0; i<rows.length; i++){
				if(fa_chrname.indexOf(rows[i].realname)==-1){
					fa_chrname+=rows[i].realname+",";
				}
			}
			fa_chrname=fa_chrname.substring(0,fa_chrname.length-1);
			$("#fk_lxr").window("close");
			$('#fk_form').form('load',{ chjtry:fa_chrname});
		}else{
			$.messager.alert("提示","请选择需要添加的联系人！");
		}
	}
	
	
})