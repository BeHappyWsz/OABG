$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/gzjh/save",
		tick : ctx + "/oabg/gzjh/tick",
		Week :ctx +"/oabg/gzjh/getWeek",
		checkouttime : ctx +"/oabg/gzjh/checkouttime"
	};

	/** 模块名称* */
	var modelName = "gzjh";

	/** 表单* */
	var form = $("." + modelName + "_form_main");
	/** 绑定按钮事件* */
	bindButtonAction();
	function addFieldSet(i){
		var html="<fieldset>"
			+"<form class='gzjh_form' >"
			+"<input id='gzjhxx_form_id' name='id' value='' type='hidden'/>"
			+"<table width='100%' class='formtable'>"
			+"<tr>"
			+"<td class='label'>日期起</td>"
			+"<td ><input class='easyui-datebox' data-startdindex='"+i+"' name='rqstart' data-options='editable:false' style='width: 95%'/></td>"
			+"<td class='label'>日期止</td>"
			+"<td ><input class='easyui-datebox' data-enddindex='"+i+"' name='rqEnd' data-options='editable:false' style='width: 95%'/></td>"
			+"<td class='label'>星期</td>"
			+"<td ><input class='easyui-textbox' name='week' data-weekindex='"+i+"' style='width: 95%'/></td>"
			+"</tr>"
			+"<tr>"
			+"<td class='label'>时间</td>"
			+"<td ><input class='easyui-textbox' name='sj' style='width: 95%'/></td>"
			+"<td class='label'>地点</td>"
			+"<td ><input class='easyui-textbox' name='address' style='width: 95%'/></td>"
			+"<td class='label'>领导</td>"
			+"<td ><input class='easyui-textbox' name='ld' style='width: 95%'/></td>"
			+"</tr>"
			+"<tr>"
			+"<td class='label'>主办部门</td>"
			+"<td colspan='5'><input class='easyui-textbox' name='zbbm' style='width: 95%'/></td>"
			+"<tr>"
			+"<tr>"
			+"<td class='label'>活动（会议）内容</td>"
			+"<td colspan='5'><input class='easyui-textbox' name='text' style='width: 95%'/></td>"
			+"</tr>"
			+"<tr>"
			+"<td class='label'>备注</td>"
			+"<td colspan='5'><input class='easyui-textbox' name='bz' style='width: 95%'/></td>"
			+"</tr>"
			+"</table>"
			+"</form>"
			+"</fieldset>";
		return $(html);
	}
	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=GZAP_SAVE]").bind("click", function() {
			save();
		});
		
		$("a[data-action=GZAP_CROSS]").bind("click", function() {
			$("#" + modelName + "_formWin").window("close");
			$("#" + modelName + "_grid").datagrid("reload");
		});
		$("a[data-action=GZAP_TICK]").bind("click", function() {
			tick();
		});
		
		$("#" + modelName + "_one").bind("click", function() {
			var $elements = $('fieldset');
			var len = $elements.length;
			var formele = addFieldSet(len);
			$("#jh").append(formele);
			bindDateBoxEvent(formele,len);
			$.parser.parse("." + modelName + "_form");  
		});
		$("#" + modelName + "_five").bind("click", function() {
			var $elements = $('fieldset');
			var len = $elements.length;
			for(i=len;i<len+5;i++){
				var formele = addFieldSet(i);
				$("#jh").append(formele);
				bindDateBoxEvent(formele,i);
			}
			$.parser.parse("." + modelName + "_form");
		});
		$("#" + modelName + "_ten").bind("click", function() {
			var $elements = $('fieldset');
			var len = $elements.length;
			for(i=len;i<len+10;i++){
				var formele = addFieldSet(i);
				$("#jh").append(formele);
				bindDateBoxEvent(formele,i);
			}
			$.parser.parse("." + modelName + "_form");
		});
	}
	
	/**
	 * 保存
	 */
	function save() {
		if (form.form("validate")) {
			/** 序列化form* */
			var formData = form.serializeObject({transcript:"overlay"});
			var rqstart=new Array();
			var rqEnd=new Array();
			var week=new Array();
			var idd=new Array();
			var sj=new Array();
			var text=new Array();
			var ld=new Array();
			var zbbm=new Array();
			var bz=new Array();
			var address=new Array();
			var $elements = $('fieldset');
			var len = $elements.length;
			$("." + modelName + "_form").each(function(i){
				var data = $($("." + modelName + "_form")[i]).serializeObject({transcript:"overlay"});
				idd.push(data.id);
				rqstart.push(data.rqstart);
				rqEnd.push(data.rqEnd);
				week.push(data.week);
				sj.push(data.sj);
				text.push(data.text);
				ld.push(data.ld);
				zbbm.push(data.zbbm);
				bz.push(data.bz);
				address.push(data.address);
			});
			formData.rqstart=rqstart.join(",");
			formData.rqEnd=rqEnd.join(",");
			formData.week=week.join(",");
			formData.idd=idd.join(",");
			formData.sj=sj.join(",");
			formData.text=text.join(",");
			formData.ld=ld.join(",");
			formData.zbbm=zbbm.join(",");
			formData.bz=bz.join(",");
			formData.address=address.join(",");
			$.messager.confirm("系统提示","是否确认保存？",function(r){
				if(r){
					$.ajax({
						type : "post",
						url : URL.save,
						data :formData,
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
		})
		}
	}
	/**
	 *提交
	 * */
	function tick(){
		var formData = form.serializeObject({transcript:"overlay"});
		var rqstart=new Array();
		var rqEnd=new Array();
		var week=new Array();
		var idd=new Array();
		var sj=new Array();
		var text=new Array();
		var ld=new Array();
		var zbbm=new Array();
		var bz=new Array();
		var address=new Array();
		var $elements = $('fieldset');
		var len = $elements.length;
		$("." + modelName + "_form").each(function(i){
			var data = $($("." + modelName + "_form")[i]).serializeObject({transcript:"overlay"});
			rqstart.push(data.rqstart);
			idd.push(data.id)
			rqEnd.push(data.rqEnd);
			week.push(data.week);
			sj.push(data.sj);
			text.push(data.text);
			ld.push(data.ld);
			zbbm.push(data.zbbm);
			bz.push(data.bz);
			address.push(data.address);
		});
		formData.rqstart=rqstart.join(",");
		formData.rqEnd=rqEnd.join(",");
		formData.week=week.join(",");
		formData.idd=idd.join(",");
		formData.sj=sj.join(",");
		formData.text=text.join(",");
		formData.ld=ld.join(",");
		formData.zbbm=zbbm.join(",");
		formData.bz=bz.join(",");
		formData.address=address.join(",");
		$.messager.confirm("系统提示","是否确认提交？",function(r){
			if(r){
				$.ajax({
					type : "post",
					url : URL.tick,
					data :formData,
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
	})
}	
	function bindDateBoxEvent(formbox,index){
		stopFirstChangeEvent: true,
		formbox.find("input[data-startdindex='"+index+"']").datebox({
			onSelect: function(date){
				var end= $("input[data-enddindex='"+index+"']").datebox('getValue');
				var start= $("input[data-startdindex='"+index+"']").datebox('getValue');
				$.ajax({
					type : "post",
					url :URL.Week,
					data : {start:start,end:end},
					success : function(data) {
						if(data.success==true){
							formbox.find("input[data-weekindex='"+index+"']").textbox("setValue",data.returnMsg);
						}else{
							showMsg(data.returnMsg);
							formbox.find("input[data-startdindex='"+index+"']").datebox('clear');
						}
						
					}
				});
		    }
		});
		formbox.find("input[data-enddindex='"+index+"']").datebox({
			stopFirstChangeEvent: true,
			onSelect: function(date){
				var end= $("input[data-enddindex='"+index+"']").datebox('getValue');
				var start= $("input[data-startdindex='"+index+"']").datebox('getValue');
				$.ajax({
					type : "post",
					url :URL.Week,
					data : {start:start,end:end},
					success : function(data) {
						if(data.success==true){
							formbox.find("input[data-weekindex='"+index+"']").textbox("setValue",data.returnMsg);
						}else{
							showMsg(data.returnMsg);
							formbox.find("input[data-enddindex='"+index+"']").datebox('clear');
						}
					}
				});
		    }
		});
	}
	$("#kssj").datebox({
		stopFirstChangeEvent: true,
		onChange:function(data){
			var formData = form.serializeObject({transcript : "overlay"});
			if(formData.id!=null&&formData.id!=""){
				var options = $(this).datetimebox('options');
		        if(options.stopFirstChangeEvent) {
		            options.stopFirstChangeEvent = false;
		            return;
		        }
			} 
			$.ajax({
				url : URL.checkouttime,
				type : "get",
				data : {timeStr:formData.kssj,timeEnd:formData.jssj},
				success : function(data){
					if(data.success){
						
					}else{
						$("#kssj").datebox('clear');
						$.messager.alert("提示",data.returnMsg);
					}
				}
			});
		}
	});
			
	$("#jssj").datebox({
		stopFirstChangeEvent: true,
		onChange:function(data){
			var formData = form.serializeObject({transcript : "overlay"});
			if(formData.id!=null&&formData.id!=""){
				var options = $(this).datetimebox('options');
		        if(options.stopFirstChangeEvent) {
		            options.stopFirstChangeEvent = false;
		            return;
		        }
			}
			$.ajax({
				url : URL.checkouttime,
				type : "get",
				data : {timeStr:formData.kssj,timeEnd:formData.jssj},
				success : function(data){
					if(data.success){
						
					}else{
						$("#jssj").datebox('clear');
						$.messager.alert("提示",data.returnMsg);
					}
				}
			});
		}
	});
	
});
