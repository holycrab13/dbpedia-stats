package org.dbpedia.stats.model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class StatsModel {

	private StatsEntryModel[] entries;

	public StatsEntryModel[] getEntries() {
		return entries;
	}

	public void setEntries(StatsEntryModel[] entries) {
		this.entries = entries;
	}
	
	
	public static StatsModel fromJson(File file) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Gson gson = new Gson();
		return gson.fromJson(new FileReader(file), StatsModel.class);
	}
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
