package com.mobon.billing.model.v15;

import java.util.List;

public class KafkaGroupSummary {
	long totalLag;
	int percentageCovered;
	List<String> partitionOffsets;
	List<String> owners;
	
	public long getTotalLag() {
		return totalLag;
	}
	public void setTotalLag(long totalLag) {
		this.totalLag = totalLag;
	}
	public int getPercentageCovered() {
		return percentageCovered;
	}
	public void setPercentageCovered(int percentageCovered) {
		this.percentageCovered = percentageCovered;
	}
	public List<String> getPartitionOffsets() {
		return partitionOffsets;
	}
	public void setPartitionOffsets(List<String> partitionOffsets) {
		this.partitionOffsets = partitionOffsets;
	}
	public List<String> getOwners() {
		return owners;
	}
	public void setOwners(List<String> owners) {
		this.owners = owners;
	}
	
}
