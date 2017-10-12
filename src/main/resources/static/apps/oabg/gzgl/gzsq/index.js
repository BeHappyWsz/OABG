$(function(){
	var URL = {
			gridData : ctx + "/oabg/gzgl/gzsq/sqgrid",
			formWin : ctx + "/oabg/gzgl/gzsq/sqform",
			getInfo : ctx + "/oabg/gzgl/gzsq/sqInfo",
			deleteByIds : ctx +"/oabg/gzgl/gzsq/deleteByIds",
			lcjlFormWin : ctx +"/oabg/gzgl/gzsq/gzlcForm",
	};
	
	/** 模块名称* */
	var modelName = "gzsq";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	var name = $("#name").val();
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
				openFormWin(row.id,row.lczt,row.sqname);
		},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"gzname",title:"公章名称",align:"left",halign:"center",width:"120"},
			    {field:"sqbm",title:"申请部门",align:"left",halign:"center",width:"150"},
			    {field:"sqname",title:"申请人",align:"left",halign:"center",width:"100"},
			    {field:"gzsl",title:"盖章数量",align:"right",halign:"center",width:"80"},
			    {field:"gztime",title:"盖章时间",align:"center",halign:"center",width:"150"},
			    {field:"sqreason",title:"申请事由",align:"left",halign:"center",width:"250"},
			    {field:"lczt",title:"流程状态",align:"center",halign:"center",width:"80",
			    	formatter : function(value,row,index){
			    		if("1" == value){
			    			return "<span style='color:orange'>"+row.lcztName+"</span>";
			    		}else if("2" == value){
			    			return "<span style='color:green'>"+row.lcztName+"</span>";
			    		}else if("9" == value){
			    			return "<span style='color:red'>"+row.lcztName+"</span>";
			    		}else{
			    			return "";
			    		}
			    	}
			    },
			    {field:"cz",title:"操作",align:"center",halign:"center",width:"200",
			    	formatter: function(value,row,index){
						return "<a class='gzsqGridLookLcjlBtns' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>查看流程记录</a>";
					}
			    }
			]],
			onLoadSuccess : function(){
				$(".gzsqGridLookLcjlBtns").linkbutton({
					plain : true,
					onClick : function(){
						var id = $(this).data("myid");
						openLcjlFormWin(id);
					}
				});
			}
		})
	}
	
	/** 绑定Grid操作按钮方法* */
	function bindGridToorBar() {

		$("a[data-action='gzsq-add']").bind("click",function(){
			openFormWin();
		});
		
		$("a[data-action='gzsq-update']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				openFormWin(row.id,row.lczt,row.sqname);
			}
		});
		
		$("a[data-action='gzsq-delete']").bind("click",function(){
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
	function openFormWin(id,lczt,sqname){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : id ? "公章[修改/查看]" : "公章[申请]",
			width : 700,
			height : 260,
			href :URL.formWin,
			onLoad : function(){
				if(id){
					if(9 != lczt && "9" != lczt && name!= sqname){
						$("a[data-action='gzsq-save']").addClass("hide");
					}
					formLoadData(id);
				}
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	/**打开出入记录页面**/
	function openLcjlFormWin(id){
		var win = $("<div id='" + modelName + "_lcjlFormWin'></div>").window({
			title : "流程记录[查看]",
			href :URL.lcjlFormWin+"?id="+id,
			width : 700,
			height : 372,
			onLoad : function(){
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
				$("#" + modelName + "_form").form("load", data);
				$('#bmdw').combotree("setValue",data.bmdwCode);
				$('#bmdw').combotree("setText",data.bmdwName);
			}
		});
	}
	/**
	 * 删除
	 */
	function deleteByIds(){
		var rows = gridChecked(grid);
		if(rows){
			if(rows.indexOf("1")==-1){
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
								
								/** 如果删除成功，刷新grid数据，关闭窗口* */
								if (data.success) {
									$.messager.show({
										title:'提示',
										msg: "删除成功！",
										timeout:1000,
										showType:'slide'
									});
									$.messager.progress('close');
									grid.datagrid('reload');
								}else{
									$.messager.show({
										title:'提示',
										msg: data.returnMsg,
										timeout:1000,
										showType:'slide'
										});
									}
								}
							});
				    	}
					});
				}
			}else{
				$.messager.alert("提示", "您勾选的数据尚未审核完成");
			}
		}
	};
	
});