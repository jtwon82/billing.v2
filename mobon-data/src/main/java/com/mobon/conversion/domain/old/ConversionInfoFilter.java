package com.mobon.conversion.domain.old;

public class ConversionInfoFilter extends ConversionInfo {
	/**
	 * 클릭 지나간 시간(분)
	 */
	private int pastClickMinute;
	private int convStatus;
	private int directHour;
	private int indirectHour;
	
	private boolean isChargeCodeChanged=false;

	public int getPastClickMinute() {
		return pastClickMinute;
	}

	public void setPastClickMinute(int pastClickMinute) {
		this.pastClickMinute = pastClickMinute;
		if (pastClickMinute < 120) {
			this.setConvStatus(ConversionCode.CONV_SESSION);
			this.setChargeCode(ConversionCode.VALID_SESSION_CODE);
		} else if (pastClickMinute < 1440) {
			this.setConvStatus(ConversionCode.CONV_DIRECT);
			this.setChargeCode(ConversionCode.VALID_DIRECT_CODE);
		} else if (pastClickMinute < 43200) {
			this.setConvStatus(ConversionCode.CONV_INDIRECT);
			this.setChargeCode(ConversionCode.VALID_INDIRECT_CODE);
		} else {
			this.setConvStatus(ConversionCode.CONV_INVALID);
		}
	}

	public void setConversionType(String inHour, String direct) {
		if ("24".equals(inHour) && "1".equals(direct)) {
			this.setConvStatus(ConversionCode.CONV_SESSION);		// 0
			this.setChargeCode(ConversionCode.VALID_SESSION_CODE);
		} else if ("24".equals(inHour) && "0".equals(direct)) {
			this.setConvStatus(ConversionCode.CONV_DIRECT);			// 1
			this.setChargeCode(ConversionCode.VALID_DIRECT_CODE);
		} else if ("0".equals(inHour) && "0".equals(direct)) {
			this.setConvStatus(ConversionCode.CONV_INDIRECT);		// 2
			this.setChargeCode(ConversionCode.VALID_INDIRECT_CODE);
		} else {
			this.setConvStatus(ConversionCode.CONV_INVALID);
		}
	}

	public int getConvStatus() {
		return convStatus;
	}

	public void setConvStatus(int convStatus) {
		this.convStatus = convStatus;
	}

	public int getDirectHour() {
		return directHour;
	}

	public void setDirectHour(int directHour) {
		this.directHour = directHour;
	}

	public int getIndirectHour() {
		return indirectHour;
	}

	public void setIndirectHour(int indirectHour) {
		this.indirectHour = indirectHour;
	}

	public boolean isChargeCodeChanged() {
		return isChargeCodeChanged;
	}

	public void setChargeCodeChanged(boolean isChargeCodeChanged) {
		this.isChargeCodeChanged = isChargeCodeChanged;
	}

}
