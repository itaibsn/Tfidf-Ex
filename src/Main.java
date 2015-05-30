
public class Main {

	final public static boolean DEBUG = false;
	
	public static void main(String[] args) {
		System.out.println("Starting");
		Corpus corpus = new Corpus((new DataSource()).init());
		System.out.println("Loading Corpus...");
		corpus.load();
		System.out.println("Calculating tf-idf...");
		corpus.calcTfIdf();
		corpus.printDocTermsOrderByTfIdfScore();
		System.out.println("done.");
	}
	
	
	//TODO: get dubug function to different class (Logger)
	public static void debug(String msg){
		if (DEBUG) {
			System.out.println("DEBUG	" + msg);
		}
	}
			
}
