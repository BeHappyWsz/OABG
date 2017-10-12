$(function(){
	var URL = {
			gridData : ctx + "/oabg/bgypsq/grid",
			deleteUrl : ctx + "/oabg/bgypsq/delete",
			formWin : ctx + "/oabg/bgypsq/form",
			getInfo : ctx + "/oabg/bgypsq/getInfo",
			lcjlFormWin : ctx + "/oabg/bgypsq/lcjlForm"
	};
	
	/** 模块名称* */
	var modelName = "bgypsq";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	
	/** 主页查询表单* */
	var queryForm = $("#" + modelName + "_qf");
	
	/** 渲染Grid* */
	renderGrid();
	
	/** 绑定Grid操作按钮方法* */
	bindGridToorBar();
	
	/** 绑定Grid操作按钮方法* */
	bindSearchBtns();
	
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			onDblClickRow : function(index,row) {
					openFormWin(row.id,row.lczt);
			},
			columns:[[
			    {field:"id",checkbox:true},
			    {field:"sqr",title:"申请人",align:"left",halign:"center",width:120},
			    {field:"sqbm",title:"申请部门",align:"left",halign:"center",width:220},
			    {field:"sqrq",title:"申请日期",align:"center",halign:"center",width:100},
			    {field:"name",title:"物品名称",align:"left",halign:"center",width:150},
			    {field:"bgypflName",title:"分类",align:"left",halign:"center",width:100},
			    {field:"sl",title:"数量",align:"right",halign:"center",width:80},
			    {field:"lczt",title:"流程状态",align:"center",halign:"center",width:80,
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
						return "<a class='bgypsqGridLookLcjlBtns' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>查看流程记录</a>";
					}
			    }
			]],
			onLoadSuccess : function(){
				$(".bgypsqGridLookLcjlBtns").linkbutton({
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
		$("a[data-action='BGYPSQ_ADDBTN']").bind("click",function(){
			openFormWin("");
		});
		
		$("a[data-action='BGYPSQ_UPDATEBTN']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				openFormWin(row.id,row.lczt);
			}
		});
		
		$("a[data-action='BGYPSQ_DELETEBTN']").bind("click",function(){
			deleteByIds();
		});
	}
	
	function bindSearchBtns(){
		$("#"+modelName+"_query").unbind().bind("click",function(){
			var formData = queryForm.serializeObject({transcript:"overlay"});
			grid.datagrid("load",formData);
		});
		$("#"+modelName+"_clear").unbind().bind("click",function(){
			queryForm.form("clear");
		});
	}
	
	/**跳转到新增/修改页面**/
	function openFormWin(id,lczt){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : id ? "办公用品[查看]" : "办公用品[申请]",
			href :URL.formWin+"?id="+id,
			width : 700,
			height : 243,
			onLoad : function(){
				if(id){
					if(9 != lczt && "9" != lczt){
						$("a[data-action='BGYPSQ_SUBMITBTN']").addClass("hide");
					}
				}
				formLoadData(id);
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
				$("#" + modelName + "_form_bgypId").combogrid("setValue",data.bgypId);
				$("#" + modelName + "_form_bgypId").combo("setText",data.name);
			}
		});
	}
	
	/**
	 * 删除
	 */
	function deleteByIds(){
		var rows = grid.datagrid("getChecked");
		var idArray = new Array();
		if (rows.length > 0) {
			for(var i = 0; i < rows.length; i++){
				var lczt = rows[i].lczt;
				if(9 != lczt && "9" != lczt){
					showMsg("只能删除审批未通过的数据！");
					return false;
				}
				idArray.push(rows[i].id);
			}
			var ids = idArray.join(",");
			$.messager.confirm("删除确认", Msg.delconfirm, function(r) {
				if (r) {
					$.ajax({
						type : "post",
						url : URL.deleteUrl,
						data : {
							ids : ids
						},
						success : function(data) {
							/** 如果删除成功，刷新grid数据* */
							if (data.success) {
								$.messager.progress("close");
								$("#" + modelName + "_grid").datagrid("reload");
								$.messager.alert("系统提示：", Msg.delSuc);
							}
						}
					});
				}
			});
		} else {
			$.messager.alert("提示", "您尚未勾选数据！");
			return false;
		}
	}
	
});