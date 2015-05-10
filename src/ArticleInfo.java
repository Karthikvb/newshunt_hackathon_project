
public class ArticleInfo {
	String title, text, language;
	public ArticleInfo(String title, String text, String language) {
		this.title = new String(title);
		this.text = new String(text);
		this.language = new String(language);
	}
	
	public String toString() {
		return "Title : " + this.title 
				+ "\n Text : " + this.text
				+ "\n Language : " + this.language;
	}
	
	public String getText() {
		return this.text;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getLanguage() {
		return this.language;
	}	
}