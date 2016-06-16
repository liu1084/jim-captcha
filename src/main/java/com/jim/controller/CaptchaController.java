package com.jim.controller;

import com.jim.service.CaptchaService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Jim on 2016/5/18.
 * This class is ...
 */
@RestController
@RequestMapping(value = "/captcha", produces = "application/json")
public class CaptchaController {
	@Autowired
	CaptchaService captchaService;

	@Value("${captcha.directory}")
	String path;

	JSONObject jsonObject;

	@RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
	public String captcha(HttpServletRequest httpServletRequest) throws IOException {
		captchaService.generateImage();
		httpServletRequest.getSession().setAttribute("captchaChallenge", captchaService.getWord());
		jsonObject = new JSONObject();
		jsonObject.put("fileName", captchaService.getFile());
        jsonObject.put("text", captchaService.getWord());
		jsonObject.put("url", this.getCaptchaURL(httpServletRequest) + captchaService.getFile());
		return jsonObject.toString();
	}


	private String getCaptchaURL(HttpServletRequest request){
		String schema = request.getScheme();
		String host = request.getServerName();
		int port = request.getServerPort();
		String contextPath = request.getContextPath();
		return schema + "://" + host + ":" + String.valueOf(port) + contextPath + "/" + path;
	}

	@RequestMapping(value = "/challenge/{challenge}", method = RequestMethod.GET)
	public String validate(@PathVariable String challenge, HttpServletRequest httpServletRequest) {
		jsonObject = new JSONObject();
		jsonObject.put("response", httpServletRequest.getSession().getAttribute("captchaChallenge").toString().compareToIgnoreCase(challenge) == 0);
		return jsonObject.toString();
	}
}
