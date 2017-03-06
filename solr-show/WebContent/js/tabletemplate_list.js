/**
 * 列表模式展现
 * 参数：（模板字符串，超链接字段名数组，信息来源中文名）
 */

//天然气信息
var list_jcy_gas_info = new TemplateTool(
		"<li class=\"list-group-item\"><div class=\"media\"><div class=\"media-body jys-media-body\">"+
		"<p class=\"list-group-item-text jys-p\">姓名：{{USER_NAME_t}} 地址：{{CONNECT_ADDRESS_t}} 部门编号: {{ORGAN_CODE_t}} " +
		"编号: {{id}} " +
		"{{infoSource}}</p></div>"+"<div class=\"media-right media-middle\"><button type=\"button\" class=\"btn btn-primary\" onclick=\"getDetailPage('{{beforeChangeData.tablename}}','{{beforeChangeData.id}}');\">查看详情</button></div> </div>",
		[ "USER_NAME_t", "CONNECT_ADDRESS_t"],"天然气信息");//模板
//案件信息
var list_case = new TemplateTool(
		"<li class=\"list-group-item\"><div class=\"media\"><div class=\"media-body jys-media-body\">"+
		"<h4 class=\"media-heading\">{{CASE_NAME_txt}}</h4>"+
		"<p class=\"list-group-item-text jys-p\">案件编号：{{caseId}}" +
		" 案件简介: {{CASE_DESC_txt}} " +
		"{{infoSource}}</p>"+
		"<p class=\"jys-search-info\"><span>创建人：<strong>{{CRT_USER_NAME_t}}</strong></span> <span>案发时间：<strong>{{CASE_TIME_os}}</strong></span></p></div>"+
		"<div class=\"media-right media-middle\"><button type=\"button\" class=\"btn btn-primary\" onclick=\"getDetailPage('{{beforeChangeData.tablename}}','{{beforeChangeData.id}}');\">查看详情</button></div> </div>",
		[ "caseId"],"案件信息");//模板
//文书笔录
var list_file = new TemplateTool(
		"<li class=\"list-group-item\"><div class=\"media\"><div class=\"media-body jys-media-body\">"+
		"<h4 class=\"media-heading\">{{FILE_NAME_t}}</h4>"+
		"<p class=\"list-group-item-text jys-p\">案件编号：{{caseId}} " +
		"{{infoSource}}</p>"+
		"<p class=\"jys-search-info\"><span>文档作者：<strong>{{CRT_USER_NAME_t}}</strong></span></p></div>"+
		"<div class=\"media-right media-middle\"><button type=\"button\" class=\"btn btn-primary\" onclick=\"getDetailPage('{{beforeChangeData.tablename}}','{{beforeChangeData.id}}');\">查看详情</button></div> </div>",
		[ "caseId"],"文书笔录");//模板
//设备信息
var list_jcy_device_info = new TemplateTool(
		"<li class=\"list-group-item\"><div class=\"media\"><div class=\"media-body jys-media-body\">"+
		"<h4 class=\"media-heading\">{{DEVICE_NAME_t}}</h4>"+
		"<p class=\"list-group-item-text jys-p\">设备型号：{{DEVICE_MODEL_t}}" +" 使用系统：{{USE_SYSTEM_t}}"+" 安装地点：{{INSTALL_LOCATION_t}}"+
		" 中标公司：{{WIN_COMPANY_t}}</p>"+
		"<p class=\"list-group-item-text jys-p\">基本参数：{{BASIC_PARAMETER_t}} "+
		"{{infoSource}}</p>"+
		"<p class=\"jys-search-info\"><span>设备数量： <strong>{{DEVICE_NUM_s}}{{UNIT_s}}</strong></span></p></div>"+
		"<div class=\"media-right media-middle\"><button type=\"button\" class=\"btn btn-primary\" onclick=\"getDetailPage('{{beforeChangeData.tablename}}','{{beforeChangeData.id}}');\">查看详情</button></div> </div>",
		[ "USE_SYSTEM_t","WIN_COMPANY_t","DEVICE_NAME_t","DEVICE_MODEL_t"],"设备信息");//模板
//文库资源
var list_library = new TemplateTool(
		"<li class=\"list-group-item\"><div class=\"media\"><div class=\"media-body jys-media-body\">"+
		"<h4 class=\"media-heading\">{{title_t}}</h4>"+
		"<p class=\"list-group-item-text jys-p\">{{filecontent}} " +
		"{{infoSource}}</p>"+
		"<p class=\"jys-search-info\"><span>文档作者：<strong>{{creatuser_s}}</strong></span><span> 文档大小：<strong>{{filesize_s}}</strong></span>"+
		"<span>   文档类型：<strong>{{filetype_s}}</strong></span><span>   创建时间：<strong>{{creattime_inc}}</strong></span></p></div>"+
		"<div class=\"media-right media-middle\"><button type=\"button\" class=\"btn btn-primary\" onclick=\"getDetailPage('{{beforeChangeData.tablename}}','{{beforeChangeData.id}}');\">查看详情</button></div> </div>",
		[ "creatuser_s"],"文库资源");//模板