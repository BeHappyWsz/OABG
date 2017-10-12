$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/zcsq/ly/save",
		gridZcglLyData : ctx + "/oabg/zcdj/gridLy"
	};

	/** 模块名称* */
	var modelName = "zcsqly";

	/** 表单* */
	var form = $("#zcsqly_form");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=zcsqly_save]").bind("click", function() {
			save();
		});
	}
	
	/**
	 * 保存
	 */
	function save() {
		if (form.form("validate")) {
			/** 序列化form* */
			var formData = form.serializeObject({transcript:"overlay"});
			var leftnum = $("#" + modelName + "_form_leftnum").val();
			if(parseInt(formData.sl) > parseInt(leftnum)){
				showMsg("库存不足！");
				return false;
			}
			$.messager.confirm("系统提示","确认提交？",function(r){
				if(r){
					$.ajax({
						type : "post",
						url : URL.save,
						data :formData,
						success : function(data) {
							if (data.success) {
								showMsg(Msg.saveSuc);
								$("#zcsqly_formWin").window("close");
								$("#" + modelName + "_grid").datagrid("reload");
							}
						}
					});
				}
			});
		}
	}
	
	$("#" + modelName + "_form_zcglId").combogrid({
		panelWidth:450,
		width:155,
		required:true,
	    idField:'zcmc',
	    textField:'zcmc',
	    editable:false,
	    pagination:true,
	    url:URL.gridZcglLyData,
	    columns:[[
	        {field:'zcmc',title:'资产名称',align:"left",halign:"center",width:120},    
	        {field:'pp',title:'品牌',align:"left",halign:"center",width:120},    
	        {field:'xh',title:'型号',align:"left",halign:"center",width:120},    
	        {field:'sl',title:'剩余数量',align:"left",halign:"center",width:80}
	    ]],
	    onSelect : function(index,row){
	    	$("#" + modelName + "_form_leftnum").val(row.sl);
	    	$("#zcsqly_form_pp").textbox('setValue',row.pp);
	    	$("#zcsqly_form_xh").textbox('setValue',row.xh);
	    	$("#zcsqly_form_zcgl_id").val(row.id);
	    }
	});
	
});
