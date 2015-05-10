/*
 * TextTranslate - Translate Text Snippet
 * Author:
 * 
 */

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class TextTranslate {

	public static void main(String[] args) throws Exception {
		//Replace client_id and client_secret with your own.  
	    Translate.setClientId("ADD_HERE");
	    Translate.setClientSecret("ADD_HERE");
	 
	    // Translate an english string to spanish
	    String englishString = "I am Aswin";
	    String spanishTranslation = Translate.execute(englishString, Language.HINDI);
	 
	    System.out.println("Original english phrase: " + englishString);
	    System.out.println("Translated spanish phrase: " + spanishTranslation);
	}
}