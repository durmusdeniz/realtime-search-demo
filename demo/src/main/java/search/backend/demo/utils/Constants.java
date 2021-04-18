package search.backend.demo.utils;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface Constants {

	//Box Coordinates for Singapore. Used as boundaries while creating random bikes.
	public static Double SG_LAT_MAX = 1.4504753;
	public static Double SG_LAT_MIN = 1.1304753;
	public static Double SG_LON_MAX = 104.0120359;
	public static Double SG_LON_MIN = 103.6920359;

	public static final Directory indexDir = new RAMDirectory();

	public static final String LAT_FIELD_NAME = "lat";
	public static final String LON_FIELD_NAME = "lon";
	public static final String SORT_FIELD_NAME = "loc";

	public static final String LOCATION_FIELD = "location";


	public static final Set<IndexSearcher> INDEX_SEARCHERS = new HashSet<>();
	public static final Set<IndexWriter> INDEX_WRITERS = new HashSet<>();


}

