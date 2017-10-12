$(function(){
	var URL = {
			gridData : ctx + "/oabg/lwypdj/grid",
			formWin : ctx + "/oabg/lwypdj/form",
			getInfo : ctx + "/oabg/lwypdj/getInfo",
			deleteUrl : ctx +"/oabg/lwypdj/delete",
			rkjlFormWin : ctx + "/oabg/lwypdj/rkjlForm",
			checkJjkc : ctx + "/oabg/lwypdj/checkHasJjjl",
			gridBjData : ctx + "/oabg/lwypdj/jjGrid"
	};
	
	/** 模块名称* */
	var modelName = "lwypdj";
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
			    {field:"name",title:"物品名称",align:"left",halign:"center",width:250},
			    {field:"dw",title:"单位",align:"left",halign:"center",width:150},
			    {field:"sl",title:"数量",align:"right",halign:"center",width:120},
			    {field:"jjsl",title:"警戒数量",align:"right",halign:"center",width:120},
			    {field:"cz",title:"操作",align:"center",halign:"center",width:200,
			    	formatter: function(value,row,index){
						return "<a class='lwypdjGridLookCrkjlBtns' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>查看出入库记录</a>";
					}
			    }
			]],
			onLoadSuccess : function(){
				$(".lwypdjGridLookCrkjlBtns").linkbutton({
					plain : true,
					onClick : function(){
						var id = $(this).data("myid");
						openRkjlFormWin(id);
					}
				});
			}
		});
	}
	
	/** 绑定Grid操作按钮方法* */
	function bindGridToorBar() {
		$("a[data-action='LWYPDJ_ADDBTN']").bind("click",function(){
			openFormWin();
		});
		
		$("a[data-action='LWYPDJ_UPDATEBTN']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				openFormWin(row.id);
			}
		});
		
		$("a[data-action='LWYPDJ_DELETEBTN']").bind("click",function(){
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
			title : id ? "劳务用品[修改/查看]" : "劳务用品[新增]",
			href :URL.formWin,
			width : 700,
			height : 210,
			onLoad : function(){				
	        	if(id){
					formLoadData(id);
				}
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	/**打开出入记录页面**/
	function openRkjlFormWin(id){
		var win = $("<div id='" + modelName + "_rkjlFormWin'></div>").window({
			title : "出入库记录[查看]",
			href :URL.rkjlFormWin+"?lwypId="+id,
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
			}
		});
	}
	
	/**
	 * 删除
	 */
	function deleteByIds(){
		commonBatchDelete(grid,URL.deleteUrl,modelName);
	}
	
	checkKcjj();
	/** 查询库存警戒 **/
	function checkKcjj(){
		$.ajax({
			url : URL.checkJjkc,
			type : "post",
			data : {},
			success : function(data){
				if(data.success){
					var windowWidth=460;
					var windowHeight=200;
					var mt = 0;
					if($("#noticeDialogWin").size() != 0){
						mt = 185;
					}
					if($("#bgypdj_kcbjFormWin").size() != 0){
						mt += 205;
					}
					var win = $("<div id='" + modelName + "_kcbjFormWin'></div>").window({
						title : "劳务用品库存警报列表",
						width : windowWidth,
						height : windowHeight,
						left:document.documentElement.clientWidth-windowWidth,
						top:document.documentElement.clientHeight-windowHeight-mt,
						resizable:false,
						modal:false,
						draggable:false,
						content:"<table id='"+modelName+"_kcbjgrid'></table>",
						onOpen : function(){
							renderJjGrid(data.data);
						},
						onClose : function() {
							win.window('destroy');
						}
					});
					/** 加入销毁列表，关闭标签页时销毁窗体* */
					simpleweb.main.window.put(modelName + "_kcbjFormWin", win, "window");
				}
				
			}
		});
	}
	
	function renderJjGrid(ids){
		$("#"+modelName+"_kcbjgrid").datagrid({
			url :URL.gridBjData+"?ids="+ids,
			columns:[[
			    {field:"id",checkbox:true},
			    {field:"name",title:"物品名称",align:"left",halign:"center",width:150},
			    {field:"dw",title:"单位",align:"left",halign:"center",width:80},
			    {field:"sl",title:"数量",align:"right",halign:"center",width:80},
			    {field:"jjsl",title:"警戒数量",align:"right",halign:"center",width:80}
			]]
		})
	}
});