$(function(){
	var URL = {
			loadBar : ctx + "/oabg/welcome/bar"
	};
	
	var modelName="oabg_zhutu";
		
	/** 主页查询表单* */
	var queryForm = $("#" + modelName + "_qf");
	
	
	/** 柱状图数据 **/
	queryAllData({});
	
	
	/** 按钮点击响应 **/
	bindClick();
	
	function loadBar(data){
		
		var chart=echarts.init(document.getElementById('oabg_zhutu_show'));
		
		option={
				title:{
					text:'收文、发文、会议通知、公章申请数量统计图',
					x:'center',
					Y:'bottom',
					textStyle:{
			        	color:'#1795d2',
			        	fontWeight:'400',
			        	fontSize:'23'
			        }
				},
				color:['#50d6d9','#b6a2de','#C1232B','#B5C334','#FCCE10','#E87C25','#27727B',
		                '#FE8463','#9BCA63','#FAD860','#F3A43B','#60C0DD',
		                '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'],
				toolbox: {
			    	right:'20px',
			    	feature: {
			            magicType: {show: true, type: ['tiled','stack']},
			            saveAsImage: {show: true}
			        }
			    },

				tooltip : {
			        trigger: 'axis',
			        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
			            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			        }
			    },
				legend:{
					data:['发文', '收文','会议通知','公章申请'],
//					orient: 'vertical',
			        left:'50px',
			        bottom:'0px'
				},
				grid:{
					left: '3%',
			        right: '4%',
			        bottom: '8%',
			        containLabel: true
				},
				xAxis:  {
			        type: 'category',
			        data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
			    },
			    yAxis: {
			        type: 'value',
			        show:true
			    },
			    series: [
					 {
					     name: '发文',
					     type: 'bar',
					     label: {
					         normal: {
					             show: true,
					             position: 'insideRight'
					         }
					     },
					     data: data.fw
					 },
					 {
					     name: '收文',
					     type: 'bar',
					     label: {
					         normal: {
					             show: true,
					             position: 'insideRight'
					         }
					     },
					     data: data.sw
					 },
					 {
					     name: '会议通知',
					     type: 'bar',
					     label: {
					         normal: {
					             show: true,
					             position: 'insideRight'
					         }
					     },
					     data: data.hy
					 },
					 {
					     name: '公章申请',
					     type: 'bar',
					     label: {
					         normal: {
					             show: true,
					             position: 'insideRight'
					         }
					     },
					     data: data.gz
					 }
				]
			    
		};
		chart.setOption(option);
	}
	
	/** 初始化查询数据**/
	function queryAllData(formdata){
//			var formData = queryForm.serializeObject({transcript:"overlay"});
//			formData.time=formData.time+"-01-01";
			$.ajax({
				url:URL.loadBar,
				type:"get",
				async : false,
				data:formdata,
				success:function(data){
					loadBar(data);
					closeLoadingDiv();
				}
		})
		
	}

	/** 加载按钮 **/
	function bindClick(){
		$("#" + modelName + "_query").bind("click", function() {
			queryAllData(queryForm.serializeObject({transcript:"overlay"}));
		});
		$("#" + modelName + "_clear").bind("click", function() {
			queryForm.form("clear");
		});
	}
	
});

