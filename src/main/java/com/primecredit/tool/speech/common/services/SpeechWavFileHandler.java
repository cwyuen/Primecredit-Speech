package com.primecredit.tool.speech.common.services;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service
public class SpeechWavFileHandler {
	
	@Autowired
	private SystemConfig systemConfig;

	private static final int MAX_PLAYTIME_PER_FILE = 40; // second


	public List<String> splitWavFile(String fileName) throws Exception {
		List<String> resultList = new ArrayList<String>();

		String workPath = systemConfig.getWorkingPath();
		
		File srcFile = new File(fileName);

		AudioInputStream srcClip = AudioSystem.getAudioInputStream(new File(fileName));
		AudioFormat audioFormat = srcClip.getFormat();
		long framesLenght = srcClip.getFrameLength();
		double time = framesLenght / audioFormat.getFrameRate();

		int numberofSplit = (int) (time / MAX_PLAYTIME_PER_FILE) + 1;

		if (numberofSplit == 1) {
			// File srcFile = new File(fileName);
			File distFile = new File(workPath + srcFile.getName());
			Files.copy(srcFile.toPath(), distFile.toPath());
			resultList.add(distFile.getAbsolutePath());
		} else {

			int count = 1;
			int startLen = 0;
			while (count <= numberofSplit) {
				long now = System.currentTimeMillis();
				String distFileName = workPath + count + "_" + now + "_" + srcFile.getName();
				copyWavAudioBySecond(fileName, distFileName, startLen, MAX_PLAYTIME_PER_FILE);
				resultList.add(distFileName);
				count++;
				startLen = startLen + MAX_PLAYTIME_PER_FILE;
			}

		}

		return resultList;
	}

