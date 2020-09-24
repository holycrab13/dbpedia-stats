package org.dbpedia.stats;
import org.apache.jena.graph.Triple;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.sparql.core.Quad;

public class AnalyzerStream implements StreamRDF {

	private StatsAnalyzerBase[] analyzers;

	private long processedTripleCount;

	public AnalyzerStream(StatsAnalyzerBase[] analyzers) {
		this.analyzers = analyzers;
		this.processedTripleCount = 0;
	}
	
	@Override
	public void start() {
		
	}

	@Override
	public void triple(Triple triple) {
		
		processedTripleCount++;

		if(processedTripleCount % 1000000 == 0) {
			System.out.println("Processed Triples: " + processedTripleCount);
		}

		for(int i = 0; i < analyzers.length; i++) {
			analyzers[i].analyze(triple);
		}
	}

	@Override
	public void quad(Quad quad) {

		processedTripleCount++;

		if(processedTripleCount % 1000000 == 0) {
			System.out.println("Processed Triples: " + processedTripleCount);
		}

		for(int i = 0; i < analyzers.length; i++) {
			analyzers[i].analyze(quad.asTriple());
		}
	}

	@Override
	public void base(String base) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prefix(String prefix, String iri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finish() {
		
	}

}
