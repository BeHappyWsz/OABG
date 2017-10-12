$(function(){
	var URL = {
			gridData : ctx + "/oabg/wddb/db/grid",
			formWin : ctx + "/oabg/wddb/db/form",
			getInfo : ctx + "/oabg/wddb/db/getInfo",
			changeIfHasBl : ctx + "/oabg/wddb/db/changeIfHasBl"
	};
	
	/** 模块名称* */
	var modelName = "wddb_db";
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
					openFormWin(row.id,row.sjly,row.glid,row.sfyb,row.lczt);
			},
			columns:[[
			    {field:"id",checkbox:true}, 
			    {field:"title",title:"标题",align:"left",halign:"center",width:250},
			    {field:"sjsj",title:"收件时间",align:"center",center:"center",width:150},
			    {field:"sfyb",title:"办理状态",align:"center",center:"center",width:150,formatter:function(value,row,index){
			    	if("1" == value){
			    		return "<span style='color:green'>已办</span>";
			    	}else{
			    		return "<span style='color:orange'>未办</span>";
			    	}
			    }},
			    {field:"blsj",title:"办理时间",align:"center",center:"center",width:150},
			    {field:"sjlyName",title:"数据来源",align:"center",halign:"center",width:120},
			    {field:"sfxh",title:"销毁状态",align:"center",center:"center",width:150,formatter:function(value,row,index){
			    	if("1" == value){
			    		return "<span style='color:red'>已销毁</span>";
			    	}else if("2" == value){
			    		return "<span style='color:green'>未销毁</span>";
			    	}else{
			    		return "";
			    	}
			    }}
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
	function openFormWin(id,sjly,glid,sfyb,lcztcode){
		var wh = {
				10 : {
					width : 710,
					height : 500,
					formWinId : "swsh_formWin"
				},
				20 : {
					width : 600,
					height : 218,
					formWinId : "qjsp_formWin"
				},
				30 : {
					width : 600,
					height : 274,
					formWinId : "ccsp_formWin"
				},
				40 : {
					width : 700,
					height : 276,
					formWinId : "zcsp_formWin"
				},
				50 : {
					width : 700,
					height : 217,
					formWinId : "bgypsp_formWin"
				},
				60 : {
					width : 700,
					height : 217,
					formWinId : "lwypsp_formWin"
				},
				70 : {
					width : 700,
					height : 222,
					formWinId : "gzsh_formWin"
				},
				80 : {
					width : 700,
					height : 172,
					formWinId : "jysh_formWin"
				},
				90 : {
					width : 710,
					height : 645,
					formWinId : "fwsh_formWin"
				},
				100 : {
					width : 710,
					height : 500,
					formWinId : "tsswHf_formWin"
				},
				110 : {
					width : 710,
					height : 507,
					formWinId : "gwjdsh_formWin"
				},
				120 : {
					width : 710,
					height : 496,
					formWinId : "hypxsh_formWin"
				},
				130 : {
					width : 600,
					height : 199,
					formWinId : "sbwxsp_formWin"
				},
				140 : {
					width : 850,
					height : 300,
					formWinId : "xxbsly_formWin"
				}
		};
		var formWinId = wh[sjly].formWinId;
		if("10" == sjly){
			if("1" == lcztcode || "2" == lcztcode){
				formWinId = "swsh_formWin";
			}else{
				formWinId = "swng_formWin";
			}
		}else if("110" == sjly){
			if("4" == lcztcode || "5" == lcztcode){
				formWinId = "gwjdsp_formWin";
			}else{
				formWinId = "gwjdsh_formWin";
			}
		}else if("120" == sjly){
			if("4" == lcztcode || "5" == lcztcode){
				formWinId = "hypxsp_formWin";
			}else{
				formWinId = "hypxsh_formWin";
			}
		}else if("90" == sjly){
			if("6"==lcztcode || "7"==lcztcode || "8"==lcztcode || "11"==lcztcode|| "10"==lcztcode){
				wh[sjly].height=535;
				formWinId = "fwsh_formWin1";
			}else{
				formWinId = "fwsh_formWin";
			}
		}
		var win = $("<div id='"+formWinId+"' class='closeWin'></div>").window({
			title : "待办工作[办理]",
			href :URL.formWin+"?sjly="+sjly+"&id="+id+"&glid="+glid+"&lcztcode="+lcztcode,
			width : wh[sjly].width,
			height : wh[sjly].height,
			onLoad : function(){
				if(10 == sjly){
					//收文管理
					$("a[data-action='swsh_nbForm']").addClass("hide");
	        		$("a[data-action='swsh_pyForm']").addClass("hide");
	        		$("a[data-action='swng_swclform']").addClass("hide");
	        		$("a[data-action='swng_save']").addClass("hide");
					if(2 == sfyb){
						//未办，则显示按钮
						if(1 == lcztcode){
							$("a[data-action='swsh_nbForm']").removeClass("hide");
						}else if(2 == lcztcode){
							$("a[data-action='swsh_pyForm']").removeClass("hide");
						}else if(3 == lcztcode){
							$("a[data-action='swng_swclform']").removeClass("hide");
						}
					}
				}else if(20 == sjly){
					//请假管理
					$("a[data-action='qjsp-czspp']").addClass("hide");
					if(2 == sfyb){
						//未办，则显示按钮
						if(1 == lcztcode){
							$("a[data-action='qjsp-czspp']").removeClass("hide");
						}else if(2 == lcztcode){
							$("a[data-action='qjsp-czspp']").removeClass("hide");
						}else if(3 == lcztcode){
							$("a[data-action='qjsp-czspp']").removeClass("hide");
						}else if(4 == lcztcode){
							$("a[data-action='qjsp-czspp']").removeClass("hide");
						}else if(5 == lcztcode){
							$("a[data-action='qjsp-czspp']").removeClass("hide");
						}else if(6 == lcztcode){
							$("a[data-action='qjsp-czspp']").removeClass("hide");
						}
					}
				}else if(30 == sjly){
					//出差管理
					$("a[data-action='ccsp-spp']").addClass("hide");
					if(2 == sfyb){
						//未办，则显示按钮
						if(1 == lcztcode){
							$("a[data-action='ccsp-spp']").removeClass("hide");
						}else if(2 == lcztcode){
							$("a[data-action='ccsp-spp']").removeClass("hide");
						}else if(3 == lcztcode){
							$("a[data-action='ccsp-spp']").removeClass("hide");
						}else if(4 == lcztcode){
							$("a[data-action='ccsp-spp']").removeClass("hide");
						}else if(5 == lcztcode){
							$("a[data-action='ccsp-spp']").removeClass("hide");
						}else if(6 == lcztcode){
							$("a[data-action='ccsp-spp']").removeClass("hide");
						}
					}
				}else if(40 == sjly){
					//资产管理
					$("a[data-action='zcsp_save']").addClass("hide");
					if(2 == sfyb){
						//未办，则显示按钮
						if(3 == lcztcode){
							$("a[data-action='zcsp_save']").removeClass("hide");
						}else if(4 == lcztcode){
							$("a[data-action='zcsp_save']").removeClass("hide");
						}else if(5 == lcztcode){
							$("a[data-action='zcsp_save']").removeClass("hide");
						}else if(6 == lcztcode){
							$("a[data-action='zcsp_save']").removeClass("hide");
						}
					}
				}else if(50 == sjly){
					//办公用品管理
					$("a[data-action='BGYPSP_DOSPBTN']").addClass("hide");
					if(2 == sfyb){
						//未办，则显示按钮
						if(1 == lcztcode){
							$("a[data-action='BGYPSP_DOSPBTN']").removeClass("hide");
						}
					}
				}else if(60 == sjly){
					//劳务用品管理
					$("a[data-action='LWYPSP_DOSPBTN']").addClass("hide");
					if(2 == sfyb){
						//未办，则显示按钮
						if(1 == lcztcode){
							$("a[data-action='LWYPSP_DOSPBTN']").removeClass("hide");
						}
					}
				}else if(70 == sjly){
					//公章管理
					$("a[data-action='gzsp-spp']").addClass("hide");
					if(2 == sfyb){
						//未办，则显示按钮
						if(1 == lcztcode){
							$("a[data-action='gzsp-spp']").removeClass("hide");
						}
					}
				}else if(80 == sjly){
					//文档借阅
					$("a[data-action='jysh_shh']").addClass("hide");
					if(2 == sfyb){
						//未办，则显示按钮
						if(1 == lcztcode){
							$("a[data-action='jysh_shh']").removeClass("hide");
						}
					}
				}else if(90 == sjly){
					formLoadData(id,sjly,glid,sfyb,lcztcode);
					if("6"==lcztcode || "7"==lcztcode || "8"==lcztcode || "11"==lcztcode|| "10"==lcztcode){
						$("a[data-action='fwsh_fwdy_']").addClass("hide");
						$("a[data-action='fwsh_fwjd_']").addClass("hide");
						$("a[data-action='fwsh_yyff_']").addClass("hide");
						if("CZ"==roleCode){
			        		if(2 == sfyb){
			        			if(7 == lcztcode){
			        				$("a[data-action='fwsh_fwjd_']").removeClass("hide");
			        			}
			        		}
			        	}else if("GWY"==roleCode){
			        		if(2 == sfyb){
			        			if(7 == lcztcode){
			        				$("a[data-action='fwsh_fwjd_']").removeClass("hide");
			        			}
			        		}
			        	}else if("BGSFZR"==roleCode){
			        		if(2 == sfyb){
			        			if(8 == lcztcode){
			        				$("a[data-action='fwsh_fwjd_']").removeClass("hide");
			        			}
			        		}
			        	}else if("JYWYSZR"==roleCode){
			        		if(2 == sfyb){
			        			if(7 == lcztcode){
			        				$("a[data-action='fwsh_fwjd_']").removeClass("hide");
			        			}else if(6 == lcztcode){
			        				$("a[data-action='fwsh_fwdy_']").removeClass("hide");
			        			}else if(8 == lcztcode){
			        				$("a[data-action='fwsh_yyff_']").removeClass("hide");
			        			}
			        		}else if(1==sfyb){
			        			if(6 == lcztcode){
			        				$("a[data-action='fwsh_fwdy_']").removeClass("hide");
			        			}
			        		}
			        	}else if("BGSZR"==roleCode){
			        		if(2 == sfyb){
			        			if(7 == lcztcode){
			        				$("a[data-action='fwsh_fwjd_']").removeClass("hide");
			        			}
			        		}
			        	}else if("XXGLY"==roleCode){
			        		if(2 == sfyb){
			        			if(7 == lcztcode){
			        				$("a[data-action='fwsh_fwjd_']").removeClass("hide");
			        			}
			        		}
			        	}
					}else{
						$("a[data-action='fwsh_hg_']").addClass("hide");
						$("a[data-action='fwsh_cs_']").addClass("hide");
						$("a[data-action='fwsh_lzsp_']").addClass("hide");
						$("a[data-action='fwsh_fhqf_']").addClass("hide");
						$("a[data-action='fwsh_fwdg_']").addClass("hide");
						if("CZ"==roleCode){
			        		if(2 == sfyb){
			        			if(1 == lcztcode){
			        				$("a[data-action='fwsh_hg_']").removeClass("hide");
			        			}
			        		}
			        	}else if("FGLD"==roleCode){
			        		if(2 == sfyb){
			        			if(4 == lcztcode){
			        				$("a[data-action='fwsh_fhqf_']").removeClass("hide");
			        			}
			        		}
			        	}else if("BGSFZR"==roleCode){
			        		if(2 == sfyb){
			        			if(1 == lcztcode){
			        				$("a[data-action='fwsh_hg_']").removeClass("hide");
			        			}else if(2 == lcztcode){
			        				$("a[data-action='fwsh_cs_']").removeClass("hide");
			        			}else if(5 == lcztcode){
			        				$("a[data-action='fwsh_fwdg_']").removeClass("hide");
			        			}
			        		}
			        	}else if("JYWYSZR"==roleCode){
			        		if(2 == sfyb){
			        			if(2 == lcztcode){
			        				$("a[data-action='fwsh_cs_']").removeClass("hide");
			        			}else if(5 == lcztcode){
			        				$("a[data-action='fwsh_fwdg_']").removeClass("hide");
			        			}
			        		}
			        	}else if("DWBM"==roleCode){
			        		if(2 == sfyb){
			        			if(3 == lcztcode){
			        				$("a[data-action='fwsh_lzsp_']").removeClass("hide");
			        			}
			        		}
			        	}
					}
				}else if(100 == sjly){
					window.editor1=KindEditor.create('#oabg_tsswHf_zw',{});
					if(2 == sfyb){
						//未办，则显示按钮
						$("a[data-action='tsswhf_saveForm']").addClass("hide");
						if(1 == lcztcode){
							$("a[data-action='tsswhf_saveForm']").removeClass("hide");
						}
					}
					checkHfrq(glid,cb1);//判断时间是否已经到期1未到期2已到期false出错
					function cb1(isIn){
						if(isIn == "false"){
							showMsg("出现错误");
							return;
						}else if(isIn == "2"){
							$("a[data-action='tsswhf_saveForm']").addClass("hide");
							showMsg("时间已过无法进行回复");
						}
					}
					//判断当前回复日期是否超过回复截止日期
					function checkHfrq(id,callback){
						$.ajax({
							url : ctx + "/oabg/tsswhf/checkHfrq",
							type:"post",
							async : false,
							data:{
								id:id
							},
							success:function(data){// 1未超过 2超过
								callback(data.isIn);
							}
						});
					}
				}else if(110 == sjly){
					$("a[data-action='GWJDSH_FORMCZSHBTN']").addClass("hide");
					$("a[data-action='GWJDSH_FORMBGSSHBTN']").addClass("hide");
					$("a[data-action='GWJDSH_FORMCWSHBTN']").addClass("hide");
					$("a[data-action='GWJDSP_FORMSPBTN']").addClass("hide");
					if(2 == sfyb){
						//未办，则显示按钮
						if(1 == lcztcode){
							$("a[data-action='GWJDSH_FORMCZSHBTN']").removeClass("hide");
						}else if(2 == lcztcode){
							$("a[data-action='GWJDSH_FORMBGSSHBTN']").removeClass("hide");
						}else if(3 == lcztcode){
							$("a[data-action='GWJDSH_FORMCWSHBTN']").removeClass("hide");
						}else if(4 == lcztcode){
							$("a[data-action='GWJDSP_FORMSPBTN']").removeClass("hide");
						}
					}
				}else if(120 == sjly){
					$("a[data-action='HYPXSH_FORMCZSHBTN']").addClass("hide");
					$("a[data-action='HYPXSH_FORMBGSSHBTN']").addClass("hide");
					$("a[data-action='HYPXSH_FORMCWSHBTN']").addClass("hide");
					$("a[data-action='HYPXSP_FORMSPBTN']").addClass("hide");
					if(2 == sfyb){
						//未办，则显示按钮
						if(1 == lcztcode){
							$("a[data-action='HYPXSH_FORMCZSHBTN']").removeClass("hide");
						}else if(2 == lcztcode){
							$("a[data-action='HYPXSH_FORMBGSSHBTN']").removeClass("hide");
						}else if(3 == lcztcode){
							$("a[data-action='HYPXSH_FORMCWSHBTN']").removeClass("hide");
						}else if(4 == lcztcode){
							$("a[data-action='HYPXSP_FORMSPBTN']").removeClass("hide");
						}
					}
				}else if(130 == sjly){
					$("a[data-action='SBWXSP_FORM_SPBTN']").addClass("hide");
					$("a[data-action='SBWXSP_FORM_DOYWX']").addClass("hide");
					if(2 == sfyb){
						//未办，则显示按钮
						if(1 == lcztcode && "1" == lcztcode ){ //待审批
							$("a[data-action='SBWXSP_FORM_SPBTN']").removeClass("hide");
						}else if(2 == lcztcode && "2" == lcztcode){//已审批
							$("a[data-action='SBWXSP_FORM_DOYWX']").removeClass("hide");
						}
					}
				}else if(140 == sjly){
					$("a[data-action='XXBSSP_FORMSPBTN']").addClass("hide");
					if(2 == sfyb){
						//未办，则显示按钮
						if(1 == lcztcode && "1" == lcztcode ){ //待审批
							$("a[data-action='XXBSSP_FORMSPBTN']").removeClass("hide");
						}
					}
				}
				
//	        	if(id){
//					formLoadData(id,sjly,glid,sfyb,lcztcode);
//				}else{
//					
//				}
	        	closeLoadingDiv();
			},
			onBeforeClose: function () {
				if("10" == sjly){
					if("1" == lcztcode || "2" == lcztcode){
						KindEditor.remove('#oabg_swsh_zw');
					}else{
						KindEditor.remove('#oabg_swng_zw');
					}
				}
            },
			onClose : function() {
				if("10" == sjly){
					window.editor1.remove();
				}else if("100" == sjly){
					window.editor1.remove();
				}
				
				changeIfHasBl(id);
				win.window('destroy');
			}
		});
	}
	
	
	/**
	 * 编辑页面获取信息
	 */
	function formLoadData(id,sjly,glid,sfyb,lcztcode){
		$.ajax({
			type : "get",
			url :URL.getInfo,
			data : {id:id,sjly:sjly,glid:glid,lcztcode:lcztcode},
			success : function(data){
				if("10" == sjly){
					//渲染富文本
					if("1" == lcztcode || "2" == lcztcode){
						window.editor1=KindEditor.create('#oabg_swsh_zw',{});
						$("#swsh_form").form("load", data);
						KindEditor.html("#oabg_swsh_zw",data.zw);
					}else{
						window.editor1=KindEditor.create('#oabg_swng_zw',{});
						$("#swng_form").form("load", data);
						KindEditor.html("#oabg_swng_zw",data.zw);
					}
				}else if("40" == sjly){
					$("#zcsp_form").form("load", data);
				}else if("80" == sjly){
					$("#jysh_form").form("load", data);
				}else if("90"==sjly){
					$("#fwsh_form").form("load", data);
					$("#fwsh_form1").form("load", data);
				}
				closeLoadingDiv();
			}
		});
	}
	
	/** 校验单个是否办理 **/
	function changeIfHasBl(id){
		$.ajax({
			url : URL.changeIfHasBl,
			type : "post",
			data : {
				id : id
			},
			success : function(data){
				if(data.success){
					grid.datagrid("reload");
					renderdbsx();
				}
			}
		});
	}
	
});