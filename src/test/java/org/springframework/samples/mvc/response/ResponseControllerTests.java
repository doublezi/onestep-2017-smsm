package org.springframework.samples.mvc.response;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
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
 * 日期：2017年11月28日
 * </br>                
 * *********************
 * </br>
 * 时间：上午11:10:41        
 * </br>        
 * *********************
 * </br>
 * 单元测试的目的：
 * </br>
 * 1. 请求地址是否准确映射控制器？
 * </br>
 * 2. 请求后响应的内容是否符合期望？
 * </br>
 */
public class ResponseControllerTests {

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		//单元测试
		this.mockMvc = standaloneSetup(new ResponseController()).build();
	}

	@Test
	public void responseBody() throws Exception {
		this.mockMvc.perform(get("/response/annotation")) //请求路径
				.andExpect(status().isOk()) //期望：状态码200
				.andExpect(content().string("The String ResponseBody"));//期望：响应流内含有balabala..
	}

	@Test
	public void responseCharsetAccept() throws Exception {
		this.mockMvc.perform(
				get("/response/charset/accept")//请求路径
					.accept(new MediaType("text", "plain", Charset.forName("UTF-8"))))//设置Accept
				.andExpect(status().isOk()) //期望：状态码200
				.andExpect(content().string(//期望：响应流内含有balabala..
						"\u3053\u3093\u306b\u3061\u306f\u4e16\u754c\uff01 (\"Hello world!\" in Japanese)"));
	}

	@Test
	public void responseCharsetProduce() throws Exception {
		this.mockMvc.perform(get("/response/charset/produce"))//请求路径
				.andExpect(status().isOk()) //期望：状态码200
				.andExpect(content().string(//期望：响应流内含有balabala..
						"\u3053\u3093\u306b\u3061\u306f\u4e16\u754c\uff01 (\"Hello world!\" in Japanese)"));
	}

	@Test
	public void responseEntityStatus() throws Exception {
		this.mockMvc.perform(get("/response/entity/status"))//请求路径
				.andExpect(status().isForbidden())//期望：状态码403
				.andExpect(content().string(//期望：响应流内含有balabala..
						"The String ResponseBody with custom status code (403 Forbidden)"));
	}
	
	@Test
	public void responseEntityHeaders() throws Exception {
		this.mockMvc.perform(get("/response/entity/headers"))//请求路径
				.andExpect(status().isOk())//期望：状态码200
				.andExpect(content().string(//期望：响应流内含有balabala..
						"The String ResponseBody with custom header Content-Type=text/plain"));
	}

}
