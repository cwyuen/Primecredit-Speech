package com.primecredit.tool.speech.common.services;

import java.io.File;

import org.springframework.stereotype.Service;

@Service
public class SystemConfig {
	
	private static String SOURCE_PATH = "";//Classpath
	private static String DICTIONARY_PATH = SOURCE_PATH + "dict";
	private static String DICTIONARY_SINGLE_WORD_PATH = DICTIONARY_PATH + File.separatorChar + "single";
	private static String DICTIONARY_MIX_WORD_PATH = DICTIONARY_PATH + File.separatorChar + "mix";
	private static final String WORKING_PATH = "/Users/maxwellyuen/Documents/samples/temp/";
	private static final String WAV_SOURCE_PATH = "/Users/maxwellyuen/Documents/samples/";
	private static boolean DEBUG = false;
	
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
	
	public String getWorkingPath() {
		return WORKING_PATH;
	}
	
	public String getWavSourcePath() {
		return WAV_SOURCE_PATH;
	}
}
