package com.example.demo;

import com.springboot.security.SpringsecurityAuthApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		Class [] classes = new Class[2];
		classes[0]= SpringsecurityAuthApplication .class;
		classes[1]= DemoApplication.class;
		SpringApplication.run(classes,args);
	}
}
