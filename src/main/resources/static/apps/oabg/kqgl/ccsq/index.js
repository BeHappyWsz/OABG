$(function(){
	var URL = {
			gridData : ctx + "/oabg/kqgl/ccsq/grid",
			formWin : ctx + "/oabg/kqgl/ccsq/form",
			getInfo : ctx + "/oabg/kqgl/ccsq/getInfo",
			lcjlFormWin : ctx +"/oabg/kqgl/ccsq/lcjlFormWin",
			getinfo : ctx + "/oabg/kqgl/ccsq/info",
			deleteByIds : ctx +"/oabg/kqgl/ccsq/deleteByIds"
	};
	
	/** 模块名称* */
	var modelName = "ccsq";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	var sxry = $("#sxry").val();
	/** 主页查询表单* */
	var queryForm = $("#" + modelName + "_qf");
	/** 渲染Grid* */
	renderGrid();
	/** 绑定Grid查询按钮方法* */
	bindSearchBar();
	/** 绑定Grid操作按钮方法* */
	bindGridToorBar();
	
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			onDblClickRow : function(index,row) {
					openFormWin(row.id,row.lczt,row.ccname);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"ccname",title:"姓名",align:"left",halign:"center",width:"100"},
			    {field:"ccbm",title:"部门",align:"left",halign:"center",width:"100"},
			    {field:"lxdh",title:"联系电话",align:"center",halign:"center",width:"150"},
			    {field:"ccaddress",title:"出差地点",align:"center",halign:"center",width:"100"},
			    {field:"ccstr",title:"出差开始时间",align:"center",halign:"center",width:"150"},
			    {field:"ccend",title:"出差结束时间",align:"center",halign:"center",width:"150"},
			    {field:"ccsy",title:"出差事由",align:"left",halign:"center",width:"200"},
			    {field:"lczt",title:"流程状态",align:"center",halign:"center",width:"80",
			    	formatter : function(value,row,index){
			    		if("1" == value||"2"==value||"3"==value||"0"==value){
			    			return "<span style='color:orange'>"+row.lcztName+"</span>";
			    		}else if("4" == value){
			    			return "<span style='color:green'>"+row.lcztName+"</span>";
			    		}else if("5" == value){
			    			return "<span style='color:red'>"+row.lcztName+"</span>";
			    		}else{
			    			return "";
			    		}
			    	}
			    },
			    {field:"cz",title:"操作",align:"center",halign:"center",width:"120",
			    	formatter: function(value,row,index){
						return "<a class='ccsqGridLookLcjlBtns' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>查看流程记录</a>";
					}
			    }
			]],
			onLoadSuccess : function(){
				$(".ccsqGridLookLcjlBtns").linkbutton({
					plain : true,
					onClick : function(){
						var id = $(this).data("myid");
						openLcjlFormWin(id);
					}
				});
			}
		})
	}
	
	/** 绑定Grid操作按钮方法* */
	function bindGridToorBar() {
		$("a[data-action='ccsq-add']").bind("click",function(){
			openFormWin();
		});
		$("a[data-action='ccsq-update']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				openFormWin(row.id,row.lczt,row.ccname);
			}
		});
		$("a[data-action='ccsq-delete']").bind("click",function(){
			deleteByIds()
		});
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
	function openFormWin(id,lczt,ccname){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : id ? "出差[修改/查看]" : "出差[申请]",
			width : 600,
			height : 382,
			href :URL.formWin,
			onLoad : function(){
				 $("#trccname").hide();
	        	if(id){
	        		if(5 != lczt && "5" != lczt  ){
	        			$("a[data-action='ccsq-save']").addClass("hide");
					}
	        		if(sxry!=ccname){
        				$("a[data-action='ccsq-save']").addClass("hide");
        			}
	        		formLoadData(id)
	        	}else{
	        		info();
	        		/*$.ajax({
						type : "post",
						url : URL.getinfo,
						success : function(data) {
							
							$("#name").textbox("setValue",data.name);
							$("#bm").combobox("setValue",data.bmCode);
							$("#bm").combobox("setText",data.bmName);
							$("#lxfs").textbox("setValue",data.lxfs);
						}
					});*/
	        	}
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
				$("#trccname").hide();
				$("#" + modelName + "_form").form("load", data);
				/*$("#" + modelName + "_form").form('load',{ lxfs:data.lxfs});*/
				/*$("#name").textbox("setValue",data.name);*/
				$("#bm").combobox("setValue",data.bmValue);
				$("#bm").combobox("setText",data.bm);
				if(data.ccrname!=null||data.ccrname!=""){
					$('#checkbox').prop("checked",true);
					$("#trccname").show();
					$.parser.parse('#trccname');
					$("#" + modelName + "_form").form('load',{ ccrname:data.ccrname});
				}else{
					$('#checkbox').prop("checked",false);
					 $("#trccname").hide();
				}
				
				
			}
		});
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
	
	/**
	 * 删除
	 */
	function deleteByIds(){
		var rows = gridChecked(grid);
		if(rows){
			if(rows.indexOf("4")==0 || rows.indexOf("5")==0){
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
									$.messager.show({
										title:'提示',
										msg: "删除成功！",
										timeout:1000,
										showType:'slide'
									});
									/** 如果删除成功，刷新grid数据，关闭窗口* */
									if (data.success) {
										$.messager.progress('close');
										grid.datagrid('reload');
									}else{
										$.messager.show({
											title:'提示',
											msg: data.returnMsg,
											timeout:1000,
											showType:'slide'
										});
									}
								}
							});
					    }
					});
				}
			}else{
				$.messager.alert("提示", "您勾选的数据尚未审核完成");
			}
		}
	}
	
	function info(){
			$.ajax({
				type : "post",
				url : URL.getinfo,
				success : function(value) {
					$("#ccsq_form").form('load',{ name:value.name});
					$("#ccsq_form").form('load',{ lxfs:value.lxfs});
					$("#bm").combobox("setValue",value.bmCode);
					$("#bm").combobox("setText",value.bmName);
					/*$("#lxfs").textbox("setValue",value.lxfs);*/
				}
			});
		}
});