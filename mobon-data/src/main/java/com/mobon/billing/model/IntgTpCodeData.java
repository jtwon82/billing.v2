package com.mobon.billing.model;

import java.io.Serializable;

public class IntgTpCodeData implements Serializable {

	private String intgTpCode;
	private String intgSeq;
	
	public IntgTpCodeData(String _intgTpCode, String _intgSeq) {
		setIntgTpCode(_intgTpCode);
		setIntgSeq(_intgSeq);
	}
	public IntgTpCodeData(String _intgTpCode, int _intgSeq) {
		setIntgTpCode(_intgTpCode);
		setIntgSeq(_intgSeq+"");
	}

	public String getIntgSeq() {
		return intgSeq;
	}

	public void setIntgSeq(String intgSeq) {
		this.intgSeq = intgSeq;
	}

	public String getIntgTpCode() {
		return intgTpCode;
	}

	public void setIntgTpCode(String intgTpCode) {
		this.intgTpCode = intgTpCode;
	}
}
