package org.springframework.samples.mvc.views;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.samples.mvc.AbstractContextControllerTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
 * 日期：2017年11月29日
 * </br>                
 * *********************
 * </br>
 * 时间：下午1:38:58        
 * </br>        
 * *********************
 * </br>
 * 在前端基建未搭建时，应该如何测试控制器？ （借鉴官方）
 * </br>
 * 1. 掌握Model对象的用法
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ViewsControllerTests extends AbstractContextControllerTests {

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		//集成测试（一般式）
		this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
	}

	@Test
	public void htmlView() throws Exception {
		this.mockMvc.perform(get("/views/html"))//请求：/views/html
				.andExpect(view().name(containsString("views/html")))//期望1：视图名称包含"views/html"
				.andExpect(model().attribute("foo", "bar"))//期望2： foo=bar 
				.andExpect(model().attribute("fruit", "apple"))//期望3： fruit=apple
				.andExpect(model().size(2));//期望4：模型属性有2个（一个是foo,另一个是fruit）
	}

	@Test
	public void viewName() throws Exception {
		this.mockMvc.perform(get("/views/viewName"))//请求：/views/viewName
				.andExpect(view().name(containsString("views/viewName")))//期望1：视图名称含有"views/viewName"
				.andExpect(model().attribute("foo", "bar"))//期望2： foo=bar
				.andExpect(model().attribute("fruit", "apple"))//期望3： fruit=apple
				.andExpect(model().size(2)); //期望4：模型属性有2个（一个是foo,另一个是fruit）
	}

	@Test
	public void uriTemplate() throws Exception {
		this.mockMvc.perform(get("/views/pathVariables/bar/apple"))//请求： /views/pathVariables/bar/apple
				.andExpect(view().name(containsString("views/html")));//期望1：视图名称含有"views/html"
	}
}
