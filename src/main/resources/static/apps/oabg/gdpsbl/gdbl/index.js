$(function(){
	var URL = {
			gridData : ctx + "/oabg/gdpsbl/gdbl/grid",
			
			getInfo : ctx + "/oabg/gdbpsl/gdbl/getInfo",
			
			lcjlFormWin : ctx + "/oabg/gdpsbl/gdbl/lcjlForm",
			//查看详情页面
			formWin : ctx +"/oabg/gdpsbl/gdbl/form",
			//判断当前回复时间
			checkBlsj : ctx + "/oabg/gdpsbl/gdbl/checkBlsj",
			
			blFormWin : ctx + "/oabg/gdpsbl/gdbl/blForm"
	};
	
	/** 模块名称* */
	var modelName = "gdbl";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	var queryForm = $("#"+modelName+"_qf");
	
	/** 渲染Grid* */
	renderGrid();
	/** 绑定Grid操作按钮方法* */
	bindGridToorBar();
	closeLoadingDiv();
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			onDblClickRow : function(index,row) {
					openWin(row.id,row.isBl);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"gdpslxName",title:"工单类型",align:"left",halign:"center",width:"100px"},
			    {field:"pssj",title:"派送时间",align:"center",halign:"center",width:"120px"},
			    {field:"bt",title:"标题",align:"left",halign:"center",width:"300px"},
			    {field:"files",title:"附件",align:"center",halign:"center",width:"100px",formatter:fjButtons},
			    {field:"blqxs",title:"办理期限起",align:"center",halign:"center",width:"115px"},
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
		if("0" == row.isBl){
			return "<span style='color:red'>未办</span>";
		}else if("1" == row.isBl){
			return "<span style='color:green'>已办</span>";
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
		$("a[data-action='gdbl_bl']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				checkBlsj(row.id,isIn);//判断时间是否已经到期1未到期2已到期false出错
				function isIn(data){
					if(data == "false"){
						showMsg("出现错误");
						return;
					}else if(data == "2"){
						showMsg("时间已过无法进行办理");
						return;
					}
					if("1" == row.isBl || 1 == row.isBl){
						showMsg("当前已办理");
					}else{
						blFormWin(row.id);
					}
				}
			}
		});
		
		
		$("#"+modelName+"_qf_query").bind("click",function(){
			var formData = queryForm.serializeObject();
			grid.datagrid('reload',formData);
		});
		
		$("#"+modelName+"_qf_clear").bind("click",function(){
			queryForm.form("clear");
		});
		
	}
	
	/**
	 * 办理页面
	 */
	function blFormWin(id) {
		var win = $("<div id='" + modelName + "_blFormWin'></div>").window({
			title : "工单办理",
			href :URL.blFormWin,
			width : 500,
			height : 146,
			onLoad : function(){
				$("#" + modelName + "_blForm_id").val(id);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	/**查看信息页面**/
	function openWin(id,isBl){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : "查看",
			width : 750,
			height : 523,
			href :URL.formWin+"?id="+id,
			onLoad : function(){	
				if(isBl == "1" || 1 == isBl){
					$("a[data-action='gdbl_blform']").hide();
					showMsg("当前已办理");
				}
				checkBlsj(id,isIn);//判断时间是否已经到期1未到期2已到期false出错
				function isIn(data){
					if(data == "false"){
						showMsg("出现错误");
						return;
					}else if(data == "2"){
						$("a[data-action='gdbl_blform']").hide();
						showMsg("时间已过无法进行办理");
					}
				}
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}

	//判断当前回复日期是否超过回复截止日期
	function checkBlsj(id,callback){
		$.ajax({
			url : URL.checkBlsj,
			type:"post",
			data:{
				id:id
			},
			success:function(data){// 1未超过 2超过3false出现错误
				callback(data.isIn);
			}
		});
	}
});