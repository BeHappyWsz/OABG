
var mapObj;
var marker = new Array();
var windowsArr = new Array();
function mapInit() {
	if(jdx.length>0 && wdy.length>0){
		mapObj = new AMap.Map("iCenter",{center:new AMap.LngLat(jdx,wdy),level:15});
		//地图初始化时，在地图上添加一个marker标记,鼠标点击marker可弹出自定义的信息窗体
		addMarker();
	}else{
	    mapObj = new AMap.Map("iCenter");
	    var MSearch;
	    mapObj.plugin(["AMap.PlaceSearch"], function() {     
	        MSearch = new AMap.PlaceSearch({ //构造地点查询类
	            pageSize:10,
	            pageIndex:1,
	            city:"江阴市", //城市
	            level:15
	        }); 
	        AMap.event.addListener(MSearch, "complete", keywordSearch_CallBack);//返回地点查询结果
	        MSearch.search(areaText); //关键字查询
	    });
	}
	//为地图注册click事件获取鼠标点击出的经纬度坐标   
    AMap.event.addListener(mapObj,'click',function(e){
    	$("#jdx").textbox("setValue",e.lnglat.getLng());
    	$("#wdy").textbox("setValue",e.lnglat.getLat());
    }); 
}
function keywordSearch_CallBack(data) {
    var poiArr = data.poiList.pois;
    var resultCount = poiArr.length;
    for (var i = 0; i < resultCount; i++) {
	    addmarker_(i, poiArr[i]);
    }
    mapObj.setFitView();
}
//添加marker&infowindow    
function addmarker_(i, d) {
    var lngX = d.location.getLng();
    var latY = d.location.getLat();
    var markerOption = {
	    map:mapObj,
        icon:"http://webapi.amap.com/images/" + (i + 1) + ".png",
        position:new AMap.LngLat(lngX, latY)
    };
    var mar = new AMap.Marker(markerOption);          
    marker.push(new AMap.LngLat(lngX, latY));

    var infoWindow = new AMap.InfoWindow({
    	content:"<h3><font color=\"#00a6ac\">&nbsp;&nbsp;" + (i + 1) + ". " + d.name + "</font></h3>" + TipContents(d.type, d.address, d.tel),
        size:new AMap.Size(300, 0), 
        autoMove:true,  
		offset:new AMap.Pixel(0,-30)
    });
    windowsArr.push(infoWindow); 
    var aa = function (e) {infoWindow.open(mapObj, mar.getPosition());};
	AMap.event.addListener(mar, "click", aa);
}
function TipContents(type, address, tel) {  //窗体内容
    if (type == "" || type == "undefined" || type == null || type == " undefined" || typeof type == "undefined") {
        type = "暂无";
    }
    if (address == "" || address == "undefined" || address == null || address == " undefined" || typeof address == "undefined") {
        address = "暂无";
    }
    if (tel == "" || tel == "undefined" || tel == null || tel == " undefined" || typeof address == "tel") {
        tel = "暂无";
    }
    var str = "&nbsp;&nbsp;地址：" + address + "<br />&nbsp;&nbsp;电话：" + tel + " <br />&nbsp;&nbsp;类型：" + type;
    return str;
}
//添加marker标记
function addMarker(){
	 mapObj.clearMap();
	 var marker = new AMap.Marker({ 					 
	 map:mapObj,					 
	 position:new AMap.LngLat(jdx,wdy), //位置 
	 icon:"http://webapi.amap.com/images/0.png" //复杂图标       
	 }); 
	 AMap.event.addListener(marker,'click',function(){ //鼠标点击marker弹出自定义的信息窗体
		//infoWindow.open(mapObj,marker.getPosition());	
	 });	
}
