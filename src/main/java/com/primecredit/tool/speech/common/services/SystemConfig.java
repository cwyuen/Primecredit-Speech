package com.primecredit.tool.speech.common.services;

import java.io.File;

import org.springframework.stereotype.Service;

@Service
public class SystemConfig {

	private static String SOURCE_PATH = "D:/source";
	private static String DICTIONARY_PATH = SOURCE_PATH + File.separatorChar + "dictionary";
	private static String DICTIONARY_SINGLE_WORD_PATH = DICTIONARY_PATH + File.separatorChar + "single";
	private static String DICTIONARY_MIX_WORD_PATH = DICTIONARY_PATH + File.separatorChar + "mix";
	private static boolean DEBUG = true;
	
	public String getSourcePath(){
		return SOURCE_PATH;
	}
	
	public String getDictionarySingleWordPath(){
		return DICTIONARY_SINGLE_WORD_PATH;
	}
	
	public String getDictionaryMixWordPath(){
		return DICTIONARY_MIX_WORD_PATH;
	}
	
	public boolean isDebugMode(){
		return DEBUG;
	}
}
