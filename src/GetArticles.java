import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class GetArticles {

	private final static String filename = "Article";
	private final String access_token = "97bbd669-4cbe-44fb-9b0f-63bba551a6ce";
	private final String url_part1 = "https://webhose.io/search?token=";
	private final String url_part2 = "&format=json&q=";
	
	private String query;
	private String url_to_query;
	private JSONObject array;
	
	Vector<String> vector;
	Vector<ArticleInfo> vectorOfArticles;
	
	public GetArticles() {
		query = new String();
		url_to_query = new String();
		vector = new Vector<String>();
		vectorOfArticles = new Vector<ArticleInfo>();
	}
	
	public GetArticles(String topic) {
		query = new String(topic);
		url_to_query = url_part1 + access_token + url_part2 + query;
		vector = new Vector<String>();
		vectorOfArticles = new Vector<ArticleInfo>();
	}
	
	private void getResponse() {
		try {
			URL url = new URL(url_to_query);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String jsonOutput = new String();
			String strTemp = "";
			while (null != (strTemp = br.readLine())) {
				//System.out.println(strTemp);
				jsonOutput += strTemp;
			}
			JSONParser parser = new JSONParser();
			try{
				Object obj = parser.parse(jsonOutput);
				array = (JSONObject)obj;
				System.out.println(array);
			}
			catch(ParseException pe){
			  System.out.println("position: " + pe.getPosition());
			  System.out.println(pe);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Vector<String> extractInfo() {
		JSONArray posts = (JSONArray) array.get("posts");
		
		Iterator i = posts.iterator();
		while (i.hasNext()) {
			JSONObject innerObj = (JSONObject) i.next();
			String title = (String) innerObj.get("title");
			String text = (String) innerObj.get("text");
			text = text.replace("\n", " ");
			String language = (String) innerObj.get("language");
			
			ArticleInfo article = new ArticleInfo(title, text, language);
			vector.add(article.toString());
			vectorOfArticles.add(article);
		}
		return vector;
	}
	
	public Vector<ArticleInfo> getLanguageSpecificArticles(String language, String filename) throws IOException {
		File file = new File(filename);
		file.createNewFile();
		FileWriter fw = new FileWriter(file);
		
		Vector<ArticleInfo> vec = new Vector<ArticleInfo>();
		Iterator<ArticleInfo> itr = vectorOfArticles.iterator();
		while(itr.hasNext()) {
			ArticleInfo art = itr.next();
			if (art.getLanguage().equalsIgnoreCase(language)) {
				vec.add(art);
				fw.write(art.getTitle() + "!@#" + art.getText() + "!@#" + art.getLanguage() +"\n");
			}
		}
		fw.flush();
		fw.close();
		return vec;
	}
	
	public static void getArticles(String topic, String language) throws IOException {
		GetArticles articles = new GetArticles(topic);
		articles.getResponse();
		articles.extractInfo();
		System.out.println(articles.getLanguageSpecificArticles(language, filename + "_" + topic + "_" + language + ".txt"));
	}
	
	public static void getAllArticles(String[] str) throws Exception {
		String language = new String("English");
		Vector<String> topic = ConceptTagging.findConcepts(str);
		for (String s:topic) {
			getArticles(s, language);
		}
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String str[] = new String[]{"AlchemyAPI has raised $2 million to extend the capabilities of its deep learning technology that applies artificial intelligence to read and understand web pages, text documents, emails, tweets, and other forms of content. Access Venture Partners led the Series A round, which the company will use to ramp up its sales and marketing, make hires and launch new services."};
		getAllArticles(str);
	}

}
