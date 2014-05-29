package yandexconverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jsonobject.OutputJSONObject;

/**
 * Используется для чтения из файла и для записи в файл с кодировкой utf-8.
 * 
 * @author Наталия
 *
 */

public class FileHandler {
	
	private static Logger log = Logger.getLogger(FileHandler.class.getName());
	
	/*
	 * статическая переменная - первый байт файла BOM
	 */
	public static final String UTF8_BOM = "\uFEFF";

	FileHandler() {

	}

	/**
	 * Чтение из файла
	 * @param filePath - директория и имя файла.
	 * @return возвращает список List<String> со строками, содержащими json-объекты.
	 */
	public List<String> readFile(String filePath) {
		List<String> jsonList = new ArrayList<String>();
		try {
			
			try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "UTF8"))) {
		//	BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "UTF8"));
			String str;
			if ((str = in.readLine()) != null)
				jsonList.add(removeUTF8BOM(str));

			while ((str = in.readLine()) != null) 
				jsonList.add(str);
		//	in.close();
			}
		} 
		catch (UnsupportedEncodingException e) 
		{
			log.log(Level.SEVERE, "Exception: ", e);
		} 
		catch (IOException e) 
		{
			log.log(Level.SEVERE, "Exception: ", e);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Exception: ", e);
		}
		log.info("File is read succesfully.");
		return jsonList;
	}
	
	/**
	 * Запись в файл подготовленный список json-объектов (в виде списка строк)
	 * @param filePath - директория и имя файла
	 * @param jsonList - список json-объектов в виде списка строк
	 */
	
	public void writeFile(String filePath, List<String> jsonList) {
		FileOutputStream fileOutputStream = null;
		BufferedWriter bufferedWriter = null;
		try {
			fileOutputStream = new FileOutputStream(new File(filePath));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, "UTF-8"));
		} catch (FileNotFoundException e){
			log.log(Level.SEVERE, "Exception: ", e);
		} catch (UnsupportedEncodingException e) {
			log.log(Level.SEVERE, "Exception: ", e);
		}
		try {
			for (String str : jsonList) {
				bufferedWriter.append(str);
				bufferedWriter.append("\n");
			}
			bufferedWriter.flush();
		} catch (IOException e) {
			log.log(Level.SEVERE, "Exception: ", e);
		} finally { 
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				log.log(Level.SEVERE, "Exception: ", e);
			}

			try {
				fileOutputStream.close();
			} catch (IOException e) {
				log.log(Level.SEVERE, "Exception: ", e);
			}
		}
		log.info("File is written succesfully.");
	}

	/**
	 * Удаляет первый байт BOM из строки с кодировкой UTF-8.
	 * @param s - строка, из которой необходимо удалить байт.
	 * @return возвращает строку без первого байта.
	 */
	private static String removeUTF8BOM(String s) {
		if (s.startsWith(UTF8_BOM)) {
			s = s.substring(1);
		}
		return s;
	}
}