	public void copyWavAudioBySecond(String sourceFileName, String destinationFileName, 
			double startSecond,
			double secondsToCopy) {
		AudioInputStream inputStream = null;
		AudioInputStream shortenedStream = null;
		try {
			File file = new File(sourceFileName);
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
			AudioFormat format = fileFormat.getFormat();
			inputStream = AudioSystem.getAudioInputStream(file);
			int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate();
			inputStream.skip((long) startSecond * bytesPerSecond);
			long framesOfAudioToCopy = (long) (secondsToCopy *  format.getFrameRate());
			shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
			File destinationFile = new File(destinationFileName);
			AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (shortenedStream != null)
				try {
					shortenedStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}


	public void convertULAW2PCM(String sourceFileName, String destinationFileName) {
		AudioInputStream inputStream = null;
		AudioInputStream shortenedStream = null;
		try {
			File pcmFile = new File(destinationFileName);
			File ulawFile = new File(sourceFileName);
			AudioInputStream ais = null;
			AudioFormat format = null;

			ais = AudioSystem.getAudioInputStream(ulawFile);
			format = ais.getFormat();

			if (AudioFormat.Encoding.ULAW.equals(format.getEncoding()) || 
					AudioFormat.Encoding.ALAW.equals(format.getEncoding())) {
				
				 AudioFormat newFormat = new AudioFormat(
						 AudioFormat.Encoding.PCM_SIGNED,
						 format.getSampleRate(),
						 format.getSampleSizeInBits() * 2,
						 format.getChannels(),
						 format.getFrameSize() * 2,
						 format.getFrameRate(),
						 true);

				//ais = AudioSystem.getAudioInputStream(format, ais);

				AudioInputStream audioInputStreamAIS = AudioSystem.getAudioInputStream(newFormat, ais);
				AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
				int nWrittenFrames = 0;
				nWrittenFrames = AudioSystem.write(audioInputStreamAIS, fileType, pcmFile);
			}else {
				long framesLenght = ais.getFrameLength();
				double time = framesLenght / format.getFrameRate();
				this.copyWavAudioBySecond(sourceFileName, destinationFileName, 0, time);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (shortenedStream != null)
				try {
					shortenedStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	public void deleteTempFiles() {
		String workPath = systemConfig.getWorkingPath();
		File folder = new File(workPath);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			file.delete();
		}

	}
	
	public List<String> listWavFiles(String dir) {

		List<String> results = new ArrayList<String>();

		File folder = new File(dir);

		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".wav");
			}

		};

		File[] listOfFiles = folder.listFiles(filter);
		for (File file : listOfFiles) {
			results.add(file.getAbsolutePath());
		}

		return results;

	}
	
	public void changePlaybackSpeedBySampleRate(String sourceFileName, String distFileName, float playBackSpeed) {
		File sourceFile1 = new File("/Users/maxwellyuen/Documents/samples/001_normal.wav");
		File sourceFile2 = new File("/Users/maxwellyuen/Documents/samples/001_slow.wav");
		File textFile = new File("/Users/maxwellyuen/Documents/samples/aaa.txt");
		
		File distFile = new File(distFileName);

		AudioInputStream ais1 = null;
		AudioInputStream ais2 = null;
		ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
		ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
		try {

			ais1 = AudioSystem.getAudioInputStream(sourceFile1);
			ais2 = AudioSystem.getAudioInputStream(sourceFile2);
			
			//(1) Audio Format
			AudioFormat sourceFormat1 = ais1.getFormat();
			AudioFormat sourceFormat2 = ais2.getFormat();
			System.out.println("getFrameRate: " + sourceFormat1.getFrameRate() + " VS " +sourceFormat2.getFrameRate());
			System.out.println("getSampleRate: " + sourceFormat1.getSampleRate()+ " VS " +sourceFormat2.getSampleRate());
			System.out.println("getSampleSizeInBits: " + sourceFormat1.getSampleSizeInBits() + " VS " +sourceFormat2.getSampleSizeInBits());
			System.out.println("getChannels: " + sourceFormat1.getChannels()+ " VS " +sourceFormat2.getChannels());
			System.out.println("getFrameSize: " + sourceFormat1.getFrameSize()+ " VS " +sourceFormat2.getFrameSize());
			System.out.println("getEncoding: " + sourceFormat1.getEncoding()+ " VS " +sourceFormat2.getEncoding());
		
			
			byte[] b = new byte[2 ^ 16];
			int read = 1;
			while (read > -1) {
				read = ais1.read(b);
				if (read > 0) {
					baos1.write(b, 0, read);
				}
			}
			byte[] b1 = baos1.toByteArray();
			
			b = new byte[2 ^ 16];
			read = 1;
			while (read > -1) {
				read = ais2.read(b);
				if (read > 0) {
					baos2.write(b, 0, read);
				}
			}
			
			byte[] b2= baos2.toByteArray();
			
			
			System.out.println("Data (byte[])");
			String str1 = Arrays.toString(b1);
			String str2 = Arrays.toString(b2);
			
			int len1 = b1.length;
			int len2 = b2.length;
			int minLen = len1;
			if(len1 > len2) {
				minLen = len2;
			}
			System.out.println("Data Length: " + len1 + " VS " + len2 + " (min " + minLen + ")" );
			
			int diffCount = 0;
			for(int i=0; i< minLen ; i++) {
				byte bTmp1 = b1[i];
				byte bTmp2 = b2[i];
				if(bTmp1 != bTmp2) {
					System.out.println("Data Diff at : " + i + " info: " + bTmp1 + " VS " + bTmp2);
					diffCount++;
				}else {
					System.out.println("Data Same at : " + i + " info: " + bTmp1 + " VS " + bTmp2);
				}
				if(diffCount > 100) {
					break;
				}
			}
			
			
			
			try (FileWriter fw = new FileWriter(textFile); BufferedWriter bw = new BufferedWriter(fw)) {
				
				bw.write(str1);
				bw.write("\n");
				bw.write(str2);
				bw.close();
				fw.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(ais1 != null) {
				try {
					ais1.close();
				} catch (IOException e) {}
			}
			if(ais2 != null) {
				try {
					ais2.close();
				} catch (IOException e) {}
			}
		
		}
	}
}
