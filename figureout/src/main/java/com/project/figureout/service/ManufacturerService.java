package com.project.figureout.service;

import com.project.figureout.model.Manufacturer;
import com.project.figureout.repository.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerService {

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

}
