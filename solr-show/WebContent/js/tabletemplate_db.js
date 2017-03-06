/**
 * 
 */
 //显示方式
var showType = 'list';
var templateMap = {};
var initCompleteCallBack="";

function initTemplate(serviceAddress,callbackFunction) {
	if(callbackFunction!=undefined&&callbackFunction!=null){
		initCompleteCallBack=callbackFunction;
	}
	// 测试后台获取模板数据
	$.ajax({
		url : serviceAddress + "/service/jespconfdsshow/queryAll",
		async : false,
		dataType : 'jsonp',
		data:{"jsonp":"initTemplateSuccess"}
	});
}
function initTemplateSuccess(data){
	for ( var i in data) {
		var hrefInfo = data[i].hrefInfo;
		var tablename = data[i].tableName.toLowerCase();
		var showTemplate = data[i].showTemplate;
		var showmoreTemplate = data[i].showmoreTemplate;
		var infoSourceName = data[i].infoSourceName;
		var detailUrl = data[i].detailUrl;
		var hrefFields;
		if ($.trim(hrefInfo) == "" || hrefInfo == null) {
			hrefFields = [];
		} else {
			hrefFields = hrefInfo.split(",");
		}
		if (showmoreTemplate == null) {
			showmoreTemplate = "";
		}
		var showTemplateTool = new TemplateTool(showTemplate,
				hrefFields, infoSourceName);
		var showmoreTemplateTool = new TemplateTool(showmoreTemplate,
				hrefFields, infoSourceName);
		templateMap[showType + "_" + tablename] = showTemplateTool;
		templateMap["detail_" + tablename] = showmoreTemplateTool;
		templateMap["url_" + tablename] = detailUrl;
	}
	if(initCompleteCallBack!=""){
		eval(initCompleteCallBack);
	}
}
/**
 * 获取模板
 * 
 * @param key
 * @returns
 */
function getTemplate(key) {
	key = key.toLowerCase();
	if (templateMap[key] != undefined) {
		return templateMap[key];
	}
	// 兼容之前方式
	return eval(key);
}
// 展示tab页内容区域-请求响应 列表展示
function showTabInfo_list(data) {
	var dataO = data.response.docs;
	var hls = data.highlighting;
	var rightcount = 0;
	var parts = "";
	var errorNum = 0;
	for (var i = 0; i < dataO.length; i++) {
		// 高亮替代
		var id = dataO[i].id;
		var tablename = dataO[i].tablename;
		var hl = hls[id];
		try {
			var tool = getTemplate(showType + '_' + tablename);
			var dataStore = clone(dataO[i]);//将dataO[i]克隆一份，作为detailTool渲染时使用
			// tool.hrefTemplate = 'mainsearch.htm';
			var oneData = tool.format(dataO[i], hl);
			var detailTool = getTemplate('detail_' + tablename);
			var detailHtml = detailTool.format(dataStore, hl);
			if (detailHtml.length > 0) {
				oneData+="<a id='show"+tablename+id+"' href=\"javascript:showDetail('"+tablename+id+"')\">展开更多</a>";	
				oneData+="<a id='first_hide"+tablename+id+"' style=\"display:none\" href=\"javascript:hideDetail('"+tablename+id+"')\">收起</a>";
	        	oneData+="<div id='detail"+tablename+id+"' style=\"display:none\">"+detailHtml+"</div>";
	        	oneData+="<a id='second_hide"+tablename+id+"' style=\"display:none\" href=\"javascript:hideDetail('"+tablename+id+"')\">收起</a>";
			}
			oneData += "</li>";
			parts += oneData;
		} catch (e) {
			console.log(e);
			errorNum++;
			continue;
		}

	}
	if (dataO.length == 0 || dataO.length - errorNum == 0) {
		parts = "<li class=\"list-group-item\">"
				+ "<p class=\"nothing\"><span class=\"glyphicon glyphicon-exclamation-sign\"></span> 没有相关搜索结果</p>"
				+ "</li>";
	}
	return parts;
}

//展开详情
function showDetail(id){
	var show = "show"+id;
	var detail = "detail"+id;
	var first_hide = "first_hide"+id;
	var second_hide = "second_hide"+id;
	document.getElementById(show).style.display="none";
	document.getElementById(detail).style.display="";
	document.getElementById(first_hide).style.display="";		
	document.getElementById(second_hide).style.display="";		
}
//收起
function hideDetail(id){
	var show = "show"+id;
	var detail = "detail"+id;
	var first_hide = "first_hide"+id;
	var second_hide = "second_hide"+id;
	document.getElementById(show).style.display="";
	document.getElementById(detail).style.display="none";
	document.getElementById(first_hide).style.display="none";	
	document.getElementById(second_hide).style.display="none";
}