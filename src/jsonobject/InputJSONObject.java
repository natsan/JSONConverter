package jsonobject;

/**
 * Вид входящего json-объекта, состоящего из стрёх параметров: url, content, title и считываемого из файла.
 * 
 * @author Наталия
 *
 */
public class InputJSONObject {
	public final static String URL = "url";
	public final static String CONTENT = "content";
	public final static String TITLE = "title";
	
	private String url;
	private String content;
	private String title;
	
	public InputJSONObject() {
		
	}
	
	public InputJSONObject(String url, String content, String title) {
		this.url = url;
		this.content = content;
		this.title = title;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public String getTitle() {
		return this.title;
	}
}
