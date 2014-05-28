package jsonobject;

/**
 * Вид модифицированного json-объекта, записываемого в файл.
 * @author Наталия
 *
 */
public class OutputJSONObject {
	public final static String URL = "url";
	public final static String SNIPPET = "snippet";
	
	private String url;
	private String snippet;
	
public OutputJSONObject() {
		
	}
	
	public OutputJSONObject(String url, String snippet) {
		this.url = url;
		this.snippet = snippet;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public String getSnippet() {
		return this.snippet;
	}

}
