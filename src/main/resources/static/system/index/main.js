$(function(){
	var windowWidth = document.documentElement.clientWidth;
	var windwoHeight = document.documentElement.clientHeight;
	renderLeftMenu("-1",function(){});
	formLoadData();
	/** 渲染左部菜单 * */
	function renderLeftMenu(codes,callback) {
		$("#indexleftmenutree").tree({    
		    url:ctx + "/system/menutree?menucodes="+codes,
		    method:'post',
		    width:155,
		    height:500,
		    onSelect : function(node){
		    	openLeftWin(node);
		    },
		    onLoadSuccess:function(){
		    	callback();
		    }
		});  
	}

	/** * */
	function openLeftWin(node) {
		var tabName = node.text;
		if ($("div[data-id=mainTabs]").tabs("exists", tabName)) {
			// 如果已存在，选中
			$("div[data-id=mainTabs]").tabs("select", tabName);
		} else {
			if(null != node.url && undefined != node.url && "" != node.url && "undefined" != node.url ){
				// if("open" == node.state){
				$("div[data-id=mainTabs]").tabs("add", {
					title : tabName,
					selected : true,
					closable : true,
					href : ctx + node.url,
					tools : [ {
						iconCls : "icon-mini-refresh",
						handler : function() {
							var currentTab = $("div[data-id=mainTabs]").tabs('getSelected');
							RefreshTab(currentTab);
						}
					} ],
					onLoad : function() {

					}
				});
			}
			
			// }
			function RefreshTab(currentTab) {
				var url = $(currentTab.panel('options')).attr('href');
				$('#tabs').tabs('update', {
					tab : currentTab,
					options : {
						href : url
					}
				});
				currentTab.panel('refresh');
			}
		}
	}
	bindMenus();
	function bindMenus(){
		$(".indexmenusbtns").unbind().bind("click",function(){
			var mycodes = $(this).data("mycodes");
			var hasres = $(this).data("hasres");
			if(1 == hasres || "1" == hasres){
				renderLeftMenu(mycodes,function(){
					hideIndexMenus();
				});	
			}else{
				$.messager.alert("系统提示","您没有访问权限");
			}
		});
		$(".indexmenusbtns-tc").unbind().bind("click",function(){
			$.messager.confirm("系统提示","确认退出？",function(r){
				if(r){
					var o = $("meta[name=csrf_name]").attr("content") || "",
					l = $("meta[name=csrf_value]").attr("content") || "",
					r = $("meta[name=suffix]").attr("content") || "";
					var e=$("<input>").attr("value",l).attr("name",o),n=$("<form>").attr("action",ctx+"/logout"+r).attr("method","POST").append(e).appendTo("body");
					n[0].submit();
				}
			});
		});
		$("a[data-action='getmainmenus']").unbind().bind("click",function(){
			$("#indexmenus").animate({
				left : 0,
				opacity : 1
			},function(){
				var tabslength = $("div[data-id='mainTabs']").tabs("tabs").length;
				for(var i = 1; i < tabslength; i++){
					$("div[data-id='mainTabs']").tabs("close",1);
				}
			});
			formLoadData();
		});
		function hideIndexMenus(){
			$("#indexmenus").animate({
				left : windowWidth,
				opacity : 0
			},function(){
				$("#indexmenus").css("left",-windowWidth);
				autoClickFirstNode();
			});
		}
		
		function autoClickFirstNode(node){
			if(node){
				var children = $("#indexleftmenutree").tree("getChildren",node.target);
				if(children.length >= 1){
					var isleaf = $("#indexleftmenutree").tree("isLeaf",children[0].target);
					if(isleaf){
						$(children[0].target).click();
					}else{
						autoClickFirstNode(children[0]);
					}
				}
			}else{
				var roots = $("#indexleftmenutree").tree("getRoots");
				if(roots.length >= 1){
					var isleaf = $("#indexleftmenutree").tree("isLeaf",roots[0].target);
					if(isleaf){
						$(roots[0].target).click();
					}else{
						autoClickFirstNode(roots[0]);
					}
				}
			}
			
		}
	}
	
	/**获取未办数量**/
	function formLoadData(){
		$.ajax({
			type : "get",
			url:ctx + "/system/getNum",
			success : function(data){
				var dzwjNum=data.dzwjNum;
				if(0==dzwjNum){
					$("#index_dzwjNum").hide();
				}else{
					$("#index_dzwjNum").addClass("circle");
					$("#index_dzwjNum").html(data.dzwjNum);
					
				}
				var gzsqNum=data.gzsqNum;
				if(0==gzsqNum){
					$("#index_gzglNum").hide();
				}else{
					$("#index_gzglNum").addClass("circle");
					$("#index_gzglNum").html(data.gzsqNum);
				}
				
				var hytzNum=data.hytzNum;
				if(0==hytzNum){
					$("#index_hytzNum").hide();
				}else{
					$("#index_hytzNum").addClass("circle");
					$("#index_hytzNum").html(data.hytzNum);
				}
				
				var bgypNum=data.bgypNum;
				if(0==bgypNum){
					$("#index_bgypNum").hide();
				}else{
					$("#index_bgypNum").addClass("circle");
					$("#index_bgypNum").html(data.bgypNum);
				}
				
				var hwpxNum=data.hwpxNum;
				if(0==hwpxNum){
					$("#index_hwpxNum").hide();
				}else{
					$("#index_hwpxNum").addClass("circle");
					$("#index_hwpxNum").html(data.hwpxNum);
				}
				
				var kqglNum=data.kqglNum;
				if(0==kqglNum){
					$("#index_kqglNum").hide();
				}else{
					$("#index_kqglNum").addClass("circle");
					$("#index_kqglNum").html(kqglNum);
				}
				
				var xxbsNum=data.xxbsNum;
				if(0==xxbsNum){
					$("#index_xxbsNum").hide();
				}else{
					$("#index_xxbsNum").addClass("circle");
					$("#index_xxbsNum").html(xxbsNum);
				}
				
				var xfrdNum=data.xfrdNum;
				if(0==xfrdNum){
					$("#index_xfrdNum").hide();
				}else{
					$("#index_xfrdNum").addClass("circle");
					$("#index_xfrdNum").html(xfrdNum);
				}
				
				var gdpsNum=data.gdpsNum;
				if(0==gdpsNum){
					$("#index_gdpsNum").hide();
				}else{
					$("#index_gdpsNum").addClass("circle");
					$("#index_gdpsNum").html(gdpsNum);
				}
				
				var zcglNum=data.zcglNum;
				if(0==zcglNum){
					$("#index_zcglNum").hide();
				}else{
					$("#index_zcglNum").addClass("circle");
					$("#index_zcglNum").html(zcglNum);
				}
				
				var gzapNum=data.gzapNum;
				if(0==gzapNum){
					$("#index_gzapNum").hide();
				}else{
					$("#index_gzapNum").addClass("circle");
					$("#index_gzapNum").html(gzapNum);
				}
			}
		});
	}
});