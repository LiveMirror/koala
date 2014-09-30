/**
 * 岗位调整
 */
var changePost = function(){
	var dialog = null;//对话框
    var departmentTree = null;//部门树
	var postGrid = null; //岗位列表
	var selectedPost = null;//已选岗位
	var selectedItem = {};//已选员工数据
	
	$("#selectedPost").delegate(".glyphicon.glyphicon-remove","click",function(){
		alert(343);
	});
	
	var init = function(employeeId){
		$.get( contextPath + '/pages/organisation/change-post.jsp').done(function(data){
			dialog = $(data);
			departmentTree = dialog.find('#departmentTree');
			postGrid = dialog.find('#postGrid');
			selectedPost = dialog.find('#selectedPost');
			loadExistPostList(employeeId);
            loadDepartmentTree(employeeId);
            dialog.find('#save').on('click',function(){
				if(selectedPost.find('.principal').length == 0){
					dialog.find('.modal-content').message({
						type: 'warning',
						content: '请设置主岗位'
				 });
					return;
				}
				var items = new Array();
				for(var prop in selectedItem){
					items.push(selectedItem[prop]);
				}
				$.ajax({
				    headers: { 
				        'Accept': 'application/json',
				        'Content-Type': 'application/json' 
				    },
				    'type': "Post",
				    'url': contextPath + '/employee/transform-post.koala?employeeId='+employeeId,
				    'data': JSON.stringify(items),
				    'dataType': 'json'
				 }).done(function(data){
					 if(data.success){
						 dialog.modal('hide');
						 $('body').message({
								type: 'success',
								content: '调岗成功'
						 });
						 loadData(employeeId);
					 }else{
						 dialog.find('.modal-content').message({
								type: 'error',
								content: data.errorMessage
						 }); 
					 }
				 });
			}).end().modal({
					keyboard: false
				}).on({
					'shown.bs.modal': function(){
						//loadDepartmentTree(employeeId);
					},
					'hidden.bs.modal': function(){
						$(this).remove();
					}
				});
            //兼容IE8 IE9
            if(window.ActiveXObject){
               if(parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10){
            	   dialog.trigger('shown.bs.modal');
               }
            }
		});
	};

	var loadData =  function(employeeId){
		$.get( contextPath + '/employee/get/'+employeeId+'.koala').done(function(result){
			var data = result;
			var employeeDetail = $('.employee-detail');
			employeeDetail.find('[data-id="sn"]').html(data.sn);
			employeeDetail.find('[data-id="name"]').html(data.name);
			employeeDetail.find('[data-id="gender"]').html(data.gender=='MALE' ? '男':'女');
			employeeDetail.find('[data-id="idNumber"]').html(data.idNumber);
			employeeDetail.find('[data-id="organizationName"]').html(data.organizationName);
			employeeDetail.find('[data-id="postName"]').html(data.postName);
			employeeDetail.find('[data-id="mobilePhone"]').html(data.mobilePhone);
			employeeDetail.find('[data-id="familyPhone"]').html(data.familyPhone);
			employeeDetail.find('[data-id="email"]').html(data.email);
			employeeDetail.find('[data-id="entryDate"]').html(data.entryDate);
			employeeDetail.find('[data-id="additionalPostNames"]').html(data.additionalPostNames);
		});
	};
	
	var loadExistPostList = function(employeeId){
		$.get( contextPath + '/employee/get-posts-by-employee.koala?employeeId='+employeeId).done(function(data){
			var postList = data;
			for(var i=0,j=postList.length; i<j; i++){
				var post = postList[i];
				selectedItem[post.postId] = {postId:post.postId, principal: post.principal};
				$('<div title="点击设置主岗位" class="selected-post '+ (post.principal?'principal':'')+'" data-value="'+post.postId+'">'+post.postName+'<a class="glyphicon glyphicon-remove"></a></div>')
				.appendTo(selectedPost)
				.click({postId: post.postId}, function(event){
					var $this = $(this);
					if($this.hasClass('principal')){
						return;
					}
					var principalPost = selectedPost.find('.principal');
					if(principalPost.length > 0){
						selectedItem[principalPost.data('value')].principal = false;
						principalPost.removeClass('principal');
					}
					selectedItem[event.data.postId].principal = true;
					$this.addClass('principal');
				})
				.find('a').click({postId: post.postId}, function(event){
					delete selectedItem[event.data.postId];
					$(this).parent().remove();
					
					$("#departmentTree .tree-selected").click();
					
					postGrid.find('[data-role="indexCheckbox"][value="'+data.postId+'"]').closest('tr').removeClass('success');
				});
			}
		});
	};
	
	/**
	 * 加载部门树
	 */
	var loadDepartmentTree = function(employeeId){
		departmentTree.loader({
			opacity: 0
		});
		
        $.get(contextPath  + '/organization/org-tree.koala').done(function(data){
        	departmentTree.loader('hide');
            var zNodes = new Array();
            $.each(data, function(){
                var zNode = {};
                if(this.organizationType == 'Company'){
                    zNode.type = 'parent';
                }else{
                    zNode.icon = 'glyphicon glyphicon-list-alt';
                }
                this.title = this.name;
                zNode.menu = this;
                if(this.children && this.children.length > 0){
                    zNode.children = getChildrenData(new Array(), this.children);
                }
                zNodes.push(zNode);
            });
            var dataSourceTree = {
                data: zNodes,
                delay: 400
            };
            
            /*部门树*/
            departmentTree.tree({
                dataSource: dataSourceTree,
                loadingHTML: '<div class="static-loader">Loading...</div>',
                multiSelect: false,
                cacheItems: true
            }).on({
                'selectParent': function(event, data){
                    loadPostList(data.data.id, employeeId);
                },
                'selectChildren': function(event, data){
                    loadPostList(data.id, employeeId);
                }
            });
        });
	};
	
    var getChildrenData = function(nodes, items){
        $.each(items, function(){
            var zNode = {};
            if(this.organizationType == 'Company'){
                zNode.type = 'parent';
            }else{
                zNode.icon = 'glyphicon glyphicon-list-alt';
            }
            this.title = this.name;
            zNode.menu = this;
            if(this.children && this.children.length > 0){
                zNode.children = getChildrenData(new Array(), this.children);
            }
            nodes.push(zNode);
        });
        return nodes;
    };

    var initRadio = function(obj){
		var $obj = $(obj);
		var parent = $obj.parent();
		if(parent.hasClass('checked')){
			$obj.attr('checked', false);
			parent.removeClass('checked');
		}else{
			$(obj).closest('table').find('[name="principal"]').parent().removeClass('checked');
			$(obj).parent().addClass('checked');
		}
	};
	
	var addPost = function(postId, postName, obj, a){
		$(obj).closest('.grid').trigger('addPost', {postId:postId, postName:postName, obj:obj, addButton:a});
	};
	
	/**
	 * 加载岗位列表
	 */
	var loadPostList = function(id, hostOrgId){
		var cols = [
			{ title:'岗位编号', name:'sn' , width: '150px'},
			{ title:'岗位名称', name:'name', width: '180px'},
			{ title:'是否主岗位', width: '120px', render: function(){
					return '<div class="radio"><span><input type="radio" name="principal" style="opacity: 0;" onclick="changePost().initRadio(this);"></span></div>';
				}
			},
			{ title:'操作', width: '120px',
				render: function(item){
					return '<a data-id="addIcon" onclick="var thiz=this;changePost().addPost('+item.id+', \''+item.name+'\', this,thiz)"><i class="glyphicon glyphicon-plus"></i></a>';
				}
			}
		];
		
		if(postGrid.data('koala.grid')){
			postGrid.data('koala.grid', null);
			postGrid.empty();
		}
		postGrid.grid({
			identity: 'id',
			isShowIndexCol: false,
			columns: cols,
			querys: [{title: '岗位名称', value: 'name'}],
			url: contextPath + '/post/paging-query-post-by-org.koala?organizationId='+id,
			dataFilter:function(result){
				var gridData = [];
				var existIds = [];
				$("#selectedPost .selected-post").each(function(i,t){
					existIds.push($(t).attr("data-value"));
				});
				$.each(result.data,function(r,i){
					var repetition = true;
					$.each(existIds,function(j,t){
						if (i.id == t) {
							repetition = false;
						}
					});
					if (repetition) {
						gridData.push(i);
					}
				});
				result.data = gridData;
                return result;
            }
		}).on({
			'addPost':function(evnet, data){
				var post = selectedPost.find('[data-value="'+data.postId+'"]');
				var principal = $(data.obj).closest('tr').find('[name="principal"]').is(":checked");
				if(post.length == 0){
					if(principal){
						if(selectedPost.find('.principal').length > 0){
							$('body').message({
								type: 'error',
								content: '主岗位已经设置'
							});
							return;
						}
						selectedItem[data.postId] = {postId:data.postId, principal: true};
					}else{
						selectedItem[data.postId] = {postId:data.postId, principal: false};
					}
					$(data.addButton).parent().parent().remove();
					$('<div title="点击设置主岗位" class="selected-post '+ (principal ? 'principal':'')+'" data-value="'+data.postId+'">'+data.postName+'<a class="glyphicon glyphicon-remove"></a></div>')
						.appendTo(selectedPost)
						.on('click', {postId: data.postId}, function(event){
							var $this = $(this);
							if($this.hasClass('principal')){
								return;
							}
							var principalPost = selectedPost.find('.principal');
							if(principalPost.length > 0){
								selectedItem[principalPost.data('value')].principal = false;
								principalPost.removeClass('principal');
							}
							selectedItem[event.data.postId].principal = true;
							$this.addClass('principal');
						})
						.find('a').on('click', function(){
							delete selectedItem[data.postId];
							$(this).parent().remove();
							
							$("#departmentTree .tree-selected").click();
							
							postGrid.find('[data-role="indexCheckbox"][value="'+data.postId+'"]').closest('tr').removeClass('success');
						});
				}else{
					if(principal){
						if(selectedPost.find('.principal').length == 0){
							post.addClass('principal');
							delete selectedItem[data.postId];
							selectedItem[data.postId] = {postId:data.postId, principal: true};                             
						}else{
							if(!post.hasClass('principal')){
								postGrid.message({
									type: 'error',
									content: '主岗位已经设置'
								});
							}
						}	
					}
				}
			}
		});
		
		if (!!window.ActiveXObject || "ActiveXObject" in window) {
			postGrid.find('.grid-table-body').css({height: '208px'});
		}
	};
	
	return {
		init: init,
		initRadio: initRadio,
		addPost: addPost
	};
};
