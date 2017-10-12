$(function(){
	var URL = {
			gridData : ctx + "/oabg/swng/grid",
			//新增页面
			formWin : ctx + "/oabg/swng/form",
			
			getInfo : ctx + "/oabg/swng/getInfo",
			
			deleteByIds : ctx +"/oabg/swng/deleteByIds",
			
			lcjlFormWin : ctx + "/oabg/swng/lcjlForm",
			
			swclFormWin : ctx + "/oabg/swng/swclForm",
			//查看详情页面
			lookFormWin : ctx + "/oabg/swng/lookForm",
			//分发批阅页面
			ffpyFormWin : ctx + "/oabg/swng/ffpyForm"
	};
	
	/** 模块名称* */
	var modelName = "swng";
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
//					lookFormWin(row.id,row.swzt);
					openFormWin(row.id,row.swzt);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"wjly",title:"文件来源",align:"left",halign:"center",width:"80px"},
			    {field:"fwjg",title:"发文机关",align:"left",halign:"center",width:"100px"},
			    {field:"bt",title:"标题",align:"left",halign:"center",width:"300px"},
			    {field:"fs",title:"份数",align:"center",halign:"center",width:"50px"},
			    {field:"ys",title:"页数",align:"center",halign:"center",width:"50px"},
			    {field:"files",title:"附件",align:"center",halign:"center",width:"100px",formatter:fjButtons},
			    {field:"swrq",title:"收文日期",align:"center",halign:"center",width:"100px"},
			    {field:"swzt",title:"流程状态",align:"center",halign:"center",width:"100px",formatter:lcztButton},
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
	//流程状态 1待拟办2待批阅3传阅与销毁4待分办5已传阅6已销毁7已拟办9提交,提交后不可修改,被退回可修改
	function lcztButton(value,row,index){
		if("3" == value || "9" == value){//3待传阅和销毁7已拟办9待提交
			return "<span style='color:red'>未办</span>";
		}else{
			return "<span style='color:green'>已办</span>";
		}
	}
	//操作
	function czButton(value,row,index){
		return "<a class='lcjlGridBtn' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>查看流程记录</a>";
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
		$("a[data-action='swng_add']").bind("click",function(){
			openFormWin();
		});
		
		//查看
		$("a[data-action='swng_open']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				lookFormWin(row.id);
			}
		});
		
		//收文处理
		$("a[data-action='swng_swcl']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){//已批阅可以进行收文处理
				if(3 == row.swzt || "3" == row.swzt){
					swclFormWin(row.id);
				}else{
					showMsg("该数据暂时无法进行收文处理");
				}
			}
		});
		//分发批阅
//		$("a[data-action='swng_ffpy']").bind("click",function(){
//			var row = gridSelectedValid(grid);
//			if(row){
//				if(7 == row.swzt || "7" == row.swzt){
//					ffpyFormWin(row.id);
//				}else{
//					showMsg("该数据无法进行分发批阅");
//				}
//			}
//		});
		//删除
		$("a[data-action='swng_delete']").bind("click",function(){
			deleteByIds();
		});
		//查询
		$("#"+modelName+"_qf_query").bind("click",function(){
			var formData = queryForm.serializeObject();
			grid.datagrid('reload',formData);
		});
		//清空
		$("#"+modelName+"_qf_clear").bind("click",function(){
			queryForm.form("clear");
		});
		
	}

	/**
	 * 分发批阅页面
	 */
	function ffpyFormWin(ids){
		var win = $("<div id='" + modelName + "_ffpyFormWin'></div>").window({
			title : "收文管理[分发批阅]",
			href :URL.ffpyFormWin,
			width : 500,
			height : 110,
			onLoad : function(){
				$("#" + modelName + "_ffpyForm_ids").val(ids);
				$("#" + modelName + "_ffpyForm_index").val("1");
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	
	/**
	 * 收文处理页面
	 */
	function swclFormWin(ids){
		var win = $("<div id='" + modelName + "_swclFormWin1'></div>").window({
			title : "收文管理[处理]",
			href :URL.swclFormWin,
			width : 560,
			height : 198,
			onLoad : function(){
				$("#" + modelName + "_swclForm_ids").val(ids);
				setTimeout(function(){//默认为传阅
					$("#" + modelName + "_swclForm_swcl").combobox("setValue",1);
				},300);
				$("#" + modelName + "_swclForm_swcl").combobox({
					disabled : true
				});
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}

	/**
	 * 修改页面
	 * 流程状态 1待拟办2待批阅3传阅与销毁4待分办5已传阅6已销毁7已拟办9提交,提交后不可修改,被退回可修改
	 * **/
	function openFormWin(id,swzt){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : id ? "修改/查看" : "新增",
			width : 710,
			height : 475,
			href :URL.formWin,
			onLoad : function(){
	        	if(id){//提交、分发批阅、收文处理
	        		$("a[data-action='swng_save']").hide();
	        		$("a[data-action='swng_ffpyform']").hide();
	        		$("a[data-action='swng_swclform']").hide();
	        		if(9 == swzt || "9" == swzt){
						$("a[data-action='swng_save']").show();
					}
	        		if(swzt == "7" || swzt == 7){
	        			$("a[data-action='swng_ffpyform']").show();
	        		}
	        		if(3 == swzt || "3" == swzt	){
	        			$("a[data-action='swng_swclform']").show();
	        		}
					formLoadData(id);
					closeLoadingDiv();
				}else{
					//隐藏，只显示提交按钮
					$("a[data-action='swng_swclform']").hide();
					$("a[data-action='swng_ffpyform']").hide();
					//收文日期 发文日期 默认选择当天
					$("#swng_form_swrq").datebox({
						value:getNowFormatDate()
					});
					$("#swng_form_fwrq").datebox({
						value:getNowFormatDate()
					});
				}
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	/**跳转到查看详情页面页面**/
	function lookFormWin(id,swzt){
		var win = $("<div id='" + modelName + "_lookFormWin'></div>").window({
			title : "查看/操作",
			width : 710,
			height : 405,
			href :URL.lookFormWin+"?id="+id,
			onLoad : function(){
				if(swzt != "7" || swzt != 7){//7已拟办,可分发批阅
					
				}
				if(swzt != "3" || swzt != "3"){//3待批阅,可直接收文处理
					
				}
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	/**获取当前时间，yyyy-MM-dd**/
	function getNowFormatDate() {
	    var date = new Date();
	    var seperator1 = "-";
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
	
	/**
	 * 编辑页面获取信息
	 */
	function formLoadData(id){
		$.ajax({
			type : "post",
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
});