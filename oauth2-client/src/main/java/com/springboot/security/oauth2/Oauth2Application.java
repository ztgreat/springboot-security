package com.springboot.security.oauth2;

import com.springboot.security.SpringsecurityAuthApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Oauth2Application {

	public static void main(String[] args) {
		//未完善
		Class [] classes = new Class[2];
		classes[0]= SpringsecurityAuthApplication.class;
		classes[1]= Oauth2Application.class;
		SpringApplication.run(classes, args);

	}
}
