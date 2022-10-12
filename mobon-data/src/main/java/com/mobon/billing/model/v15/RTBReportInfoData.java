package com.mobon.billing.model.v15;

import java.io.Serializable;

import com.adgather.constants.old.GlobalConstants;
import com.mobon.billing.model.ClickViewData;

public class RTBReportInfoData extends ClickViewData implements Serializable {

	private	int	time	;	//	시간(타임)	time
	private	String	tagId	;	//	슬롯 아이디	tagId
	private	String	abType	;	//	AB 구분자	abType
	private	String	rtbType	;	//	RTB 타입 (A, B, C)	rtbType
	private	int	uniqueViewCnt	;	//	유니크한 노출수 (현재 미개발)	uniqueViewCnt
	private	int	winNotice	;	//	입찰 성공 횟수	winNotice
	private	int	bidTry	;	//	모비온에서 입찰 시도 횟수	bidTry
	private	int	bidRequest	;	//	카카오측에서 입찰 요청 횟수	bidRequest
	private	float	succPoint	;	//	카카오 입찰 성공 CPM (second_price_plus)	succPoint
	private	float	realPoint	;	//	카카오 지면에 실제노출할때의 CPM (second_price_plus)	realPoint
	private	float	cost	;	//	모비온 입찰(시도) 요청 CPM	cost
	private	float	succCost	;	//	모비온 입찰 성공 CPM - 입찰 요청했던 당시의 (rtb_media_scirpt 의 bidCost)	succCost
	private	float	realCost	;	//	모비온 입찰 성공 CPM - 입찰 요청했던 당시의 (rtb_media_scirpt 의 bidCost)	realCost
	private	float	clickPoint	;	//		clickPoint
	private	int	inDirectPrice	;	//	간접 과금금액	inDirectPrice
	private	int	convCnt	;	//	전환수	convCnt
	private	int	realTimePrice	;	//	실시간 과금금액	realTimePrice
	private	int	directPrice	;	//	직접 과금금액	directPrice
	private int viewCnt1=0; // 광고주 총노출
	
	
	public RTBReportInfoData() {
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getRtbType() {
		return rtbType;
	}

	public void setRtbType(String rtbType) {
		this.rtbType = rtbType;
	}

	public int getUniqueViewCnt() {
		return uniqueViewCnt;
	}

	public void setUniqueViewCnt(int uniqueViewCnt) {
		this.uniqueViewCnt = uniqueViewCnt;
	}

	public int getWinNotice() {
		return winNotice;
	}

	public void setWinNotice(int winNotice) {
		this.winNotice = winNotice;
	}

	public int getBidTry() {
		return bidTry;
	}

	public void setBidTry(int bidTry) {
		this.bidTry = bidTry;
	}

	public int getBidRequest() {
		return bidRequest;
	}

	public void setBidRequest(int bidRequest) {
		this.bidRequest = bidRequest;
	}

	public float getSuccPoint() {
		return succPoint;
	}

	public void setSuccPoint(float succPoint) {
		this.succPoint = succPoint;
	}

	public float getRealPoint() {
		return realPoint;
	}

	public void setRealPoint(float realPoint) {
		this.realPoint = realPoint;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getSuccCost() {
		return succCost;
	}

	public void setSuccCost(float succCost) {
		this.succCost = succCost;
	}

	public float getRealCost() {
		return realCost;
	}

	public void setRealCost(float realCost) {
		this.realCost = realCost;
	}

	public float getClickPoint() {
		return clickPoint;
	}

	public void setClickPoint(float clickPoint) {
		this.clickPoint = clickPoint;
	}

	public int getInDirectPrice() {
		return inDirectPrice;
	}

	public void setInDirectPrice(int inDirectPrice) {
		this.inDirectPrice = inDirectPrice;
	}

	public int getConvCnt() {
		return convCnt;
	}

	public void setConvCnt(int convCnt) {
		this.convCnt = convCnt;
	}

	public int getRealTimePrice() {
		return realTimePrice;
	}

	public void setRealTimePrice(int realTimePrice) {
		this.realTimePrice = realTimePrice;
	}

	public int getDirectPrice() {
		return directPrice;
	}

	public void setDirectPrice(int directPrice) {
		this.directPrice = directPrice;
	}

	@Override
	public String generateKey() {
		keyCode = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s"
				, this.getAdGubun(), this.getPlatform()
				, this.getAdvertiserId(), this.getScriptUserId(), this.getType(), this.getYyyymmdd(), this.getProduct(), this.getSiteCode(), this.getKno()
				, this.getScriptNo(), this.getInterlock());
		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getPlatform());
		return keyCode;
	}

	@Override
	public void sumGethering(Object _from) {
		RTBReportInfoData from = (RTBReportInfoData)_from;
		
		if ( GlobalConstants.VIEW.equals( from.getType() ) ) {
			if(from.getViewCnt()>0)		this.setViewCnt( this.getViewCnt() + from.getViewCnt() );
			if(from.getViewCnt2()>0)	this.setViewCnt2( this.getViewCnt2() + from.getViewCnt2() );
			if(from.getViewCnt3()>0)	this.setViewCnt3( this.getViewCnt3() + from.getViewCnt3() );
		}
		else if ( GlobalConstants.CLICK.equals( from.getType() ) ) {
			if( from.getClickCnt()>0 )	this.setClickCnt( this.getClickCnt() + from.getClickCnt() );
			else this.setClickCnt( this.getClickCnt() + 1 );
		}
		this.setPoint( this.getPoint() + from.getPoint() );
		this.setMpoint( this.getMpoint() + from.getMpoint() );
	
	}

	public String getAbType() {
		return abType;
	}

	public void setAbType(String abType) {
		this.abType = abType;
	}

	public int getViewCnt1() {
		return viewCnt1;
	}

	public void setViewCnt1(int viewCnt1) {
		this.viewCnt1 = viewCnt1;
	}

}
