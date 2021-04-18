package search.backend.demo.rest;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LatLonDocValuesField;
import org.apache.lucene.document.LatLonPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import search.backend.demo.utils.Constants;

import java.io.IOException;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.IntStream;

@RestController
public class SearchEndpoint {

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/generate")
	public String generateCoordinates() throws IOException {

		if(Constants.INDEX_WRITERS.size() == 0){
			//Generating the coords of scooters if the search index is empty.
			populateSearchIndex();
		}

		return "OK";
	}


	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/search/{scooters}/{radius}/{lat}/{lon}")
	public String getScooters(@PathVariable int scooters, @PathVariable int radius, @PathVariable double lat, @PathVariable double lon) throws Exception {

		if(Constants.INDEX_WRITERS.size() == 0){
			//In case the random coords not created yet
			populateSearchIndex();
		}

		if(Constants.INDEX_SEARCHERS.size() == 0){
			Constants.INDEX_SEARCHERS.add(new IndexSearcher(DirectoryReader.open(Constants.INDEX_WRITERS.stream().findFirst().get())));
		}


		Sort sortField = new Sort(LatLonDocValuesField.newDistanceSort(Constants.SORT_FIELD_NAME, lat, lon));
		TopDocs docs = Constants.INDEX_SEARCHERS.stream().findFirst().get().search(LatLonPoint.newDistanceQuery(Constants.LOCATION_FIELD, lat,lon, radius), scooters,sortField);
		StringJoiner sj = new StringJoiner(";");
		for (ScoreDoc scoreDoc : docs.scoreDocs) {
			Document foundScooter = Constants.INDEX_SEARCHERS.stream().findFirst().get().doc(scoreDoc.doc);
			sj.add(foundScooter.getField(Constants.LAT_FIELD_NAME).numericValue() + "," + foundScooter.getField(Constants.LON_FIELD_NAME).numericValue());
		}
		return sj.toString();
	}


	private void populateSearchIndex() throws IOException {

		if(Constants.INDEX_WRITERS.size() == 0){
			Constants.INDEX_WRITERS.add(new IndexWriter(Constants.indexDir, new IndexWriterConfig(
				new WhitespaceAnalyzer()).setOpenMode(IndexWriterConfig.OpenMode.CREATE)));
		}


		Random random = new Random();

		IntStream.range(0, 200000).parallel().forEach(
				s ->{

					double lat = random.doubles(Constants.SG_LAT_MIN,(Constants.SG_LAT_MAX)).findFirst().getAsDouble();
					double lon = random.doubles(Constants.SG_LON_MIN,(Constants.SG_LON_MAX)).findFirst().getAsDouble();

					Document scooter = new Document();
					scooter.add(new StoredField(Constants.LAT_FIELD_NAME ,lat));
					scooter.add(new StoredField(Constants.LON_FIELD_NAME ,lon));
					scooter.add(new LatLonDocValuesField(Constants.SORT_FIELD_NAME, lat, lon));
					scooter.add(new LatLonPoint(Constants.LOCATION_FIELD, lat,lon));
					try {
						Constants.INDEX_WRITERS.stream().findFirst().get().addDocument(scooter);
					}catch (IOException e){
						e.printStackTrace();
					}
				}
			);
	}






}
