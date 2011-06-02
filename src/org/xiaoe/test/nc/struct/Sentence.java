package org.xiaoe.test.nc.struct;

public class Sentence {
	private String en;
	private String ch;
	
	public Sentence(String en, String ch) {
		this.en = en;
		this.ch = ch;
	}
	
	public String getEnglish() {
		return en;
	}
	
	public String getChinese() {
		return ch;
	}
}
