package com.primecredit.tool.speech.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.primecredit.tool.speech.dict.services.DictImportService;

@Service
public class ApplicationService {

	private static final Logger logger = Logger.getLogger(ApplicationService.class);

	@Autowired
	private DictImportService dictoryImportService;

	public void run() {

		logger.debug("Your application started with option names : ");
		dictoryImportService.scan();
		
		//dictoryImportService.test();
	}

}
