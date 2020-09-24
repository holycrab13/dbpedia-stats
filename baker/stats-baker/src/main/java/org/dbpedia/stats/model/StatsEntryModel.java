package org.dbpedia.stats.model;

public class StatsEntryModel {

	private long tripleCount;
	
	private long propertyCount;
	
	private long instanceCount;
	
	private String time;

	public long getTripleCount() {
		return tripleCount;
	}

	public void setTripleCount(long tripleCount) {
		this.tripleCount = tripleCount;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public long getPropertyCount() {
		return propertyCount;
	}

	public void setPropertyCount(long propertyCount) {
		this.propertyCount = propertyCount;
	}

	public long getInstanceCount() {
		return instanceCount;
	}

	public void setInstanceCount(long instanceCount) {
		this.instanceCount = instanceCount;
	}
}
