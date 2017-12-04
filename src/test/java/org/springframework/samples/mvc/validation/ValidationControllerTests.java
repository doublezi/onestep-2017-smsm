package org.springframework.samples.mvc.validation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
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
 * 时间：下午1:18:52        
 * </br>        
 * *********************
 * </br>
 * 测试，就是让你做一个能预见未来的人
 */
public class ValidationControllerTests {

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		//单元测试 /. standaloneSetuo(一般都是面向单元测试
		this.mockMvc = standaloneSetup(new ValidationController()).alwaysExpect(status().isOk()).build();
	}

	@Test
	public void validateSuccess() throws Exception {
		this.mockMvc.perform(get("/validate?number=3&date=2029-07-04"))
				.andExpect(content().string("No errors"));
	}

	@Test
	public void validateErrors() throws Exception {
		this.mockMvc.perform(get("/validate?number=3&date=2010-07-01"))
				.andExpect(content().string("Object has validation errors"));
	}

}
