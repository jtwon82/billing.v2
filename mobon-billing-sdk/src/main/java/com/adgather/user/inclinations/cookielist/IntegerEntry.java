package com.adgather.user.inclinations.cookielist;

public class IntegerEntry {
	private int maxLength; 			// List의 최대 크키(Position을 확인하기 위한 용도와 초기값 설정하기 위함)
	private int position;			// 변경 및 수정할 위치의 position
	private int value;				// 변경 값
	
	public IntegerEntry() {}
	
	public IntegerEntry(int maxLength, int position, int value) {
		this.maxLength = maxLength;
		this.position = position;
		this.value = value;
	}
	
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
}
