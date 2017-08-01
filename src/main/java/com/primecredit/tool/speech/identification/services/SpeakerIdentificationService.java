package com.primecredit.tool.speech.identification.services;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
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
		printSpeakerIntervals(clusters, sourceFileName);
		return null;
	}
	
	private void printSpeakerIntervals(ArrayList<SpeakerCluster> speakers, String fileName) {
		int idx = 0;
		for (SpeakerCluster spk : speakers) {
			idx++;
			ArrayList<Segment> segments = spk.getSpeakerIntervals();
			for (Segment seg : segments)
				System.out.println(fileName + " " + " " + time(seg.getStartTime()) + " " + time(seg.getLength())
						+ " Speaker" + idx);
		}
	}
	
	private String time(int milliseconds) {
		return (milliseconds / 60000) + ":" + (Math.round((double) (milliseconds % 60000) / 1000));
	}
	
	
}
