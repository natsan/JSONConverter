package yandexconverter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jsonobject.InputJSONObject;
import jsonobject.OutputJSONObject;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * В данном классе использует внешняя библиотека Jackson json
 * 1. Конвертирует строку в json-объект.
 * 2. Конвертирует входящий json-объект (InputJSONObject: url, content, title) в другой json-объект (OutputJSONObject: url, snippet).
 * 3. Конвертирует json-объект в строку.
 * 
 * @author Наталия
 *
 */

public class JSONConverter {
	
	private static Logger log = Logger.getLogger(JSONConverter.class.getName());
	
	/**
	 * объект JsonFactory, необходим для преобразования строки в json-объект (из библиотеки Jackson json)
	 */
	JsonFactory _jfactory;

	JSONConverter() {
		_jfactory = new JsonFactory();
	}

	/**
	 * Преобразует список строк в список json-объектов (@see InputJSONObject)
	 * @param jsonStringList - список строк, заключающих в себе json-объекты
	 * @return возвращает список с экземплярами класса InputJSONObject
	 */
	public List<InputJSONObject> parseStringToJSONList(List<String> jsonStringList) {
		List<InputJSONObject> jsonList = new ArrayList<InputJSONObject>();

		for(int i = 0; i<jsonStringList.size(); i++) {
			InputJSONObject jsonObject = parseStringToJSON(jsonStringList.get(i));
			jsonList.add(jsonObject);
		}
		log.info("JSON-list is read.");
		return jsonList;
	}

	/**
	 * Преобразует строку в json-объект InputJSONObject. Используется в методе parseStringToJSONList() ("@see parseStringToJSONList)
	 * @param str - строка для преобразования
	 * @return возвращает экземпляр класса InputJSONObject
	 */
	private InputJSONObject parseStringToJSON(String str) {
		InputJSONObject json = new InputJSONObject();
		try {

			JsonParser jParser = _jfactory.createParser(str);

			while (jParser.nextToken() != JsonToken.END_OBJECT) {

				String fieldname = jParser.getCurrentName();
				if (InputJSONObject.URL.equals(fieldname)) {
					jParser.nextToken();
					json.setUrl(jParser.getText());
					//		System.out.println(jParser.getText());
				}
				if (InputJSONObject.CONTENT.equals(fieldname)) {
					jParser.nextToken();
					json.setContent(jParser.getText());
					//		System.out.println(jParser.getText());
				}
				if (InputJSONObject.TITLE.equals(fieldname)) {
					jParser.nextToken();
					json.setTitle(jParser.getText());
					//		System.out.println(jParser.getText());
				}
			}
			jParser.close();
		}
		catch (JsonParseException e) {
			log.log(Level.SEVERE, "Exception: ", e);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Exception: ", e);
		}
		return json;
	}

	/**
	 * Конвертирует json-список с объектами InputJSONObject к json-списку с объектами OutputJSONObject
	 * @param inputJsonList - список с json-объектами InputJSONObject (@see InputJSONObject)
	 * @return возвращает список с json-объектами OutputJSONObject (@see OutputJSONObject)
	 */
	public List<OutputJSONObject> convertJSONList(List<InputJSONObject> inputJsonList) {
		List<OutputJSONObject> outputJsonList = new ArrayList<OutputJSONObject>();
		for (InputJSONObject inputJson : inputJsonList) 
			outputJsonList.add(convertJSONObject(inputJson));
		
		return outputJsonList;
	}

	/**
	 * Конвертирует json-объект InputJSONObject к json-объекту OutputJSONObject. Используется в методе convertJSONList() (@see converJSONList)
	 * @param inputJson - экземпляр класса InputJSONObject (@see InputJSONObject)
	 * @return экземпляр класса OutputJSONObject (@see OutputJSONObject)
	 */
	private OutputJSONObject convertJSONObject(InputJSONObject inputJson) {
		OutputJSONObject outputJson = new OutputJSONObject();
		outputJson.setUrl(inputJson.getUrl());
		outputJson.setSnippet(transformString(inputJson.getTitle(), inputJson.getContent(), 300));
	//	System.out.println("Snippet: " + outputJson.getSnippet());

		return outputJson;
	}

