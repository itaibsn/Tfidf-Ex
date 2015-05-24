import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Corpus {

	private CorpusDataSource dataSource;
	private List<Doc> docs;
	
	// a map from each term to the number of docs containing the term
	private Map<String, Integer> generalTermFrequencies;
	
	public Corpus(CorpusDataSource dataSource) {
		docs = new ArrayList<Doc>();
		generalTermFrequencies = new HashMap<String, Integer>();
		this.dataSource = dataSource;
	}

	public void load() {
		Main.debug("Loading docs from corpuse data source..\n");
		String docText = null;
		while (dataSource.hasMore()) {
			docText = dataSource.loadNextDocText();
			if (docText != null){
				loadNewDoc(docText);
			}
		} 
		Main.debug("Number of docs loaded: " + getNumberOfDocs() );
		Main.debug("General Term Frequencies map: " + generalTermFrequencies + "\n");
	}
	
	public void loadNewDoc(String text){
		Doc newDoc = new Doc(dataSource.getCurrentDocId(), this);
		newDoc.parseAndLoad(text);
		docs.add(newDoc);
	}

	public void updateTerm(String term) {
		Integer oldTokenFrequency = generalTermFrequencies.get(term);
		generalTermFrequencies.put(term, oldTokenFrequency != null ? ++oldTokenFrequency : 1);
	}

	public void calcTfIdf() {
		Main.debug("Calculating tf-idf...\n");
		for (Doc doc : docs) {
			doc.calcTfIdf();
		}
	}

	public void printDocTermsOrderByTfIdfScore() {
		for (Doc doc : docs) {
			System.out.println(doc);
		}
	}

	public Map<String, Integer> getGeneralTermFrequencies() {
		return generalTermFrequencies;
	}

	public int getNumberOfDocs() {
		return docs.size();
	}
	
}
