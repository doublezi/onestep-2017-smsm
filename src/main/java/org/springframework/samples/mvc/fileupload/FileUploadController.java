package org.springframework.samples.mvc.fileupload;

import java.io.IOException;

import org.springframework.mvc.extensions.ajax.AjaxUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
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
 * 时间：上午10:02:48        
 * </br>        
 * *********************
 * </br>
 * 这个控制器和FormController有异曲同工之处
 * </br>
 */
@Controller
@RequestMapping("/fileupload")
public class FileUploadController {
	
	@ModelAttribute //第一方法（优先执行）
	public void ajaxAttribute(WebRequest request, Model model) {
		System.err.println("execute the method[[ajaxAttribute]].");
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
		
	}

	@RequestMapping(method=RequestMethod.GET)
	public void fileUploadForm() {
		System.err.println("execute the method[[fileUploadForm]].");
	}

	@RequestMapping(method=RequestMethod.POST)
	public void processUpload(@RequestParam MultipartFile file, Model model) throws IOException {
		System.err.println("file name is[["+file+"]]");
		model.addAttribute("message", "File '" + file.getOriginalFilename() + "' uploaded successfully");
	}
	
}
