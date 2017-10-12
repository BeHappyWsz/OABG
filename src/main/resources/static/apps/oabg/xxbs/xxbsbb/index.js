$(function(){
	var modelName = "xxbsbb";
//	var grid = $("#" +modelName+ "_grid");
	var queryForm = $("#s_"+modelName+"_index_form");
	var URL = {
			baobiao : ctx + "/oabg/xxbs/baobiao",
			excel : ctx + "/oabg/xxbs/export",
			print : ctx + "/oabg/xxbs/print"
	};

	bindClick();
	
	getBaoBiao();

	/** 加载按钮 **/
	function bindClick(){
		$("#" + modelName + "_query").bind("click", function() {
			getBaoBiao();
		});
		$("#" + modelName + "_clear").bind("click", function() {
			clear();
		});
		$("#" + modelName + "_excel").bind("click", function() {
			var formData = queryForm.serialize();
			OutExcel(formData);
		});
		$("#" + modelName + "_print").bind("click", function() {
			var formData = queryForm.serialize();
			Outprint(formData);
		});
	}
	
	function clear(){  
		queryForm.form('clear');
	}; 
    function getBaoBiao(){
    	var nlpgtj_formData=queryForm.serializeObject();
		$("#xxbsbb_tb").panel({href:URL.baobiao, queryParams:nlpgtj_formData,onLoad:function() {
			closeLoadingDiv();
		}});
		
    };
	
    //导出excel
	function OutExcel(data){
		window.open(URL.excel+"?"+data);
	};
	 //打印
	function Outprint(data){
		window.open(URL.print+"?"+data);
	};


});