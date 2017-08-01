package com.primecredit.tool.speech.lium.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.primecredit.tool.speech.common.services.SpeechWavFileHandler;
import com.primecredit.tool.speech.common.services.SystemConfig;
import com.primecredit.tool.speech.lium.bean.DiarizationSpeech;

@Service
public class LIUMSpeakerDiarizationHandler {
	
	@Autowired
	private SpeechWavFileHandler wavHandler;
	
	@Autowired
	private SystemConfig systemConfig;
	
	private final static Logger logger = Logger.getLogger(LIUMSpeakerDiarizationHandler.class.getName());

	

	public List<DiarizationSpeech> diarization(String sourceFileName) {
		String workPath = systemConfig.getWorkingPath();
		long now = System.currentTimeMillis();
		String destinationFileName = workPath + "temp_" + now + ".wav";
		String segmentFile = workPath + "temp_" + now + ".seg";

		wavHandler.convertULAW2PCM(sourceFileName, destinationFileName);

		String[] args = new String[] { 
				"--fInputMask=" + destinationFileName, 
				"--sOutputMask=" + segmentFile,
				"--doCEClustering", 
				"Diarization" };
		
		try {
			//DiarizationService.startDiarization(args);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		try {
			List<DiarizationSpeech> diarizations = parseDiarizationList(segmentFile);
			return diarizations;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private List<DiarizationSpeech> parseDiarizationList(String segmentFile) throws Exception {
		List<DiarizationSpeech> results = new ArrayList<DiarizationSpeech>();
		
		File file = new File(segmentFile);
		if(!file.exists()) {
			return results; 
		}
		
		
		List<String> fileContent = new ArrayList<String>();

		try (FileReader fr = new FileReader(segmentFile); BufferedReader br = new BufferedReader(fr)) {

			while (br.ready()) {
				fileContent.add(br.readLine());
			}
			br.close();
			fr.close();
		}
		
		//Convert to object
		for(String line: fileContent) {
			char[] linechar = line.toCharArray();
			if (linechar.length == 0) {
				continue; // empty line
			}
			if (linechar[0] == '\n') {
				continue; // empty line
			}
			if (linechar[0] == '#') {
				continue; // empty line
			}
			if ((linechar[0] == ';') && (linechar[1] == ';')) {
				continue; // rem line
			}
			DiarizationSpeech ds = new DiarizationSpeech();
			
			StringTokenizer stringTokenizer = new StringTokenizer(line, " ");
			int result = 0;
			while (stringTokenizer.hasMoreTokens()) {
				if (result == 0) {
					ds.setShow(stringTokenizer.nextToken());
					
				} else if (result == 1) {
					ds.setSegmentChannel(stringTokenizer.nextToken());
					
				} else if (result == 2) {
					ds.setSegmentStart(Integer.parseInt(stringTokenizer.nextToken()));
					
				} else if (result == 3) {
					ds.setSegmentLen(Integer.parseInt(stringTokenizer.nextToken()));
					
				} else if (result == 4) {
					ds.setSegmentGender(stringTokenizer.nextToken());
					
				} else if (result == 5) {
					ds.setSegmentBand(stringTokenizer.nextToken());
					
				} else if (result == 6) {
					ds.setSegmentEnvironement(stringTokenizer.nextToken());
					
				} else if (result == 7) {
					ds.setName(stringTokenizer.nextToken());
					break;
				}
				result++;
			}		
			if (result != 7) {
				throw new IOException("segmentation read error \n" + line + "\n ");
			}
			results.add(ds);
		}
		System.out.println(fileContent);
		Collections.sort(results);
		return results;
	}
}
