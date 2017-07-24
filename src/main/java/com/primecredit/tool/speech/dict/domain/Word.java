package com.primecredit.tool.speech.dict.domain;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;


@NodeEntity
public class Word {
	
	private Long id;
	private String name;
	private String initials;
	private String vowel;
	private String tone;
	
	@Relationship(type = "verb", direction = Relationship.OUTGOING)
	private List<VerbRelationship> verbs = new ArrayList<VerbRelationship>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInitials() {
		return initials;
	}
	public void setInitials(String initials) {
		this.initials = initials;
	}
	public String getVowel() {
		return vowel;
	}
	public void setVowel(String vowel) {
		this.vowel = vowel;
	}
	public String getTone() {
		return tone;
	}
	public void setTone(String tone) {
		this.tone = tone;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.getName());
		sb.append("(");
		sb.append(this.getInitials());
		sb.append(this.getVowel());
		sb.append(this.getTone());
		sb.append(")");
		return sb.toString();
	}
	public List<VerbRelationship> getVerbs() {
		return verbs;
	}
	public void setVerbs(List<VerbRelationship> verbs) {
		this.verbs = verbs;
	}
	
	public void addVerbRelationship(VerbRelationship verb) {
		this.verbs.add(verb);
	}
}
