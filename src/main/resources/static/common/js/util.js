/**
 * 通用js 作者：周超 日期：2015-06-04
 */

/**ie下无法使用数组.indexOf方法，必须加入此函数才能使用*/
if (!Array.indexOf) {
	Array.prototype.indexOf = function(obj) {
		for (var i = 0; i < this.length; i++) {
			if (this[i] == obj) {
				return i;
			}
		}
		return -1;
	}
}

/**扩展contains方法*/
if (!Array.contains) {
	Array.prototype.contains = function(item) {
		return RegExp("\\b" + item + "\\b").test(this);
	};
}

function calcTime(date1, date2) {
	var date3 = date2.getTime() - date1.getTime() // 时间差的毫秒数
	// 计算出相差天数
	var days = Math.floor(date3 / (24 * 3600 * 1000))
	// 计算出小时数
	var leave1 = date3 % (24 * 3600 * 1000) // 计算天数后剩余的毫秒数
	var hours = Math.floor(leave1 / (3600 * 1000))
	// 计算相差分钟数
	var leave2 = leave1 % (3600 * 1000) // 计算小时数后剩余的毫秒数
	var minutes = Math.floor(leave2 / (60 * 1000))

	// 计算相差秒数
	var leave3 = leave2 % (60 * 1000) // 计算分钟数后剩余的毫秒数
	var seconds = Math.round(leave3 / 1000)

	return (hours == 0 ? "" : hours + "小时 ") + (minutes == 0 ? "" : minutes + "分钟 ") + seconds + " 秒";
}

/**格式化日期，放在datebox中显示默认值*/
function formatterDate(date) {
	var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
	var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
	+ (date.getMonth() + 1);
	return date.getFullYear() + '-' + month + '-' + day;
}

Array.prototype.indexOf = function(val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) return i;
    }
    return -1;
};

Array.prototype.remove = function(val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};
