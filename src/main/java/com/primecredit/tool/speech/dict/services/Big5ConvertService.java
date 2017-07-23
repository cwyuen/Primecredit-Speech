package com.primecredit.tool.speech.dict.services;

import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Service;

@Service
public class Big5ConvertService {

	public String convertToUrlBig5(String utf8){
		byte[] big5Str = new byte[0];
		
		try {
			big5Str = utf8.getBytes("big5");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuilder sb = new StringBuilder();
		for(byte b : big5Str){		
			sb.append("%");
			sb.append(String.format("%02X", b));
		}
		
		return sb.toString();
	}
}

