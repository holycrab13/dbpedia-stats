package org.dbpedia.stats;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Downloader
{
	private String latestCollectionHash;
	
	private static String collectionHashAPI = "https://databus.dbpedia.org/system/api/collection-hash?uri=";
	
	public Downloader() {
		latestCollectionHash = "";		
	}
	
	/**
	 * Quick and dirty implementation of a download client, downloading the contents of a 
	 * Databus collection. Code is ugly and I know it! 
	 * This download process will be replaced by the official databus download client as soon as it 
	 * takes less time to install than writing this code :)
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public void download(String targetPath, String collection) throws IOException, ParseException
	{

		if(!targetPath.endsWith("/")) {
			targetPath += "/";
		}

		File directory = new File(targetPath);

		if(!directory.exists() && !directory.mkdir()) {
			System.out.println("Target path " + targetPath + " could not be created.");
			return;
		}

		System.out.println("Loading collection " + collection);


		String query = get("GET", collection, "text/sparql");

		if(!isURLEncoded(query)) {
			query = URLEncoder.encode(query, "UTF-8");
		}

		String queryResult = query("https://databus.dbpedia.org/repo/sparql", query);

		ArrayList<String> files = new ArrayList<String>();

		JSONObject obj = new JSONObject(queryResult);	

		JSONArray bindings = obj.getJSONObject("results").getJSONArray("bindings");

		for (int i = 0; i < bindings.length(); i++)
		{
			JSONObject binding = bindings.getJSONObject(i);
			String key = binding.keys().next();

			JSONObject result = binding.getJSONObject(key);
			files.add(result.getString("value"));
		}

		for(String file : files) {

			System.out.println("Downloading file: " + file);

			String filename = file.substring(file.lastIndexOf('/') + 1);
			String prefix = filename.substring(0,filename.indexOf('.'));
			String suffixes = filename.substring(filename.indexOf('.'));
			String uniqname = prefix + suffixes;

			try {
				InputStream in = new URL(file).openStream();
				Files.copy(in, Paths.get(targetPath + uniqname), StandardCopyOption.REPLACE_EXISTING);
				in.close();
	
				System.out.println("File saved to " + targetPath + uniqname);
				
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Done.");
	}

	private static String query(String endpoint, String query) throws ParseException, IOException {

		HttpClient client = HttpClientBuilder.create().build();

		String body = "default-graph-uri=&format=application%2Fsparql-results%2Bjson&query=" + query;

		HttpEntity entity = new ByteArrayEntity(body.getBytes("UTF-8"));

		HttpPost request = new HttpPost(endpoint);
		request.setEntity(entity);
		request.setHeader("Content-type", "application/x-www-form-urlencoded");
		HttpResponse response = client.execute(request);
		HttpEntity responseEntity = response.getEntity();

		if(responseEntity != null) {
			return EntityUtils.toString(responseEntity);
		}
		return null;

	}

	private boolean isURLEncoded(String query) {

		Pattern hasWhites = Pattern.compile("\\s+");
		Matcher matcher = hasWhites.matcher(query);

		return !matcher.find();

	}

	private String get(String method, String urlString, String accept) throws IOException {


		System.out.println(method + ": " + urlString + " / ACCEPT: " + accept);

		HttpClient client = HttpClientBuilder.create().build();

		if(method.equals("GET")) {

			HttpGet request = new HttpGet(urlString);
			
			if(accept != null) {
				request.addHeader("Accept",  accept);
			}
			HttpResponse response = client.execute(request);
			HttpEntity responseEntity = response.getEntity();

			if(responseEntity != null) {
				return EntityUtils.toString(responseEntity);
			}
		}

		return null;
	}

	public boolean hasNewCollectionData(String collectionUri) throws IOException {
		String url = collectionHashAPI + URLEncoder.encode(collectionUri, "UTF-8");
		String hash = get("GET", url, null);

		if(!hash.contentEquals(latestCollectionHash)) {
			latestCollectionHash = hash;
			return true;
		}
		
		return false;
	}

	
	public void clearDirectory(String dataDir) throws IOException {
		File theDir = new File(dataDir);
		FileUtils.cleanDirectory(theDir);
	}


}