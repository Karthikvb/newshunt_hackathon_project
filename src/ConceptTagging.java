import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alchemyapi.api.AlchemyAPI;

public class ConceptTagging {
	Vector<String> tweets;
	Vector<String> topics;
	
	private final String API_KEY = "00a8b60633f56b64bcaca24d6af7cf6f2cf5c35b";
	
	public String DocumentCategorizer(String text) throws IOException {
		  
		  File test = new File("en-doccat.bin");
		  String classificationModelFilePath = test.getAbsolutePath();
		  DocumentCategorizerME classificationME = new DocumentCategorizerME(
		    new DoccatModel(
		      new FileInputStream(classificationModelFilePath)));
		  String documentContent = text;
		  double[] classDistribution = classificationME
		    .categorize(documentContent);
		  
		  String predictedCategory = classificationME
		    .getBestCategory(classDistribution);
		  System.out.println("Model prediction : " + predictedCategory);
		  return predictedCategory;
	}
	
	public ConceptTagging(String[] tweets) {
		this.tweets = new Vector<String>();
		for (String t : tweets) {
			this.tweets.add(t);
		}
		this.topics = new Vector<String>();
	}
	
	
 
	private void sendPost(String tweet) throws Exception {
		 
		String url = " http://access.alchemyapi.com/calls/text/TextGetRankedConcepts";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("apikey", API_KEY);
		con.setRequestProperty("text", tweet);
		con.setRequestProperty("outputMode", "json");
 
		// Send post request
		con.setDoOutput(true);
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(response.toString());
 
	}
	
	// utility method
    private static String getStringFromDocument(Document doc) {
        try {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);

            return writer.toString();
        } catch (TransformerException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public String getConcepts(Document doc) {
    	Vector<String> textVec = new Vector<String>();
    	Vector<String> relevanceVec = new Vector<String>();
    	
    	doc.getDocumentElement().normalize();
        System.out.println("Root element :" 
           + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("concepts");
        for (int temp = 0; temp < nList.getLength(); temp++) {
        	Node nNode = nList.item(temp);
        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        		Element eElement = (Element) nNode;
        		textVec.add(eElement
                        .getElementsByTagName("text")
                        .item(temp)
                        .getTextContent());
        		relevanceVec.add(eElement
                        .getElementsByTagName("relevance")
                        .item(temp)
                        .getTextContent());
        	}
        }
        
        System.out.println(textVec);
        System.out.println(relevanceVec);
        
        float fmax = 0;
        int index = 0;
        for (int i=0;i<relevanceVec.size();i++) {
        	if (Float.parseFloat(relevanceVec.get(i)) > fmax) {
        		fmax = Float.parseFloat(relevanceVec.get(i));
        		index = i;
        	}
        }
		return textVec.get(index);
    }
    
	public String findTag(String tweet) throws Exception {
		AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromFile("/Users/karthikeyanvb/Downloads/alchemy/testdir/api_key.txt");
		Document doc = alchemyObj.TextGetRankedConcepts(tweet);
	    //System.out.println(getStringFromDocument(doc));
	    return getConcepts(doc);
	}
	

	public Vector<String> startConceptTagging() throws Exception {
		for (String tweet : tweets) {
			topics.addElement(findTag(tweet));
		}
		return topics;
	}
	
	public static Vector<String> findConcepts(String[] tweets) throws Exception {
		ConceptTagging ct = new ConceptTagging(tweets);
		Vector<String> vec = ct.startConceptTagging();
		return vec;
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String str[] = new String[]{"AlchemyAPI has raised $2 million to extend the capabilities of its deep learning technology that applies artificial intelligence to read and understand web pages, text documents, emails, tweets, and other forms of content. Access Venture Partners led the Series A round, which the company will use to ramp up its sales and marketing, make hires and launch new services."};
		//System.out.println(findConcepts(str));
	}

}
