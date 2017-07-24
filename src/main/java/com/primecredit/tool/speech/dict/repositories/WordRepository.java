package com.primecredit.tool.speech.dict.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import com.primecredit.tool.speech.dict.domain.Word;

public interface WordRepository extends GraphRepository<Word> {

	public Word findByName(@Param("name") String name);

	@Query("MERGE (n1:Word {name:{n1Name}})-[:verb]-(n2:word {name:{n2Name}})")
	public void createVerbRelationship(@Param("n1Name") String word1, @Param("n2Name") String word2);
}
