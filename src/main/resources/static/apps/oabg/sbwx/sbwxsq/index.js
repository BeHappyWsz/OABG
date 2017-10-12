$(function(){
	var URL = {
			gridData : ctx + "/oabg/sbwx/sbwxsq/grid",
			formWin : ctx + "/oabg/sbwx/sbwxsq/form",
			getInfo : ctx + "/oabg/sbwx/sbwxsq/getInfo",
			lcjlFormWin : ctx +"/oabg/sbwx/sbwxsq/lcjlForm",
			deleteByIds : ctx +"/oabg/sbwx/sbwxsq/deleteByIds"
	};
	
	/** 模块名称* */
	var modelName = "sbwxsq";
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
			    {field:"bxrq",title:"报修日期",align:"center",halign:"center",width:"120"},
			    {field:"gzyy",title:"故障原因",align:"left",halign:"center",width:"250"},
			    {field:"lczt",title:"流程状态",align:"center",halign:"center",width:"120",
			    	formatter : function(value,row,index){//1待批阅2已审批3已维修9未通过
			    		if("1" == value){
			    			return "<span style='color:orange'>"+row.lcztName+"</span>";
			    		}else if("2" == value){
			    			return "<span style='color:orange'>"+row.lcztName+"</span>";
			    		}else if("3" == value){
			    			return "<span style='color:gray'>"+row.lcztName+"</span>";
			    		}else if("9" == value){
			    			return "<span style='color:red'>"+row.lcztName+"</span>";
			    		}
			    	}
			    },
			    {field:"cz",title:"操作",align:"center",halign:"center",width:"120",
			    	formatter: function(value,row,index){
						return "<a class='sbwxsqGridLookLcjlBtns' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>查看流程记录</a>";
					}
			    }
			]],
			onLoadSuccess : function(){
				$(".sbwxsqGridLookLcjlBtns").linkbutton({
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

		$("a[data-action='SBWXSQ_ADDBTN']").bind("click",function(){
			openFormWin();
		});
		$("a[data-action='SBWXSQ_UPDATEBTN']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				openFormWin(row.id,row.lczt);
			}
		});
		$("a[data-action='SBWXSQ_DELETEBTN']").bind("click",function(){
			deleteByIds()
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
			title : id ? "设备维修[修改/查看]" : "设备维修[申请]",
			width : 600,
			height : 221,
			href :URL.formWin,
			onLoad : function(){				
	        	if(id){
	        		if(9 != lczt && "9" != lczt ){
						$("a[data-action='SBWXSQ_SAVEBTN']").addClass("hide");
					}
	        		formLoadData(id)
	        	}else{
	        		$("#"+modelName+"_form_bxrq").datebox({
						value:getNowFormatDate(),
					});
	        	}
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
	    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
	    return currentdate;
	}
	
});