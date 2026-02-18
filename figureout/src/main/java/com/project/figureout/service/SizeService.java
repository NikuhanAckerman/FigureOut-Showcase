package com.project.figureout.service;

import com.project.figureout.model.Size;
import com.project.figureout.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }

}
