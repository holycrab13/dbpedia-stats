package org.dbpedia.stats;

import java.io.File;
import java.io.IOException;

import java.text.ParseException;

import org.dbpedia.stats.analyzer.InstanceCountAnalyzer;
import org.dbpedia.stats.analyzer.PropertyCountAnalyzer;
import org.dbpedia.stats.analyzer.TripleCountAnalyzer;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		String collectionUri = args[1];
		String dataDir = "./data/";
		String statsFile = args[3];
		
		File dataDirFile = new File(dataDir);
		dataDirFile.mkdirs();
		
		StatsAnalyzerBase[] analyzers = new StatsAnalyzerBase[] {
			new TripleCountAnalyzer(),
			new PropertyCountAnalyzer(),
			new InstanceCountAnalyzer()
		};

		System.out.println("COLLECTION_URI: " + collectionUri);
		System.out.println("STATS_FILE: " + statsFile);
		
		Downloader downloader = new Downloader();
		StatsFileBaker baker = new StatsFileBaker(statsFile);	
		
		if(!baker.initialize()) {
			System.out.println("Exiting.");
			return;
		}
		
			System.out.println("Starting...");
			boolean running = true;
			
			while(running) {
				
				Thread.sleep(10000);
				
				System.out.println("Checking for collection data changes...");
				
				try {
					
					if(!downloader.hasNewCollectionData(collectionUri)) {
						continue;
					}
						
					System.out.println("Change detected! Clearing up old data...");
					
					downloader.clearDirectory(dataDir);
	
					System.out.println("Done. Downloading...");
					
					downloader.download(dataDir, collectionUri);
				
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("Done. Baking new stats...");
				
				try {
					baker.bakeStats(dataDir, analyzers);
				} catch(JsonSyntaxException | JsonIOException | IOException e) {
					e.printStackTrace();
				}		

				System.out.println("Done. Waiting for data changes...");
			}
			
		
		
	}

}
