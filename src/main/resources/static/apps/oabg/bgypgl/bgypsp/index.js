$(function(){
	var URL = {
			gridData : ctx + "/oabg/bgypsp/grid",
			formWin : ctx + "/oabg/bgypsp/form",
			getInfo : ctx + "/oabg/bgypsp/getInfo",
			lcjlFormWin : ctx + "/oabg/bgypsq/lcjlForm",
			spFormWin : ctx + "/oabg/bgypsp/spForm"
	};
	
	/** 模块名称* */
	var modelName = "bgypsp";
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
			url :URL.gridData+"?spgrid=1&lczts=1,2",
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
			    {field:"leftnum",title:"当前库存",align:"right",halign:"center",width:100},
			    {field:"lczt",title:"流程状态",align:"center",halign:"center",width:80,
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
			    {field:"cz",title:"操作",align:"center",halign:"center",width:"200",
			    	formatter: function(value,row,index){
						return "<a class='bgypspGridLookLcjlBtns' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>查看流程记录</a>";
					}
			    }
			]],
			onLoadSuccess : function(){
				$(".bgypspGridLookLcjlBtns").linkbutton({
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
		$("a[data-action='BGYPSP_SPBTN']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(1 == row.lczt || "1" == row.lczt){
					openSpFormWin(row.id);
				}else{
					showMsg("只能审批待审批的数据！");
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
			title : "办公物品[审批]",
			href :URL.formWin+"?id="+id,
			width : 700,
			height : 217,
			onLoad : function(){
				if(1 != lczt && "1" != lczt){
					$("a[data-action='BGYPSP_DOSPBTN']").addClass("hide");
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
			title : "办公物品[审批]",
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