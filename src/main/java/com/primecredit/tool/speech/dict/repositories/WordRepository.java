package com.primecredit.tool.speech.dict.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import com.primecredit.tool.speech.dict.domain.Word;

public interface WordRepository extends GraphRepository<Word> {

	public Word findByName(@Param("name") String name);

	
}
