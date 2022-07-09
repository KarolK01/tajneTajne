package com.example.demo.data.service;

import com.example.demo.data.model.Miejsce;
import com.example.demo.data.repository.MiejsceRepository;
import com.example.demo.data.repository.ZamowienieRepository;
import org.atmosphere.config.service.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
@Service
public class InitService {
    @Autowired
    private ZamowienieRepository zamowienieRepository;
    @Autowired
    private MiejsceRepository miejsceRepository;
    public enum Status {
        ZAJETE,
        WOLNE
    }
    public enum Location{
        PRZY_LEWYM_OKNIE,
        PRZY_PRAWYM_OKNIE,
        NA_SRODKU
    }
    @PostConstruct
    public void init (){
        Miejsce miejsce1 = miejsceRepository.save(new Miejsce(1l, Location.PRZY_LEWYM_OKNIE, Status.WOLNE));
        Miejsce miejsce2 = miejsceRepository.save(new Miejsce(2l, Location.NA_SRODKU, Status.ZAJETE));
        Miejsce miejsce3 = miejsceRepository.save(new Miejsce(3l, Location.NA_SRODKU, Status.WOLNE));
        Miejsce miejsce4 = miejsceRepository.save(new Miejsce(4l, Location.NA_SRODKU, Status.WOLNE));
        Miejsce miejsce5 = miejsceRepository.save(new Miejsce(5l, Location.PRZY_PRAWYM_OKNIE, Status.WOLNE));
        Miejsce miejsce6 = miejsceRepository.save(new Miejsce(6l, Location.PRZY_LEWYM_OKNIE, Status.WOLNE));
        Miejsce miejsce7 = miejsceRepository.save(new Miejsce(7l, Location.NA_SRODKU, Status.WOLNE));
        Miejsce miejsce8 = miejsceRepository.save(new Miejsce(8l, Location.NA_SRODKU, Status.WOLNE));
        Miejsce miejsce9 = miejsceRepository.save(new Miejsce(9l, Location.NA_SRODKU, Status.WOLNE));
        Miejsce miejsce10 = miejsceRepository.save(new Miejsce(10l, Location.PRZY_PRAWYM_OKNIE, Status.WOLNE));

    }
}
