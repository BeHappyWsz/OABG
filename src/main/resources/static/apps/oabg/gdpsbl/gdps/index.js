$(function(){
	var URL = {
			gridData : ctx + "/oabg/gdpsbl/gdps/grid",
			//新增页面
			formWin : ctx + "/oabg/gdpsbl/gdps/form",
			
			getInfo : ctx + "/oabg/gdpsbl/gdps/getInfo",
			
			lcjlFormWin : ctx + "/oabg/gdpsbl/gdps/lcjlForm",
			//查看详情页面
			lookFormWin : ctx +"/oabg/gdpsbl/gdps/lookForm"
	};
	
	/** 模块名称* */
	var modelName = "gdps";
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
					openWin(row.id);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"gdpslxName",title:"工单类型",align:"left",halign:"center",width:"100px"},
			    {field:"sldw",title:"受理单位",align:"left",halign:"center",width:"100px"},
			    {field:"pssj",title:"派送时间",align:"center",halign:"center",width:"120px"},
			    {field:"bt",title:"标题",align:"left",halign:"center",width:"300px"},
			    {field:"files",title:"附件",align:"center",halign:"center",width:"100px",formatter:fjButtons},
//			    {field:"blqxs",title:"办理期限起",align:"center",halign:"center",width:"115px"},
			    {field:"blqxe",title:"办理期限止",align:"center",halign:"center",width:"115px"},
			    {field:"gdpszt",title:"流程状态",align:"center",halign:"center",width:"90px",formatter:lcztButton},
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
		$("a[data-action='gdps_add']").bind("click",function(){
			openFormWin();
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
	function openFormWin(){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : "新增",
			width : 710,
			height : 680,
			href :URL.formWin,
			onLoad : function(){
				//渲染富文本
	        	window.editor1=KindEditor.create('#oabg_gdps_gdnr',{});
				$("#gdps_form_pssj").datetimebox({
					value:getNowFormatDate() 
				});
	        	closeLoadingDiv();
			},
			onBeforeClose: function () {
                KindEditor.remove('#oabg_gdps_gdnr');
            },
			onClose : function() {
				window.editor1.remove();
				win.window('destroy');
			}
		});
	}
	
	
	/**查看已存在信息页面**/
	function openWin(id){
		var win = $("<div id='" + modelName + "_lookFormWin'></div>").window({
			title : "查看",
			width : 750,
			height : 640,
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

	
});