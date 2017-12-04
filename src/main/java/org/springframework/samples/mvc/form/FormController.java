package org.springframework.samples.mvc.form;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.mvc.extensions.ajax.AjaxUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * 
 * </br>
 * *********************
 * </br>
 * 作者：隔壁老王(ಥ _ ಥ) 21岁
 * </br>          
 * *********************
 * </br>
 * 日期：2017年12月1日
 * </br>                
 * *********************
 * </br>
 * 时间：下午1:47:14        
 * </br>        
 * *********************
 * </br>
 * 重点关注{@code @ModelAttribute}
 * </br>
 * 表单使用了spring-Tag进行构建,最终还是以原生的HTML形式呈现在浏览器上
 * </br>
 * 为什么要使用'spring-Tag'？多半原因是因为它为开发者提供了另一种选择暨提高前端效率的开发
 * </br>
 */
@Controller
@RequestMapping("/form")
@SessionAttributes("formBean")
public class FormController {

	//所有的请求进来'FormController'后会先调用ajaxAttribute(WebRequest,Model)
	@ModelAttribute   
	public void ajaxAttribute(WebRequest request, Model model) {//判断请求类型：异步 ？ 同步？
		System.err.println("[MARK]'request' attribute is [["+request+"]]");
		System.err.println("[MARK]execute 'ajaxAttribute(WebRequet,Model)' method");
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
		System.err.println("[MARK]Is contains 'ajaxRequest ? '[["+model.containsAttribute("ajaxRequest")+"]]");
		Map<String, Object> map = model.asMap();
		for(Map.Entry<String, Object> entry :map.entrySet()) {
			System.out.println("Key[["+entry.getKey()+"]]   Value[["+entry.getValue()+"]]");
		}
		
	}

	// Invoked initially to create the "form" attribute
	// Once created the "form" attribute comes from the HTTP session (see @SessionAttributes)

	@ModelAttribute("formBean")
	public FormBean createFormBean() {//实例化一个FormBean对象
		System.err.println("[MARK]execute 'createFormBean()' method");
		return new FormBean();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public void form() {
		System.err.println("[MARK]execute 'form()' method");
	}

	
	@RequestMapping(method=RequestMethod.POST)
	public String processSubmit(@Valid FormBean formBean, BindingResult result,  //如果要使用BindingResult，必须紧随@Valid身后
								@ModelAttribute("ajaxRequest") boolean ajaxRequest, 
								Model model, RedirectAttributes redirectAttrs) { //提交表单后，执行此方法
		System.err.println("[MARK]execute 'processSubmit(FormBean,BindingResult,Boolean,Model,RedirectAttributes)' method");
		if (result.hasErrors()) {
			return null;
		}
		// Typically you would save to a db and clear the "form" attribute from the session 
		// via SessionStatus.setCompleted(). For the demo we leave it in the session.
		String message = "Form submitted successfully.  Bound " + formBean;
		// Success response handling
		if (ajaxRequest) {
			// 成功时
			model.addAttribute("message", message);
			Map<String, Object> map = model.asMap();
			for(Map.Entry<String, Object> entry :map.entrySet()) {
				System.out.println("Key[["+entry.getKey()+"]]   Value[["+entry.getValue()+"]]");
			}
			return null;
		} else {
			// store a success message for rendering on the next request after redirect
			// redirect back to the form to render the success message along with newly bound values
			//失败时
			redirectAttrs.addFlashAttribute("message", message);
			return "redirect:/form"; //重定向	
		}
	}
	
}
