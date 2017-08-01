package com.primecredit.tool.speech.identification.services;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.primecredit.tool.speech.identification.bean.DiarizationSpeech;

import edu.cmu.sphinx.speakerid.Segment;
import edu.cmu.sphinx.speakerid.SpeakerCluster;
import edu.cmu.sphinx.speakerid.SpeakerIdentification;

@Service
public class SpeakerIdentificationService {
	
	public List<DiarizationSpeech> diarization(String sourceFileName) throws Exception {
		SpeakerIdentification sd = new SpeakerIdentification();
		URL url = new File(sourceFileName).toURI().toURL();
		ArrayList<SpeakerCluster> clusters = sd.cluster(url.openStream());
		List<DiarizationSpeech> dsList = calculateSpeakerIntervals(clusters, sourceFileName);
		return dsList;
	}
	
	private List<DiarizationSpeech> calculateSpeakerIntervals(ArrayList<SpeakerCluster> speakers, String fileName) {
		
		List<DiarizationSpeech> dsList = new ArrayList<DiarizationSpeech>();
		
		int idx = 0;
		for (SpeakerCluster spk : speakers) {
			idx++;
			ArrayList<Segment> segments = spk.getSpeakerIntervals();
			for (Segment seg : segments) {
				
				DiarizationSpeech ds = new DiarizationSpeech();
				ds.setName("Speaker" + idx);
				ds.setSegmentStart(toSecond(seg.getStartTime()));
				ds.setSegmentLen(toSecond(seg.getLength()));
				
				dsList.add(ds);
				System.out.println(fileName + " " + " " + toSecond(seg.getStartTime()) + " " + toSecond(seg.getLength())
						+ " Speaker" + idx);
			}
		}
		
		Collections.sort(dsList);
		
		return dsList;
	}
	
	private int toSecond(int milliseconds) {
		return (int) (Math.round((double) (milliseconds) / 1000));
	}
	
	
}
