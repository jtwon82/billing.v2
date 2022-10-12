package com.adgather.user.inclinations.cookieval.inter;

/**
 * 데이터 서비스 대기기능
 * @author yhlim
 *
 */
public abstract class Waitable {
	/** public valueable **/
	private long waitTime;
	
	/** public method **/
	public long getWaitTime() {
		return waitTime;
	}
	public void setWaiteTime(long waitTime) {
		this.waitTime = waitTime;
	}
	
	/** Waitable 객체 설정 **/
	public static boolean isWaitData(Object obj, long curTime) {
		if(obj == null)		return false;
		if(!(obj instanceof Waitable))	return false;
		
		long waitTime =  ((Waitable)obj).getWaitTime();
		return waitTime > 0 && waitTime >= curTime;
	}
	
	public static boolean setWaitData(Object obj, long waitTime) {
		if(obj == null)		return false;
		if(!(obj instanceof Waitable)) 	return false;
		
		((Waitable)obj).setWaiteTime(waitTime);
		return true;
	}
	
	public static boolean setWaitData(Object srcObj, Object destObj) {
		// destObj에 waitTime  적용(destObj or srcObj max값 적용)
		if(destObj == null)		return false;
		if(srcObj == null)			return false;
		if(!(destObj instanceof Waitable))	return false;
		if(!(srcObj instanceof Waitable))	return false;
		
		((Waitable)destObj).setWaiteTime(Long.max(((Waitable)destObj).getWaitTime(), ((Waitable)srcObj).getWaitTime()));
		return true;
	}
	
	public static void clearWaitData(Object obj, long curTime) {
		if(!(obj instanceof Waitable))		return;

		Waitable waitable = (Waitable)obj;
		
		if(waitable.getWaitTime() > 0 && waitable.getWaitTime() < curTime) {
			waitable.setWaiteTime(0);
		}
	}
}
