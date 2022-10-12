package com.adgather.user.inclinations.etc;

public class FreqInfo {
	private String inctCookieName;
	private String freqCookieName;
	private int maxFreq;
	private String unlimitedFreqCookieName;
	private int maxUnlimitedFreq;

	public FreqInfo(String inctCookieName, String freqCookieName, int maxFreq) {
		this.inctCookieName = inctCookieName;
		this.freqCookieName = freqCookieName;
		this.maxFreq = maxFreq;
	}

	public FreqInfo(String inctCookieName, String freqCookieName, int maxFreq, String unlimitedFreqCookieName, int maxUnlimitedFreq) {
		this.inctCookieName = inctCookieName;
		this.freqCookieName = freqCookieName;
		this.maxFreq = maxFreq;
		this.unlimitedFreqCookieName = unlimitedFreqCookieName;
		this.maxUnlimitedFreq = maxUnlimitedFreq;
	}
	
	public String getInctCookieName() {
		return inctCookieName;
	}
	public void setInctCookieName(String inctCookieName) {
		this.inctCookieName = inctCookieName;
	}
	public String getFreqCookieName() {
		return freqCookieName;
	}
	public void setFreqCookieName(String freqCookieName) {
		this.freqCookieName = freqCookieName;
	}
	public int getMaxFreq() {
		return maxFreq;
	}
	public void setMaxFreq(int maxFreq) {
		this.maxFreq = maxFreq;
	}
	
	public String getUnlimitedFreqCookieName() {
		return unlimitedFreqCookieName;
	}

	public void setUnlimitedFreqCookieName(String unlimitedFreqCookieName) {
		this.unlimitedFreqCookieName = unlimitedFreqCookieName;
	}

	public int getMaxUnlimitedFreq() {
		return maxUnlimitedFreq;
	}

	public void setMaxUnlimitedFreq(int maxUnlimitedFreq) {
		this.maxUnlimitedFreq = maxUnlimitedFreq;
	}
}
