var ibasLabel = (function(tar){
	var target = domUtil.getTar(tar);
	function check(tar) {
		//判断是否为input元素
		if (target.tagName.toLowerCase() === 'input') {
			//判断是否为text类型
			if (target.getAttribute('type').toLowerCase() === 'text') {
				return true;
			}
		}
		return false;
	};
	if (target == null) {
		throw new Error(target + ' is null.');
	} else {
		if (check()) {
			return (function(){
				var targetEle = target,
					vinput,
					topDiv,
					arr = [],
					divarr = [],
					cot = 0,			//计数器（仅仅为了作为索引值）
					labelLength = 6,
					labelCot = 1,		//计算器（用于统计当前可用的标签个数）
					defaultMaxLabelCot = 10,//默认最多的标签数量（和labelCot对比）
					defalutLanguage = {
						limitLength : '标签的长度不能超过%个字节.',
						lessThanMinLen : '标签的长度太小',
						limitLabelCount : '最多的标签数量为%个'
					};
				var setLabelLength = function(len_) {
					if (len_ < 1) {
						throw new Error(defalutLanguage.lessThanMinLen);
					} else {
						labelLength = len_;
					}
				};
				var setLanguage = function(obj) {
					for (var i in obj) {
						defalutLanguage[i] = obj[i];
					}
				}
				var setDefaultMaxLabel = function(dml) {
					defaultMaxLabelCot = dml;
				};
				var init = function() {
					topDiv = domUtil.newEle('div');
					vinput = domUtil.newEle('input');
					vinput.name = targetEle.name;
					vinput.style.visibility = 'hidden';
					vinput.style.display = 'none';
					targetEle.name = '';
					//topDiv.classList.add('ibas-label-div');
					$(topDiv).addClass('ibas-label-div');
					//targetEle.beforeInsert(vinput);
					ibasHtmlExt.beforeInsert_h(targetEle,vinput);
					//targetEle.insertMid(topDiv);
					ibasHtmlExt.insertMid_h(targetEle,topDiv);
					$(targetEle).keypress(inputInput);
				};
				var inputInput = function(e){
					if (e.keyCode == 13) {
						//回车发生时，新建一个标签
						newLabel(this.value);
						this.value = '';
					}
				};
				var newLabel = function(text_) {
					if (defaultMaxLabelCot < labelCot) {
						alert(defalutLanguage.limitLabelCount.replace('%',defaultMaxLabelCot));
						return;
					}
					if (text_ == '') {
						return ;
					}
					if (text_.length > labelLength) {
						alert(
							defalutLanguage.limitLength.replace('%',labelLength)
						);
						return;
					}
					var ret = arr.pushExt(text_,3);
					if (ret != -1) {
						//ret为重复的标记
						//divarr[ret].classList.add('ibas-IL-warn');
						$(divarr[ret]).addClass('ibas-IL-warn');
						divarr[ret].style.background = 'rgb(74, 91, 128)';
						setTimeout(function(){
							$(divarr[ret]).removeClass('ibas-IL-warn');
							divarr[ret].style.background = '';
						},1000);
						return;
					}
					var labelDiv = domUtil.newEle('div');
					var labelSpan = domUtil.newEleWithConten('span',text_);
					var closeSpan = domUtil.newEleWithConten('span','x');
					labelDiv.index = cot;
					cot++;
					labelCot++;
					//labelDiv.classList.add('ibas-label-cdiv');
					//labelSpan.classList.add('ibas-label-label');
					//closeSpan.classList.add('ibas-label-btn-close');
					
					//labelDiv.classListAdd('ibas-label-cdiv');
					$(labelDiv).addClass('ibas-label-cdiv');
					//labelSpan.classListAdd('ibas-label-label');
					$(labelSpan).addClass('ibas-label-label');
					//closeSpan.classListAdd('ibas-label-btn-close');
					$(closeSpan).addClass('ibas-label-btn-close');
					//topDiv.appendChild(labelDiv);
					
					//targetEle.beforeInsert(labelDiv);
					ibasHtmlExt.beforeInsert_h(targetEle,labelDiv);
					labelDiv.appendChild(labelSpan);
					labelDiv.appendChild(closeSpan);
					divarr.push(labelDiv);
					closeSpan.onclick = closeSpanClick;
					vinput.value = arr.joinExt(',',2);
				};
				var closeSpanClick = function() {
					//console.log(this.parentElement);
					arr[this.parentElement.index] = '';
					topDiv.removeChild(this.parentElement);
					vinput.value = arr.joinExt(',',2);
					divarr[this.parentElement.index] = '';
					labelCot--;
				};
				var showValue = function() {
					console.log(vinput.value);
				};
				return {
					init : init,
					newLabel : newLabel,
					showValue : showValue,
					setLabelLength : setLabelLength,
					setLanguage : setLanguage,
					setDefaultMaxLabel : setDefaultMaxLabel,
					topDiv : topDiv
				};
			})();
		} else {
			throw new Error('Something is error.');
		}
	}
});