$(function(){
	var URL = {
			gridData : ctx + "/oabg/gzjh/grid",
			formWin : ctx + "/oabg/gzjh/form",
			getInfo : ctx + "/oabg/gzjh/getinfo",
			deleteByIds : ctx +"/oabg/gzjh/deleteByIds",
			Win :ctx +"/oabg/gzjh/win",
			Week :ctx +"/oabg/gzjh/getWeek",
			excel :ctx +"/oabg/gzjh/excel"
	};
	/** 模块名称* */
	var modelName = "gzjh";
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
		var role = $("#gzjh_role").val();
		var column="";
		if(role.indexOf("BGSZR")!=-1){
			column = [[
					    {field:"id",checkbox:true}, 
					    {field:"gzjh",title:"工作计划类型",align:"center",halign:"center",width:"200"},
					    {field:"kssj",title:"开始时间",align:"center",halign:"center",width:"200"},
					    {field:"jssj",title:"结束时间",align:"center",halign:"center",width:"150"},
					    {field:"jhbt",title:"工作计划标题",align:"left",halign:"center",width:"200"},
					    {field:"lczt",title:"状态",align:"center",halign:"center",width:"80",
					    	formatter : function(value,row,index){
					    		if("1" == value){
					    			return "<span style='color:green'>已提交</span>";
					    		}else if("2" == value){
					    			return "<span style='color:orange'>未提交</span>";
					    		}
					    	}
					    }
					]]
		}else{
			column =[[
			    {field:"id",checkbox:true}, 
			    {field:"gzjh",title:"工作计划类型",align:"center",halign:"center",width:"200"},
			    {field:"kssj",title:"开始时间",align:"center",halign:"center",width:"200"},
			    {field:"jssj",title:"结束时间",align:"center",halign:"center",width:"150"},
			    {field:"jhbt",title:"工作计划标题",align:"left",halign:"center",width:"200"},
			    {field:"sfyd",title:"状态",align:"center",halign:"center",width:"80",
			    	formatter : function(value,row,index){
			    		if("1" == value){
			    			return "<span style='color:green'>已读</span>";
			    		}else if("2" == value){
			    			return "<span style='color:orange'>未读</span>";
			    		}
			    	}
			    }
			]]
		}
		
		grid.datagrid({
			url :URL.gridData,
			onDblClickRow : function(index,row) {
				if(row.lczt=="1"&&row.lczt==1){
					openWin(row.id);
				}else{
					openFormWin(row.id);
				}
			},
			columns:column,
		})
	}
	
	
	/** 绑定Grid操作按钮方法* */
	function bindGridToorBar() {

		$("a[data-action='GZAP_ADD']").bind("click",function(){
			openFormWin();
		});
		
		$("a[data-action='GZAP_UPDATE']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(row.lczt=="1"&&row.lczt==1){
					openWin(row.id);
				}else{
					openFormWin(row.id);
				}
				
			}
		});
		
		$("a[data-action='GZAP_DELETE']").bind("click",function(){
			deleteByIds();
		});
		
		$("a[data-action='GZAP-EXCEL']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(row.lczt=="2"&&row.lczt==2){
					showMsg("该计划尚未提交，无法导出");
				}else{
					OutExcel(row.id)
				}
				
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
	};
	function createform(i){
		var html="<fieldset>"
				+"<form class='gzjh_form' >"
				+"<input id='gzjhxx_form_id' name='id' value='' type='hidden'/>"
				+"<table width='100%' class='formtable'>"
				+"<tr>"
				+"<td class='label'>日期起</td>"
				+"<td ><input class='easyui-datebox' data-startdindex='"+i+"' name='rqstart' data-options='editable:false' style='width: 95%'/></td>"
				+"<td class='label'>日期止</td>"
				+"<td ><input class='easyui-datebox' data-enddindex='"+i+"' name='rqEnd' data-options='editable:false' style='width: 95%'/></td>"
				+"<td class='label'>星期</td>"
				+"<td ><input class='easyui-textbox' name='week' data-weekindex='"+i+"' style='width: 95%'/></td>"
				+"</tr>"
				+"<tr>"
				+"<td class='label'>时间</td>"
				+"<td ><input class='easyui-textbox' name='sj' style='width: 95%'/></td>"
				+"<td class='label'>地点</td>"
				+"<td ><input class='easyui-textbox' name='address' style='width: 95%'/></td>"
				+"<td class='label'>领导</td>"
				+"<td ><input class='easyui-textbox' name='ld' style='width: 95%'/></td>"
				+"</tr>"
				+"<tr>"
				+"<td class='label'>主办部门</td>"
				+"<td colspan='5'><input class='easyui-textbox' name='zbbm' style='width: 95%'/></td>"
				+"<tr>"
				+"<tr>"
				+"<td class='label'>活动（会议）内容</td>"
				+"<td colspan='5'><input class='easyui-textbox' name='text' style='width: 95%'/></td>"
				+"</tr>"
				+"<tr>"
				+"<td class='label'>备注</td>"
				+"<td colspan='5'><input class='easyui-textbox' name='bz' style='width: 95%'/></td>"
				+"</tr>"
				+"</table>"
				+"</form>"
				+"</fieldset>";
		return $(html);
	}
	/**跳转到新增/修改页面**/
	function openFormWin(id){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title :  "新增",
			width : 980,
			height : 642,
			href :URL.formWin,
			onLoad : function(){
				for(var i=0;i<10;i++){
					var formbox = createform(i);
					$("#jh").append(formbox);
					bindDateBoxEvent(formbox,i);
				}
				$.parser.parse('#jh');  
				if(id){
					formLoadData(id);
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
			success : function(data){
				$("#gzjh").combobox("setValue",data.map.gzjh);
				$("#gzjh").combobox("setText",data.map.gzjhName);
				$("#kssj").datebox("setValue",data.map.kssj);
				$("#jssj").datebox("setValue",data.map.jssj);
				$("#jhbt").textbox("setValue",data.map.jhbt)
				$("." + modelName + "_form_main").form("load", {id:data.map.id});
				$("." + modelName + "_form").each(function(i){
					$(this).form("load", data.listMsp[i]);
				});
			}
		});
	}
	function deleteByIds(){
		var rows = gridChecked(grid);
		if(rows){
			if(rows.indexOf("1")!=0){
				var ids=gridCheckedValid(grid);
				if(ids){
					$.messager.confirm('提示','您确认想要删除吗？',function(r){    
					    if(r){
					    	$.ajax({
								type : "post",
								url :URL.deleteByIds,
								data : {ids:ids},
								success : function(data) {
									/** 显示后台返回的提示信息* */
									showMsg(Msg.delSuc);
									/** 如果删除成功，刷新grid数据，关闭窗口* */
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
			}else{
				showMsg("只能删除未提交的数据");
			}
		}
		
	}
	
	function openWin(id){
		var win = $("<div id='" + modelName + "_Win'></div>").window({
			title :  "查看",
			width : 980,
			height : 642,
			href :URL.Win+"?id="+id,
			onLoad : function(){
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
				grid.datagrid("reload");
			}
		});
	}
	function bindDateBoxEvent(formbox,index){
		formbox.find("input[data-startdindex='"+index+"']").datebox({
			onSelect: function(date){
				var end= $("input[data-enddindex='"+index+"']").datebox('getValue');
				var start= $("input[data-startdindex='"+index+"']").datebox('getValue');
				$.ajax({
					type : "post",
					url :URL.Week,
					data : {start:start,end:end},
					success : function(data) {
						if(data.success==true){
							formbox.find("input[data-weekindex='"+index+"']").textbox("setValue",data.returnMsg);
						}else{
							showMsg(data.returnMsg);
							formbox.find("input[data-startdindex='"+index+"']").datebox('clear');
						}
					}
				});
		    }
		});
		formbox.find("input[data-enddindex='"+index+"']").datebox({
			onSelect: function(date){
				var end= $("input[data-enddindex='"+index+"']").datebox('getValue');
				var start= $("input[data-startdindex='"+index+"']").datebox('getValue');
				$.ajax({
					type : "post",
					url :URL.Week,
					data : {start:start,end:end},
					success : function(data) {
						if(data.success==true){
							formbox.find("input[data-weekindex='"+index+"']").textbox("setValue",data.returnMsg);
						}else{
							showMsg(data.returnMsg);
							formbox.find("input[data-enddindex='"+index+"']").datebox('clear');
						}
					}
				});
		    }
		});
	}
	/**
	 * 导出
	 * */
	function OutExcel(id){
		window.open(URL.excel+"?id="+id);
	};
});