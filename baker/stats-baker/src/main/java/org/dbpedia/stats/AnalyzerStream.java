package org.dbpedia.stats;
import org.apache.jena.graph.Triple;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.sparql.core.Quad;

public class AnalyzerStream implements StreamRDF {

	private StatsAnalyzerBase[] analyzers;

	public AnalyzerStream(StatsAnalyzerBase[] analyzers) {
		this.analyzers = analyzers;
	}
	
	@Override
	public void start() {
		
	}

	@Override
	public void triple(Triple triple) {
		for(int i = 0; i < analyzers.length; i++) {
			analyzers[i].analyze(triple);
		}
	}

	@Override
	public void quad(Quad quad) {
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
