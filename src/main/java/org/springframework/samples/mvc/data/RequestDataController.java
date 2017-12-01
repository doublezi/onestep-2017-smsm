package org.springframework.samples.mvc.data;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 
 * </br>
 * *********************
 * </br>
 * 作者：隔壁老王(ಥ _ ಥ) 21岁
 * </br>          
 * *********************
 * </br>
 * 日期：2017年11月24日
 * </br>                
 * *********************
 * </br>
 * 时间：下午3:59:19        
 * </br>        
 * *********************
 * </br>
 * 【能学到什么？】URI Template;Matrix Variable;RequestMapping method的部分可接受的形参类型含义与作用(见方法形参)
 */
@Controller
@RequestMapping("/data")  //【假设】令root_url=localhost:8080/spring-mvc-showcase/date 
public class RequestDataController {

	@RequestMapping(value="param", method=RequestMethod.GET) //url:root_url/param
	public @ResponseBody String withParam(@RequestParam String foo) { //?foo="zzn_bar"(前台数据（首先，foo必须是个请求参数，其次才能传入后台）)
		return "Obtained 'foo' query parameter value '" + foo + "'";
	}

	@RequestMapping(value="group", method=RequestMethod.GET) //url:root_url/group
	public @ResponseBody String withParamGroup(JavaBean bean) {
		return "Obtained parameter group " + bean;
	}

	@RequestMapping(value="path/{var}", method=RequestMethod.GET)//url:root_url/path/
	public @ResponseBody String withPathVariable(@PathVariable String var) {
		return "Obtained 'var' path variable value '" + var + "'";
	}
	/**
	 * 标准注释：</br>
	 * {@code @PathVariable String path} 使得{@code path}成为路径变量，并能够支持URI Template </br>
	 * {@code @MatrixVariable String foo} 使得{@code foo}成为矩阵变量，并能够支持Matrix URIs </br>
	 * @param path 路径变量名
	 * @param foo  矩阵变量名
	 * @return {@code path}和{@code foo}的值与说明
	 */
	@RequestMapping(value="{path}/simple", method=RequestMethod.GET)
	public @ResponseBody String withMatrixVariable(@PathVariable String path, @MatrixVariable String foo) {
		return "Obtained matrix variable 'foo=" + foo + "' from path segment '" + path + "'";
	}
	/**
	 * 标准注释：
	 * {@code @PathVariable String path1} 使得{@code path1}成为路径变量，并能够支持URI Template </br>
	 * {@code @PathVariable String path2} 使得{@code path2}成为路径变量，并能够支持URI Template </br>
	 * </br>
	 * {@code @MatrixVariable(value="foo", pathVar="path1") String foo1} 使得{@code foo1}成为矩阵变量，矩阵变量名为foo，矩阵变量在{@code path1}中发挥作用</br>
	 * {@code @MatrixVariable(value="foo", pathVar="path2") String foo2} 使得{@code foo2}成为矩阵变量，矩阵变量名为foo，矩阵变量在{@code path2}中发挥作用 </br>
	 * 
	 * @param path1 路径变量名
	 * @param foo1 矩阵变量名
	 * @param path2 路径变量名
	 * @param foo2 矩阵变量名
	 * @return {@code foo1,foo2,path1,path2}的值和相关说明
	 */
	@RequestMapping(value="{path1}/{path2}", method=RequestMethod.GET)
	public @ResponseBody String withMatrixVariablesMultiple (
			@PathVariable String path1, @MatrixVariable(value="foo", pathVar="path1") String foo1,
			@PathVariable String path2, @MatrixVariable(value="foo", pathVar="path2") String foo2) {

		return "Obtained matrix variable foo=" + foo1 + " from path segment '" + path1
				+ "' and variable 'foo=" + foo2 + " from path segment '" + path2 + "'";
	}
	/**
	 * 普通字符串accept绑定了{@code @RequestHeader}后,就可以获取请求头的Accept属性值
	 * @param accept 获取accept属性值
	 * @return 
	 */
	@RequestMapping(value="header", method=RequestMethod.GET)
	public @ResponseBody String withHeader(@RequestHeader String accept) { //be bound to a web request header
		return "Obtained 'Accept' header '" + accept + "'";
	}
	/**
	 * 普通字符串openid_provider绑定了{@code @CookieValue}后，就可以获取。
	 * 特别需要注意的是，一个网站通常包括多个cookie，要获取特定cookie信息，一定要指定cookie的名（name value）
	 * 如： {@code @CookieValue(value="JSESSIONID")}String openid_provider。表示获取cookie id为"JSESSIONID"的值。
	 * 如果不特指，会报错。（400状态码）
	 * @param openid_provider
	 * @return
	 */
	@RequestMapping(value="cookie", method=RequestMethod.GET)
	public @ResponseBody String withCookie(@CookieValue(value="JSESSIONID") String openid_provider) {
		return "Obtained 'openid_provider' cookie '" + openid_provider + "'";
	}
	/**
	 * {@code @RequestBody String body}:接受异步数据
	 * @param body 
	 * @return
	 */
	@RequestMapping(value="body", method=RequestMethod.POST)
	public @ResponseBody String withBody(@RequestBody String body) {
		return "Posted request body '" + body + "'";
	}
	/**
	 * {@code HttpEntity<String> entity} 获取HTTP对象，进而获取Request Body和Request Headers
	 * @param entity HTTP对象
	 * @return Request Body/Headers的相关信息
	 */
	@RequestMapping(value="entity", method=RequestMethod.POST)
	public @ResponseBody String withEntity(HttpEntity<String> entity) {
		return "Posted request body '" + entity.getBody() + "'; headers = " + entity.getHeaders();
	}

}
