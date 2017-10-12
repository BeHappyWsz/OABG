$(function(){
	var URL = {
			gridData : ctx + "/oabg/fwng/grid",
			
			formWin : ctx + "/oabg/fwng/form",
			
			formSearch : ctx + "/oabg/fwng/search",
			
			getInfo : ctx + "/oabg/fwng/getInfo",
			
			deleteByIds : ctx +"/oabg/fwng/deleteByIds",
			
			lcjlFormWin : ctx + "/oabg/fwng/lcjlForm"
	};
	
	/** 模块名称* */
	var modelName = "fwng";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	/** 主页查询表单* */
	var queryForm = $("#" + modelName + "_qf");
	
	/** 渲染Grid* */
	renderGrid();
	
//	closeLoadingDiv();
	
	/** 绑定Grid操作按钮方法* */
	bindGridToorBar();
	/** 绑定Grid查询按钮方法* */
	bindSearchBar();
	
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			onDblClickRow : function(index,row) {
					openFormWin(row.id,row.fwzt);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"gzbm",title:"部门",align:"left",halign:"center",width:"150px"},
			    {field:"bt",title:"标题",align:"left",halign:"center",width:"300px"},
			    {field:"files",title:"附件",align:"left",halign:"center",width:"200px",formatter : renderDownload},
			    {field:"mj",title:"密级",align:"center",halign:"center",width:"120px"},
			    {field:"rq",title:"日期",align:"center",halign:"center",width:"150px"},
			    {field:"cklc",title:"流程",align:"center",halign:"center",width:"150px",formatter : renderFlowBtn},
			    {field:"fwzt",title:"通过状态",align:"center",halign:"center",width:"150px",hidden:true}
			]],
			onLoadSuccess : function() {
				$(".lczt_fwngBtn").linkbutton({
	    	 		iconCls : "icon-standard-database-refresh"
		       	});
				
				$(".lczt_fwngBtn").bind("click", function() {
        	 		var id = $(this).data("rowid");
        	 		openLcjlFormWin(id);
	        	 });
				
				$(".fileDownBtn").each(function(){
					var fileName = $(this).data("filename");
					var icon = getLxByFileName(fileName);
					$(this).linkbutton({
						iconCls : icon,
						plain : true,
						onClick : function(){
							var url = $(this).data("url");
							doDwonLoad(url);
						}
					});
					$(this).tooltip({    
						position: 'right',    
						content: "<span style='color:#fff'>"+fileName+"</span>",    
						onShow: function(){        
							$(this).tooltip('tip').css({            
								backgroundColor: '#666',            
								borderColor: '#666'        
							});    
						}
					});
				});
			}
		})
	}
	
	/** 绑定Grid操作按钮方法* */
	function bindGridToorBar() {

		$("a[data-action='fwng_add']").bind("click",function(){
			openFormWin();
		});
		
		$("a[data-action='fwng_update']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				openFormWin(row.id);
			}
		});
		
		$("a[data-action='fwng_search']").bind("click",function(){
			openFormSearch();
		});
		
		$("a[data-action='fwng_delete']").bind("click",function(){
			deleteByIds();
		});
	}
	
	/**查看流程**/
	function renderFlowBtn(value,row,index){
		
		return "<a class='lczt_fwngBtn' data-rowid='"+row.id+"'>查看流程记录</a>";
		
	}
	/**下载**/
	function renderDownload(value,row,index){
		if(row.files.length == 0){
			return;
		}
		var button = "";
		var files = row.files.split("&");
		for (var i=0;i<files.length;i++){
			var file = files[i].split("#");
			button += " <a class='fileDownBtn' href='#' data-url='"+file[0]+"' data-filename='"+file[1]+"' ></a>";
		}
		return button;
	}
	/**文件下载**/
	function doDwonLoad(url){
		window.open(ctx+url);
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
	function openFormWin(id,fwzt){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : id ? "修改/查看" : "新增",
			width : 750,
			height : 700,
			href :URL.formWin,
			onLoad : function(){
				//渲染富文本
	        	window.editor1=KindEditor.create('#oabg_fwng_zw',{});
	        	if(id){
					formLoadData(id);
					if("9"!=fwzt){
						$("#oabg_fwng_form").layout('remove','south');
					}
					
				}else{
					$("#fwng_ngrq").datetimebox({
						value:getNowFormatDate(),
						showSeconds: false   
					});
				}
	        	closeLoadingDiv();
			},
			onBeforeClose: function () {
                KindEditor.remove('#oabg_fwng_zw');
            },
			onClose : function() {
				window.editor1.remove();
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
			dataType : "json",
			url :URL.getInfo,
			data : {id:id},
			success : function(data){
				$("#" + modelName + "_form").form("load", data);
				KindEditor.html("#oabg_fwng_zw",data.zw);
			}
		});
	}
	
	/**
	 * 删除
	 */
	function deleteByIds(){
		var ids=gridCheckedValid(grid);
		if(ids){
			$.messager.confirm('提示','您确认想要删除吗？',function(r){    
			    if(r){
			    	$.ajax({
						type : "post",
						dataType : "json",
						url :URL.deleteByIds,
						data : {ids:ids},
						success : function(data) {
							/** 显示后台返回的提示信息* */
							showMsg(data.returnMsg);
							/** 如果删除成功，刷新grid数据，关闭窗口* */
							if (data.success) {
								$.messager.progress('close');
								grid.datagrid('reload');
							}
						}
					});
			    }
			});
		}else{
			$.messager.alert("提示","请选择需要【删除】的信息！");
		}
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
});