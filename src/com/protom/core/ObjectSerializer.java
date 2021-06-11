package com.protom.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.protom.annotation.DateFormat;
import com.protom.annotation.Skip;
import com.protom.annotation.SkipNull;
import com.protom.enumeration.Format;

public class ObjectSerializer {

	public static String Serializer (Object obj, Format format) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		//Instanzio una mappa chiave-valore per contenere i dati
		Map<String, Object> map = new HashMap<>();
		
		//Instanzio un'Array per contenere i metodi dell'oggetto passato
		Method[] methods = obj.getClass().getMethods();
		
		for (Method m: methods) {
			//Filtro posto sui metodi eccetto "getClass"
			if(m.getName().startsWith("get") && !m.getName().startsWith("getClass")) {
				//Se il metodo utilizzato possiede un'annotazione Skip ne saltiamo l'esecuzione
				if(m.getAnnotation(Skip.class) != null) {
					continue;
				}
				/*
				 * Se il metodo utilizzato possiede un'annotazione SkipNull ne saltiamo l'esecuzione
				 * se il valore è uguale a NULL
				 */
				if(m.getAnnotation(SkipNull.class) != null && m.invoke(obj) == null) {
					continue;
				}
				
				
				
				// Se il metodo ha l'annotation DateFormat e restituisce un valore di tipo LocalDate.
				if(m.getAnnotation(DateFormat.class) != null && m.invoke(obj) instanceof LocalDate ){
					// Salvo il valore ritornato dal metodo
					LocalDate date = (LocalDate) m.invoke(obj);
					DateFormat df = m.getAnnotation(DateFormat.class);
					
					// Eseguo la sostituzione dei parametri
					String date_sorted = df.format().replace("dd", String.valueOf(date.getDayOfMonth()));
					date_sorted = date_sorted.replace("MM", String.valueOf(date.getMonthValue()));
					date_sorted = date_sorted.replace("YY", String.valueOf(date.getYear()));
					
					/* 
					 * Aggiungo l'attributo alla mappa usando come chiave il nome del metodo (get non verrà visualizzato) 
					 */
					map.put(m.getName().substring(3).toLowerCase(), date_sorted);
				} else {
					// Come valore verrà impostato il toString() dell'oggetto restituito dal metodo.
					map.put(m.getName().substring(3).toLowerCase(), m.invoke(obj));
				}
				
				
				
			}
		}
		
		
		//Instanzio un Array che contiene tutti campi dichiarati nella classe passata
		Field[] fields = obj.getClass().getDeclaredFields();
		
		for (Field f: fields) {
			
			//Controllo se il campo è mappato all'interno dell'HashMap   
			if (f.canAccess(obj) && map.containsKey(f.getName())) {
				
				//Evito di stampare il campo se possiede l'annotazione Skip
				if(f.getAnnotation(Skip.class)!=null) {
					continue;
				}
				
				/*
				 * Evito di stampare il campo se possiede l'annotazione SkipNull
				 * e il suo contenuto è uguale a NULL
				 */
				if(f.getAnnotation(SkipNull.class) != null && f.get(obj) == null) {
					continue;
				}
				
				map.put(f.getName().toLowerCase(), f.get(obj));
			}
		}
		
		switch(format) {
		case XML: return toXML(obj.getClass().getSimpleName(), map);
		case JSON: return toJSON(obj.getClass().getSimpleName(), map);
		}
		return "Non è stato possibile serializzare l'oggetto inserito";
		
	}
	
	
	public static String toXML(String class_name, Map<String, Object> map) {
		
		//Instanzio uno StringBuilder
		StringBuilder builder = new StringBuilder();
		
		//Scrivo il primo tag XML inserendo il nome della classe passata
		builder.append("<").append(class_name).append(">\n");
		
		//ForEach dell'HashMap 
		for(String key: map.keySet()) {
			//Per ogni chiave creo un tag ed inserisco il valore all'interno di quest'ultimo
			builder.append("<").append(key).append(">\n");
			builder.append(map.get(key)+"\n");
			builder.append("</").append(key).append(">\n");
		}
		
		//Scrivo la chiusura del tag principale della classe
		builder.append("</").append(class_name).append(">\n");
		
		//Inserisco il contenuto dello StringBuilder in una variabile String da ritornare
		String xml_file = builder.toString();
		return xml_file;	
	}
	
	public static String toJSON(String class_name, Map<String, Object> map) {

		//Instanzio uno StringBuilder
		StringBuilder builder = new StringBuilder();
		
		//Scrivo il primo tag JSON che identifica il nome della classe
		builder.append("{\n'class':'").append(class_name).append("'\n");
		
		//ForEach dell'HashMap 
		for(String key: map.keySet()) {
			//Ogni campo verrà mappato in formato JSON  
			builder.append("'").append(key).append("':");
			builder.append("'").append(map.get(key)).append("'\n");
		}
		//Chiusura del file JSON
		builder.append("}");
		
		//Inserisco il contenuto dello StringBuilder in una variabile String da ritornare
		String json_file = builder.toString();
		return json_file;
	}
	
}
