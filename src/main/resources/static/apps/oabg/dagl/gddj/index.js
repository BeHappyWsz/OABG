$(function(){
	var URL = {
			gridData : ctx + "/oabg/gddj/grid",
			
			formWin : ctx + "/oabg/gddj/form",
			
			getInfo : ctx + "/oabg/gddj/getInfo",
			
			deleteByIds : ctx +"/oabg/gddj/deleteByIds",
			
			exportUrl : ctx +"/oabg/gddj/export"
	};
	
	/** 模块名称* */
	var modelName = "gddj";
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
					openFormWin(row.id,'查看/编辑');
			},
			columns:[[
			    {field:"id",checkbox:true}, 
//			    {field:"gdlx",title:"归档类型",align:"center",halign:"center",width:"60px"},
			    {field:"swrq",title:"收文日期",align:"center",halign:"center",width:"100px"},
			    {field:"fwzh",title:"发(原)文字号",align:"left",halign:"center",width:"140px"},
			    {field:"fwjg",title:"发文机关",align:"left",halign:"center",width:"120px"},
			    {field:"bt",title:"文件标题",align:"left",halign:"center",width:"220px"},
			    {field:"fs",title:"份数",align:"right",halign:"center",width:"60px"},
			    {field:"files",title:"附件",align:"right",halign:"center",width:"120px",formatter:fjButtons},
			    {field:"dabh",title:"归档编号",align:"center",halign:"center",width:"140px"},
			    {field:"gdrq",title:"归档日期",align:"center",halign:"center",width:"100px"},
			]],
			onLoadSuccess : function(){
				$(".fileBtn").each(function(){
					var fileName = $(this).data("filename");
					icon = getLxByFileName(fileName);
					$(this).linkbutton({
						iconCls: icon,
						plain : true,
						onClick : function(){
							var url = $(this).data("url");
							doDwonLoad(url);
						}
					});
					$(this).tooltip({
						positions : 'right',
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
	
	//附件下载
	function fjButtons(value,row,index){
		if(row.files.length == 0){
			return;
		}
		var button = "";
		var files = row.files.split("&");
		for (var i=0;i<files.length;i++){
			var file = files[i].split("#");
			button += " <a class='fileBtn'  href='#' data-url='"+file[0]+"' data-filename='"+file[1]+"'  ></a>";
		}
		return button;
	}
	/**文件下载**/
	function doDwonLoad(url){
		window.open(ctx+url);
	}
	
	/** 绑定Grid操作按钮方法* */
	function bindGridToorBar() {

		$("a[data-action='gddj_add']").bind("click",function(){
			openFormWin();
		});
		
		$("a[data-action='gddj_delete']").bind("click",function(){
			deleteByIds();
		});
		
		$("#"+modelName+"_qf_query").bind("click",function(){
			var formData = queryForm.serializeObject();
			grid.datagrid('reload',formData);
		});
		
		$("#"+modelName+"_qf_clear").bind("click",function(){
			queryForm.form("clear");
		});
		
		$("#"+modelName+"_qf_export").bind("click",function(){
			var gdlx = $("#"+modelName+"_gdlx").combo('getValue');
			if("1" == gdlx || 1 == gdlx){//已进行了查询
				$.messager.confirm("提示","是否导出收文登记记录?",function(r){
					if(r){
						window.open(ctx+"/oabg/gddj/export");
					}
				});
			}else{
				showMsg("请先查询正常收文列表");
			}
		});
	}
	
	/**页面**/
	function openFormWin(id,title){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : "文档查看",
			width : 710,
			height : 255,
			href :URL.formWin,
			onLoad : function(){
	        	if(id){
					formLoadData(id);
				}else{
					$("#gddj_form_gdrq").datetimebox({
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
	
	/** 自动生成档案编号 **/
	function getDabh(){
		var date = new Date();
		return data.getFullYear()+date.getMonth();
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
				KindEditor.html("#oabg_gddj_zw",data.zw);
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