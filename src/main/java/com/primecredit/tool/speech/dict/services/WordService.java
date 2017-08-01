package com.primecredit.tool.speech.dict.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.primecredit.tool.speech.dict.domain.VerbRelationship;
import com.primecredit.tool.speech.dict.domain.Word;
import com.primecredit.tool.speech.dict.repositories.WordRepository;

@Service
public class WordService {

	private Logger logger = Logger.getLogger(WordService.class);
	
	@Autowired
	private WordRepository wordRepository;
	
	private void save(Word word){
		wordRepository.save(word);
	}
	
	public boolean isExistWord(String name){
		Word word = wordRepository.findByName(name);
		if(word == null){
			return false;
		}
		return true;
	}
	
	public void createWordNode(Word word){
		if(!isExistWord(word.getName())){
			save(word);
		}
	}
	
	public void createVerbRelationship(String input) {
		String[] verbs = input.split("");
		
		for(int index = 0; index < verbs.length; index++) {
			
			String start =verbs[index];
			Word startWord = wordRepository.findByName(start);
					
			if(index < verbs.length -1) {
				String end = verbs[index + 1];
				Word endWord = wordRepository.findByName(end);
				VerbRelationship verbRelationship = new VerbRelationship(startWord, endWord);
				startWord.addVerbRelationship(verbRelationship);
				save(startWord);
			}
		}
		
	}
	
	public Word getWord(String name) {
		return wordRepository.findByName(name);
	}
	
	
}
