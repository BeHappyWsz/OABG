$(function(){
	var URL = {
			gridData : ctx + "/oabg/xxbsly/grid",
			formWin : ctx + "/oabg/xxbsly/form",
			XXBSLY : ctx + "/oabg/xxbsly/xxbsly",
			DAFEN : ctx + "/oabg/xxbsly/dafen",
			lcjlFormWin : ctx + "/oabg/xxbsly/lcjlFormWin",
			
	};
	
	/** 模块名称* */
	var modelName = "xxbsly";
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
		grid.datagrid({
			url :URL.gridData,
			onDblClickRow : function(index,row) {
				openFormWin(row.id,row.lczt,row.score);
		},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"bsbm",title:"报送部门",align:"left",halign:"center",width:"150"},
			    {field:"bssj",title:"报送时间",align:"center",halign:"center",width:"150"},
			    {field:"bsbt",title:"标题",align:"left",halign:"center",width:"200"},
			    {field:"score",title:"得分",align:"right",halign:"center",width:"100",
			    	formatter:function(val,rowData,rowIndex){
			            if(val!=null)
			                return val.toFixed(1);
			       }
			    	},
			    {field:"lczt",title:"流程状态",align:"center",halign:"center",width:"100",
			    	formatter : function(value,row,index){
			    		if("1" == value){
			    			return "<span style='color:orange'>未办</span>";
			    		}else if("2" == value){
			    			return "<span style='color:green'>已办</span>";
			    		}else if("3" == value){
			    			return "<span style='color:red'>"+row.lcztName+"</span>";
			    		}else{
			    			return "";
			    		}
			    	}
			    },
			    {field:"cz",title:"操作",align:"center",halign:"center",width:"200",
			    	formatter: function(value,row,index){
						return "<a class='xxbslyGridLookLcjlBtns' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>查看流程记录</a>";
					}
			    }
			]],
			onLoadSuccess : function(){
				$(".xxbslyGridLookLcjlBtns").linkbutton({
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

		$("a[data-action='XXBSSP_SPBTN']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(row.lczt==1 && row.lczt == "1"){
						openWin(row.id,row.lczt);
				}else{
					showMsg("只能处理未办的申请");
				}
				
			}
		});
		$("a[data-action='XXBSSP_DAFEN']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(row.lczt==2 && row.lczt == "2"){
					if(row.score < 100){
						openDfWin(row.id)
					}else{
						showMsg("该信息分数已是最高分(100分)，无法进行打分");
					}
					
				}else{
					showMsg("未录用的信息无法打分");
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
	}
	
	/**跳转到新增/修改页面**/
	function openFormWin(id,lczt,score){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : lczt == 1 ? "信息报送[录用]" : "信息报送[打分]",
			width : 850,
			height :300,
			href :URL.formWin+"?id="+id,
			onLoad : function(){
				if(1 != lczt && "1" != lczt){
					$("a[data-action='XXBSSP_FORMSPBTN']").addClass("hide");
					if(score >= 100){
						$("a[data-action='XXBSSP_SCORE']").addClass("hide");
					}
				}
				if(1 == lczt && "1" == lczt){
					$("a[data-action='XXBSSP_SCORE']").addClass("hide");
				}
				$("#xxbsly_form_id").val(id);
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
	
	function openDfWin(id){
		var win = $("<div id='" + modelName + "_dfFormWin'></div>").window({
			title : "打分",
			href :URL.DAFEN,
			width :340,
			height : 106,
			onLoad : function(){
				$("#df_spform_id").val(id);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	function openWin(id,lczt){
		var win = $("<div id='" + modelName + "_lyFormWin'></div>").window({
			title : "审批",
			href :URL.XXBSLY,
			width :340,
			height : 139,
			onLoad : function(){
				$("#xxbsly_spform_id").val(id);
				if(1 != lczt && "1" != lczt){
					$("a[data-action='XXBSSP_DOSPBTN']").addClass("hide");
					
				}
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
});