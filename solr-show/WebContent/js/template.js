//format参数缓存集合
var objMap = {};

var arrayToStringTemplate="{{if total==1}}" +
		"{{each data as value}}" +
		"{{value}}" +
		"{{/each}}" +
		"{{else if total>1}}" +
		"{{each data as value}}" +
		"{{value}}… " +
		"{{/each}}" +
		"{{/if}}";
template.config("escape", false);
var arrayRender=template.compile(arrayToStringTemplate);
/**
 * 为简易搜查询结果展示提供模板支持,同时提供通用性
 * @param template
 * @param hrefFields
 * @returns
 */
function TemplateTool(thistemplate,hrefFields,infoSource){
	//超链接字段
	this.hrefFields=hrefFields,
	this.hrefTemplate='main.htm',
	//模板
	this.thistemplate=thistemplate,
	this.infoSource=infoSource,
	//obj-值集合,hl高亮集合
	this.format=function(obj,hl){
		objMap[obj.id]=obj;
		var result="";
		//存储初始的obj，做链接时传参不应含有color=red的span修饰
		var beforeChangeData=clone(obj);
		obj.beforeChangeData=beforeChangeData;
		//高亮替换
		for(var key in hl){
			obj[key]=hl[key];
		}
		var jsonData={data:[],total:1};
		//字段为数组时，以...连接成字符串
		for(var key in obj){
			if(isArray(obj[key])){
				jsonData.data=obj[key];
				jsonData.total=obj[key].length;
				obj[key]=arrayRender(jsonData);
			}
		}
		//字段为数组时，以...连接成字符串
		for(var key in obj.beforeChangeData){
			if(isArray(obj.beforeChangeData[key])){
				jsonData.data=obj.beforeChangeData[key];
				jsonData.total=obj.beforeChangeData[key].length;
				obj.beforeChangeData[key]=arrayRender(jsonData);
			}
		}
		//渲染链接字段
		for(var i=0;i<hrefFields.length;i++){
			var temp=obj[hrefFields[i]];
			var tempOld=obj.beforeChangeData[hrefFields[i]];
			if(temp!=undefined&&tempOld!=undefined){	
				obj[hrefFields[i]]="<a onclick=\"hrefClick('"+tempOld+"');\">"+temp+"</a>";
			}
		}
		this.thistemplate=this.thistemplate.replace("{{infoSource}}","[<font style=\"color:#AAAAAA\">信息来源："+this.infoSource+"</font>]");
		template.config("escape", false);
		var render=template.compile(this.thistemplate);
		result=render(obj);
		return result;
	};
	}
//判断是否为数组类型
function isArray(obj){ 
	return (typeof obj=='object')&&obj.constructor==Array; 
	} 
//克隆对象
function clone(obj){
	var o;
	switch(typeof obj){
	case 'undefined': break;
	case 'string'   : o = obj + '';break;
	case 'number'   : o = obj - 0;break;
	case 'boolean'  : o = obj;break;
	case 'object'   :
		if(obj === null){
			o = null;
		}else{
			if(obj instanceof Array){
				o = [];
				for(var i = 0, len = obj.length; i < len; i++){
					o.push(clone(obj[i]));
				}
			}else{
				o = {};
				for(var k in obj){
					o[k] = clone(obj[k]);
				}
			}
		}
		break;
	default:		
		o = obj;break;
	}
	return o;	
}