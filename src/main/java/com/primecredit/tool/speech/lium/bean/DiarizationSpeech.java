package com.primecredit.tool.speech.lium.bean;

import java.util.ArrayList;
import java.util.List;

public class DiarizationSpeech implements Comparable<DiarizationSpeech>{
	
	private String show = null;
	private String name = null;
	private String segmentChannel = "1";
	private int segmentStart = 0;
	private int segmentLen = 0;
	private String segmentGender = null;
	private String segmentBand = null;
	private String segmentEnvironement = null;
	private String sourceFileName = null;
	private List<String> speechTextList = new ArrayList<String>();
	private String speechText = "";


	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSegmentChannel() {
		return segmentChannel;
	}

	public void setSegmentChannel(String segmentChannel) {
		this.segmentChannel = segmentChannel;
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

	public String getSegmentGender() {
		return segmentGender;
	}

	public void setSegmentGender(String segmentGender) {
		this.segmentGender = segmentGender;
	}

	public String getSegmentBand() {
		return segmentBand;
	}

	public void setSegmentBand(String segmentBand) {
		this.segmentBand = segmentBand;
	}

	public String getSegmentEnvironement() {
		return segmentEnvironement;
	}

	public void setSegmentEnvironement(String segmentEnvironement) {
		this.segmentEnvironement = segmentEnvironement;
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