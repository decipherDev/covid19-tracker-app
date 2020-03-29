package com.github.covidtracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.covidtracker.entities.CovidAffectedStates;

public interface CovidRepository extends JpaRepository<CovidAffectedStates, Integer> {

}
