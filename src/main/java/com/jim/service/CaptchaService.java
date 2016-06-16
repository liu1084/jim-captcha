package com.jim.service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Jim on 2016/5/18.
 * This class is ...
 */
public interface CaptchaService extends BaseService {
	void generateImage() throws IOException;
	boolean validate(String word);
	String getFile();
	String getWord();
	String getCaptchaUrl(HttpServletRequest httpServletRequest);
}
