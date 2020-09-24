package org.dbpedia.stats;


import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.RiotException;
import org.apache.jena.riot.system.ErrorHandlerFactory;
import org.dbpedia.stats.model.StatsEntryModel;
import org.dbpedia.stats.model.StatsModel;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class StatsFileBaker
{
	private String statsFilePath;

	
	public StatsFileBaker(String statsFile) {
		this.statsFilePath = statsFile;
	}

	public void bakeStats(String dataDir, StatsAnalyzerBase[] analyzers) 
			throws JsonSyntaxException, JsonIOException, IOException {
		
		StatsModel model = StatsModel.fromJson(new File(statsFilePath));
		StatsEntryModel entry = new StatsEntryModel();

		for(int i = 0; i < analyzers.length; i++) {
			analyzers[i].initialize(entry);
		}
		
		for(int i = 0; i < analyzers.length; i++) {
			analyzers[i].begin();
		}
		
		File dir = new File(dataDir);
		File[] directoryListing = dir.listFiles();

		for (File child : directoryListing) {
			createStatsForFile(child, analyzers);
		}
		
		for(int i = 0; i < analyzers.length; i++) {
			analyzers[i].finish();
		}
		
		entry.setTime(createNowString());
		model.setEntries(ArrayUtils.add(model.getEntries(), entry));

		writeStatsToFile(model);
	}

	private String createNowString() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		return dtf.format(now).replace(' ', 'T');
	}

	private void createStatsForFile(File file, StatsAnalyzerBase[] analyzers)  {
		System.out.println("=================================================================================");
		System.out.println("Baking stats for file: " + file.getName());
		System.out.println("=================================================================================");
		
		try (InputStream in = getBufferedReaderForCompressedFile(file)) {
			RDFParser.create()
			.source(in)
			.lang(RDFLanguages.TTL)
			.errorHandler(ErrorHandlerFactory.errorHandlerStrict)
			.parse(new AnalyzerStream(analyzers));
		} catch (CompressorException | RiotException | IOException e) {
			e.printStackTrace();
		}
	}

	private void writeStatsToFile(StatsModel model) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(statsFilePath));
		writer.write(model.toJson());
		writer.close();
	}
	
	public static InputStream getBufferedReaderForCompressedFile(File file) throws FileNotFoundException, CompressorException {
	    FileInputStream fin = new FileInputStream(file);
	    BufferedInputStream bis = new BufferedInputStream(fin);
	    return new CompressorStreamFactory().createCompressorInputStream(bis);
	}

	public boolean initialize() {
		File statsFile = new File(statsFilePath);
		StatsModel model = null;
		
		if(statsFile.exists()) {
			
			try {
				model = StatsModel.fromJson(new File(statsFilePath));
				return true;
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				System.out.println("Unable to parse existing stats file.");
				e.printStackTrace();
				System.out.println("Creating new stats file.");
			}
			
			if(!statsFile.delete()) {
				System.out.println("Unable to delete invalid stats file.");
				return false;
			}
		}
				
		File folder = statsFile.getParentFile();

		if(!folder.exists() && !folder.mkdirs()) {
			System.out.println("Unable to create stats directory.");
			return false;
		}
		
		try {
			if(!statsFile.createNewFile()) {
				System.out.println("Unable to create stats file.");
				return false;
			}
			
			model = new StatsModel();
			model.setEntries(new StatsEntryModel[0]);
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(statsFile));
			writer.write(model.toJson());
			writer.close();		
			return true;
			
		} catch (IOException e) {
			System.out.println("Unable to access stats file.");
			e.printStackTrace();
			return false;
		}
	}
}