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

@RunWith(SpringJUnit4ClassRunner.class)
public class MappingControllerTests extends AbstractContextControllerTests {

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		
		//���ɲ���
		this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
	}

	@Test
	public void byPath() throws Exception {
		/*
		 * ������ GET /mapping/path
		 * ����Ӧ�� Mapped by path! 
		 * �����ġ�content().string(String expectedContent)
		 */
		this.mockMvc.perform(get("/mapping/path")).andExpect(content().string("Mapped by path!"));
	}

	@Test
	public void byPathPattern() throws Exception {
		/*
		 * ������ GET /mapping/path/wildcard
		 * ����Ӧ�� "Mapped by path pattern ('/mapping/path/wildcard')"
		 * �����ġ� content().string(String expectedContent)
		 */
		this.mockMvc.perform(get("/mapping/path/wildcard"))
				.andExpect(content().string("Mapped by path pattern ('/mapping/path/wildcard')"));
	}

	@Test
	public void byMethod() throws Exception {
		/*
		 * ������ GET /mapping/method  
		 * ����Ӧ�� Mapped by path+method
		 * �����ġ�content().string(String expectedContent)
		 */
		this.mockMvc.perform(get("/mapping/method"))
				.andExpect(content().string("Mapped by path + method"));
	}

	@Test
	public void byParameter() throws Exception {
		/*
		 * ������ GET /mapping/parameter?foo=bar
		 * ����Ӧ�� Mapped by path + method + presence of query parameter!
		 * �����ġ�content().string(String expectedContent)
		 */
		this.mockMvc.perform(get("/mapping/parameter?foo=bar"))
				.andExpect(content().string("Mapped by path + method + presence of query parameter!"));
	}

	@Test
	public void byNotParameter() throws Exception {
		/*
		 * ������ GET /mapping/parameter
		 * ����Ӧ��"Mapped by path + method + not presence of query parameter!"
		 * �����ġ� content().string(String expectedContent)
		 */
		this.mockMvc.perform(get("/mapping/parameter"))
				.andExpect(content().string("Mapped by path + method + not presence of query parameter!"));
	}

	@Test
	public void byHeader() throws Exception {
		/*
		 * ������ GET /mapping/header
		 * ��ͷ    ��  FooHeader:foo ����Request Headers�У���F12����Network����
		 * ����Ӧ�� "Mapped by path + method + presence of header!"
		 * �����ġ� header();content().string(String expectedContent)
		 */
		this.mockMvc.perform(get("/mapping/header").header("FooHeader", "foo"))
				.andExpect(content().string("Mapped by path + method + presence of header!"));
	}

	@Test
	public void byHeaderNegation() throws Exception {
		/*
		 * ������ GET /mapping/header
		 * ����Ӧ�� Mapped by path + method + absence of header!
		 * �����ġ�content().string(String expectedContent)
		 */
		this.mockMvc.perform(get("/mapping/header"))
 				.andExpect(content().string("Mapped by path + method + absence of header!"));
	}

	@Test
	public void byConsumes() throws Exception {
		/*
		 * ������ POST /mapping/consumes
		 * ���������͡� Content-Type:application/json����Response Headers�У���F12����Network����
		 * �����ݸ�ʽ�� json��ʽ
		 * ����Ӧ����Ӧ���ݵ�ǰ׺���� Mapped by path + method + consumable media type (javaBean
		 * �����ġ�
		 */
		this.mockMvc.perform(
				post("/mapping/consumes")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{ \"foo\": \"bar\", \"fruit\": \"apple\" }".getBytes()))
				.andExpect(content().string(startsWith("Mapped by path + method + consumable media type (javaBean")));
	}

	@Test
	public void byProducesAcceptJson() throws Exception {
		/*
		 * ������GET /mapping/produces
		 * �����ܡ�Accept:application/json����Request Headers�У���F12����Network����
		 * ����Ӧ��{"foo":"bar","fruit":"apple","love":"tall girl"}
		 * �����ġ�accept(MediaType... mediaTypes);jsonPath(String expression, Object... args).value(Object expectedValue)
		 * 
		 */
		this.mockMvc.perform(get("/mapping/produces").accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.foo").value("bar"))
				.andExpect(jsonPath("$.fruit").value("apple"))
				.andExpect(jsonPath("$.love").value("tall girl"));
	}

	@Test
	public void byProducesAcceptXml() throws Exception {
		/*
		 * ������GET /mapping/produces
		 * �����ܡ�Accept:application/xml����Request Headers�У���F12����Network����
		 * ����Ӧ��<?xml version="1.0" encoding="UTF-8" standalone="yes"?><javaBean><foo>bar</foo><fruit>apple</fruit><love>tall girl</love></javaBean>
		 * �����ġ�accept(MediaType... mediaTypes);xpath(String expression, Object... args).string(String expectedValue)
		 */
		this.mockMvc.perform(get("/mapping/produces").accept(MediaType.APPLICATION_XML))
				.andExpect(xpath("/javaBean/foo").string("bar"))
				.andExpect(xpath("/javaBean/fruit").string("apple"))
				.andExpect(xpath("/javaBean/love").string("tall girl"));
		
	}

	@Test
	public void byProducesJsonExtension() throws Exception {
		/*
		 * ������/mapping/produces.json
		 * ����Ӧ��{"foo":"bar","fruit":"apple","love":"tall girl"}
		 * �����ġ�jsonPath(String expression, Object... args).value(Object expectedValue)
		 * ����ȷ��Written [JavaBean {foo=[bar], fruit=[apple],love=[tall girl]}] as "application/json" using [MappingJackson2HttpMessageConverter@75e01201]
		 */
		this.mockMvc.perform(get("/mapping/produces.json"))
				.andExpect(jsonPath("$.foo").value("bar"))
				.andExpect(jsonPath("$.fruit").value("apple"))
				.andExpect(jsonPath("$.love").value("tall girl"));
	}

	@Test
	public void byProducesXmlExtension() throws Exception {
		/*
		 * ������/mapping/produces.xml
		 * ����Ӧ��<?xml version="1.0" encoding="UTF-8" standalone="yes"?><javaBean><foo>bar</foo><fruit>apple</fruit><love>tall girl</love></javaBean>
		 * �����ġ�xpath(String expression, Object... args).string(String expectedValue)
		 * ����ȷ�� Written [JavaBean {foo=[bar], fruit=[apple],love=[tall girl]}] as "application/xml" using [Jaxb2RootElementHttpMessageConverter@41925502]
		 */
		this.mockMvc.perform(get("/mapping/produces.xml"))
				.andExpect(xpath("/javaBean/foo").string("bar"))
				.andExpect(xpath("/javaBean/fruit").string("apple"))
				.andExpect(xpath("/javaBean/love").string("tall girl"));
	}

}
