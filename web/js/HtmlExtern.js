/**
 * Created by IBAS on 2017/2/14.
 * 这里的使用了Array中的兼容方法
 */

var ibasHtmlExt = (function() {
    window.Element = (typeof Element == 'undefined')?Object:Element;
    function afterInsert () {
        if (!Element.prototype.insertAfter) {
            Element.prototype.insertAfter = function(tar) {
                var ele = this;
                if (ele.parentElement) {
                    if (ele.nextSibling) {
                        ele.parentElement.insertBefore(tar,ele.nextSibling);
                    } else {
                        ele.parentElement.appendChild(tar);
                    }
                } else {
                    throw new Error(ele + " have not parrent element.");
                }
            };
        }
    };
    function afterInsert_h(ele,tar) {
        if (ele.parentElement) {
            if (ele.nextSibling) {
                ele.parentElement.insertBefore(tar,ele.nextSibling);
            } else {
                ele.parentElement.appendChild(tar);
            }
        } else {
            throw new Error(ele + " have not parrent element.");
        }
    }
    function beforeInsert() {
        if (!Element.prototype.beforeInsert) {
            Element.prototype.beforeInsert = function(tar) {
                var ele = this;
                if (ele.parentElement) {
                    ele.parentElement.insertBefore(tar,ele);
                } else {
                    throw new Error(tar + 'have not parent element.');
                }
            };
        }
    };
    function beforeInsert_h(ele,tar) {
        if (ele.parentElement) {
            ele.parentElement.insertBefore(tar,ele);
        } else {
            throw new Error(tar + 'have not parent element.');
        }
    }
    //将tar放在ele(this)的本身和parent中间
    //初始
    //<ele-parent>
    //  <ele></ele>
    //</ele-parent>
    //结果
    //<ele-parent>
    //  <tar>
    //      <ele></ele>
    //  </tar>
    //</ele-parent>
    function insertMid() {
        if (!Element.prototype.insertMid) {
            Element.prototype.insertMid = function(tar) {
                var ele = this;
                if (ele.parentElement) {
                    ele.beforeInsert(tar);
                    ele.parentElement.removeChild(ele);
                    tar.appendChild(ele);
                }
            };
        }
    };
    function insertMid_h(ele,tar) {
        if (ele.parentElement) {
            beforeInsert_h(ele,tar);
            ele.parentElement.removeChild(ele);
            tar.appendChild(ele);
        }
    };
    //将tar元素放置到ele (this)元素后面
    function insertAfter() {
        if (Element.prototype.insertAfter) {
            Element.prototype.insertAfter = function(tar) {
                var ele = this;
                if (ele.parentElement) {
                    if (ele.nextSibling) {
                        ele.parentElement.insertBefore(ele.nextSibling);
                    } else {
                        ele.parentElement.appendChild(tar);
                    }
                } else {
                    throw new Error(ele + " have not parrent element.");
                }
            }
        }
    };
    function insertAfter_h(ele,tar) {
        if (ele.parentElement) {
            if (ele.nextSibling) {
                ele.parentElement.insertBefore(ele.nextSibling);
            } else {
                ele.parentElement.appendChild(tar);
            }
        } else {
            throw new Error(ele + " have not parrent element.");
        }
    }
    return {
        afterInsert : afterInsert,
        beforeInsert : beforeInsert,
        insertMid : insertMid,
        insertAfter : insertAfter,
        afterInsert_h : afterInsert_h,
        beforeInsert_h : beforeInsert_h,
        insertMid_h : insertMid_h,
        insertAfter_h : insertAfter_h
    }
})();
