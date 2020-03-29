package com.github.covidtracker.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class CovidAffectedStates {
	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	
	@Column(name = "indian_national")
	private int indianNational;
	
	@Column(name = "foreign_national")
	private int foreignNational;
	
	private int cured;
	private int death;
	
}
