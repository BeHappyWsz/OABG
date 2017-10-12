$(function(){
	var URL = {
			gridData : ctx + "/oabg/jysh/grid",
			formWin : ctx + "/oabg/jysh/form",
			
			gh : ctx + "/oabg/jysh/gh",
			getInfo : ctx + "/oabg/jysh/getInfo",
			shFormWin : ctx + "/oabg/jysh/shForm",
			deleteUrl : ctx +"/oabg/jysh/delete"
	};
	
	/** 模块名称* */
	var modelName = "jysh";
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
					openFormWin(row.id,row.lczt);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"sqr",title:"申请人",align:"left",halign:"center",width:"100"},
			    {field:"sqbm",title:"部门单位",align:"left",halign:"center",width:"100"},
//			    {field:"lxdh",title:"联系电话",align:"left",halign:"center",width:"150"},
			    {field:"sqrq",title:"申请日期",align:"center",halign:"center",width:"100"},
			    {field:"wdbt",title:"文档标题",align:"left",halign:"center",width:"240"},
//			    {field:"wdbh",title:"文档标号",align:"left",halign:"center",width:"150"},
			    {field:"lczt",title:"流程状态",align:"center",halign:"center",width:"100",
			    	formatter : function(value,row,index){
			    		if("1" == value){//1待审批2已审批9未通过
			    			return "<span style='color:red'>未办</span>";
			    		}else if("2" == value || "9" == value){
			    			return "<span style='color:green'>已办</span>";
			    		}else{
			    			return "";
			    		}
			    	}
			    },
			    {field:"gh",title:"归还状态",align:"center",halign:"center",width:"100",
			    	formatter: function(value,row,index){
			    		if("2" == value){
							return "<a class='jyshgGhBtns' data-myid='"+row.id+"' data-wdbt='"+row.wdbt+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>归还</a>";
			    		}else if("1" == value){
			    			return "<span style='color:green'>已归还</span>";
			    		}
					}
			    }
			]],
			onLoadSuccess : function(){
				$(".jyshgGhBtns").linkbutton({
					plain : true,
					onClick : function(){
						var id = $(this).data("myid");
						var wdbt = $(this).data("wdbt");
						zlgh(id,wdbt);
					}
				});
			}
		})
	}
	
	/*资料归还*/
	function zlgh(id,wdbt) {
		$.messager.confirm('提示','确认借阅已归还吗？',function(r){    
		    if(r){
				$.ajax({
					type : "get",
					url :URL.gh,
					data : {id:id},
					success : function(data){
						$.messager.show({
							title:'提示',
							msg:wdbt+'文档已归还',
							timeout:5000,
							showType:'slide'
						});
						grid.datagrid('reload');
					}
				});
		    }
		 });
	}
	
	/** 绑定Grid操作按钮方法* */
	function bindGridToorBar() {
		$("a[data-action='jysh_sh']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row.lczt=="1"){
				if(row){
					openShFormWin1(row.id);
				}
			}else{
				showMsg("该数据无法进行审核");
			}
		});
	}
	
	
	/**
	 * 审批页面
	 */
	function openShFormWin1(id){
		var win = $("<div id='" + modelName + "_shformWin'></div>").window({
			title :"审核",
			href :URL.shFormWin+"?id="+id,
			width : 500,
			height : 176,
			onLoad : function(){				
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
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
	function openFormWin(id,lczt){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : id ? "[查看]" : "[新增]",
			href :URL.formWin,
			width : 700,
			height : 275,
			onLoad : function(){				
	        	if(id){
					formLoadData(id);
				}else{
					$("#bgypdj_rksj").datetimebox({
						value:getNowFormatDate(),
						showSeconds: false   
					});
				}
	        	if(lczt == 2 || lczt == 9){//1未审核2已审核9审核不通过
	        		$("#jysh_south").html("");
	        	}
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	/**
	 * 审批页面
	 */
	function openShFormWin(id){
		var win = $("<div id='" + modelName + "_shformWin'></div>").window({
			title :"审核",
			href :URL.shFormWin,
			width : 700,
			height : 220,
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
	
	/**跳转到入库新增页面**/
	function openRkFormWin(id){
		var win = $("<div id='" + modelName + "_rKformWin'></div>").window({
			title : "入库[新增]",
			href :URL.formRkWin,
			width : 700,
			height : 300,
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
			href :URL.rkjlFormWin+"?bgypId="+id,
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
				if(data.lcztCode==2||data.lcztCode==3){
					$("#jysh_south").html("");
				}
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