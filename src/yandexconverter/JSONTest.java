package yandexconverter;
import java.util.List;

import jsonobject.InputJSONObject;
import jsonobject.OutputJSONObject;
/**
 * Здесь производится запуск примера. На входе файл "intern-task.json.filtered", в котором
 * хранится список json-объектов (url, content, title), далее этот список преобразуется в
 * список json-объектов (url, snippet), который записывается в файл "intern-task_modified.json".
 * @author Наталия
 *
 */
public class JSONTest {

	public static void main(String[] args) {
		FileHandler fileHandler = new FileHandler();
		List<String> data = fileHandler.readFile("intern-task.json.filtered");
		JSONConverter converter = new JSONConverter();
		List<InputJSONObject> jsonList = converter.parseStringToJSONList(data);
		List<OutputJSONObject> outputList = converter.convertJSONList(jsonList);
		List<String> stringList = converter.convertJSONListToString(outputList);
		fileHandler.writeFile("intern-task_modified.json", stringList);

	}

}
