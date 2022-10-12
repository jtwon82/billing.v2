package com.adgather.util.old;

/** 값이 존재하지 않거나(null), 비어 있을 때 발생한다.
 */
public class EmptyValueException extends RuntimeException
{
	private static final long serialVersionUID = -4001154964403512217L;
	private final String _sName; // 비어있는 값의 이름

	public EmptyValueException() { this(null); }

	public EmptyValueException(String sName) {
		super(sName == null ? "value is empty." : String.format("\"%s\" named value is empty.", sName));
		_sName = sName;
	}

	public String getName() { return _sName; }

} // EmptyValueException
