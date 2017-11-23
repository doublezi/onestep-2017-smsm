package org.springframework.samples.mvc.mapping;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.samples.mvc.AbstractContextControllerTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
/**
 * 
 * ***************************
 * 作者：隔壁老王(ಥ _ ಥ)        
 * ***************************
 * 日期：2017年11月23日              
 * ***************************
 * 时间：下午4:20:53              
 * ***************************
 * consume和produce的区别及作用参考：
 * 1. byConsumes() 和 byProduces*()
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class MappingControllerTests extends AbstractContextControllerTests {

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		
		//集成测试
		this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
	}

	@Test
	public void byPath() throws Exception {
		/*
		 * 【请求】 GET /mapping/path
		 * 【响应】 Mapped by path! 
		 * 【核心】content().string(String expectedContent)
		 */
		this.mockMvc.perform(get("/mapping/path")).andExpect(content().string("Mapped by path!"));
	}

	@Test
	public void byPathPattern() throws Exception {
		/*
		 * 【请求】 GET /mapping/path/wildcard
		 * 【响应】 "Mapped by path pattern ('/mapping/path/wildcard')"
		 * 【核心】 content().string(String expectedContent)
		 */
		this.mockMvc.perform(get("/mapping/path/wildcard"))
				.andExpect(content().string("Mapped by path pattern ('/mapping/path/wildcard')"));
	}

	@Test
	public void byMethod() throws Exception {
		/*
		 * 【请求】 GET /mapping/method  
		 * 【响应】 Mapped by path+method
		 * 【核心】content().string(String expectedContent)
		 */
		this.mockMvc.perform(get("/mapping/method"))
				.andExpect(content().string("Mapped by path + method"));
	}

	@Test
	public void byParameter() throws Exception {
		/*
		 * 【请求】 GET /mapping/parameter?foo=bar
		 * 【响应】 Mapped by path + method + presence of query parameter!
		 * 【核心】content().string(String expectedContent)
		 */
		this.mockMvc.perform(get("/mapping/parameter?foo=bar"))
				.andExpect(content().string("Mapped by path + method + presence of query parameter!"));
	}

	@Test
	public void byNotParameter() throws Exception {
		/*
		 * 【请求】 GET /mapping/parameter
		 * 【响应】"Mapped by path + method + not presence of query parameter!"
		 * 【核心】 content().string(String expectedContent)
		 */
		this.mockMvc.perform(get("/mapping/parameter"))
				.andExpect(content().string("Mapped by path + method + not presence of query parameter!"));
	}

	@Test
	public void byHeader() throws Exception {
		/*
		 * 【请求】 GET /mapping/header
		 * 【头    】  FooHeader:foo （在Request Headers中（按F12进入Network））
		 * 【响应】 "Mapped by path + method + presence of header!"
		 * 【核心】 header();content().string(String expectedContent)
		 */
		this.mockMvc.perform(get("/mapping/header").header("FooHeader", "foo"))
				.andExpect(content().string("Mapped by path + method + presence of header!"));
	}

	@Test
	public void byHeaderNegation() throws Exception {
		/*
		 * 【请求】 GET /mapping/header
		 * 【响应】 Mapped by path + method + absence of header!
		 * 【核心】content().string(String expectedContent)
		 */
		this.mockMvc.perform(get("/mapping/header"))
 				.andExpect(content().string("Mapped by path + method + absence of header!"));
	}
	
	// 【★】consumes以Content-type为标准缩小映射范围
	@Test
	public void byConsumes() throws Exception {
		/*
		 * 【请求】 POST /mapping/consumes
		 * 【内容类型】【★】 Content-Type:application/json（在Response Headers中（按F12进入Network））
		 * 【内容格式】 json格式
		 * 【响应（响应内容的前缀）】 Mapped by path + method + consumable media type (javaBean
		 * 【核心】
		 */
		this.mockMvc.perform(
				post("/mapping/consumes")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{ \"foo\": \"bar\", \"fruit\": \"apple\" }".getBytes()))
				.andExpect(content().string(startsWith("Mapped by path + method + consumable media type (javaBean")));
	}
	
	// 【★】produce以Accept为标准缩小映射范围
	@Test
	public void byProducesAcceptJson() throws Exception {
		/*
		 * 【请求】GET /mapping/produces
		 * 【接受】【★】Accept:application/json（在Request Headers中（按F12进入Network））
		 * 【响应】{"foo":"bar","fruit":"apple","love":"tall girl"}
		 * 【核心】accept(MediaType... mediaTypes);jsonPath(String expression, Object... args).value(Object expectedValue)
		 * 
		 */
		this.mockMvc.perform(get("/mapping/produces").accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.foo").value("bar"))
				.andExpect(jsonPath("$.fruit").value("apple"))
				.andExpect(jsonPath("$.love").value("tall girl"));
	}
	
	// 【★】produce以Accept为标准缩小映射范围
	@Test
	public void byProducesAcceptXml() throws Exception {
		/*
		 * 【请求】GET /mapping/produces
		 * 【接受】【★】Accept:application/xml（在Request Headers中（按F12进入Network））
		 * 【响应】<?xml version="1.0" encoding="UTF-8" standalone="yes"?><javaBean><foo>bar</foo><fruit>apple</fruit><love>tall girl</love></javaBean>
		 * 【核心】accept(MediaType... mediaTypes);xpath(String expression, Object... args).string(String expectedValue)
		 */
		this.mockMvc.perform(get("/mapping/produces").accept(MediaType.APPLICATION_XML))
				.andExpect(xpath("/javaBean/foo").string("bar"))
				.andExpect(xpath("/javaBean/fruit").string("apple"))
				.andExpect(xpath("/javaBean/love").string("tall girl"));
		
	}
	
	
	@Test
	public void byProducesJsonExtension() throws Exception {
		/*
		 * 【请求】/mapping/produces.json
		 * 【响应】{"foo":"bar","fruit":"apple","love":"tall girl"}
		 * 【核心】jsonPath(String expression, Object... args).value(Object expectedValue)
		 * 【正确】Written [JavaBean {foo=[bar], fruit=[apple],love=[tall girl]}] as "application/json" using [MappingJackson2HttpMessageConverter@75e01201]
		 */
		this.mockMvc.perform(get("/mapping/produces.json"))
				.andExpect(jsonPath("$.foo").value("bar"))
				.andExpect(jsonPath("$.fruit").value("apple"))
				.andExpect(jsonPath("$.love").value("tall girl"));
	}

	@Test
	public void byProducesXmlExtension() throws Exception {
		/*
		 * 【请求】/mapping/produces.xml
		 * 【响应】<?xml version="1.0" encoding="UTF-8" standalone="yes"?><javaBean><foo>bar</foo><fruit>apple</fruit><love>tall girl</love></javaBean>
		 * 【核心】xpath(String expression, Object... args).string(String expectedValue)
		 * 【正确】 Written [JavaBean {foo=[bar], fruit=[apple],love=[tall girl]}] as "application/xml" using [Jaxb2RootElementHttpMessageConverter@41925502]
		 */
		this.mockMvc.perform(get("/mapping/produces.xml"))
				.andExpect(xpath("/javaBean/foo").string("bar"))
				.andExpect(xpath("/javaBean/fruit").string("apple"))
				.andExpect(xpath("/javaBean/love").string("tall girl"));
	}

}
