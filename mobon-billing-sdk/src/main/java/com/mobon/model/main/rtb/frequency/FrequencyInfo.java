package com.mobon.model.main.rtb.frequency;

/**
 * Frequency SR/CW 통계 정보를 저장한다.
 * @author Administrator
 *
 */
public class FrequencyInfo  {
	public FrequencyInfo() {}
		
	private double frequencyCtr;
	private double frequencyCtrSum;
	private double maxFreq;
	private String userId;
	private String siteCode;
	private int frequencyViewCnt;
	
	public void setFrequencyCtr(double frequencyCtr) {
		this.frequencyCtr = frequencyCtr;
	} 
	public double getFrequencyCtr() {
		return frequencyCtr;
	}
	public void setFrequencyCtrSum(double frequencyCtrSum) {
		this.frequencyCtrSum = frequencyCtrSum;
	} 
	public double getFrequencyCtrSum() {
		return frequencyCtrSum;
	}
	public void setMaxFreq(int maxFreq) {
		this.maxFreq = maxFreq;
	} 
	public double getMaxFreq() {
		return maxFreq;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserId() {
		return userId;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setFrequencyViewCnt(int viewcnt) {
		this.frequencyViewCnt = viewcnt;
	}
	public int getFrequencyViewCnt() {
		return frequencyViewCnt;
	}
}
