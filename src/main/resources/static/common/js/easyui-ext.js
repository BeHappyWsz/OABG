/**扩展列可拖动**/
$.extend($.fn.datagrid.methods, {
			columnMoving : function(jq) {
				return jq.each(function() {
							var target = this;
							var cells = $(this).datagrid('getPanel').find('div.datagrid-header td[field]');
							cells.draggable({
										revert : true,
										cursor : 'pointer',
										edge : 5,
										proxy : function(source) {
											var p = $('<div class="tree-node-proxy tree-dnd-no" style="position:absolute;border:1px solid #ff0000"/>').appendTo('body');
											p.html($(source).text());
											p.hide();
											return p;
										},
										onBeforeDrag : function(e) {
											e.data.startLeft = $(this).offset().left;
											e.data.startTop = $(this).offset().top;
										},
										onStartDrag : function() {
											$(this).draggable('proxy').css({
														left : -10000,
														top : -10000
													});
										},
										onDrag : function(e) {
											$(this).draggable('proxy').show().css({
														left : e.pageX + 15,
														top : e.pageY + 15
													});
											return false;
										}
									}).droppable({
										accept : 'td[field]',
										onDragOver : function(e, source) {
											$(source).draggable('proxy').removeClass('tree-dnd-no').addClass('tree-dnd-yes');
											$(this).css('border-left', '1px solid #ff0000');
										},
										onDragLeave : function(e, source) {
											$(source).draggable('proxy').removeClass('tree-dnd-yes').addClass('tree-dnd-no');
											$(this).css('border-left', 0);
										},
										onDrop : function(e, source) {
											$(this).css('border-left', 0);
											var fromField = $(source).attr('field');
											var toField = $(this).attr('field');
											setTimeout(function() {
														swapField(fromField, toField);
														$(target).datagrid();
														$(target).datagrid('columnMoving');
													}, 0);
										}
									});

							// swap Field to another location
							function swapField(from, to) {
								var columns = $(target).datagrid('options').columns;
								var cc = columns[0];
								_swap(from, to);
								function _swap(fromfiled, tofiled) {
									var fromtemp;
									var totemp;
									var fromindex = 0;
									var toindex = 0;
									for (var i = 0; i < cc.length; i++) {
										if (cc[i].field == fromfiled) {
											fromindex = i;
											fromtemp = cc[i];
										}
										if (cc[i].field == tofiled) {
											toindex = i;
											totemp = cc[i];
										}
									}
									cc.splice(fromindex, 1, totemp);
									cc.splice(toindex, 1, fromtemp);
								}
							}
						});
			}
		});