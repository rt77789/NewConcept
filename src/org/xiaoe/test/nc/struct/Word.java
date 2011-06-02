package org.xiaoe.test.nc.struct;

public class Word {
	// # word.
	private String word;
	// # pronounce.
	private String pronounce;
	// # mean.
	private String mean;
	
	public Word(String word, String pronounce, String mean) {
		this.word = word;
		this.pronounce = pronounce;
		this.mean = mean;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getPronounce() {
		return pronounce;
	}

	public void setPronounce(String pronounce) {
		this.pronounce = pronounce;
	}

	public String getMean() {
		return mean;
	}

	public void setMean(String mean) {
		this.mean = mean;
	}
}
