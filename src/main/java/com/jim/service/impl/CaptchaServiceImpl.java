/*
 * Copyright © 2016 JIM liu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jim.service.impl;


import com.jim.captcha.color.RandomColorFactory;
import com.jim.captcha.encoder.EncoderHelper;
import com.jim.captcha.predefined.*;
import com.jim.captcha.service.Captcha;
import com.jim.captcha.service.ConfigurableCaptchaService;
import com.jim.service.CaptchaService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Jim on 2016/5/18.
 * This class is ...
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Autowired
    private ConfigurableCaptchaService configurableCaptchaService;

    @Autowired
    private RandomColorFactory randomColorFactory;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${captcha.destination}")
    private String path;
    private String file;
	private String word;


	public void setWord(String word) {
		this.word = word;
	}

	@Override
    public String getWord() {
        return this.word;
    }

	@Override
	public String getCaptchaUrl(HttpServletRequest req) {
		String scheme = req.getScheme();             // http
		String serverName = req.getServerName();     // hostname.com
		int serverPort = req.getServerPort();        // 80
		String contextPath = req.getContextPath();   // /mywebapp
		String servletPath = req.getServletPath();   // /servlet/MyServlet
		String pathInfo = req.getPathInfo();         // /a/b;c=123
		String queryString = req.getQueryString();          // d=789

		// Reconstruct original requesting URL
		StringBuffer url =  new StringBuffer();
		url.append(scheme).append("://").append(serverName);

		if ((serverPort != 80) && (serverPort != 443)) {
			url.append(":").append(serverPort);
		}

		url.append(contextPath).append(servletPath);

		if (pathInfo != null) {
			url.append(pathInfo);
		}
		if (queryString != null) {
			url.append("?").append(queryString);
		}
		return url.toString();
	}


	@Override
	public void createDestination(){
		try {
			File file = new File(this.path);
			if (!file.isDirectory() || !file.exists()){
				FileUtils.forceMkdir(file);
			}
		}catch (SecurityException ex){
			ex.fillInStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    @Override
    public boolean validate(String word) {
        return false;
    }

    @Override
    public void generateImage() {
		this.createDestination();

        randomColorFactory.setMin(new Color(10, 10, 15));
        randomColorFactory.setMax(new Color(255, 30, 10));
        configurableCaptchaService.setColorFactory(randomColorFactory);
        int counter = new Random().nextInt() % 5;

        switch (counter) {
            case 0:
                configurableCaptchaService.setFilterFactory(new CurvesRippleFilterFactory(configurableCaptchaService.getColorFactory()));
                break;
            case 1:
                configurableCaptchaService.setFilterFactory(new MarbleRippleFilterFactory());
                break;
            case 2:
                configurableCaptchaService.setFilterFactory(new DoubleRippleFilterFactory());
                break;
            case 3:
                configurableCaptchaService.setFilterFactory(new WobbleRippleFilterFactory());
                break;
            case 4:
                configurableCaptchaService.setFilterFactory(new DiffuseRippleFilterFactory());
                break;
			default:
				configurableCaptchaService.setFilterFactory(new CurvesRippleFilterFactory(configurableCaptchaService.getColorFactory()));
        }
        FileOutputStream fos = null;

        try {
            this.file = UUID.randomUUID().toString() + ".png";
            File tmp = new File(this.path + "/" + this.file);
            fos = new FileOutputStream(tmp);
            Captcha result = EncoderHelper.getChallangeAndWriteImage(configurableCaptchaService, "png", fos);
			this.setWord(result.getChallenge());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getFile() {
        return this.file;
    }
}
