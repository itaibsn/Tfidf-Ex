import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class TesterDataSource implements CorpusDataSource {

	private List<String> descriptionsList;
	private Iterator<String> dataIterator;
	private Integer currentDocId;
	
	@Override
	public CorpusDataSource init() {
		descriptionsList = Arrays.asList(
				"this is a sample a",
				"another example. this example is another example");
		
		currentDocId = 0;
		dataIterator = descriptionsList.iterator();
		return this;
	}

	@Override
	public boolean hasMore() {
		return dataIterator.hasNext();
	}

	@Override
	public String loadNextDocText() {
		currentDocId++;
		return dataIterator.next();
	}

	@Override
	public String getCurrentDocId() {
		return "Doc" + currentDocId;
	}

}
