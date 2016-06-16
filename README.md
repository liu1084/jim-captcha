# jim-captcha

- This project will generate captcha image in src/main/resources/static/app/dist/img, you can config the path in application.properties file.
- If you run it, http://YOUR-DOMAIN:PORT/captcha will generate challenge code & a new PNG file
- And you can validate it by call a API: http://YOUR-DOMAIN:PORT/captcha/challenge/code.
- demo png:

![](https://github.com/liu1084/jim-captcha/blob/master/3f386186-b85c-40e8-ae5b-87e36c075361.png)

![](https://github.com/liu1084/jim-captcha/blob/master/demo.png)



# install

    mvn clean install

# run

    jar -jar captcha-0.0.1-SNAPSHOT.jar
