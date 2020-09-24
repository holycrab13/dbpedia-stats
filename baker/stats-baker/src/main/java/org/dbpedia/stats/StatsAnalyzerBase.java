package org.dbpedia.stats;
import org.apache.jena.graph.Triple;
import org.dbpedia.stats.model.StatsEntryModel;

public abstract class StatsAnalyzerBase {

	protected StatsEntryModel model;
	
	public abstract void begin();

	public abstract void analyze(Triple triple);
	
	public abstract void finish();
	
	public void initialize(StatsEntryModel model) {
		this.model = model;
	}
}
