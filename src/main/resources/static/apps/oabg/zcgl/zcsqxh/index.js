$(function(){
	var URL = {
			gridData : ctx + "/oabg/zcsq/xh/grid",
			formWin : ctx + "/oabg/zcsq/xh/form",
			formRkWin : ctx + "/oabg/zcsq/xh/rkform",
			getInfo : ctx + "/oabg/zcsq/xh/getInfo",
			deleteUrl : ctx +"/oabg/zcsq/xh/delete",
			lcFormWin : ctx + "/oabg/zcsq/ly/lcForm"
	};
	
	/** 模块名称* */
	var modelName = "zcsqxh";
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
					openFormWin(row.id);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"sqr",title:"申请人",align:"left",halign:"center",width:120},
			    {field:"sqbm",title:"申请部门",align:"left",halign:"center",width:150},
			    {field:"lxdh",title:"联系电话",align:"left",halign:"center",width:150},
			    {field:"sqrq",title:"申请日期",align:"center",halign:"center",width:120},
			    {field:"zcmc",title:"资产名称",align:"left",halign:"center",width:120},
			    {field:"sl",title:"数量",align:"right",halign:"center",width:120},
			    {field:"ztCode",title:"流程状态",align:"center",halign:"center",width:120,
			    	formatter : function(value,row,index){
			    		if("3" == value||"4" == value||"5" == value||"6" == value||"7" == value){
			    			return "<span style='color:orange'>"+row.zt+"</span>";
			    		}else if("2" == value){
			    			return "<span style='color:green'>"+row.zt+"</span>";
			    		}else if("9" == value){
			    			return "<span style='color:red'>"+row.zt+"</span>";
			    		}else{
			    			return "";
			    		}
			    	}
			    },
			    {field:"cz",title:"操作",align:"center",halign:"center",width:200,
			    	formatter: function(value,row,index){
						return "<a class='lookLcBtns' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>查看流程</a>";
					}
			    }
			]],
			onLoadSuccess : function(){
				$(".lookLcBtns").linkbutton({
					plain : true,
					onClick : function(){
						var id = $(this).data("myid");
						openLcFormWin(id);
					}
				});
			}
		})
	}
	
	/** 绑定Grid操作按钮方法* */
	function bindGridToorBar() {
		$("a[data-action='zcsqxh_add']").bind("click",function(){
			openFormWin();
		});
		
		$("a[data-action='zcsqxh_update']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				openFormWin(row.id);
			}
		});
		
		$("a[data-action='zcsqxh_delete']").bind("click",function(){
			deleteByIds();
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
	
	/**跳转到新增/修改页面**/
	function openFormWin(id){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : id ? "申请销毁[查看]" : "申请销毁[新增]",
			href :URL.formWin,
			width : 700,
			height : 276,
			onLoad : function(){
	        	if(id){
	        		$("a[data-action='zcsqxh_save']").addClass("hide");
					formLoadData(id);
				}else{
					$("#zcsqxh_form_sqrq").datebox({
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
	function openLcFormWin(id){
		var win = $("<div id='" + modelName + "_lcFormWin'></div>").window({
			title : "流程记录[查看]",
			href :URL.lcFormWin+"?zcglSqjlId="+id,
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
				if(9 == data.lcCode || "9" == data.lcCode){
					$("a[data-action='zcsqxh_save']").removeClass("hide");
				}
			}
		});
	}
	
	/**
	 * 删除
	 */
	function deleteByIds(){
		var rows = grid.datagrid("getChecked");
		var ids =new Array(); //可以删除的id
		if(rows.length>0){
			$.messager.confirm('提示','您确认想要删除吗？',function(r){    
			    if(r){
			    	for(var i= 0;i<rows.length;i++){
						var row = rows[i];
						if(row.ztCode == '9'||row.zt == '2'){
							ids.push(row.id);
						}else{
							$.messager.alert("提示","数据已在审批流程中,不能删除!");
							break;
						}
					}
			    	
			    	if(ids.length > 0){
						var idstr = ids.join(",");
						$.ajax({
							type : "post",
							url :URL.deleteUrl,
							data : {ids:idstr},
							success : function(data) {
								/** 显示后台返回的提示信息* */
								showMsg(Msg.delSuc);
								/** 如果删除成功，刷新grid数据，关闭窗口* */
								if (data.success) {
									$.messager.progress('close');
									grid.datagrid('reload');
								}
							}
						});
			    	}
			    }
			});
		}else{
			$.messager.alert("提示","请选择需要【删除】的信息！");
		}
	}
	
});