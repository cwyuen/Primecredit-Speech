package com.primecredit.tool.speech.dict.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.primecredit.tool.speech.dict.domain.Word;
import com.primecredit.tool.speech.dict.repositories.WordRepository;

@Service
public class WordService {

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
}
