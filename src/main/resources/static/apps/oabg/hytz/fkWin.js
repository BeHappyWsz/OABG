$(function() {
	var URL = {
		/** 保存表单**/
		save : ctx + "/oabg/hytz/hyfk",
		lxr: ctx + "/oabg/hytz/lxrchry"
	};

	/** 模块名称* */
	var modelName = "fk";
	/** a标签的href*/
		/** 表单* */
	var form = $("#" + modelName + "_form");
	/** 绑定按钮事件* */
	bindButtonAction();
	function bindButtonAction() {
		/** 绑定保存事件* */
		$("a[data-action=hytz-fk]").bind("click", function() {
			save();
		});
		$("#fk_chry").bind("click", function() {
			var id = $("#bmcode").val();
			var names = $("#fa_chrname").val();
			openLxr(id,names);
		});
	}
	
	var hyid = $("#hyid").val();
	/**
	 * 保存
	 */
	function save() {
		if (form.form("validate")) {
			/** 序列化form* */
			var formData = form.serializeObject({transcript:"overlay"});
			$.ajax({
				type : "post",
				url : URL.save,
				data :formData,
				success : function(data) {
					$.messager.progress("close");
					if (data.success) {
						showMsg(data.returnMsg);
						$("#hytz_formWin").window("close");
						$("#hytz_fkWin").window("close");
						$("#hytz_mWin").window("close");
						$("#hytz_grid").datagrid("reload");
					}
				}
			});
		}
	}
	var roleCode = $("#roleCode").val();
	if(roleCode.indexOf("DWBM")!=-1){
		$("#sfcj").combobox({
			onSelect :function(data){
				if(data.value==1&&data.value=="1"){
					$.parser.parse('#fa_chrname'); 
					$('#fa_chrname').textbox({ required:true })
					$("#hide").show();
				}else{
					$("#hide").hide();
					$('#fa_chrname').textbox({ required:false })
				}
			}
		});
	}
	/**部门参会人员*/
	function openLxr(id,names){
			var win = $("<div id='" + modelName + "_lxr'></div>").window({
				title : "联系人",
				width : 650,
				height :400,
				href :URL.lxr+"?bmc="+id,
				onLoad : function(){
					$("#lxcgry_names").val(names);
					closeLoadingDiv();
				},
				onClose : function() {
					win.window('destroy');
				}
			});
	}
	
});
