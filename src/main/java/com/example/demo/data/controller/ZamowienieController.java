package com.example.demo.data.controller;

import com.example.demo.data.model.Zamowienie;
import com.example.demo.data.repository.ZamowienieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ZamowienieController {

    @Autowired
    private ZamowienieRepository zamowienieRepository;

    @GetMapping(path = "/zamowienie")
    public List<Zamowienie> getZamowienie(){
        return zamowienieRepository.findAll();
    }

}
