var ibasFile = (function(tar){
	var target = domUtil.getTar(tar),
		tarDiv;
	function check() {
		//判断是否为input元素
		if (target.tagName.toLowerCase() === 'input') {
			//判断是否为file类型
			if (target.getAttribute('type').toLowerCase() === 'file') {
				return true;
			}
		}
		return false;
	};
	if (target == null) {
		return null;
	} else {
		if (check()) {
			return (function(){
				var targetEle = target,
					topDiv = tarDiv,
					totalDiv,
					chiDiv = [],
					total = 0,
					defaultLanguage = {
						clickToSelectFile : 'Click To Select File',
						reSelect : 're-select',
						toMin : 'to be minimized',
						toMax : 'to be maximized',
						files : 'files'
					},
					typeRegx = /\.[a-zA-Z0-9]+/;
				//仅仅允许三种形式
				//types = ['*.txt','*.jpg']
				//types = ['txt','jpg']
				//types = '*.txt,*.jpg';
				var setFileTypeLimit = function(types) {
					if (types instanceof Array) {
						if (types.length > 0) {
							if (typeRegx.exec(types[0]) != null) {
								if (typeRegx.exec(types[0]).join() === types[0]) {
									targetEle.accept = types.join(',');
								}
							} else {
								targetEle.accept = '.' + types.join(',.');
							}
						}
					} else {
						targetEle.accept = types;
					}
				};
				var setLanguage = function(obj) {
					for (var i in obj) {
						defaultLanguage[i] = obj[i];
					}
				}
				function min_list() {
					var ele = chiDiv[1].getElementsByClassName('ibas-file-item');
					for (var i = 0;i < ele.length;i++) {
						ele[i].style.display = 'none';
					}
					totalDiv.style.display = 'block';
					$(this).removeClass('glyphicon-chevron-down');
					//this.classList.remove('glyphicon-chevron-down');
					$(this).addClass('glyphicon-chevron-down');
					//this.classList.add('glyphicon-chevron-down');
					this.innerText = defaultLanguage.toMax;
					this.onclick = max_list;
				}
				function max_list() {
					var ele = chiDiv[1].getElementsByClassName('ibas-file-item');
					for (var i = 0;i < ele.length;i++) {
						ele[i].style.display = 'block';
					}
					totalDiv.style.display = 'none';
					$(this).addClass('glyphicon-chevron-down');
					//this.classList.add('glyphicon-chevron-down');
					$(this).removeClass('glyphicon-chevron-down');
					//this.classList.remove('glyphicon-chevron-down');
					this.innerText = defaultLanguage.toMin;
					this.onclick = min_list;
				}
				var selectNewFile = function() {
					var files = this.files;
					//删除1中的所有元素
					var ele = chiDiv[1].children,
						len = ele.length;
					for (var i = 0;i < len;i++) {
						chiDiv[1].removeChild(ele[0]);
					}
					if (files.length > 0) {
						total = files.length;
						chiDiv[0].style.display = 'none';
						chiDiv[1].style.display = 'block';
						//在1中新建元素
						var re_span = domUtil.newEleWithConten('span',defaultLanguage.reSelect);
						$(re_span).addClass('glyphicon glyphicon-share-alt ibas-file-span');
						//re_span.classList.add('glyphicon','glyphicon-share-alt','ibas-file-span');
						var min_span = domUtil.newEleWithConten('span',defaultLanguage.toMin);
						$(min_span).addClass('glyphicon glyphicon-chevron-up ibas-file-span');
						//min_span.classList.add('glyphicon','glyphicon-chevron-up','ibas-file-span');
						min_span.onclick = min_list;
						min_span.style.float = 'right';
						var t = this;
						re_span.onclick = function () {
							t.click();
						};
						chiDiv[1].appendChild(re_span);
						chiDiv[1].appendChild(min_span);
						for (var i = 0;i < files.length;i++) {
							var tspan = domUtil.newEleWithConten('span',targetEle.files[i].name);
							$(tspan).addClass('glyphicon glyphicon-file ibas-file-item ibas-file-span');
							//tspan.classList.add('glyphicon glyphicon-file ibas-file-item ibas-file-span');
							chiDiv[1].appendChild(tspan);
						}
						totalDiv = domUtil.newEleWithConten('div',total + '' + defaultLanguage.files);
						chiDiv[1].appendChild(totalDiv);
						totalDiv.style.display = 'none';
						$(totalDiv).addClass('ibas-file-count glyphicon glyphicon-file');
						//totalDiv.classList.add('ibas-file-count glyphicon glyphicon-file');
					} else {
						chiDiv[1].style.display = 'none';
						chiDiv[0].style.display = 'block';
					}
				}
				function init() {
					//新建元素
					topDiv = domUtil.newEle('div');
					$(topDiv).addClass('ibas-file-div');
					//topDiv.classList.add('ibas-file-div');
					topDiv.onclick = function(event) {
						targetEle.click();
						event.stopPropagation();
					};
					chiDiv.push(domUtil.newEle('div'));	//	第一个为未选择文件时的提示
					chiDiv.push(domUtil.newEle('div'));	//	第二个为选中文件后的文件列表
					chiDiv[1].onclick = function(event) {
						//阻止事件冒泡
						event.stopPropagation();
					};
					//隐藏第二个元素
					chiDiv[1].style.display = 'none';
					//为第一个元素添加子元素
					var chiDivIcon = domUtil.newEle('div');
					$(chiDivIcon).addClass('glyphicon glyphicon-folder-open ibas-file-tip text-center');
					//chiDivIcon.classList.add('glyphicon','glyphicon-folder-open','ibas-file-tip','text-center');
					var chiDivText = domUtil.newEleWithConten('div',defaultLanguage.clickToSelectFile);
					$(chiDivText).addClass('text-center');
					//chiDivText.classList.add('text-center');
					//将元素添加到dom中
					if (targetEle.nextSibling) {
						targetEle.parentElement.insertBefore(topDiv,targetEle.nextSibling);
					} else {
						targetEle.parentElement.appendChild(topDiv);
					}
					topDiv.appendChild(chiDiv[0]);
					topDiv.appendChild(chiDiv[1]);
					chiDiv[0].appendChild(chiDivIcon);
					chiDiv[0].appendChild(chiDivText);
					targetEle.onchange = selectNewFile;
				};

				return {
					targetEle : targetEle,
					topDiv : topDiv,
					init : init,
					setFileTypeLimit : setFileTypeLimit,
					setLanguage : setLanguage
				}
			})();
		} else {
			throw new Error(target + ' is not input element or type is not file .');
			return null;
		}
	}
});