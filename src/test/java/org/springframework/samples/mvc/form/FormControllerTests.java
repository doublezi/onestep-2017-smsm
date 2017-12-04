package org.springframework.samples.mvc.form;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
/**
 * 
 * </br>
 * *********************
 * </br>
 * 作者：隔壁老王(ಥ _ ಥ) 21岁
 * </br>          
 * *********************
 * </br>
 * 日期：2017年12月4日
 * </br>                
 * *********************
 * </br>
 * 时间：上午9:32:39        
 * </br>        
 * *********************
 * </br>
 * 如何测试表单？
 * </br>
 * 借鉴这里头的{@code submitSuccess()} or {@code submitSuccessAjax()}
 */
public class FormControllerTests {

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		
		//视图解析器
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/");
		viewResolver.setSuffix(".jsp");
		
		//单元测试
		this.mockMvc = standaloneSetup(new FormController()).setViewResolvers(viewResolver).build();
	}

	@Test
	public void submitSuccess() throws Exception {
		String timezone = getTimezone(1941, 12, 16); 
		this.mockMvc.perform(
				post("/form") //提交表单都是使用POST(.param()为具体的提交内容)
					.param("name", "Joe")//.-<form:input path="name"
					.param("age", "56") //.-<form:input path="age"
					.param("birthDate", "1941-12-16")//.-<form:input path="birthDate"
					.param("phone", "(347) 888-1234")//.-<form:input path="phone"
					.param("currency", "$123.33")//.-<form:input path="currency"
					.param("percent", "89%")//.-<form:input path="percent"
					.param("inquiry", "comment")//.-<form:input path="inquiry"
					.param("inquiryDetails", "what is?")//.-<form:input path="inquiryDetails"
					.param("additionalInfo[mvc]", "true")//.-<form:checkbox path="additionalInfo[mvc]"
					.param("_additionalInfo[mvc]", "on")//.-<form:checkbox path="_additionalInfo[mvc]"
					.param("additionalInfo[java]", "true")//.<form:checkbox path="additionalInfo[java]"
					.param("_additionalInfo[java]", "on")//.<form:checkbox path="_additionalInfo[java]"
					.param("subscribeNewsletter", "false"))//.<form:radiobutton path="subscribeNewsletter"
				.andDo(print())
				.andExpect(status().isMovedTemporarily())//.状态码302
				.andExpect(redirectedUrl("/form")) //. redirectedUrl重定向URL
				.andExpect(flash().attribute("message",
						"Form submitted successfully.  Bound properties name='Joe', age=56, " +
						"birthDate=Tue Dec 16 00:00:00 " + timezone + " 1941, phone='(347) 888-1234', " +
						"currency=123.33, percent=0.89, inquiry=comment, inquiryDetails='what is?'," +
						" subscribeNewsletter=false, additionalInfo={java=true, mvc=true}"));
	}

	@Test
	public void submitSuccessAjax() throws Exception {
		String timezone = getTimezone(1941, 12, 16); 
		this.mockMvc.perform(
				post("/form")
					.header("X-Requested-With", "XMLHttpRequest") //当X-Requested-With="XMLHttpRequest"时，是ajax
					.param("name", "Joe")
					.param("age", "56")
					.param("birthDate", "1941-12-16")
					.param("phone", "(347) 888-1234")
					.param("currency", "$123.33")
					.param("percent", "89%")
					.param("inquiry", "comment")
					.param("inquiryDetails", "what is?")
					.param("additionalInfo[mvc]", "true")
					.param("_additionalInfo[mvc]", "on")
					.param("additionalInfo[java]", "true")
					.param("_additionalInfo[java]", "on")
					.param("subscribeNewsletter", "false"))
				.andExpect(status().isOk())//. 状态码200
				.andExpect(view().name("form"))//. 
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute("message",
						"Form submitted successfully.  Bound properties name='Joe', age=56, " +
						"birthDate=Tue Dec 16 00:00:00 " + timezone + " 1941, phone='(347) 888-1234', " +
						"currency=123.33, percent=0.89, inquiry=comment, inquiryDetails='what is?'," +
						" subscribeNewsletter=false, additionalInfo={java=true, mvc=true}"));
	}

	@Test
	public void submitError() throws Exception {
		this.mockMvc.perform(
				post("/form"))
				.andExpect(status().isOk())
				.andExpect(view().name("form"))
				.andExpect(model().errorCount(2))
				.andExpect(model().attributeHasFieldErrors("formBean", "name", "age"));
	}
	
	private String getTimezone(int year, int month, int day)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		Date date = calendar.getTime();
		TimeZone timezone = TimeZone.getDefault();
		boolean inDaylight = timezone.inDaylightTime(date);
		return timezone.getDisplayName(inDaylight, TimeZone.SHORT);
	}

}
