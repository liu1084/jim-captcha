/*
 * Copyright © 2016 JIM liu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
