package org.dbpedia.stats.analyzer;

import java.util.HashSet;

import org.apache.jena.graph.Triple;
import org.dbpedia.stats.StatsAnalyzerBase;

public class PropertyCountAnalyzer extends StatsAnalyzerBase {

	private HashSet<String> propertySet;

	
	@Override
	public void analyze(Triple triple) {
		try {
			propertySet.add(triple.getPredicate().getURI());
		} catch(UnsupportedOperationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void begin() {
		propertySet = new HashSet<String>();
	}

	@Override
	public void finish() {
		model.setPropertyCount(propertySet.size());
	}
}
