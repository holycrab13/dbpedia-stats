package org.dbpedia.stats.analyzer;

import java.util.HashSet;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.dbpedia.stats.StatsAnalyzerBase;

public class InstanceCountAnalyzer extends StatsAnalyzerBase {

private HashSet<String> instanceSet;

	
	@Override
	public void analyze(Triple triple) {
		tryAddNode(triple.getSubject());
		tryAddNode(triple.getObject());
	}
	
	private void tryAddNode(Node node) {
		
		if(node.isBlank()) {
			return;
		}
		
		if(!node.isURI()) {
			return;
		}
		
		instanceSet.add(node.getURI());
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
