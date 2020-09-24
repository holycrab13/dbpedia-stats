package org.dbpedia.stats.model;

public class StatsEntryModel {

	private long tripleCount;
	
	private long propertyCount;
	
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
}
