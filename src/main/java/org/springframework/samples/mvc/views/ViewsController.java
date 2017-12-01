package org.springframework.samples.mvc.views;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * 
 * </br>
 * *********************
 * </br>
 * 作者：隔壁老王(ಥ _ ಥ) 21岁
 * </br>          
 * *********************
 * </br>
 * 日期：2017年11月28日
 * </br>                
 * *********************
 * </br>
 * 时间：下午3:06:13        
 * </br>        
 * *********************
 * </br>
 * 以下四种视图渲染方式可借鉴（源于官方，忠于官方）:</br>
 * 1. {@code prepare(Model):string}（常规）</br>
 * 2. {@code usingRequestToViewNameTranslator(Model):void}（基于DefaultRequestToViewNameTranslator）</br>
 * 3. {@code pathVars(String,String):String}（基于URI Template Pattern）</br>
 * 4. {@code dataBing(JavaBean,Model):String}（基于Validation的视图渲染）
 */
@Controller
@RequestMapping("/views/*")
public class ViewsController {
	
	/*
	 * 了解一下 Model model。
	 */
	@RequestMapping(value="html", method=RequestMethod.GET) //请求
	public String prepare(Model model) {
		model.addAttribute("foo", "bar");
		model.addAttribute("fruit", "apple");
		return "views/html"; 
	}
	/*
	 * 【URL】/views/viewName
	 * 【关注】DefaultRequestToViewNameTranslator隐式确定视图名。（查看源码）
	 * 【说明】DefaultRequestToViewNameTranslator根据/views/viewName寻找viewName.*文件 
	 */
	@RequestMapping(value="/viewName", method=RequestMethod.GET)
	public void usingRequestToViewNameTranslator(Model model) {
		model.addAttribute("foo", "bar");
		model.addAttribute("fruit", "apple");
	}
	/*
	 * URL=/views/pathVariables/bar/apple
	 * 
	 */
	@RequestMapping(value="pathVariables/{foo}/{fruit}", method=RequestMethod.GET)
	public String pathVars(@PathVariable String foo, @PathVariable String fruit) {
		// No need to add @PathVariables "foo" and "fruit" to the model
		// They will be merged in the model before rendering
		return "views/html";
	}
	/*
	 * URL=/views/dataBinding/bar/apple
	 */
	@RequestMapping(value="dataBinding/{foo}/{fruit}", method=RequestMethod.GET)
	public String dataBinding(@Valid JavaBean javaBean, Model model) {
		System.out.println(javaBean.toString());
		// JavaBean "foo" and "fruit" properties populated from URI variables 
		return "views/dataBinding";
	}

}
