$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/bgypsq/save",
		gridBgypData : ctx + "/oabg/bgypdj/grid"
	};

	/** 模块名称* */
	var modelName = "bgypsq";

	/** 表单* */
	var form = $("#" + modelName + "_form");

	/** 绑定按钮事件* */
	bindButtonAction();

	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=BGYPSQ_SUBMITBTN]").bind("click", function() {
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
								$("#" + modelName + "_formWin").window("close");
								$("#" + modelName + "_grid").datagrid("reload");
							}
						}
					});
				}
			});
		}
	}
	
	$("#" + modelName + "_form_bgypId").combogrid({
		panelWidth:370,
		width:155,
		required:true,
	    idField:'id',
	    textField:'name',
	    editable:false,
	    pagination:true,
	    url:URL.gridBgypData,
	    columns:[[
	        {field:'name',title:'物品名称',align:"left",halign:"center",width:140},
	        {field:'bgypflName',title:'分类',align:"left",halign:"center",width:100},
	        {field:'sl',title:'剩余数量',align:"right",halign:"center",width:100}
	    ]],
	    onSelect : function(index,row){
	    	$("#" + modelName + "_form_leftnum").val(row.sl);
	    }
	});
	
});
