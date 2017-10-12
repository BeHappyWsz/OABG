$(function(){
	var URL = {
			gridData : ctx + "/oabg/zcsp/grid",
			formWin : ctx + "/oabg/zcsp/form",
			spFormWin : ctx + "/oabg/zcsp/spForm",
			formRkWin : ctx + "/oabg/zcsp/rkform",
			getInfo : ctx + "/oabg/zcsp/getInfo",
			deleteUrl : ctx +"/oabg/zcsp/delete",
			lcjlFormWin : ctx + "/oabg/zcsp/lcjlForm",
			rkjlFormWin : ctx + "/oabg/zcsp/rkjlForm"
	};
	
	/** 模块名称* */
	var modelName = "zcsp";
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
					openFormWin(row.id,row.zcglsplc,row.sqlxcode);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"sqr",title:"申请人",align:"left",halign:"center",width:150},
			    {field:"sqbm",title:"申请部门",align:"center",halign:"center",width:100},
			    {field:"sqbmcode",title:"申请部门",hidden:true},
			    {field:"sqlx",title:"申请类型",align:"center",halign:"center",width:150},
			    {field:"sqlxcode",title:"申请类型",hidden:true},
			    {field:"lxdh",title:"联系电话",align:"left",halign:"center",width:150},
			    {field:"sqrq",title:"申请日期",align:"center",halign:"center",width:150},
			    {field:"zcmc",title:"资产名称",align:"left",halign:"center",width:120},
			    {field:"sl",title:"数量",align:"right",halign:"center",width:50},
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
			    {field:"zcglsplc",title:"流程状态",hidden:true},
			    {field:"cz",title:"操作",align:"center",halign:"center",width:"200",
			    	formatter: function(value,row,index){
						return "<a class='zcdjspGridLookLcjlBtns' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>查看流程记录</a>";
					}
			    }
			]],
			onLoadSuccess : function(){
				$(".zcdjspGridLookLcjlBtns").linkbutton({
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
		$("a[data-action='zcsp_cz']").unbind().bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(row.zcglsplc=="3"||row.zcglsplc=="7"){
					var zcsp_iscw = $("#zcsp_iscw").val();
					if(1 == zcsp_iscw || "1" == zcsp_iscw){
						//只能审批财务处的申请
						if(row.sqbmcode == "114"){
							openSpFormWin1(row.id);
						}else{
							showMsg("只能审批本部门下的未办申请");
						}
					}else{
						openSpFormWin1(row.id);
					}
				}else{
					showMsg("只能审批未办的申请");
				}
			}
		});
		$("a[data-action='zcsp_fgld']").unbind().bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(row.zcglsplc=="4"){
					openSpFormWin1(row.id);
				}else{
					showMsg("只能审批未办的申请");
				}
			}
		});
		$("a[data-action='zcsp_gly']").unbind().bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(row.zcglsplc=="5"){
					openSpFormWin1(row.id);
				}else{
					showMsg("只能审批未办的申请");
				}
			}
		});
		$("a[data-action='zcsp_jz']").unbind().bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(row.zcglsplc=="6"||row.zcglsplc=="4"){
					openSpFormWin1(row.id);
				}else{
					showMsg("只能审批未办的申请");
				}
			}
		});
		$("a[data-action='zcsp_ff']").unbind().bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(row.zcglsplc=="8"){
					openSpFormWin1(row.id);
				}else{
					showMsg("只能分发待分发的申请");
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
	
	/**
	 * 审批页面
	 */
	function openSpFormWin1(id){
		var win = $("<div id='" + modelName + "_spformWin'></div>").window({
			title :"审批",
			href :URL.spFormWin+"?id="+id,
			width : 500,
			height : 178,
			onLoad : function(){	
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	/**跳转到新增/修改页面**/
	function openFormWin(id,zcglsplc){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : id ? "资产[查看]" : "资产[新增]",
			href :URL.formWin+"?id="+id,
			width : 700,
			height : 213,
			onLoad : function(){
				$("a[data-action='zcsp_fgldform']").addClass("hide");
				$("a[data-action='zcsp_czform']").addClass("hide");
				$("a[data-action='zcsp_glyform']").addClass("hide");
				$("a[data-action='zcsp_jzform']").addClass("hide");
				if(zcglsplc=="3"){
					var zcsp_iscw = $("#zcsp_iscw").val();
					if(1 == zcsp_iscw || "1" == zcsp_iscw){
						//只能审批财务处的申请
						if(row.sqbmcode == "114"){
							$("a[data-action='zcsp_czform']").removeClass("hide");
						}
					}else{
						$("a[data-action='zcsp_czform']").removeClass("hide");
					}
					
				}else if(zcglsplc=="4"){
					$("a[data-action='zcsp_fgldform']").removeClass("hide");
					$("a[data-action='zcsp_jzform']").removeClass("hide");
				}else if(zcglsplc=="5"){
					$("a[data-action='zcsp_glyform']").removeClass("hide");
				}else if(zcglsplc=="6"){
					$("a[data-action='zcsp_jzform']").removeClass("hide");
				}else if(zcglsplc=="7"){
					$("a[data-action='zcsp_czform']").removeClass("hide");
				}else if(zcglsplc == "8"){
					$("a[data-action='zcsp_ffform']").removeClass("hide");
				}
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	
	
	/**跳转到入库新增页面**/
	function openRkFormWin(id){
		var win = $("<div id='" + modelName + "_rKformWin'></div>").window({
			title : "入库[新增]",
			href :URL.formRkWin,
			width : 700,
			height : 300,
			onLoad : function(){				
	        	if(id){
					formLoadData(id);
				}else{
					$("#bgypdj_rksj").datetimebox({
						value:getNowFormatDate(),
						showSeconds: false   
					});
				}
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	/**获取当前时间，yyyy-MM-dd hh:mm**/
	function getNowFormatDate() {
	    var date = new Date();
	    var seperator1 = "-";
	    var seperator2 = ":";
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
	            + " " + date.getHours() + seperator2 + date.getMinutes();
	    return currentdate;
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
			}
		});
	}
	
	/**
	 * 删除
	 */
	function deleteByIds(){
		commonBatchDelete(grid,URL.deleteUrl,modelName);
	}
	
});
