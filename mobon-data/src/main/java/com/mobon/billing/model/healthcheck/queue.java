package com.mobon.billing.model.healthcheck;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class queue implements Serializable{
	int externalQueueSize=0;
	int actionQueueSize=0;
	int shopInfoQueueSize=0;
	int shopStatsQueueSize=0;
	int nearQueueSize=0;
	int clickViewPointQueueSize=0;
	int convQueueSize=0;
	int clientEnvirmentQueueSize=0;
	int clickViewQueueSize=0;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	public int getExternalQueueSize() {
		return externalQueueSize;
	}
	public void setExternalQueueSize(int externalQueueSize) {
		this.externalQueueSize = externalQueueSize;
	}
	public int getActionQueueSize() {
		return actionQueueSize;
	}
	public void setActionQueueSize(int actionQueueSize) {
		this.actionQueueSize = actionQueueSize;
	}
	public int getShopInfoQueueSize() {
		return shopInfoQueueSize;
	}
	public void setShopInfoQueueSize(int shopInfoQueueSize) {
		this.shopInfoQueueSize = shopInfoQueueSize;
	}
	public int getShopStatsQueueSize() {
		return shopStatsQueueSize;
	}
	public void setShopStatsQueueSize(int shopStatsQueueSize) {
		this.shopStatsQueueSize = shopStatsQueueSize;
	}
	public int getNearQueueSize() {
		return nearQueueSize;
	}
	public void setNearQueueSize(int nearQueueSize) {
		this.nearQueueSize = nearQueueSize;
	}
	public int getClickViewPointQueueSize() {
		return clickViewPointQueueSize;
	}
	public void setClickViewPointQueueSize(int clickViewPointQueueSize) {
		this.clickViewPointQueueSize = clickViewPointQueueSize;
	}
	public int getConvQueueSize() {
		return convQueueSize;
	}
	public void setConvQueueSize(int convQueueSize) {
		this.convQueueSize = convQueueSize;
	}
	public int getClientEnvirmentQueueSize() {
		return clientEnvirmentQueueSize;
	}
	public void setClientEnvirmentQueueSize(int clientEnvirmentQueueSize) {
		this.clientEnvirmentQueueSize = clientEnvirmentQueueSize;
	}
	public int getClickViewQueueSize() {
		return clickViewQueueSize;
	}
	public void setClickViewQueueSize(int clickViewQueueSize) {
		this.clickViewQueueSize = clickViewQueueSize;
	}
}
