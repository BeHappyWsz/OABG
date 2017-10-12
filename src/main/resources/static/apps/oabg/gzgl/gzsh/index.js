$(function(){
	
	var URL = {
			gridData : ctx + "/oabg/gzgl/gzsh/shgrid",
			formWin : ctx + "/oabg/gzgl/gzsh/shform",
			info : ctx + "/oabg/gzgl/gzsh/shInfo",
			lcjlFormWin : ctx +"/oabg/gzgl/gzsh/gzlcForm",
			QueryWin : ctx + "/oabg/gzgl/gzsh/query",
			spFormWin : ctx+"/oabg/gzgl/gzsh/spForm",
	};
	
	/** 模块名称* */
	var modelName = "gzsh";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	
		
	/** 主页查询表单* */
	var queryForm = $("#" + modelName + "_qf");
	/** 渲染Grid* */
	renderGrid();
	/** 绑定查询表单按钮方法* */
	bindSearchBar();
	bindButtonAction()
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			onDblClickRow : function(index,row) {
				openFormWin(row.id,row.lczt);
		},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"gzname",title:"公章名称",align:"left",halign:"center",width:"120"},
			    {field:"sqbm",title:"申请部门",align:"left",halign:"center",width:"150"},
			    {field:"sqname",title:"申请人",align:"left",halign:"center",width:"100"},
			    {field:"gzsl",title:"盖章数量",align:"right",halign:"center",width:"150"},
			    {field:"gztime",title:"盖章时间",align:"center",halign:"center",width:"150"},
			    {field:"sqreason",title:"申请事由",align:"left",halign:"center",width:"200"},
			    {field:"lczt",title:"流程状态",align:"center",halign:"center",width:"80",
			    	formatter : function(value,row,index){
			    		if("1" == value){
			    			return "<span style='color:orange'>未办</span>";
			    		}else if("2" == value){
			    			return "<span style='color:green'>已办</span>";
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
	
	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=gzsp-sp]").bind("click", function() {
			var row = gridSelectedValid(grid);
			if(row){
				if(1!=row.lczt||"1"!=row.lczt){
					showMsg("只批待审批的申请");
				}else{
					openSpFormWin(row.id,row.lczt)
				}
				
			}
		});
		$("a[data-action='gzsp-query']").bind("click",function(){
			openQueryWin();
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
	
	/**审批页面**/
	function openFormWin(id,lczt){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : "审核",
			width : 600,
			height : 222,
			href :URL.formWin+"?id="+id,
			onLoad : function(){
				if(1!=lczt||"1"!=lczt){
					$("a[data-action=gzsp-spp]").addClass('hide');
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
	 * 查询公章使用情况
	 */
	
	function openQueryWin(){
		var win = $("<div id='" + modelName + "_QueryWin'></div>").window({
			title : "公章查询",
			width :650,
			height : 550,
			href :URL.QueryWin,
			onLoad : function(){
				closeLoadingDiv();
			},onClose : function() {
				win.window('destroy');
			}
		});
	}
	/**直接审批**/
	function openSpFormWin(id,lczt){
		var win = $("<div id='" + modelName + "_spFormWin'></div>").window({
			title : "审核",
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