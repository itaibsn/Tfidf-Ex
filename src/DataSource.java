import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;


public class DataSource implements CorpusDataSource {

	final public static String FILE_NAME = "idList";
	private final static String URL_PREFIX = "http://itunes.apple.com/lookup?id=";
	
	private List<String> idList;
	private Iterator<String> idIterator;
	private String currentDocId;
	
	public DataSource() {
		idList = new ArrayList<String>();
	}

	@Override
	public String getCurrentDocId(){
		return currentDocId;
	}
	
	@Override
	public CorpusDataSource init() {
		Main.debug("Reading ids from file: " + FILE_NAME + " ...");
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(FILE_NAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return this;
		}
		
		while (scanner.hasNextLine()) {
			idList.add(scanner.nextLine());
		}
		
		if (scanner != null){
			scanner.close();
		}
		idIterator = idList.iterator();
		return this;
	}
	
	@Override
	public boolean hasMore(){
		if (idIterator == null) 
			return false;
		return idIterator.hasNext();
	}
	
	@Override
	public String loadNextDocText(){
		currentDocId = idIterator.next();
		
		String response = null, 
			   url = URL_PREFIX + currentDocId;
		try {
			response = connectUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return retrieveDescriptionFromResponse(response);
	}
	
	private String retrieveDescriptionFromResponse(String response) {
		JSONObject jsonResponse;
		String description = null;
		Main.debug("Parsing response..");
		try {
			jsonResponse = new JSONObject(response);
			description = getDescriptionField(jsonResponse); 
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//Main.debug("description = " + description);
		return description;
	}

	private String getDescriptionField(JSONObject response) throws JSONException {
		return response.getJSONArray("results").getJSONObject(0).getString("description");
	}

	private static String connectUrl(String urlString) throws Exception {
		Main.debug("Connecting " + urlString + "...");
		BufferedReader reader = null;
	    try {
	        URL url = new URL(urlString);
	        reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1){
	            buffer.append(chars, 0, read); 
	        }
	        return buffer.toString();
	    } finally {
	        if (reader != null)
	            reader.close();
	    }
	}
	
}
