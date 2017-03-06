/**
 * 点击展开更多详情
 * 参数：（模板字符串，超链接字段名数组，信息来源中文名）
 */
//案件信息展开详情
var detail_case = new TemplateTool(
		"<b>①当事人信息：</b>{{parties_txt}}" +
		"<br><b>②办理单位：</b>{{UNDERTAKER_ORGAN_NAME_txt}}" +
		"<br><b>③办理人员：</b>{{UNDERTAKER_USER_NAME_txt}}",
		[],"");//模板
//文书笔录展开详情
var detail_file = new TemplateTool(
		"<b>①文件路径：</b>{{FILE_PATH_os}}",
		[],"");//模板

//天然气信息展开详情
var detail_jcy_gas_info = new TemplateTool(
		"",
		[],"");//模板
//设备信息展开详情
var detail_jcy_device_info = new TemplateTool(
		"",
		[],"");
//文库资源展开详情
var detail_library = new TemplateTool(
		"",
		[],"");