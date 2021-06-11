package com.protom.entity;

import java.time.LocalDate;

import com.protom.annotation.DateFormat;
import com.protom.annotation.Skip;
import com.protom.annotation.SkipNull;

public class User {
	
	public String given;
	private String family;
	private String email;
	private LocalDate birthDate;

	
	public User(String given, String family, String email) {
		super();
		this.given = given;
		this.family = family;
		this.email = email;
	}
	

	public User(String given, String family, String email, LocalDate birthDate) {
		super();
		this.given = given;
		this.family = family;
		this.email = email;
		this.birthDate = birthDate;
	}



	@Skip
	public String getGiven() {
		return given;
	}

	public void setGiven(String given) {
		this.given = given;
	}

	@SkipNull
	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@DateFormat(format = "dd/MM/yyyy")
	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	
	
}

