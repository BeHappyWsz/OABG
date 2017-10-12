$(function(){
	var URL = {
			gridData : ctx + "/oabg/tssw/grid",
			//新增页面
			formWin : ctx + "/oabg/tssw/form",
			
			getInfo : ctx + "/oabg/tssw/getInfo",
			
			deleteByIds : ctx +"/oabg/tssw/deleteByIds",
			
			lcjlFormWin : ctx + "/oabg/tssw/lcjlForm",
			//查看详情页面
			lookFormWin : ctx +"/oabg/tssw/lookForm"
	};
	
	/** 模块名称* */
	var modelName = "tssw";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	var queryForm = $("#"+modelName+"_qf");
	
	/** 渲染Grid* */
	renderGrid();
	/** 绑定Grid操作按钮方法* */
	bindGridToorBar();
	
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			onDblClickRow : function(index,row) {
//					openFormWin(row.id,row.tsswztCode);
				//只能查看且页面样式改变
					openWin(row.id);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"swrq",title:"收文日期",align:"center",halign:"center",width:"100px"},
			    {field:"tsswlxName",title:"收文类型",align:"center",halign:"center",width:"100px"},
			    {field:"bt",title:"标题",align:"left",halign:"center",width:"300px"},
			    {field:"files",title:"附件",align:"center",halign:"center",width:"100px",formatter:fjButtons},
			    {field:"hfrqs",title:"回复日期起",align:"center",halign:"center",width:"115px"},
			    {field:"hfrqe",title:"回复日期止",align:"center",halign:"center",width:"115px"},
			    {field:"tsswztCode",title:"流程状态",align:"center",halign:"center",width:"100px",formatter:lcztButton},
			    {field:"cz",title:"操作",align:"center",halign:"center",width:"100px",formatter:czButton},
			]],
			onLoadSuccess : function() {
				$(".lcjlGridBtn").linkbutton({
					plain : true,
					onClick : function(){
						var id = $(this).data("myid");
						openLcjlFormWin(id);
					}
				});
				
				$(".fileBtn").each(function(){
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
	
	//流程状态 1待回复2待处理
	function lcztButton(value,row,index){
		if("1" == value){
			return "<span style='color:green'>已办</span>";
		}else if("2" == value){
			return "<span style='color:orange'>已回复</span>";
		}
	}
	//附件下载
	function fjButtons(value,row,index){
		if(row.files.length == 0){
			return;
		}
		var button = "";
		var files = row.files.split("&");
		for (var i=0;i<files.length;i++){
			var file = files[i].split("#");
			button += " <a class='fileBtn' href='#' data-url='"+file[0]+"' data-filename='"+file[1]+"' ></a>";
		}
		return button;
	}
	
	/**文件下载**/
	function doDwonLoad(url){
		window.open(ctx+url);
	}
	
	//操作
	function czButton(value,row,index){
		return "<a class='lcjlGridBtn' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>查看流程记录</a>";
	}
	
	/**打开流程记录页面**/
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
	
	/** 绑定Grid操作按钮方法* */
	function bindGridToorBar() {
		//新增
		$("a[data-action='tssw_add']").bind("click",function(){
			openFormWin();
		});
		
		//查看
		$("a[data-action='tssw_open']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
//				openFormWin(row.id,row.tsswztCode);
				openWin(row.id);
			}
		});
		//删除
		$("a[data-action='tssw_delete']").bind("click",function(){
			deleteByIds();
		});
		
		$("#"+modelName+"_qf_query").bind("click",function(){
			var formData = queryForm.serializeObject();
			grid.datagrid('reload',formData);
		});
		
		$("#"+modelName+"_qf_clear").bind("click",function(){
			queryForm.form("clear");
		});
		
	}
	
	/**新增信息页面**/
	function openFormWin(id,tsswzt){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : id ? "修改/查看" : "新增",
			width : 710,
			height : 653,
			href :URL.formWin,
			onLoad : function(){
				//渲染富文本
	        	window.editor1=KindEditor.create('#oabg_tssw_zw',{});
	        	if(id){
	        		$("a[data-action='tssw_save']").addClass("hide");
					formLoadData(id);
				}else{
					$("#tssw_form_swrq").datetimebox({
						value:getNowFormatDate() 
					});
				}
	        	closeLoadingDiv();
			},
			onBeforeClose: function () {
                KindEditor.remove('#oabg_tssw_zw');
            },
			onClose : function() {
				window.editor1.remove();
				win.window('destroy');
			}
		});
	}
	
	
	/**查询已存在信息页面**/
	function openWin(id){
		var win = $("<div id='" + modelName + "_lookFormWin'></div>").window({
			title : "查看",
			width : 750,
			height : 523,
			href :URL.lookFormWin+"?id="+id,
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
	
	/**跳转到查询页面**/
	function openFormSearch(){
		var win = $("<div id='" + modelName + "_formSearch'></div>").window({
			title : "发文查询",
			width : 1000,
			height : 700,
			href :URL.formSearch,
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
				KindEditor.html("#oabg_tssw_zw",data.zw);
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
							showMsg(Msg.delSuc);
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
});