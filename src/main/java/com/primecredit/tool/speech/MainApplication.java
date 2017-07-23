package com.primecredit.tool.speech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.primecredit.tool.speech.service.ApplicationService;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class MainApplication {

	public static void main(String[] args) throws Exception {

		ApplicationContext context = SpringApplication.run(MainApplication.class, args);

		ApplicationService service = context.getBean(ApplicationService.class);
		service.run();
	}
}
