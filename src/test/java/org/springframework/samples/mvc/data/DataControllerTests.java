package org.springframework.samples.mvc.data;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.samples.mvc.AbstractContextControllerTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
public class DataControllerTests extends AbstractContextControllerTests {

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		//���ɲ���
		this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
	}

	@Test
	public void param() throws Exception {
		this.mockMvc.perform(get("/data/param?foo=bar"))
				.andExpect(content().string("Obtained 'foo' query parameter value 'bar'"));
	}

	@Test
	public void group() throws Exception {//����ǰ̨����ֵ��ǰ��ֵ����װJavaBean
		this.mockMvc.perform(get("/data/group?param1=foo&param2=bar&param3=baz"))
				.andExpect(content().string(startsWith(//startsWith
						"Obtained parameter group org.springframework.samples.mvc.data.JavaBean@")));
	}

	@Test
	public void pathVar() throws Exception {
		this.mockMvc.perform(get("/data/path/foo"))//varǰ��ֵfoo��
				.andExpect(content().string("Obtained 'var' path variable value 'foo'"));
	}

	@Test
	public void matrixVar() throws Exception {//matrixvars;foo=bar[URI-Template-name;MatriName=MatriValue]
		this.mockMvc.perform(get("/data/matrixvars;foo=bar/simple")).andDo(print())
				.andExpect(content().string("Obtained matrix variable 'foo=bar' from path segment 'matrixvars'"));
	}

	@Test
	public void matrixVarMultiple() throws Exception {
		// matrixvars;foo=bar1[URI-Template-name;MatriName=MatriValue] //���������ʽ����� ����ֵ[ģ�ͼܹ�]
		// multiple;foo=bar2[URI-Template-name;MatriName=MatriValue] // ͬ�ϡ�
		this.mockMvc.perform(get("/data/matrixvars;foo=bar1/multiple;foo=bar2"))
				.andExpect(content().string("Obtained matrix variable foo=bar1 from path segment 'matrixvars' and variable 'foo=bar2 from path segment 'multiple'"));
	}

	@Test
	public void header() throws Exception {
		this.mockMvc.perform(get("/data/header").accept(MediaType.ALL))
				.andExpect(content().string("Obtained 'Accept' header '*/*'"));//Accept:text/plain, */*; q=0.01
	}

	@Test
	public void requestBody() throws Exception {
		this.mockMvc.perform(
				post("/data/body")
					.contentType(MediaType.TEXT_PLAIN)//�������ͣ� text/plain
					.content("foo".getBytes()))
				.andExpect(content().string("Posted request body 'foo'"));
	}
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void requestBodyAndHeaders() throws Exception {
		this.mockMvc.perform(
				post("/data/entity")
					.contentType(MediaType.TEXT_PLAIN)
					.content("foo".getBytes()))
				.andExpect(content().string(
						"Posted request body 'foo'; headers = {Content-Type=[text/plain], Content-Length=[3]}"));
	}

}
