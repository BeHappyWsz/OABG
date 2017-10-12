$(function(){
	
	var URL ={
			getUserMsg : ctx +'/oabg/txl/getUserMsg',
			gridData : ctx + "/oabg/txl/grid",
			formWin : ctx + "/oabg/txl/form",
			getInfo : ctx + "/oabg/txl/getInfo",
			deleteByIds : ctx + "/oabg/txl/deleteByIds",
	};
	
	
	var modelName = "txl";
	var grid = $("#"+modelName+"_grid");
	var qform = $("#"+modelName+"_qf");
	
	renderBmdwTree();
	renderGrid();
	renderFormBar();
	
	//渲染部门单位树
	var bmdwCode = "";
	function renderBmdwTree(){
		$('#txl_bmtree').tree({
			url : 'ds/dsTree/bmdw',
			onBeforeSelect : function(node){
				var isLeaf = $(this).tree('isLeaf',node.target);
				if(!isLeaf){
//					showMsg('选择错误');
				}else{
					bmdwCode = node.id;
					renderGrid(node.id);
					renderGridBar(node.id,uBmdwCode);
				}
				return isLeaf;
			},
			onLoadSuccess : function(node, data){
				if(node.id == '101'){
					
				}
			}
		});
	}
	
	function renderGrid(bmdw){
		grid.datagrid({
			url : URL.gridData,
			queryParams : {bmdw :bmdw},
			onDblClickRow : function(index,row){
				openFormWin(row.id);
			},
			columns :[[
			    {field:"id",checkbox:true}, 
			    {field:"isUser",hidden:true},//判断该条信息是否包含账户信息,如果包含则无法删除 
			    {field:"realname",title:"姓名",align:"left",halign:"center",width:"100px"},
			    {field:"xbName",title:"性别",align:"center",halign:"center",width:"60px"},
			    {field:"bmdwName",title:"工作部门",align:"center",halign:"center",width:"150px"},
			    {field:"bmzwName",title:"部门职位",align:"center",halign:"center",width:"100px"},
			    {field:"mobile",title:"手机号码",align:"center",halign:"center",width:"110px"},
			    {field:"bgdh",title:"办公电话",align:"center",halign:"center",width:"110px"},
			    {field:"telphone",title:"宅电",align:"center",halign:"center",width:"110px"},
			    {field:"dz",title:"办公地址",align:"left",halign:"center",width:"150px"},
			    {field:"bz",title:"备注",align:"left",halign:"center",width:"150px"},
			]]
		});
	}
	
	function openFormWin(id){
		var win = $("<div id='"+modelName+"_formWin'></div>").window({
			title : id ? "查看/修改" : "新增",
			width : 1000,
			height: 250,
			href :URL.formWin,
			onLoad : function(){
				if(id){//查看修改
					loadFormData(id);
					if(isUpdate == "0"){
						$("#txl_open_south").hide();
					}
				}else{//新增,如果点击了左侧部门tree，则自动加载点击的部门单位
					$("#txl_form_bmdw").combotree("setValue",bmdwCode);
				}
				closeLoadingDiv();
			},
			onClose : function(){
				win.window('destroy');
			}
		});
	}
	/**
	 * 双击查看信息，并加载
	 */
	function loadFormData(id){
		$.ajax({
			url : URL.getInfo,
			type : "post",
			data : {id:id},
			success : function(data){
				$("#txl_form").form("load",data);
			}
		});
	}
	
	function deleteByIds(){
		var ids = gridCheckedValid(grid);
		if(ids){
			$.messager.confirm('提示','您确认删除吗?',function(r){
				if(r){
					$.messager.progress({msg:'请稍后...'});
					$.ajax({
						url : URL.deleteByIds,
						type : "post",
						data :{ids :ids},
						dataType : "json",
						success :function(data){
							if(data.success){
								showMsg(Msg.delSuc);
								$.messager.progress('close');
								grid.datagrid("reload");
							}
						}
					});
				}
			});
		}
	}
	/**
	 * bmdw:单击左侧tree的部门编号
	 * bmdwCode:当前用户的部门编号
	 */
	var isUpdate = "";
	function renderGridBar(bmdw,bmdwCode){
		var add = $("a[data-action='TXL_ADD']");
		var del = $("a[data-action='TXL_DELETE']");
		add.unbind("click");
		del.unbind("click");
//		if(isAdmin =="yes"  && bmdw == undefined){//管理员初次到达页面
//			alert("1");
//			add.bind("click",function(){
//				openFormWin();
//			});
//			del.bind("click",function(){
//				deleteByIds();
//			});
//			return ;
//		}else if(isAdmin =="no" && bmdw == undefined ){//其他用户初次到达
//			alert("2");
//			add.linkbutton({
//				disabled : true
//			});
//			del.linkbutton({
//				disabled : true
//			});
//			return ;
//		}
		//object转string
		bmdw = bmdw+"";
		bmdwCode = bmdwCode+"";
		if(bmdw != bmdwCode && isAdmin == "no"){//非本部门且非管理员无法维护
			add.linkbutton({
				disabled : true
			});
			del.linkbutton({
				disabled : true
			});
			isUpdate = "0";
		}
		else if(bmdw == bmdwCode && isAdmin == "no"){//非管理员可维护本部门信息(新增、修改)
			add.linkbutton({
				disabled : false
			});
			add.bind("click",function(){
				openFormWin();
			});
			del.linkbutton({
				disabled : true
			});
			isUpdate = "1";
		}else if(isAdmin == 'yes'){//管理员维护所有(增删改)
			add.bind("click",function(){
				openFormWin();
			});
			del.bind("click",function(){
				deleteByIds();
			});
			isUpdate = "2";
		}
	}
	
	function renderFormBar(){
		$("#"+modelName+"_form_query").bind("click",function(){
			var formData = qform.serializeObject({transcript:"overlay"});
			grid.datagrid("reload",formData);
		});
		
		$("#"+modelName+"_form_clear").bind("click",function(){
			qform.form('clear');
		});
	}
});