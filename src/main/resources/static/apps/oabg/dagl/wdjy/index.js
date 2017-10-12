$(function(){
	var URL = {
			gridData : ctx + "/oabg/wdjy/grid",
			
			formWin : ctx + "/oabg/wdjy/form",
			
			getInfo : ctx + "/oabg/wdjy/getInfo",
			
			deleteByIds : ctx +"/oabg/wdjy/delete",

			lcjlFormWin : ctx +"/oabg/wdjy/lcjlForm"
	};
	
	/** 模块名称* */
	var modelName = "wdjy";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	var queryForm = $("#"+modelName+"_qf");
	
	/** 渲染Grid* */
	renderGrid();
	/** 绑定Grid操作按钮方法* */
	bindGridToorBar();
	bindSearchBtns();
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			onDblClickRow : function(index,row) {
					openFormWin(row.id,row.lczt);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"sqr",title:"申请人",align:"left",halign:"center",width:"100px"},
			    {field:"sqbm",title:"部门单位",align:"left",halign:"center",width:"120px"},
			    {field:"sqrq",title:"申请日期",align:"center",halign:"center",width:"120px"},
			    {field:"wdbt",title:"文档标题",align:"left",halign:"center",width:"250px"},
			    {field:"lczt",title:"流程状态",align:"left",halign:"center",width:"80px",
			    	formatter : function(value,row,index){
			    		if("1" == value){//1待审批2已审批9未通过
			    			return "<span style='color:green'>"+row.lcztName+"</span>";
			    		}else if("2" == value){
			    			return "<span style='color:green'>"+row.lcztName+"</span>";
			    		}else if("9" == value){
			    			return "<span style='color:red'>"+row.lcztName+"</span>";
			    		}else{
			    			return "";
			    		}
			    	}
			    },
			    {field:"gh",title:"归还状态",align:"center",halign:"center",width:"75px",
			    	formatter:function(value,row,index){
			    		if(value == "1"){
			    			return "<span style='color:green'>"+row.ghName+"</span>";
			    		}else if(value == "2"){
			    			return "<span style='color:red'>"+row.ghName+"</span>";
			    		}
			    	}
			    },
			    {field:"cz",title:"操作",align:"center",halign:"center",width:"200px",
			    	formatter: function(value,row,index){
						return "<a class='wdjyGridLookLcjlBtns' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>查看流程记录</a>";
					}
			    },
			]],
			onLoadSuccess : function(){
				$(".wdjyGridLookLcjlBtns").linkbutton({
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

		$("a[data-action='wdjy_add']").bind("click",function(){
			openFormWin();
		});
		
		$("a[data-action='wdjy_edit']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				openFormWin(row.id,row.lczt);
			}
		});
		
		$("a[data-action='wdjy_delete']").bind("click",function(){
			deleteByIds();
		});
	}
	
	function bindSearchBtns(){
		$("#"+modelName+"_query").bind("click",function(){
			var formData = queryForm.serializeObject({transcript:"overlay"});
			grid.datagrid("load",formData);
		});
		$("#"+modelName+"_clear").bind("click",function(){
			queryForm.form("clear");
		});
	}
	/**跳转到新增/修改页面**/
	function openFormWin(id,lczt){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : id ? "文档借阅[查看]" : "文档借阅[申请]",
			width : 710,
			height : 276,
			href :URL.formWin,
			onLoad : function(){
				if(id){
					formLoadData(id);
					if(9 != lczt && "9" != lczt){
						$("a[data-action='wdjy_save']").addClass("hide");
					}
				}else{
					$("#wdjy_form_sqrq").datetimebox({
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
	
	/**打开申请借阅记录页面**/
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
	
	/**
	 * 编辑页面获取信息
	 */
	function formLoadData(id){
		$.ajax({
			type : "get",
			url :URL.getInfo,
			data : {id:id},
			success : function(data){
				$("#"+modelName+"_form").form("load", data);
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
						url : URL.deleteByIds,
						data : {
							ids : ids
						},
						success : function(data) {
							/** 如果删除成功，刷新grid数据* */
							if (data.success) {
								$.messager.progress("close");
								$("#" + modelName + "_grid").datagrid("reload");
								showMsg(Msg.delSuc);
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