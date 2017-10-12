(function($){
    $.fn.extend({
    	buildMenu:function(params){
    		var main = $("<div class='custree-menu-main' data-mouseonmenu='0'></div>");
    		$(this).append(main);
    		params.main = main;
    		getMenuData(params,renderMenu);
        }
    });  
    
    function getMenuData(params,callback){
		$.ajax({
			url : ctx+"/system/menutree",
			method : "post",
			data : {},
			success : function(data){
				callback(params,data);
			}
		});
	}
	
	function renderMenu(params,data){
//		params.menu.append();
		renderRoots(params,data)
		//渲染根节点
		function renderRoots(params,data){
			for(var i = 0; i < data.length; i++){
				var rootP = data[i];
				var rootAccs = $("<div class='custree-across custree-ps' data-myid='"+rootP.id+"'><div class='across-icon "+rootP.iconCls+"'></div><div class='across-text'>"+rootP.text+"</div></div>");
				rootAccs.css("width",params.rootWidth+"px");
				params.main.append(rootAccs);
				if(null != rootP.children && undefined != rootP.children && rootP.children.length != 0){
					renderKids(params,rootAccs,rootP.children);
				}
				//绑定根节点鼠标事件
				rootBindEvents(params,rootAccs);
			}

			function rootBindEvents(params,rootAccs){
				rootAccs.mouseover(function(){
					changeMouseState("root",params,1,rootAccs.data("myid"));
				});
				rootAccs.mouseleave(function(){
					changeMouseState("root",params,0);
				});
			}
		}
		//渲染子节点
		function renderKids(params,node,data){
			var lastMain = $("<div class='custree-menu-left-main' data-mypid='"+node.data("myid")+"'></div>");
			lastMain.css("width",params.kidWidth+"px");
			node.append(lastMain);
			params.main.append(lastMain);
			for(var i = 0; i < data.length; i++){
				var kidP = data[i];
				//计算文字最大宽度
				var maxWidth = params.kidWidth;
				var textmaxWidth = (maxWidth-30-5)+"px";
				var kidKs = $("<div class='custree-kids custree-ps' data-myid='"+kidP.id+"' data-myid='"+kidP.pid+"' >" +
								"<div class='kids-icon "+kidP.iconCls+"'></div>" +
								"<div class='kids-text' style='max-width:"+textmaxWidth+"'>"+kidP.text+"</div>" +
							"</div>");
				lastMain.append(kidKs);
				if(null != kidP.children && undefined != kidP.children && kidP.children.length != 0){
					renderKids(params,kidKs,kidP.children);
				}
				//绑定子节点鼠标事件
				kidsBindEvents(params,kidKs,kidP);
			}
			
			function kidsBindEvents(params,kidKs,kidP){
				kidKs.mouseover(function(){
					var id = kidKs.data("myid");
					changeMouseState("kid",params,1,id);
				});
				kidKs.mouseleave(function(){
					changeMouseState("kid",params,0);
				});
				if(null != kidP.url && "" != kidP.url){
					kidKs.click(function(){
						var node = kidP;
						$(".custree-menu-left-main").hide();
						params.main.data("mouseonmenu",0);
						params.onSelect(node);
					});
				}
			}
		}
	}
	
	function setOptions(node,opts){
		for(var v in opts){
			node.data("opt"+v) = opts[v];
		}
	}
	
	function changeMouseState(type,params,state,id){
		params.main.data("mouseonmenu",state);
		if(0 == state || "0" == state){
			setTimeout(function(){
				var isonmenu = params.main.data("mouseonmenu");
				if(1 != isonmenu && "1" != isonmenu){
					$(".custree-menu-left-main").hide();
				}
			},300);
		}else{
			var pnode = $(".custree-ps[data-myid='"+id+"']");
			var left = pnode.parent().position().left + pnode.width();
			var top;
			if("root" == type){
				top = pnode.position().top;
				$(".custree-menu-left-main").hide();
			}else{
				top = pnode.parent().position().top+pnode.position().top;
				var emenus = $(".custree-kids[data-myid='"+id+"']").parent().find(".custree-kids");
				emenus.each(function(){
					$(".custree-menu-left-main[data-mypid='"+$(this).data("myid")+"']").hide();
				});
			}
			$(".custree-menu-left-main[data-mypid='"+id+"']").css("left",left+"px");
			$(".custree-menu-left-main[data-mypid='"+id+"']").css("top",top+"px");
			$(".custree-menu-left-main[data-mypid='"+id+"']").show();
		}
	}
})(jQuery);