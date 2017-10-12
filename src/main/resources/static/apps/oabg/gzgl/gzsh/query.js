$(function(){
	var URL = {
			gridData : ctx + "/oabg/gzgl/gzsh/querygrid",
			/*ghFormWin: ctx + "/oabg/gzgl/gzdj/ghFormWin", */
	};
	
	/** 模块名称* */
	var modelName = "query";
	/** 主页列表* */
	var grid = $("#" + modelName + "_grid");
	/** 主页查询表单* */
	var queryForm = $("#" + modelName + "_qf");
	/** 渲染Grid* */
	renderGrid();
	/** 绑定查询按钮* */
	bindSearchBar()
	function renderGrid(){
		grid.datagrid({
			url :URL.gridData,
			rownumbers :true,
			columns:[[
			    /*{field:"id",title:"序号",align:"left",halign:"center"},*/
			    {field:"gzname",title:"公章名称",align:"left",halign:"center",width:"200"},
			    {field:"sybm",title:"公章使用部门",align:"left",halign:"center",width:"200"},
			    {field:"syname",title:"公章使用人",align:"left",halign:"center",width:"100"},
			    {field:"gzsl",title:"盖章数量",align:"right",halign:"center",width:"100"}
			   /* {field:"cz",title:"操作",align:"center",halign:"center",width:"100",
			    	formatter: function(value,row,index){
						return "<a class='queryGridLookLcjlBtns' data-myid='"+row.id+"' href='#' style='background-color:#36BDEF;color:#FFFFFF;'>归还</a>";
					}
			    }*/
			]],
			/*onLoadSuccess : function(){
				$(".queryGridLookLcjlBtns").linkbutton({
					plain : true,
					onClick : function(){
						var id = $(this).data("myid");
						openGhFormWin(id);
					}
				});
			}*/
		})
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
		/**打开出入记录页面**/
		/*function openGhFormWin(id){
			var win = $("<div id='" + modelName + "_QueryFormWin'></div>").window({
				title : "公章归还",
				href :URL.ghFormWin,
				width : 350,
				height :110,
				onLoad : function(){
					closeLoadingDiv();
					$("#query_spform_id").val(id);
					
				},
				onClose : function() {
					win.window('destroy');
				}
			});
		}*/
/*	setTimeout(function(){
		$("#gllzname").combobox({    
		    url:ctx+"/ds/getGzsjname",    
		    valueField:'code',    
		    textField:'name',  
		});  
	},500)
*/	
});