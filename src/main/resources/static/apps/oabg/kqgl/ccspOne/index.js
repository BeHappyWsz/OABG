$(function(){
	var URL = {
			gridData : ctx + "/oabg/kqgl/ccspOne/grid",
			lcjlFormWin:ctx + "/oabg/kqgl/ccspOne/lcjlFormWin",
			formWin:ctx+"/oabg/kqgl/ccspOne/spFirst",
			spFormWin : ctx + "/oabg/kqgl/ccspOne/ccspF"
	};
	
	/** 模块名称* */
	var modelName = "ccsp";
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
		var js =$("#ccsp_js").val();
		grid.datagrid({
			url :URL.gridData,
			onDblClickRow : function(index,row) {
				openFormWin(row.id,row.lczt);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"qjname",title:"姓名",align:"left",halign:"center",width:"100"},
			    {field:"qjbm",title:"部门",align:"left",halign:"center",width:"100"},
			    {field:"lxdh",title:"联系电话",align:"center",halign:"center",width:"110"},
			    {field:"ccaddress",title:"出差地点",align:"center",halign:"center",width:"100"},
			    {field:"qjstr",title:"出差开始时间",align:"center",halign:"center",width:"150"},
			    {field:"qjend",title:"出差结束时间",align:"center",halign:"center",width:"150"},
			    {field:"qjsy",title:"出差事由",align:"left",halign:"center",width:"200"},
			    {field:"lczt",title:"流程状态",align:"center",halign:"center",width:"120",
			    	formatter : function(value,row,index){
			    		var js = row.role;
			    		if("1" == value){
			    			if(js.indexOf("CZ")!=-1){
			    				return "<span style='color:orange'>未办</span>";
							}else{
								return "<span style='color:green'>已办</span>";
							}
			    		}
			    		if("2" == value){
			    			if(js.indexOf("FGLD")!=-1){
			    				return "<span style='color:orange'>未办</span>";
							}else if(js.indexOf("JZ")!=-1){
								return "<span style='color:orange'>未办</span>";
							}else{
								return "<span style='color:green'>已办</span>";
							}
			    		}
			    		if("3" == value){
			    			if(js.indexOf("JZ")!=-1){
			    				return "<span style='color:orange'>未办</span>";
							}else{
								return "<span style='color:green'>已办</span>";
							}
			    		}
			    		if("0" == value){
			    			if(js.indexOf("BGSFZR")!=-1){
			    				return "<span style='color:orange'>未办</span>";
							}else{
								return "<span style='color:green'>已办</span>";
							}
			    		}
			    		 if("4" == value){
			    			return "<span style='color:green'>已办</span>";
			    		} 
			    		 if("5" == value){
			    			return "<span style='color:red'>已办</span>";
			    		}else{
			    			return "";
			    		}
			    	},
			    },
			    {field:"cz",title:"操作",align:"center",halign:"center",width:"120",
			    	formatter: function(value,row,index){
						return "<a class='ccspGridLookLcjlBtns' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>查看流程记录</a>";
					}
			    }
			]],
			onLoadSuccess : function(){
				$(".ccspGridLookLcjlBtns").linkbutton({
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
		$("a[data-action='ccsp-sp']").bind("click",function(){
			var row = gridSelectedValid(grid);
			var lczt = row.lczt
			if(row){
				var js =$("#ccsp").val();
				if(1==lczt||"1"==lczt){
					if(js.indexOf("CZ")==-1){
						showMsg("只能审批待审批的申请");
					}else{
						openSpFormWin(row.id,row.lczt);
					}
				}else if(2==lczt||"2"==lczt){
					if(/*js!="FGLD"*/js.indexOf("JZ")!=-1){
						openSpFormWin(row.id,row.lczt);
					}else{
						if(js.indexOf("FGLD")==-1){
							showMsg("只能审批待审批的申请");
						}else{
							openSpFormWin(row.id,row.lczt);
						}
					}
				}else if(3==lczt||"3"==lczt){
					if(/*js!="JZ"*/js.indexOf("JZ")==-1){
						showMsg("只能审批待审批的申请");
					}else{
						openSpFormWin(row.id,row.lczt);
					}
				}else if(4==lczt||5==lczt||"4"==lczt||"5"==lczt){
					showMsg("只能审批待审批的申请");
				}else if(0==lczt||"0"==lczt){
					if(/*js!="FGLD"*/js.indexOf("JZ")!=-1){
						openSpFormWin(row.id,row.lczt);
					}else{
						if(/*js!="BGSFZR"*/js.indexOf("BGSFZR")==-1){
							showMsg("只能审批待审批的申请");
						}else{
							openSpFormWin(row.id,row.lczt);
						}
					}
					
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
	function openFormWin(id,lczt){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : "审批",
			width : 600,
			height : 274,
			href :URL.formWin+"?id="+id,
			onLoad : function(){
				var js =$("#ccsp").val();
				if(1==lczt||"1"==lczt){
					if(js.indexOf("CZ")==-1){
						$("a[data-action='ccsp-spp']").addClass("hide");	
					}
				}
				if(2==lczt||"2"==lczt){
					if(/*js!="FGLD"*/js.indexOf("JZ")!=-1){
						$("a[data-action='ccsp-spp']").show();	
					}else{
						if(js.indexOf("FGLD")==-1){
							$("a[data-action='ccsp-spp']").addClass("hide");
						}
					}
					
				}
				if(3==lczt||"3"==lczt){
					if(/*js!="JZ"*/js.indexOf("JZ")==-1){
						$("a[data-action='ccsp-spp']").addClass("hide");	
					}
				}
				if(4==lczt||5==lczt||"4"==lczt||"5"==lczt){
						$("a[data-action='ccsp-spp']").addClass("hide");	
				}
				if(0==lczt||"0"==lczt){
					if(/*js!="BGSFZR"*/js.indexOf("BGSFZR")==-1){
						$("a[data-action='ccsp-spp']").addClass("hide");	
					}
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
	/**直接打开审批页面*/
	function openSpFormWin(id,lczt){
		var win = $("<div id='" + modelName + "_spFormWin'></div>").window({
			title : "出差[审批]",
			href :URL.spFormWin,
			width : 500,
			height : 177,
			onLoad : function(){
				/*var js =$("#ccsp_spform_js").val();
				if(1==lczt||"1"==lczt){
					if(js.indexOf("CZ")==-1){
						$("a[data-action='ccsp-sppp']").addClass("hide");	
					}
				}
				if(2==lczt||"2"==lczt){
					if(js!="FGLD"js.indexOf("JZ")!=-1){
						$("a[data-action='qjsp-czspp']").show();	
					}else{
						if(js.indexOf("FGLD")==-1){
							$("a[data-action='qjsp-czspp']").addClass("hide");
						}
					}
					
				}
				if(3==lczt||"3"==lczt){
					if(js!="JZ"js.indexOf("JZ")==-1){
						$("a[data-action='ccsp-sppp']").addClass("hide");	
					}
				}
				if(4==lczt||5==lczt||"4"==lczt||"5"==lczt){
						$("a[data-action='ccsp-sppp']").addClass("hide");	
				}
				if(6==lczt||"6"==lczt){
					if(js!="BGSFZR"js.indexOf("BGSFZR")==-1){
						$("a[data-action='ccsp-sppp']").addClass("hide");	
					}
				}*/
				$("#" + modelName + "_spform_id").val(id);
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
});