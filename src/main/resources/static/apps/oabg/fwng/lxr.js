$(function(){
	var URL = {
			gridData : ctx + "/oabg/fwng/lxrgrid"
	};
	
	/** 模块名称* */
	var modelName = "fwng_lxr";
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
			    {field:"bmdwName",title:"工作部门",align:"left",halign:"center",width:"250px"},
			    {field:"realname",title:"名字",align:"left",halign:"center",width:"150px"}
			]],
			onLoadSuccess :function(){
				var data = grid.datagrid("getData");
				var flag=$("#fwng_lxr_flag").val();
				var checkids = $("#fwng_sjr").val();
				var cscheckids=$("#fwng_csdw").val();
				if("zs"==flag){
					if(checkids.length!=0){
						for(var i= 0;i<data.rows.length;i++){
							var id=data.rows[i].id;
							if(checkids.indexOf(id)!=-1){
								grid.datagrid('checkRow', i);
							}
						}
					}
				}else if("cs"==flag){
					if(cscheckids.length!=0){
						for(var i= 0;i<data.rows.length;i++){
							var id=data.rows[i].id;
							if(cscheckids.indexOf(id)!=-1){
								grid.datagrid('checkRow', i);
							}
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
		$("a[data-action=fwng_xz_zs]").bind("click", function() {
			data();
			queren();
		});
		
		$("a[data-action=fwng_xz_cs]").bind("click", function() {
			data1();
			queren1();
		});
	}
	var modelName1 = "fwng";
	
	var sjr = $("#"+modelName1+"_sjr");
	
	var csdw=$("#"+modelName1+"_csdw");
	
	var names=$("#"+modelName1+"_username").val();
	
	var csnames=$("#"+modelName1+"_cs").val();
	
	if(names.length>0){
		names+=",";
	}
	
	if(csnames.length>0){
		csnames+=",";
	}
	
	var form = $("#"+modelName1+"_form");
	
	var sjrid=sjr.val();
	
	if(sjrid.length>0){
		sjrid+=",";
	}
	
	var csdwid=csdw.val();
	
	if(csdwid.length>0){
		csdwid+=",";
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
	
	function data1(){
		var data = grid.datagrid("getData");
		for(var i=0;i<data.rows.length;i++){
			if(csdwid.length>0){
				if(csdwid.indexOf(data.rows[i].id)!=-1){
					csdwid=csdwid.replace(data.rows[i].id+",", "")
				}
			}
			if(csnames.length>0){
				if(csnames.indexOf(data.rows[i].realname)!=-1){
					csnames=csnames.replace(data.rows[i].realname+",", "")
				}
			}
		}	
	}
	
	function queren(){
		var rows = grid.datagrid("getChecked");
		var data = grid.datagrid("getData");
		for(var i = 0 ; i < data.rows.length;i++){
			if(data.rows[i].checked == true){
				rows.push(data.rows[i]);
			}
		}
		if(rows.length > 0){
			if(names!= null && names != ""){
				for(var i=0; i<rows.length; i++){
					if(names.indexOf( rows[i].realname ) == -1){
						names += rows[i].realname+",";
						if(sjrid.indexOf(rows[i].id) == -1){
							sjr.val(rows[i].id);
							sjrid+=sjr.val()+",";
						}
					}
				}
				names=names.substring(0,names.length-1);
				sjrid=sjrid.substring(0,sjrid.length-1);
			}else{
				for(var i=0; i<rows.length; i++){
					names+=rows[i].realname+","; 
					if(sjrid.indexOf(rows[i].id)==-1){
						sjr.val(rows[i].id);
						sjrid+=sjr.val()+",";
					}
				}
				names=names.substring(0,names.length-1);
				sjrid=sjrid.substring(0,sjrid.length-1);
			}
			$("#fwng_lxr").window("close");
			form.form('load',{zs:names,zsdws:sjrid});
		}else{
			$.messager.alert("提示","请选择需要添加的联系人！");
		}
	}
	
	function queren1(){
		var rows = grid.datagrid("getChecked");
		var data = grid.datagrid("getData");
		for(var i = 0 ; i < data.rows.length;i++){
			if(data.rows[i].checked == true){
				rows.push(data.rows[i]);
			}
		}
		if(rows.length > 0){
			if(csnames!= null && csnames != ""){
				for(var i=0; i<rows.length; i++){
					if(csnames.indexOf( rows[i].realname ) == -1){
						csnames += rows[i].realname+",";
						if(csdwid.indexOf(rows[i].id) == -1){
							csdw.val(rows[i].id);
							csdwid+=csdw.val()+",";
						}
					}
				}
				csnames=csnames.substring(0,csnames.length-1);
				csdwid=csdwid.substring(0,csdwid.length-1);
			}else{
				for(var i=0; i<rows.length; i++){
					csnames+=rows[i].realname+","; 
					if(csdwid.indexOf(rows[i].id)==-1){
						csdw.val(rows[i].id);
						csdwid+=csdw.val()+",";
					}
				}
				csnames=csnames.substring(0,csnames.length-1);
				csdwid=csdwid.substring(0,csdwid.length-1);
			}
			$("#fwng_lxr").window("close");
			form.form('load',{cs:csnames,csdws:csdwid});
		}else{
			$.messager.alert("提示","请选择需要添加的联系人！");
		}
	}
	
});