package com.adgather.user.inclinations.etc;

import java.util.HashMap;
import java.util.Map;

public class FreqCountResult {
	private boolean bCounted;
	private boolean bOver;
	private Map<String, Object> info = new HashMap<String, Object>();
	private int freqCnt;
	private int unlimitedFreqCnt;
	
	public boolean isCounted() {
		return bCounted;
	}

    public void setCounted(boolean bCounted) {
		this.bCounted = bCounted;
	}
	public boolean isOver() {
		return bOver;
	}
	public void setOver(boolean bOver) {
		this.bOver = bOver;
	}
	public Map<String, Object> getInfo() {
		return info;
	}
	public void putInfo(String key, Object value) {
		this.info.put(key, value);
	}
	public int getFreqCnt() {
		return freqCnt;
	}
	public void setFreqCnt(int freqCnt) {
		this.freqCnt = freqCnt;
	}
	
	
	public int getUnlimitedFreqCnt() {
        return unlimitedFreqCnt;
    }

    public void setUnlimitedFreqCnt(int unlimitedFreqCnt) {
        this.unlimitedFreqCnt = unlimitedFreqCnt;
    }
}
