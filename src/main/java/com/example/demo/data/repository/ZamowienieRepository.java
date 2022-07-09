package com.example.demo.data.repository;

import com.example.demo.data.model.Zamowienie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZamowienieRepository extends JpaRepository<Zamowienie, Long> {
}
