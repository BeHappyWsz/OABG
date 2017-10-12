$(function(){
	var URL = {
			gridData : ctx + "/oabg/hypx/grid?gridfrom=2",
			formWin : ctx + "/oabg/hypx/hypxsh/form",
			getInfo : ctx + "/oabg/hypx/getInfo",
			lcjlFormWin : ctx + "/oabg/hypx/lcjlForm",
			shFormWin : ctx + "/oabg/hypx/hypxsh/shForm"
	};
	
	/** 模块名称* */
	var modelName = "hypxsh";
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
			    {field:"sqrq",title:"申请日期",align:"center",halign:"center",width:120},
			    {field:"mc",title:"会议（培训）名称",align:"left",halign:"center",width:250},
			    {field:"ysdx",title:"预算",align:"left",halign:"center",width:100},
			    {field:"blqk",title:"办理情况",align:"center",halign:"center",width:180,
			    	formatter : function(value,row,index){
			    		if("1" == value){
			    			return "<span style='color:red'>未办</span>";
			    		}else if("2" == value){
			    			return "<span style='color:green'>已办</span>";
			    		}else{
			    			return "";
			    		}
			    	}
			    },
			    {field:"lczt",title:"流程状态",hidden:true},
			    {field:"cz",title:"操作",align:"center",halign:"center",width:"200",
			    	formatter: function(value,row,index){
						return "<a class='hypxshGridLookLcjlBtns' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>查看流程记录</a>";
					}
			    }
			]],
			onLoadSuccess : function(){
				$(".hypxshGridLookLcjlBtns").linkbutton({
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
		$("a[data-action='HYPXSH_CZSHBTN']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(1 == row.lczt || "1" == row.lczt){
					openShFormWin(row.id,"cz");
				}else{
					showMsg("只能审核待处长审核的数据！");
				}
			}
		});
		$("a[data-action='HYPXSH_BGSSHBTN']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(2 == row.lczt || "2" == row.lczt){
					openShFormWin(row.id,"bgs");
				}else{
					showMsg("只能审核待办公室审核的数据！");
				}
			}
		});
		$("a[data-action='HYPXSH_CWSHBTN']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(3 == row.lczt || "3" == row.lczt){
					openShFormWin(row.id,"cw");
				}else{
					showMsg("只能审核办公室已审核的数据！");
				}
			}
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
	
	/**跳转到详情页面**/
	function openFormWin(id,lczt){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : "会议（培训）[审核]",
			href :URL.formWin+"?id="+id,
			width : 700,
			height : 496,
			onLoad : function(){
				if(1 != lczt && "1" != lczt){
					$("a[data-action='HYPXSH_FORMCZSHBTN']").addClass("hide");
				}
				if(2 != lczt && "2" != lczt){
					$("a[data-action='HYPXSH_FORMBGSSHBTN']").addClass("hide");
				}
				if(3 != lczt && "3" != lczt){
					$("a[data-action='HYPXSH_FORMCWSHBTN']").addClass("hide");
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
	 * 删除
	 */
	function deleteByIds(){
		commonBatchDelete(grid,URL.deleteUrl,modelName);
	}
	
	/**跳转到新增/修改页面**/
	function openShFormWin(id,type){
		var win = $("<div id='" + modelName + "_shFormWin'></div>").window({
			title : "会议（培训）[审核]",
			href :URL.shFormWin,
			width : 500,
			height : 177,
			onLoad : function(){
				$("#" + modelName + "_shform_id").val(id);
				$("#" + modelName + "_shform_stype").val(type);
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
});