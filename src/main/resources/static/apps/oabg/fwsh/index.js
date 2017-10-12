$(function(){
	var URL = {
			gridData : ctx + "/oabg/fwsh/grid",
			formWin : ctx + "/oabg/fwsh/form",
			formWin1 : ctx + "/oabg/fwsh/form1",
			getInfo : ctx + "/oabg/fwng/getInfo",
			lcjlFormWin : ctx + "/oabg/fwsh/lcjlForm",
			hgformWin : ctx + "/oabg/fwsh/hg",
			csformWin : ctx + "/oabg/fwsh/cs",
			lzspformWin : ctx + "/oabg/fwsh/lzsp",
			fhqfformWin : ctx + "/oabg/fwsh/fhqf",
			fwdgformWin : ctx + "/oabg/fwsh/fwdg",
			fwjdformWin : ctx + "/oabg/fwsh/fwjd",
			yyffformWin : ctx + "/oabg/fwsh/yyff",
			print: ctx + "/oabg/fwsh/print",
			save : ctx + "/oabg/fwng/fwsh/save"
	};
	
	/** 模块名称* */
	var modelName = "fwsh";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	/** 主页查询表单* */
	var queryForm = $("#" + modelName + "_qf");
	/** 渲染Grid* */
	renderGrid();
	/**查询按钮**/
	bindSearchBar() ;
	/**操作按钮**/
	bindGridToorBar();
	
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			onDblClickRow : function(index,row) {
				if(row){
					if("6"==row.fwzt || "7"==row.fwzt || "8"==row.fwzt || "11"==row.fwzt|| "10"==row.fwzt){
						openFormWin1(row.id,row.fwzt);
					}else{
						openFormWin(row.id,row.fwzt);
					}
				}
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"gzbm",title:"部门",align:"left",halign:"center",width:"150px"},
			    {field:"bt",title:"标题",align:"left",halign:"center",width:"300px"},
			    {field:"files",title:"附件",align:"left",halign:"center",width:"200px",formatter : renderDownload},
			    {field:"ddsj",title:"到达时间",align:"center",halign:"center",width:"120px"},
			    {field:"fqrName",title:"发起人",align:"left",halign:"center",width:"100px"},
			    {field:"rq",title:"发起时间",align:"center",halign:"center",width:"120px"},
			    {field:"fwzt",title:"流程状态",align:"left",halign:"center",width:"150px",formatter : lcztButton},
			    {field:"cz",title:"操作",align:"center",halign:"center",width:"150px",formatter : renderFlowBtn}
			]],
			onLoadSuccess : function() {
		       	//查看流程状态
		       	$(".lcztBtn").linkbutton({
	    	 		iconCls : "icon-standard-database-refresh"
		       	});
           	 	
	           	$(".lcztBtn").bind("click", function() {
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
	
	//待修改
	//流程状态 
	function lcztButton(value,row,index){
		
		if(roleCode.indexOf("CZ")!=-1 || roleCode.indexOf("DWBM")!=-1){
			if(roleCode.indexOf("BGSFZR")!=-1){
				if("1"==value || "2"==value || "5"==value || "8"==value){
					return "<span style='color:red'>未办</span>";
				}else{
					return "<span style='color:green'>已办</span>";
				}
			}else{
				if("1"==value || "3"==value){
					return "<span style='color:red'>未办</span>";
				}else{
					return "<span style='color:green'>已办</span>";
				}
			}
			
		}else if(roleCode.indexOf("JYWYSZR")!=-1){
			if("2"==value || "5"==value || "8"==value){
				return "<span style='color:red'>未办</span>";
			}else{
				return "<span style='color:green'>已办</span>";
			}
		}else if(roleCode.indexOf("FGLD")!=-1){
			if("4"==value){
				return "<span style='color:red'>未办</span>";
			}else{
				return "<span style='color:green'>已办</span>";
			}
		}
		
	}
	/**
	 * 渲染按钮
	 */
	function renderFlowBtn(value,row,index){
		return "<a class='lcztBtn' data-rowid='"+row.id+"'>查看流程记录</a>";
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
	
	
	/**绑定列表按钮**/
	function bindGridToorBar() {
		grid.datagrid({
			onSelect:function(index,row){
				if(roleCode.indexOf("CZ")!=-1 || roleCode.indexOf("DWBM")!=-1){
					if(roleCode.indexOf("BGSFZR")!=-1){
						if("1"==row.fwzt){
							$("a[data-action='fwsh_hg']").linkbutton({
								disabled:false
							});
							$("a[data-action='fwsh_cs']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_lzsp']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_fwdg']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_yyff']").linkbutton({
								disabled:true
							});
						}else if("2"==row.fwzt){
							$("a[data-action='fwsh_hg']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_cs']").linkbutton({
								disabled:false
							});
							$("a[data-action='fwsh_lzsp']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_fwdg']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_yyff']").linkbutton({
								disabled:true
							});
						}else if("3"==row.fwzt){
							$("a[data-action='fwsh_hg']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_cs']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_lzsp']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_fwdg']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_yyff']").linkbutton({
								disabled:true
							});
						}else if("5"==row.fwzt){
							$("a[data-action='fwsh_hg']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_cs']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_lzsp']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_fwdg']").linkbutton({
								disabled:false
							});
							$("a[data-action='fwsh_yyff']").linkbutton({
								disabled:true
							});
						}else if("8"==row.fwzt || "10"==row.fwzt || "11"==row.fwzt){
							$("a[data-action='fwsh_hg']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_cs']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_lzsp']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_fwdg']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_yyff']").linkbutton({
								disabled:false
							});
						}else{
							$("a[data-action='fwsh_hg']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_cs']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_fwdg']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_yyff']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_lzsp']").linkbutton({
								disabled:true
							});
						}
						
					}else{
						if("1"==row.fwzt){
							$("a[data-action='fwsh_lzsp']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_hg']").linkbutton({
								disabled:false
							});
						}else if("3"==row.fwzt){
							$("a[data-action='fwsh_lzsp']").linkbutton({
								disabled:false
							});
							$("a[data-action='fwsh_hg']").linkbutton({
								disabled:true
							});
						}else{
							$("a[data-action='fwsh_lzsp']").linkbutton({
								disabled:true
							});
							$("a[data-action='fwsh_hg']").linkbutton({
								disabled:true
							});
						}
					}
					
				}else if(roleCode.indexOf("JYWYSZR")!=-1){
					if("2"==row.fwzt){
						$("a[data-action='fwsh_cs']").linkbutton({
							disabled:false
						});
						$("a[data-action='fwsh_fwdg']").linkbutton({
							disabled:true
						});
						$("a[data-action='fwsh_yyff']").linkbutton({
							disabled:true
						});
					}else if("5"==row.fwzt){
						$("a[data-action='fwsh_cs']").linkbutton({
							disabled:true
						});
						$("a[data-action='fwsh_fwdg']").linkbutton({
							disabled:false
						});
						$("a[data-action='fwsh_yyff']").linkbutton({
							disabled:true
						});
					}else if("8"==row.fwzt || "10"==row.fwzt || "11"==row.fwzt){
						$("a[data-action='fwsh_cs']").linkbutton({
							disabled:true
						});
						$("a[data-action='fwsh_fwdg']").linkbutton({
							disabled:true
						});
						$("a[data-action='fwsh_yyff']").linkbutton({
							disabled:false
						});
					}else{
						$("a[data-action='fwsh_cs']").linkbutton({
							disabled:true
						});
						$("a[data-action='fwsh_fwdg']").linkbutton({
							disabled:true
						});
						$("a[data-action='fwsh_yyff']").linkbutton({
							disabled:true
						});
					}
				}
			}
		})
		$("a[data-action='fwsh_hg']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(1 == row.fwzt || "1" == row.fwzt){
					hgFormWin(row.id,row.fwzt);
				}else{
					showMsg("只能核稿待核稿的数据！");
				}
			}
		});
		
		$("a[data-action='fwsh_cs']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(2 == row.fwzt || "2" == row.fwzt){
					csFormWin(row.id,row.fwzt);
				}else{
					showMsg("只能初审待初审的数据！");
				}
				
			}
		});
		
		$("a[data-action='fwsh_lzsp']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(3 == row.fwzt || "3" == row.fwzt){
					if(roleCode.indexOf("BGSFZR")!=-1){
						showMsg("只能审批待审批的数据！");
					}else{
						lzspFormWin(row.id,row.fwzt);
					}
				}else{
					showMsg("只能审批待审批的数据！");
				}
				
			}
		});
		
		$("a[data-action='fwsh_fhqf_agree']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(4 == row.fwzt || "4" == row.fwzt){
					save(row.id,row.fwzt,"1");
				}else{
					showMsg("只能复核待复核的数据！");
				}
				
			}
		});
		
		$("a[data-action='fwsh_fhqf_cancel']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(4 == row.fwzt || "4" == row.fwzt){
					save(row.id,row.fwzt,"2");
				}else{
					showMsg("只能复核待复核的数据！");
				}
				
			}
		});
		
		$("a[data-action='fwsh_fwdg']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(5 == row.fwzt || "5" == row.fwzt){
					fwdgFormWin(row.id,row.fwzt);
				}else{
					showMsg("只能定稿待定稿的数据！");
				}
				
			}
		});
		
		$("a[data-action='fwsh_yyff']").bind("click",function(){
			var row = gridSelectedValid(grid);
			if(row){
				if(8 == row.fwzt || "8" == row.fwzt || 10 == row.fwzt || "10" == row.fwzt|| "11" == row.fwzt){
					yyffFormWin(row.id,row.fwzt);
				}else{
					showMsg("只能打印待打印的数据！");
				}
				
			}
		});
	}
	
	function hgFormWin(id,fwzt){
		var win = $("<div id='" + modelName + "_hgFormWin'></div>").window({
			title : "发文拟稿[核稿]",
			href :URL.hgformWin,
			width : 500,
			height : 173,
			onLoad : function(){
				$("#" + modelName + "_hgform_id").val(id);
				$("#" + modelName + "_hgform_fwzt").val(fwzt);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	function csFormWin(id,fwzt){
		var win = $("<div id='" + modelName + "_csFormWin'></div>").window({
			title : "发文拟稿[初审]",
			href :URL.csformWin,
			width : 500,
			height : 238,
			onLoad : function(){
				$("#" + modelName + "_csform_id").val(id);
				$("#" + modelName + "_csform_fwzt").val(fwzt);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	function lzspFormWin(id,fwzt){
		var win = $("<div id='" + modelName + "_lzspFormWin'></div>").window({
			title : "发文拟稿[流转审批]",
			href :URL.lzspformWin,
			width : 500,
			height : 174,
			onLoad : function(){
				$("#" + modelName + "_lzspform_id").val(id);
				$("#" + modelName + "_lzspform_fwzt").val(fwzt);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	function fhqfFormWin(id,fwzt){
		var win = $("<div id='" + modelName + "_fhqfFormWin'></div>").window({
			title : "发文拟稿[复核签发]",
			href :URL.fhqfformWin,
			width : 500,
			height : 207,
			onLoad : function(){
				$("#" + modelName + "_fhqfform_id").val(id);
				$("#" + modelName + "_fhqfform_fwzt").val(fwzt);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	function fwdgFormWin(id,fwzt){
		var win = $("<div id='" + modelName + "_fwdgFormWin'></div>").window({
			title : "发文拟稿[发文定稿]",
			href :URL.fwdgformWin,
			width : 500,
			height : 173,
			onLoad : function(){
				$("#" + modelName + "_fwdgform_id").val(id);
				$("#" + modelName + "_fwdgform_fwzt").val(fwzt);
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	function yyffFormWin(id,fwzt){
		var win = $("<div id='" + modelName + "_yyffFormWin'></div>").window({
			title : "发文拟稿[打印分发]",
			href :URL.yyffformWin,
			width : 500,
			height : 250,
			onLoad : function(){
				formLoadData(id);
				$("#" + modelName + "_yyffform_id").val(id);
				$("#" + modelName + "_yyffform_fwzt").val(fwzt);
				if("11"==fwzt || 11==fwzt){
					$("a[data-action='fwsh_yyff_ff']").addClass("hide");
				}
				closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	/**跳转到新增/修改页面**/
	function openFormWin(id,fwzt){
		var win = $("<div id='" + modelName + "_formWin'></div>").window({
			title : "查看详情",
			width : 710,
			height : 700,
			href : URL.formWin+"?id="+id,
			onLoad : function(){
				formLoadData(id);
	        	if(roleCode.indexOf("CZ")!=-1 || roleCode.indexOf("DWBM")!=-1){
	        		if(roleCode.indexOf("BGSFZR")!=-1){
						$("a[data-action='fwsh_hg_']").addClass("hide");
						$("a[data-action='fwsh_cs_']").addClass("hide");
						$("a[data-action='fwsh_lzsp_']").addClass("hide");
			    		$("a[data-action='fwsh_fwdg_']").addClass("hide");
			    		if("1"==fwzt){
		        			$("a[data-action='fwsh_hg_']").removeClass("hide");
		        		}else if("2"==fwzt){
		        			$("a[data-action='fwsh_cs_']").removeClass("hide");
		        		}else if("5"==fwzt){
		        			$("a[data-action='fwsh_fwdg_']").removeClass("hide");
		        		}
		        	}else{
		        		$("a[data-action='fwsh_lzsp_']").addClass("hide");
		        		$("a[data-action='fwsh_hg_']").addClass("hide");
		        		if("1"==fwzt){
		        			$("a[data-action='fwsh_hg_']").removeClass("hide");
		        		}else if("3"==fwzt){
		        			$("a[data-action='fwsh_lzsp_']").removeClass("hide");
		        		}
		        	}
	        		
	        	}else if(roleCode.indexOf("FGLD")!=-1){
	        		if("4"!=fwzt){
	        			$("a[data-action='fwsh_fhqf_agree_']").addClass("hide");
	        			$("a[data-action='fwsh_fhqf_cancel_']").addClass("hide");
	        		}
	        	}else if(roleCode.indexOf("JYWYSZR")!=-1){
	        		$("a[data-action='fwsh_cs_']").addClass("hide");
	        		$("a[data-action='fwsh_fwdg_']").addClass("hide");
	        		if("2"==fwzt){
	        			$("a[data-action='fwsh_cs_']").removeClass("hide");
	        		}else if("5"==fwzt){
	        			$("a[data-action='fwsh_fwdg_']").removeClass("hide");
	        		}
	        	}
	        	closeLoadingDiv();
			},
			onClose : function() {
				win.window('destroy');
			}
		});
	}
	
	/**跳转到新增/修改页面**/
	function openFormWin1(id,fwzt){
		var win = $("<div id='" + modelName + "_formWin1'></div>").window({
			title : "查看详情",
			width : 710,
			height : 617,
			href : URL.formWin1+"?id="+id,
			onLoad : function(){
				formLoadData(id);
				if(roleCode.indexOf("BGSFZR")!=-1){
	        		$("a[data-action='fwsh_fwdy_']").addClass("hide");
	        		$("a[data-action='fwsh_fwjd_']").addClass("hide");
	        		$("a[data-action='fwsh_yyff_']").addClass("hide");
	        		if("6"==fwzt || "7"==fwzt || "8"==fwzt || "10"==fwzt || "11"==fwzt){
	        			$("a[data-action='fwsh_fwdy_']").removeClass("hide");
	        			if("8"==fwzt || "10"==fwzt || "11"==fwzt){
		        			$("a[data-action='fwsh_yyff_']").removeClass("hide");
		        		}
	        		}else if("7"== fwzt){
	        			$("a[data-action='fwsh_fwjd_']").removeClass("hide");
	        		}
	        	}else if(roleCode.indexOf("JYWYSZR")!=-1){
	        		$("a[data-action='fwsh_fwdy_']").addClass("hide");
	        		$("a[data-action='fwsh_fwjd_']").addClass("hide");
	        		$("a[data-action='fwsh_yyff_']").addClass("hide");
	        		if("6"==fwzt || "7"==fwzt || "8"==fwzt || "10"==fwzt || "11"==fwzt){
	        			$("a[data-action='fwsh_fwdy_']").removeClass("hide");
	        			if("8"==fwzt || "10"==fwzt || "11"==fwzt){
		        			$("a[data-action='fwsh_yyff_']").removeClass("hide");
		        		}
	        		}else if("7"== fwzt){
	        			$("a[data-action='fwsh_fwjd_']").removeClass("hide");
	        		}
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
			dataType : "json",
			url :URL.getInfo,
			data : {id:id},
			success : function(data){
				$("#" + modelName + "_form").form("load", data);
				$("#" + modelName + "_form1").form("load", data);
				$("#" + modelName + "_yyffFormWin").form("load", data);

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
	
	//保存复核签发的内容
	function save(id,fwzt,tgzt) {
		/** 序列化form* */
		$.messager.confirm("系统提示","确认审批",function(r){
			if(r){
				$.messager.progress({text:'正在保存，请稍后......'});
				$.ajax({
					type : "post",
					dataType : "json",
					url : URL.save,
					data :{"fwngid":id,"fwzt":fwzt,"tgzt":tgzt},
					success : function(data) {
						$.messager.progress("close");
						if (data.success) {
							showMsg(Msg.saveSuc);
							$("#" + modelName + "_formWin").window("close");
							$("#" + modelName + "_grid").datagrid("reload");
						}
					}
				});
			}
		});
	}
});