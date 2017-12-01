package org.springframework.samples.mvc.convert;

import java.util.Collection;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
 * 日期：2017年11月30日
 * </br>                
 * *********************
 * </br>
 * 时间：上午11:19:13        
 * </br>        
 * *********************
 * </br>
 * {@code primitive(Integer)} Query String Parameter->Integer type
 * </br>
 * {@code date(Date)} Query String Parameter->Date type
 * </br>
 * {@code collection(Collection<Integer>)} Query String Parameter->Collection<Integer> type
 * </br>
 * {@code formattedCollection(Collection<Date>)} Query String Parameter-> Collection<Date> type
 * </br>
 * {@code bean(JavaBean)} 重点关注这个 Query String Parameter->JavaBean type
 * </br>
 * valueObject(SocialSecurityNumber) Query String Parameter->SocialSecurityNumber type
 * </br>
 * customerConverter(String) Query String Parameter->String
 */
@Controller
@RequestMapping("/convert")
public class ConvertController {

	@RequestMapping("primitive")//. url:/convert/primitive?value=3 /.关注 value=3
	public @ResponseBody String primitive(@RequestParam Integer value) { //. 使用@RequestParam时，受绑的形参需要和请求参数同名
		return "Converted primitive " + value; //. value=3
	}

	// requires Joda-Time on the classpath
	@RequestMapping("date/{value}") //. /url:convert/date/2010-07-04 /.关注 2010-07-04
	public @ResponseBody String date(@PathVariable @DateTimeFormat(iso=ISO.DATE) Date value) {
		return "Converted date " + value; //. value=Sun Jul 04 00:00:00 CST 2010
	}

	@RequestMapping("collection")//. url1:/convert/collection?values=1&values=2&values=3&values=4&values=5 /.关注'?'后面的
								 //. url2:/convert/collection?values=1,2,3,4,5 /.关注'?'后面的
	public @ResponseBody String collection(@RequestParam Collection<Integer> values) {//. 使用@RequestParam时，受绑形参需要和请求参数同名
		return "Converted collection " + values; //. values=[1, 2, 3, 4, 5]
	}

	@RequestMapping("formattedCollection")//. url:/convert/formattedCollection?values=2010-07-04,2011-07-04
	public @ResponseBody String formattedCollection(@RequestParam @DateTimeFormat(iso=ISO.DATE) Collection<Date> values) {
		return "Converted formatted collection " + values; //.values=[Sun Jul 04 00:00:00 CST 2010, Mon Jul 04 00:00:00 CST 2011]
	}
	/*
	 * 关注'?'后面的值： 
	 * url1: /convert/bean?primitive=3  (attribute is 'primitive')
	 * url2: /convert/bean?date=2010-07-04 (attribute is 'date')
	 * url3: /convert/bean?masked=(205) 333-3333  (attribute is 'masked')
	 * url4: /convert/bean?list[0]=1&list[1]=2&list[2]=3 (attribute is 'list' )
	 * url5: /convert/bean?formattedList[0]=2010-07-04&formattedList[1]=2011-07-04 (attribute is 'formattedList')
	 * url6: /convert/bean?map[0]=apple&map[1]=pear (attribute is 'map')
	 * url7: /convert/bean?nested.foo=bar&nested.list[0].foo=baz&nested.map[key].list[0].foo=bip (attribute is 'nested')
	 */
	@RequestMapping("bean")
	public @ResponseBody String bean(JavaBean bean) {
		return "Converted " + bean;
	}

	@RequestMapping("value")//. url:/convert/value?value=123456789
	public @ResponseBody String valueObject(@RequestParam SocialSecurityNumber value) {//. 将value值封装为SocialSecurityNumber
		return "Converted value object " + value;
	}

	@RequestMapping("custom")//. url:/convert/custom?value=123-45-6789
	public @ResponseBody String customConverter(@RequestParam @MaskFormat("###-##-####") String value) {//. 形参由请求参数来充当
		return "Converted '" + value + "' with a custom converter"; //. value=123456789
	}

}
