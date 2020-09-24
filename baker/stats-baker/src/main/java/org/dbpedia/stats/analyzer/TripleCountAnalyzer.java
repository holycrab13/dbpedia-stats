package org.dbpedia.stats.analyzer;

import org.apache.jena.graph.Triple;
import org.dbpedia.stats.StatsAnalyzerBase;

public class TripleCountAnalyzer extends StatsAnalyzerBase {

	private int tripleCount;

	@Override
	public void analyze(Triple triple) {
		tripleCount++;
	}

	@Override
	public void begin() {
		tripleCount = 0;		
	}

	@Override
	public void finish() {
		model.setTripleCount(tripleCount);
	}
	
	
}
