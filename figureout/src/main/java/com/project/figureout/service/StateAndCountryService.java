package com.project.figureout.service;

import com.project.figureout.model.Country;
import com.project.figureout.model.State;
import com.project.figureout.repository.CountryRepository;
import com.project.figureout.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateAndCountryService {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CountryRepository countryRepository;

    public List<State> getAllStates() {
        return stateRepository.findAll();
    }

    public State getStateById(long id) {
        return stateRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Estado não encontrado pelo ID."));
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public Country getCountryById(long id) {
        return countryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("País não encontrado pelo ID."));
    }

}
