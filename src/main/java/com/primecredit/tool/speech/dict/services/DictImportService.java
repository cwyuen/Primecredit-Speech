package com.primecredit.tool.speech.dict.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.primecredit.tool.speech.common.services.SystemConfig;
import com.primecredit.tool.speech.dict.domain.Word;

@Service
public class DictImportService {

	private static final Logger logger = Logger.getLogger(DictImportService.class);

	private static final String TRADITIONAL_CHINESE_KEY = "［繁體字";
	private static final String SIMPLIFIED_CHINESE_KEY = "［简体字";

	@Autowired
	private SystemConfig systemConfig;
	
	@Autowired
	private PinYinService pinYinService;
	
	@Autowired
	private WordService wordService;

	public void scan() {
		List<String> singleWordFileList = listSingleWordFile();
		int count = 0;
		for (String fileName : singleWordFileList) {
			List<String> list = scanSingleWord(fileName);
			for(String str : list){
				Word word = pinYinService.cantonesePinYin(str);
				System.out.println(word);
				wordService.createWordNode(word);
			}
			count++;
			
			if(systemConfig.isDebugMode()){
				if(count > 5){
					break;
				}
			}
		}
	}

	private List<String> listSingleWordFile() {
		List<String> fileList = new ArrayList<String>();

		String path = systemConfig.getDictionarySingleWordPath();
		File srcPath = new File(path);
		if (srcPath.isDirectory()) {
			File[] srcFiles = srcPath.listFiles();
			for (File srcFile : srcFiles) {
				fileList.add(srcFile.getAbsolutePath());
			}
		}

		return fileList;
	}

	private List<String> scanSingleWord(String fileName) {
		
		List<String> results = new ArrayList<String>();
		File srcFile = new File(fileName);

		try (FileReader fr = new FileReader(srcFile); BufferedReader br = new BufferedReader(fr)) {

			logger.debug("scanSingleWord : " + srcFile.getName());

			String line;
			boolean isStart = false;
			String chineseType = "";
			while ((line = br.readLine()) != null) {

				// Read Line
				if (isStart) {
					
					if(line.trim() != ""){
						String tranChinese = extractTraditionalChineseWord(line, chineseType);
						if(tranChinese != null){
							results.add(tranChinese);
						}
					}
				}

				if (line.startsWith(SIMPLIFIED_CHINESE_KEY)) {
					isStart = true;
					chineseType = SIMPLIFIED_CHINESE_KEY;
				} else if (line.startsWith(TRADITIONAL_CHINESE_KEY)) {
					isStart = true;
					chineseType = TRADITIONAL_CHINESE_KEY;
				}
			}
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		
		return results;
	}
	
	private String extractTraditionalChineseWord(String sourceLine, String chineseType){
		sourceLine = sourceLine.replaceAll("\\s","");
		String[] sourceList =sourceLine.split("");
		if(sourceList.length > 2){
			if(TRADITIONAL_CHINESE_KEY.equalsIgnoreCase(chineseType)){
				return sourceList[0];
				
			}else if(SIMPLIFIED_CHINESE_KEY.equalsIgnoreCase(chineseType)){
				return sourceList[1];
			}
		}
		
		return null;
	}

}