	/**
	 * Преобразует заголовок title и содержание content аннотации (поля класса InputJSONObject)
	 * к анонсу snippet (поле класса OutputJSONObject) заданного размера.
	 * Используется в методе OutputJSONObject().
	 * @param title - заголовок
	 * @param content - содержание
	 * @param endIndex - количество символов в анонсе snippet
	 * @return возвращает сформированный анонс snippet для класса OutputJSONObject
	 */
	private String transformString(String title, String content, int endIndex){
		int beginIndex = 0;
		content = deleteHtml(content);

		StringBuilder result = new StringBuilder();

		if(!content.contains(title)) {
			result.append(title);
			result.append(" ");
		} else {
			String temp = content.substring(0, title.length());
			if(!temp.contains(title)) {
				result.append(title);
				result.append(" ");
			}		
		}
		result.append(content);

		if (result.length()<endIndex)
			return result.toString();
		
		String snippet = result.toString();
		snippet = snippet.replaceAll("[\\s]{2,}", " ");
		snippet = result.substring(beginIndex, endIndex);
		return cutToPoint(snippet);
	}

	/**
	 * Удаляет html-теги из строки. Используется в методе transformString().
	 * @param str - срока, из которой необходимо удалить теги
	 * @return возвращает сроку без тегов
	 */
	public String deleteHtml(String str) {
		str = str.replaceAll("<[a-zA-Z0-9!#\\s/=\":;_-]+>", " ");
		str = str.replaceAll("[ஐ]|[\\[/p\\]]|[&quot;]|[\\n]", " ");	
		str = str.replaceAll("[\\s]{1,}[,]", ", ");
		str = str.replaceAll("[\\s]{1,}[.]", ". ");
		str = str.replaceAll("[\\s]{1,}[;]", "; ");
		str = str.replaceAll("[\\s]{2,}", " ");
		return str;
	}

	/**
	 * Разбивает строку на предложения. Обрезает строку на точке предпоследнего
	 * предложения и вставляет многоточие вместо послед. предложения.
	 *  Используется в методе transformString().
	 * @param str - строка, в которой содержатся предложения. Послед. предложение обрезано, поэтому его надо удалить и добавить многоточие.
	 * @return возвращает сроку без последнего недоконченного предложения.
	 */
	public String cutToPoint(String str) {
		String [] arrstr = str.split("[.?!]\\s+(?=[А-Я-])");

		if(arrstr.length==0)
			return str;
		if (arrstr.length==1) 
			return arrstr[0]+".";

		StringBuilder s = new StringBuilder();
		try {
			for (int i = 0; i < arrstr.length-1; i++) {
				s.append(arrstr[i]);
				s.append(". ");
			}

			s = s.replace(s.lastIndexOf("."), s.length()-1, "...");
		}catch(StringIndexOutOfBoundsException e){
			log.log(Level.SEVERE, "Exception: ", e);
		}
		return s.toString();	
	}

	/**
	 * Преобразует список с json-объектами OutputJSONObject к списку строк.
	 * @param jsonList - список json-объектов OutputJSONObject
	 * @return возвращает список строк
	 */
	public List<String> convertJSONListToString(List<OutputJSONObject> jsonList) {
		List<String> stringList = new ArrayList<String>();
		for (OutputJSONObject outputJson : jsonList) 
			stringList.add(convertJSONObjectToString(outputJson));

		log.info("New JSON-list is prepared.");
		return stringList;
	}

	/**
	 * Преобразует json-объект в строку с помощью ObjectMapper (библиотека Jackson)
	 * @param json - json-объект класса OutputJSONObject (@see OutputJSONObject)
	 * @return возвращает json-объект в виде строки
	 */
	private String convertJSONObjectToString(OutputJSONObject json) {

		ObjectMapper mapper = new ObjectMapper();
		String stringJson = "";
		try {
			stringJson = mapper.writeValueAsString(json);
		} catch (JsonProcessingException e) {
			log.log(Level.SEVERE, "Exception: ", e);
		}

		return stringJson;
	}

}
