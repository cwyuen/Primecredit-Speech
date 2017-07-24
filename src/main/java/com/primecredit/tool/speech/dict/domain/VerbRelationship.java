package com.primecredit.tool.speech.dict.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.springframework.stereotype.Component;

@RelationshipEntity(type = "verb")
public class VerbRelationship {
	
	@GraphId
	private Long id;
	
	@StartNode
	Word startWord;
	
	@EndNode
	Word endWord;
	
	private Collection<String> verbs = new ArrayList<String>();
	
	public VerbRelationship() {
		
	}
	
	public VerbRelationship(Word startWord, Word endWord) {
		this.startWord = startWord;
		this.endWord = endWord;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Collection<String> getVerbs() {
		return verbs;
	}

	public void setVerbs(Collection<String> verbs) {
		this.verbs = verbs;
	}

	
	public Word getStartWord() {
		return startWord;
	}

	public void setStartWord(Word startWord) {
		this.startWord = startWord;
	}

	public Word getEndWord() {
		return endWord;
	}

	public void setEndWord(Word endWord) {
		this.endWord = endWord;
	}

	public void addVerbRelationship(String name) {
		this.verbs.add(name);
	}
	
}
