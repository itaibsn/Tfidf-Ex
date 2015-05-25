import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Map.Entry;


public class Doc {
	
	private final static int NUMBER_OF_RESULTS_TO_PRINT = 10;
	private final static boolean CASE_SENSITIVE = true;
	private final static String PARSING_DELIMITERS = " \t\n\r\f,.?!@#$%^&*{}[]<>\\/:;~`“”-–+|\"•●®";
	
	private Corpus ownerCorpus;
	private String id;
 
	// a map from each term in the doc, to the number of times it appears in the doc.
	private Map<String, Integer> termCounters;
	
	// after calling calcTfIdf(), this set of pairs (Map.Entry is just a pair):	 term -> tf-idf score,
	// will be populate, and sorted by tf-idf score.
	private SortedSet<Map.Entry<String, Double>> tfIdfSortedSet ;
	
	public Doc(String id, Corpus ownerCorpus) {
		this.id = id;
		this.termCounters = new HashMap<String, Integer>();
		this.ownerCorpus = ownerCorpus;
		this.tfIdfSortedSet = getNewValueSortedSetOfPairs();
	}
	
	//get a new empty sorted by value set of pairs. (term -> tf-idf score) 
	private SortedSet<Map.Entry<String, Double>> getNewValueSortedSetOfPairs() {
		
		//create a comparator to compare the pairs by their weight
		Comparator<Map.Entry<String, Double>> MyComperator = new Comparator<Map.Entry<String,Double>>() {
			@Override
			public int compare(Entry<String, Double> p1, Entry<String, Double> p2) {
				int res = p2.getValue().compareTo(p1.getValue());
				return res != 0 ? res : 1;	//in case of equal, give priority to one of them, otherwise it considered as duplication 
			}
		};
	
		//return a new sorted TreeSet, sorted by value
		return new TreeSet<Map.Entry<String, Double>>(MyComperator);	 
	}	
	
	public Map<String, Integer> getTermCounters() {
		return termCounters;
	}	
	
	public void parseAndLoad(String text) {
		StringTokenizer st = new StringTokenizer(text,PARSING_DELIMITERS);
		while (st.hasMoreTokens()) {
		    updateTerm(CASE_SENSITIVE? st.nextToken() : st.nextToken().toLowerCase());
		}
		Main.debug(id + " term counters map: " + termCounters + "\n");
	}

	private void updateTerm(String term) {
		Integer oldTermCounter = termCounters.get(term);
		if (oldTermCounter == null) {	// if its term first appearance
			termCounters.put(term, 1);
			ownerCorpus.updateTerm(term);
		} else {
			termCounters.put(term,  ++oldTermCounter);
		}
	}

	public void calcTfIdf() {
		//iterate over all terms at doc
		for (Entry<String, Integer> entry : getTermCounters().entrySet()) {
			String term = entry.getKey();
			Integer termFrequency = entry.getValue();
			Double TfidfScore =  calcTermTfidf(term, termFrequency);
			addTermAndTfidfScoreEntry(term,TfidfScore);
		}
	}
	
	private Double calcTermTfidf(String term, Integer termFrequency) {
		return ((double)termFrequency) * calcIdf(term);
	}
	
	private  Double calcIdf(String term) {
		return Math.log10((double)ownerCorpus.getNumberOfDocs()/(double)ownerCorpus.getGeneralTermFrequencies().get(term));
	}

	private void addTermAndTfidfScoreEntry(String term, Double tfidfScore) {
		tfIdfSortedSet.add(new AbstractMap.SimpleImmutableEntry<String, Double>(term, tfidfScore));
	}
	
	@Override
	public String toString() {
		String result = id + ":";
		boolean lastIteration = false;
		int count = NUMBER_OF_RESULTS_TO_PRINT;  
		String entryFormat = " %s %.4f";
		for (Entry<String, Double> entry : tfIdfSortedSet) {
			if (count - 1 == 0) {
				lastIteration = true;
			} 
			result+= String.format( lastIteration? entryFormat : (entryFormat + ","), entry.getKey(), entry.getValue());
			
			if (lastIteration) {
				break;
			}
			count--;
		}

		return result.toString();
	}

}
