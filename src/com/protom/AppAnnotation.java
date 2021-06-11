package com.protom;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import com.protom.core.ObjectSerializer;
import com.protom.entity.User;
import com.protom.enumeration.Format;

public class AppAnnotation {

	public static void main (String[] args) {
		User utente1 = new User("Francesco", "Pisani", "francesco.pisani@gmail.com", LocalDate.of(1994, 4, 16));
		User utente2 = new User("Danilo", null, "dinuzzo@protom.com");
		User utente3 = new User(null, "Moscati", null);
		
		try {
			
			System.out.println(ObjectSerializer.Serializer(utente1, Format.JSON));
			
			System.out.println(ObjectSerializer.Serializer(utente2, Format.JSON));
			
			System.out.println(ObjectSerializer.Serializer(utente3, Format.XML));
			
		} catch (IllegalArgumentException | InvocationTargetException e) {
			System.err.println("Si è verificato un errore: ");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err.println("Si è verificato un errore: ");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Si è verificato un errore generico.");
		}
	}
	
}
