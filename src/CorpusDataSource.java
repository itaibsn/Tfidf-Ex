
public interface CorpusDataSource {
	public CorpusDataSource init();
	public boolean hasMore();
	public String loadNextDocText();
	public String getCurrentDocId();
}
