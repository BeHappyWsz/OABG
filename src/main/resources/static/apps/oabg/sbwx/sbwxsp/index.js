$(function(){
	var URL = {
			gridData : ctx + "/oabg/sbwx/sbwxsp/grid",
			formWin : ctx + "/oabg/sbwx/sbwxsp/form",
			getInfo : ctx + "/oabg/sbwx/sbwxsp/getInfo",
			lcjlFormWin : ctx +"/oabg/sbwx/sbwxsp/lcjlForm",
			
			spFormWin : ctx + "/oabg/sbwx/sbwxsp/spForm",
			doYwxs : ctx + "/oabg/sbwx/sbwxsp/doYwxs"
	};
	
	/** 模块名称* */
	var modelName = "sbwxsp";
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
					openFormWin(row.id,row.lczt);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"sqr",title:"申请人",align:"left",halign:"center",width:"100"},
			    {field:"bxbm",title:"报修部门",align:"left",halign:"center",width:"100"},
			    {field:"bmfzr",title:"部门负责人",align:"left",halign:"center",width:"100"},
			    {field:"bxrq",title:"报修日期",align:"center",halign:"center",width:"120"},
			    {field:"gzyy",title:"故障原因",align:"left",halign:"center",width:"250"},
			    {field:"lczt",title:"流程状态",align:"center",halign:"center",width:"100",
			    	formatter : function(value,row,index){//1待批阅2已审批3已维修9未通过
			    		if("1" == value ){
			    			return "<span style='color:red'>未办</span>";
			    		}else if("3" == value || "2" == value){
			    			return "<span style='color:green'>已办</span>";
			    		}else{
			    			return "";
			    		}
			    	}
			    },
			    {field:"wxzt",title:"维修状态",align:"center",halign:"center",width:"100",
			    	formatter : function(value,row,index){
			    		if(row.lczt == "2" || row.lczt == 2){
			    			return "<a class='wxztBtns' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>已维修</a>";
			    		}else if(row.lczt == "3" || row.lczt == 3){
			    			return "<span style='color:green'>已维修</span>";
			    		}else{
			    			return "";
			    		}
			    	}
			    },
			    {field:"cz",title:"操作",align:"center",halign:"center",width:"120",
			    	formatter: function(value,row,index){
						return "<a class='sbwxspGridLookLcjlBtns' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>查看流程记录</a>";
					}
			    }
			]],
			onLoadSuccess : function(){
				$(".sbwxspGridLookLcjlBtns").linkbutton({
					plain : true,
					onClick : function(){
						var id = $(this).data("myid");
						openLcjlFormWin(id);
					}
				});
				
				$(".wxztBtns").linkbutton({
					plain : true,
					onClick : function(){
						var id = $(this).data("myid");
						doYwx(id);
					}
				});
			}
		})
	}
	
	/** 绑定Grid操作按钮方法* */
	function bindGridToorBar() {

		$("a[data-action='SBWXSP_OPEN']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				openFormWin(row.id,row.lczt);
			}
		});
		
		$("a[data-action='SBWXSP_SPBTN']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(row.lczt == "1" || row.lczt == 1){
					openSpForm(row.id);
				}else{
					showMsg("该数据无法进行审批");
				}
			}
		});
		
		$("a[data-action='SBWXSP_YWXBTN']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(row.lczt == "2" || row.lczt == 2){
					doYwx(row.id);
				}else{
					showMsg("该数据无法进行已维修状态更换");
				}
			}
//			doYwx();
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
	
	/**跳转到查询审批页面**/
	function openFormWin(id,lczt){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : "查看",
			width : 600,
			height : 199,
			href :URL.formWin+"?id="+id,
			onLoad : function(){				
	        	if(id){
	        		if(1 == lczt && "1" == lczt ){ //待审批
						$("a[data-action='SBWXSP_FORM_DOYWX']").addClass("hide");
					}else if(2 == lczt && "2" == lczt){//已审批
						$("a[data-action='SBWXSP_FORM_SPBTN']").addClass("hide");
					}else if(3 == lczt && "3" == lczt){//已维修
						$("a[data-action='SBWXSP_FORM_SPBTN']").addClass("hide");
						$("a[data-action='SBWXSP_FORM_DOYWX']").addClass("hide");
					}
//	        		formLoadData(id);暂时不需要
	        	}
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	/**
	 * 打开审批页面
	 */
	function openSpForm(id){
		var win = $("<div id='" + modelName + "_spFormWin'></div>").window({
			title : "申请审批",
			width : 500,
			height : 177,
			href :URL.spFormWin+"?id="+id,
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
			dataType : "json",
			url :URL.getInfo,
			data : {id:id},
			success : function(data){
				$("#" + modelName + "_form").form("load", data);
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
	 * 已维修
	 */
	function doYwx(ids){
		$.messager.confirm("变更状态", "是否变更为已维修状态?", function(r) {
			if (r) {
				$.ajax({
					type : "post",
					url : URL.doYwxs,
					data : {
						ids : ids
					},
					success : function(data) {
						if (data.success) {
							$.messager.progress("close");
							showMsg(data.returnMsg);
							$("#" + modelName + "_grid").datagrid("reload");
						}
					}
				});
			}
		});
	}
});