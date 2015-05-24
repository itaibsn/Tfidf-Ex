
public class Main {

	final public static boolean DEBUG = true;
	
	public static void main(String[] args) {
		Corpus corpus = new Corpus((new DataSource()).init());
		corpus.load();
		corpus.calcTfIdf();
		corpus.printDocTermsOrderByTfIdfScore();
		debug("done.");
	}
	
	public static void debug(String msg){
		if (DEBUG) {
			System.out.println("DEBUG	" + msg);
		}
	}
			
}