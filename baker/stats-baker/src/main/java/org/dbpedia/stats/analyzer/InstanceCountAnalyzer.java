package org.dbpedia.stats.analyzer;

import java.util.HashSet;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.dbpedia.stats.StatsAnalyzerBase;

public class InstanceCountAnalyzer extends StatsAnalyzerBase {

	private HashSet<String> instanceSet;

	private static String rdfsTypeProperty = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	
	@Override
	public void analyze(Triple triple) {

		if(!triple.getPredicate().isURI() || !triple.getSubject().isURI()) {
			return;
		}
		
		if(!triple.getPredicate().getURI().equals(rdfsTypeProperty)) {
			return;
		}

		instanceSet.add(triple.getSubject().getURI());
	}
	
	@Override
	public void begin() {
		instanceSet = new HashSet<String>();
	}

	@Override
	public void finish() {
		model.setInstanceCount(instanceSet.size());
	}
	
}
