$(function(){
	var URL = {
			gridData : ctx + "/oabg/zcdj/grid",
			formWin : ctx + "/oabg/zcdj/form",
			formRkWin : ctx + "/oabg/zcdj/rkform",
			getInfo : ctx + "/oabg/zcdj/getInfo",
			deleteUrl : ctx +"/oabg/zcdj/delete",
			rkjlFormWin : ctx + "/oabg/zcdj/rkjlForm"
	};
	
	/** 模块名称* */
	var modelName = "zcdj";
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
				openRkFormWin(row.id);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"zcmc",title:"资产名称",align:"left",halign:"center",width:250},
			    {field:"pp",title:"品牌",align:"left",halign:"center",width:150},
			    {field:"dw",title:"单位",align:"left",halign:"center",width:120},
			    {field:"sl",title:"数量",align:"right",halign:"center",width:120},
			    {field:"bm",title:"部门",align:"left",halign:"center",width:150},
			    {field:"syr",title:"使用人",align:"left",halign:"center",width:150},
			    {field:"cz",title:"操作",align:"center",halign:"center",width:200,
			    	formatter: function(value,row,index){
						return "<a class='zcdjGridLookCrkjlBtns' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>查看出入库记录</a>";
					}
			    }
			]],
			onLoadSuccess : function(){
				$(".zcdjGridLookCrkjlBtns").linkbutton({
					plain : true,
					onClick : function(){
						var id = $(this).data("myid");
						openRkjlFormWin(id);
					}
				});
			}
		})
	}
	
	/** 绑定Grid操作按钮方法* */
	function bindGridToorBar() {
		$("a[data-action='ZCDJ_ZCPD']").bind("click",function(){
			openFormWin();
		});
		
		$("a[data-action='ZCDJ_ZCRK']").bind("click",function(){
			openRkFormWin();
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
			title : id ? "资产[查看]" : "资产[新增]",
			href :URL.formWin,
			width : 700,
			height : 276,
			onLoad : function(){
				formLoadFData(id);
	        	if(id){
					
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
	
	/**跳转到入库新增页面**/
	function openRkFormWin(id){
		var win = $("<div id='" + modelName + "_rKformWin'></div>").window({
			title : "入库[新增]",
			href :URL.formRkWin,
			width : 700,
			height : 243,
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
	function openRkjlFormWin(id){
		var win = $("<div id='" + modelName + "_rkjlFormWin'></div>").window({
			title : "出入库记录[查看]",
			href :URL.rkjlFormWin+"?zcglId="+id,
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
				$("#" + modelName + "_rKform").form("load", data);
			}
		});
	}
	
	/**
	 * 编辑页面获取信息
	 */
	function formLoadFData(id){
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