/**
 * 地图标记
 */
function chooseMap(ssdqId){
	var area="";
	if($("#"+ssdqId).size()>0){
		area = $("#"+ssdqId).combobox("getText");
	}else{
		area = "常州市";
	}
	var jd = $("#jd"+"_"+ssdqId).textbox("getValue");
	var wd = $("#wd"+"_"+ssdqId).textbox("getValue");
	if(area == "" || area == null){
		$.messager.alert("系统提示","请先选择所属地区");
		return;
	}
	var win = $('<div id="chooseMapWin"></div>').window({
    	title:'地图标记',
    	width:700,
		height:500,
        href:encodeURI(ctx+"/mapView/chooseMapWin.do?jd="+jd+"&wd="+wd+"&area="+area),
        modal:true,
        collapsible:false,
        minimizable:false,
        maximizable:false,
        resizable:false,
        onLoad:function(){
        	$(function(){
        	    $('div[data-id=loading]').fadeOut('normal', function() {
        	        $(this).remove();
        	    });        
        	});
        	$("a[data-id=searchMapBtn]").bind('click',function(){
        		searchMap();
        	});
        	$("a[data-id=confirmChooseBtn]").bind('click',function(){
        		confirmChoose();
        	});
        	$("#chooseMapWin_ssdqId").val(ssdqId);
        },
        onClose:function(){
        	win.window('destroy');
        }
    });
}

/**
 * 查询
 */
function searchMap(){
	var area = $("#area").textbox("getValue");
	$("#jdx").textbox("setValue","");
	$("#wdy").textbox("setValue","");
	$("#mapView").load(ctx+"/mapView/showMapWin.do?area="+encodeURI(area));
}

/**
 * 确认选择
 */
function confirmChoose(){
	var ssdqId = $("#chooseMapWin_ssdqId").val();
	var lngx = $("#jdx").textbox("getValue");
	var latY = $("#wdy").textbox("getValue");
	if(lngx == "" || lngx == null || latY == "" || latY == null){
		$.messager.alert("系统提示","请标记经纬度");
		return;
	}
	$("#jd"+"_"+ssdqId).textbox("setValue",lngx);
	$("#wd"+"_"+ssdqId).textbox("setValue",latY);
	$("#chooseMapWin").window("close");
}