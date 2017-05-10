/**
 * 列表模式展现
 * 参数：（模板字符串，超链接字段名数组，信息来源中文名）
 */
//文库资源
var list_judgment_theft = new TemplateTool(
		"<li class=\"list-group-item\"><div class=\"media\"><div class=\"media-body jys-media-body\">"+
		"<a href=\"{{path_t}}\" target=\"_blank\"><h4 class=\"media-heading\">{{title_t}}</h4></a>"+
		"<p class=\"list-group-item-text jys-p\">{{content_t}}" +
		"</p>"+
		"<p class=\"jys-search-info\"><span>法庭：<strong>{{court_t}}</strong></span><span> 案件类型：<strong>{{caseType_t}}</strong></span>"+
		"<span>   编号：<strong>{{number_t}}</strong></span><span>   创建时间：<strong>{{judgement_time_t}}</strong></span></p></div>"+
		"<div class=\"media-right media-middle\"></div> </div>",
		[ "caseType_t"],"文库资源");//模板