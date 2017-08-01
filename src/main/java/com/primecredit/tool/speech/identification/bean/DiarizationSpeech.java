package com.primecredit.tool.speech.identification.bean;

import java.util.ArrayList;
import java.util.List;

public class DiarizationSpeech implements Comparable<DiarizationSpeech>{
	
	
	private String name = null;
	private int segmentStart = 0;
	private int segmentLen = 0;
	
	private String sourceFileName = null;
	private List<String> speechTextList = new ArrayList<String>();
	private String speechText = "";


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public int getSegmentStart() {
		return segmentStart;
	}

	public void setSegmentStart(int segmentStart) {
		this.segmentStart = segmentStart;
	}

	public int getSegmentLen() {
		return segmentLen;
	}

	public void setSegmentLen(int segmentLen) {
		this.segmentLen = segmentLen;
	}


	public String getSourceFileName() {
		return sourceFileName;
	}

	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}
	
	public String getSpeechText() {
		return speechText;
	}

	public void setSpeechText(String speechText) {
		this.speechText = speechText;
	}
	
	@Override
	public int compareTo(DiarizationSpeech object) {
		
		if (object == null) {
            throw new NullPointerException("Null parameter");
        } else if (!this.getClass().equals(object.getClass())) {
            throw new ClassCastException("Possible ClassLoader issue.");
        } else {
        		Long src = new Long(this.segmentStart);
        		Long dist = new Long(object.getSegmentStart());
            return src.compareTo(dist);
        }
	}

	public List<String> getSpeechTextList() {
		return speechTextList;
	}

	public void setSpeechTextList(List<String> speechTextList) {
		this.speechTextList = speechTextList;
	}

}