package com.primecredit.tool.speech.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.primecredit.tool.speech.common.services.SpeechWavFileHandler;
import com.primecredit.tool.speech.common.services.SystemConfig;
import com.primecredit.tool.speech.dict.services.DictImportService;
import com.primecredit.tool.speech.identification.bean.DiarizationSpeech;
import com.primecredit.tool.speech.identification.services.SpeakerIdentificationService;
import com.primecredit.tool.speech.recognition.services.GoogleSpeechConvertService;

@Service
public class ApplicationService {

	private static final Logger logger = Logger.getLogger(ApplicationService.class);

	@Autowired
	private DictImportService dictoryImportService;

	@Autowired
	private SpeechWavFileHandler speechWavFileHandler;
	
	@Autowired
	private SpeakerIdentificationService speakerIdentificationService;
	
	@Autowired
	private SystemConfig systemConfig;
	
	@Autowired
	private GoogleSpeechConvertService googleSpeechConvertService;

	public void run() throws Exception {

		logger.debug("Your application started with option names : ");

		// (1) Remove all temp folder
		speechWavFileHandler.deleteTempFiles();

		// (2) List all Wav file
		List<String> wavFiles = speechWavFileHandler.listWavFiles(systemConfig.getWavSourcePath());
		
		Iterator<String> wavIter = wavFiles.iterator();
		while(wavIter.hasNext()) {
			String sourceFileName = wavIter.next();
			logger.info("Speaker Diarization - File Name: " + sourceFileName);
			File sourceFile = new File(sourceFileName);
			String shortFileName = sourceFile.getName();
			
			//(3) Speaker Diarization
			List<DiarizationSpeech> dsList = new ArrayList<DiarizationSpeech>();
			try {
				dsList = speakerIdentificationService.diarization(sourceFileName);
			} catch (Exception e) {
				logger.error(e);
				
			}
			
			//(4) Split Wav file by speaker
			if(dsList != null) {
				int count = 1;
				for(DiarizationSpeech ds: dsList) {
					int startLen = ds.getSegmentStart();
					int segmentLen = ds.getSegmentLen();
					String distFile = systemConfig.getWorkingPath() + String.valueOf(count) + "_" + shortFileName;
					ds.setSourceFileName(distFile);
					speechWavFileHandler.copyWavAudioBySecond(sourceFileName, distFile, (double) startLen, (double) segmentLen);
					count++;		
					
				}
			}
			
			//(5) Call Speech Convertor Service
			for(DiarizationSpeech ds: dsList) {
				if(systemConfig.isGoogleSpeechApiEnable()) {
					List<String> speechTextList = googleSpeechConvertService.convert(ds.getSourceFileName());
					ds.setSpeechTextList(speechTextList);					
				}
			}
			
			//(99) Export to file Result
			int index = sourceFileName.indexOf(".");
			String filefirstName = sourceFileName.substring(0, index);
			//String ext = sourceFileName.substring(index);
			File textFile = new File(filefirstName + ".txt");
			
			try (FileWriter fw = new FileWriter(textFile); BufferedWriter bw = new BufferedWriter(fw)) {
				
				for(int version = 1; version<=5; version++) {
					bw.write("Version (" + version + ")");
					bw.write("\n");
					for(DiarizationSpeech ds: dsList) {
						
						bw.write(ds.getName() + " : " + ds.getSpeechTextList().get(version-1));
						bw.write("\n");

					}
					
					bw.write("==============================================");
					bw.write("\n");
					bw.write("\n");
				}
				bw.close();
				fw.close();
			}
		}
		// dictoryImportService.scan();

		// dictoryImportService.test("因為呢你呢啲呢百裏面呢佢有五嗰啲錢呢係一啲現金回贈嚟㗎咁點鐘回贈呢啲冇得退番畀你㗎你只可以洗咗佢呀先咁不如呢你洗澡了59蚊無法先之後呢你先至好打理取消我塔羅");
	}

}
