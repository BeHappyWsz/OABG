$(function(){
	var URL = {
			gridData : ctx + "/oabg/swsh/grid",
			
			formWin : ctx + "/oabg/swsh/form",
			
			getInfo : ctx + "/oabg/swsh/getInfo",
			
			lcjlFormWin : ctx + "/oabg/swsh/lcjlForm",
			/** 拟办**/
			nbFormWin : ctx + "/oabg/swsh/nbForm",
			/** 批阅**/
			spFormWin : ctx + "/oabg/swsh/spForm"
	};
	
	/** 模块名称* */
	var modelName = "swsh";
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
					openFormWin(row.id,row.swzt);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"swrq",title:"收文日期",align:"center",halign:"center",width:"90px"},
			    {field:"fwrq",title:"原文日期",align:"center",halign:"center",width:"90px"},
			    {field:"fwzh",title:"原文号",align:"left",halign:"center",width:"150px"},
			    {field:"fwjg",title:"发文机关",align:"left",halign:"center",width:"100px"},
			    {field:"bt",title:"文件标题",align:"left",halign:"center",width:"320px"},
			    {field:"files",title:"附件",align:"center",halign:"center",width:"90px",formatter:fjButtons},
			    {field:"swzt",title:"流程状态",align:"center",halign:"center",width:"90px",formatter:lcztButton},
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
	
	//流程状态 1拟办2批阅3传阅与分办4已传阅5已分办6已销毁7已拟办9待提交,提交后不可修改,被退回可修改
	function lcztButton(value,row,index){
		
		if("FGLD" == role || "JZ" == role){//当前用户为分管领导
			if(row.isPy == "1"){
				return "<span style='color:green'>已办</span>";
			}else{
				if(row.swzt == "6" || "4" == row.swzt){//已传阅 已销毁
					return "<span style='color:green'>已办</span>";
				}
				return "<span style='color:red'>未办</span>";
			}
		}
		//下面为办公室负责人的判断与显示状态
		if("1" == value){
			return "<span style='color:red'>未办</span>";
		}else {
			return "<span style='color:green'>已办</span>";
		}
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
		$("a[data-action='swsh_nb']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(row.swzt == "1" || row.swzt == 1){
					nbFormWin(row.id);
				}else{
					showMsg("该数据无法进行拟办");
				}
			}
		});
		
		$("a[data-action='swsh_py']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(row.swzt == "2" && "0" == row.isPy){
					spFormWin(row.id);
				}else{
					showMsg("该数据无法进行批阅");
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
	 * 办公室负责人拟办页面
	 * 流程状态 1拟办2批阅3传阅与分办4已分办5已传阅6已销毁7已拟办9待提交,提交后不可修改,被退回可修改
	 */
	function nbFormWin(ids) {
		var win = $("<div id='" + modelName + "_nbFormWin'></div>").window({
			title : "收文管理[拟办]",
			href :URL.nbFormWin,
			width : 500,
			height : 212,
			onLoad : function(){
				$("#" + modelName + "_nbform_ids").val(ids);
				$("#" + modelName + "_nbform_index").val("1");
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	/**
	 * 分管领导批阅页面
	 * 流程状态 1拟办2批阅3传阅与分办4已分办5已传阅6已销毁7已拟办9待提交,提交后不可修改,被退回可修改
	 */
	function spFormWin(ids) {
		var win = $("<div id='" + modelName + "_spFormWin'></div>").window({
			title : "收文管理[批阅]",
			href :URL.spFormWin,
			width : 500,
			height : 177,
			onLoad : function(){
				$("#" + modelName + "_spform_ids").val(ids);
				$("#" + modelName + "_spform_index").val("1");
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	/**跳转到新增/修改页面**/
	function openFormWin(id,lczt){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : id ? "修改/查看" : "新增",
			width : 710,
			height : 405,
			href :URL.formWin+"?id="+id,
			onLoad : function(){
				if(1 != lczt && "1" != lczt){
					$("a[data-action='swsh_nbForm']").addClass("hide");
				}
				if(2 != lczt && "2" != lczt){
					$("a[data-action='swsh_pyForm']").addClass("hide");
				}
				formLoadData(id);
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
				//判断当前用户是否已经审核
				if("1" == data.isPy || 1 == data.isPy){
					$("a[data-action='swsh_pyForm']").addClass("hide");
					showMsg("当前已批阅");
				}
			}
		});
	}

});