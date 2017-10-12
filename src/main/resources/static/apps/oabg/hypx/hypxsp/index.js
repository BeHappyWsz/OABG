$(function(){
	var URL = {
			gridData : ctx + "/oabg/hypx/grid?gridfrom=3",
			formWin : ctx + "/oabg/hypx/hypxsp/form",
			getInfo : ctx + "/oabg/hypx/getInfo",
			lcjlFormWin : ctx + "/oabg/hypx/lcjlForm",
			spFormWin : ctx + "/oabg/hypx/hypxsp/spForm"
	};
	
	/** 模块名称* */
	var modelName = "hypxsp";
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
						return "<a class='hypxspGridLookLcjlBtns' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>查看流程记录</a>";
					}
			    }
			]],
			onLoadSuccess : function(){
				$(".hypxspGridLookLcjlBtns").linkbutton({
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
		$("a[data-action='HYPXSP_SPBTN']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(4 == row.lczt || "4" == row.lczt){
					openSpFormWin(row.id);
				}else{
					showMsg("只能审批财务已审核的数据！");
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
			title : "会议（培训）[审批]",
			href :URL.formWin+"?id="+id,
			width : 700,
			height : 496,
			onLoad : function(){
				if(4 != lczt && "4" != lczt){
					$("a[data-action='HYPXSP_FORMSPBTN']").addClass("hide");
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
	function openSpFormWin(id){
		var win = $("<div id='" + modelName + "_spFormWin'></div>").window({
			title : "会议（培训）[审批]",
			href :URL.spFormWin,
			width : 500,
			height : 177,
			onLoad : function(){
				$("#" + modelName + "_spform_id").val(id);
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
});