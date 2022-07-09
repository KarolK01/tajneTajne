package com.example.demo.data.controller;

import com.example.demo.data.model.Miejsce;
import com.example.demo.data.repository.MiejsceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BiletyController {
    @Autowired
    private MiejsceRepository miejsceRepository;

    @GetMapping(path = "/miejsca")
    public List<Miejsce> getMiejsca(){return miejsceRepository.findAll();}
}