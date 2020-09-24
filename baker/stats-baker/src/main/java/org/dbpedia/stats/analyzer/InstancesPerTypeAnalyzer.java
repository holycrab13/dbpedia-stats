package org.dbpedia.stats.analyzer;

import java.util.HashMap;
import java.util.Map;

import org.apache.jena.graph.Triple;
import org.dbpedia.stats.StatsAnalyzerBase;


public class InstancesPerTypeAnalyzer extends StatsAnalyzerBase {

	private Map<String, Long> instanceTypeCounts;
	
	private static String rdfsTypeProperty = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	
	@Override
	public void analyze(Triple triple) {
		
		if(!triple.getPredicate().isURI() || !triple.getObject().isURI()) {
			return;
		}
		
		if(!triple.getPredicate().getURI().equals(rdfsTypeProperty)) {
			return;
		}

		String uri = triple.getObject().getURI();
		
		if(instanceTypeCounts.containsKey(uri)) {
			instanceTypeCounts.put(uri, instanceTypeCounts.get(uri) + 1);
		} else {
			instanceTypeCounts.put(uri, (long) 1);
		}
	}
	
	@Override
	public void begin() {
		instanceTypeCounts = new HashMap<String, Long>();
	}

	@Override
	public void finish() {
		model.setInstanceCountsPerType(instanceTypeCounts);
	}
	
}
