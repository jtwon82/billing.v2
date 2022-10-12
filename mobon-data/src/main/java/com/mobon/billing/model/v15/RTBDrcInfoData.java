package com.mobon.billing.model.v15;

import java.io.Serializable;

import com.adgather.constants.old.GlobalConstants;
import com.mobon.billing.model.ClickViewData;

public class RTBDrcInfoData extends ClickViewData implements Serializable {

	private String tagId; // 슬롯아이디 tagId
	private String abType; // abType abType
	private String rtbType; // rtbType rtbType
	private String serviceHostId; // 서버 아이피 serviceHostId
	private long actDate; // 날짜 actDate
	private String mobileLinkCate; // 링크카테 mobileLinkCate
	private String gender; // 성별 gender
	private String age; // 나이 age
	private String scriptId; // 스크립트아이디 scriptId

	public RTBDrcInfoData() {
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getAbType() {
		return abType;
	}

	public void setAbType(String abType) {
		this.abType = abType;
	}

	public String getRtbType() {
		return rtbType;
	}

	public void setRtbType(String rtbType) {
		this.rtbType = rtbType;
	}

	public String getServiceHostId() {
		return serviceHostId;
	}

	public void setServiceHostId(String serviceHostId) {
		this.serviceHostId = serviceHostId;
	}

	public long getActDate() {
		return actDate;
	}

	public void setActDate(long actDate) {
		this.actDate = actDate;
	}

	public String getMobileLinkCate() {
		return mobileLinkCate;
	}

	public void setMobileLinkCate(String mobileLinkCate) {
		this.mobileLinkCate = mobileLinkCate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getScriptId() {
		return scriptId;
	}

	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
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
		RTBDrcInfoData from = (RTBDrcInfoData)_from;
		
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


}
